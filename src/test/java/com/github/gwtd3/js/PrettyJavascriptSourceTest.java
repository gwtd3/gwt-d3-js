package com.github.gwtd3.js;

import com.github.gwtd3.js.maven.Maven;
import com.google.gwt.junit.client.GWTTestCase;

public class PrettyJavascriptSourceTest extends GWTTestCase {

	public void testD3VersionMatchesPomVersion() {
		String expectedVersion = Maven.projectVersion();
		assertEquals(expectedVersion, d3Version());
	}

	@Override
	public String getModuleName() {
		return "com.github.gwtd3.js.PrettyJavascriptSourceTest";
	}

	private static final native String d3Version() /*-{
		return $wnd.d3.version;
	}-*/;
}
