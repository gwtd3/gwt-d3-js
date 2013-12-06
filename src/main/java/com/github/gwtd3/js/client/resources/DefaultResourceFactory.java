package com.github.gwtd3.js.client.resources;

import com.google.gwt.core.shared.GWT;

public class DefaultResourceFactory implements ResourceFactory {

	private static final Resources RESOURCES = GWT.create(Resources.class);

	@Override
	public Resources getResources() {
		return RESOURCES;
	}

}
