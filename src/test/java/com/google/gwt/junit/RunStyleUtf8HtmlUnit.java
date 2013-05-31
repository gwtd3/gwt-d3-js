package com.google.gwt.junit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.google.gwt.core.ext.TreeLogger;

public class RunStyleUtf8HtmlUnit extends RunStyleHtmlUnit {

	private boolean developmentMode;

	public RunStyleUtf8HtmlUnit(JUnitShell shell) {
		super(shell);
	}

	protected static class Utf8HtmlUnitThread extends HtmlUnitThread {
		private final BrowserVersion browser;
		private final TreeLogger treeLogger;
		private final String url;

		public Utf8HtmlUnitThread(BrowserVersion browser, String url,
				TreeLogger treeLogger, boolean developmentMode) {
			super(browser, url, treeLogger, developmentMode);

			this.browser = browser;
			this.treeLogger = treeLogger;
			this.url = url;
		}

		@Override
		public void run() {
			WebClient webClient = new WebClient(browser);
			webClient.setAlertHandler(this);
			// Adding a handler that ignores errors to work-around
			// https://sourceforge.net/tracker/?func=detail&aid=3090806&group_id=47038&atid=448266
			webClient.setCssErrorHandler(new ErrorHandler() {

				public void error(CSSParseException exception) {
					// ignore
				}

				public void fatalError(CSSParseException exception) {
					treeLogger.log(TreeLogger.WARN,
							"CSS fatal error: " + exception.getURI() + " ["
									+ exception.getLineNumber() + ":"
									+ exception.getColumnNumber() + "] "
									+ exception.getMessage());
				}

				public void warning(CSSParseException exception) {
					// ignore
				}
			});
			webClient.setIncorrectnessListener(this);
			webClient.setThrowExceptionOnFailingStatusCode(false);
			webClient.setThrowExceptionOnScriptError(true);
			webClient.setOnbeforeunloadHandler(this);
			webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

				@Override
				public void loadScriptError(HtmlPage htmlPage, URL scriptUrl,
						Exception exception) {
					treeLogger.log(TreeLogger.ERROR, "Load Script Error: "
							+ exception, exception);
				}

				@Override
				public void malformedScriptURL(HtmlPage htmlPage, String url,
						MalformedURLException malformedURLException) {
					treeLogger.log(TreeLogger.ERROR, "Malformed Script URL: "
							+ malformedURLException.getLocalizedMessage());
				}

				@Override
				public void scriptException(HtmlPage htmlPage,
						ScriptException scriptException) {
					treeLogger.log(TreeLogger.DEBUG, "Script Exception: "
							+ scriptException.getLocalizedMessage() + ", line "
							+ scriptException.getFailingLine());
				}

				@Override
				public void timeoutError(HtmlPage htmlPage, long allowedTime,
						long executionTime) {
					treeLogger.log(TreeLogger.ERROR, "Script Timeout Error "
							+ executionTime + " > " + allowedTime);
				}
			});
			setupWebClient(webClient);
			try {
				WebRequest webRequest = new WebRequest(new URL(url));
				webRequest.setCharset("utf-8");
				Page page = webClient.getPage(webRequest);
				webClient.waitForBackgroundJavaScriptStartingBefore(2000);
				if (treeLogger.isLoggable(TreeLogger.SPAM)) {
					treeLogger.log(TreeLogger.SPAM, "getPage returned "
							+ ((HtmlPage) page).asXml());
				}
				// TODO(amitmanjhi): call webClient.closeAllWindows()
			} catch (FailingHttpStatusCodeException e) {
				treeLogger.log(TreeLogger.ERROR, "HTTP request failed", e);
				return;
			} catch (MalformedURLException e) {
				treeLogger.log(TreeLogger.ERROR, "Bad URL", e);
				return;
			} catch (IOException e) {
				treeLogger
						.log(TreeLogger.ERROR, "I/O error on HTTP request", e);
				return;
			}
		}
	}

	@Override
	public boolean setupMode(TreeLogger logger, boolean developmentMode) {
		this.developmentMode = developmentMode;
		return true;
	}

	@Override
	protected HtmlUnitThread createHtmlUnitThread(BrowserVersion browser,
			String url) {
		return new Utf8HtmlUnitThread(browser, url, shell.getTopLogger()
				.branch(TreeLogger.SPAM, "logging for HtmlUnit thread"),
				developmentMode);
	}
}
