package com.github.gwtd3.js.maven;


import com.google.gwt.core.client.GWT;

public class Maven {
	public static String projectVersion() {
		MavenConstants constants = GWT.create(MavenConstants.class);
		return constants.projectVersion();
	}
}
