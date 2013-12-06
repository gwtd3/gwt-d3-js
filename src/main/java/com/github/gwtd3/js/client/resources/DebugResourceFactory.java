package com.github.gwtd3.js.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.TextResource;

public class DebugResourceFactory implements ResourceFactory {

	public interface DebugResources extends Resources {

		@Source("d3.js")
		@Override
		public TextResource d3JsScript();
	}

	private static final DebugResources RESOURCES = GWT
			.create(DebugResources.class);

	@Override
	public Resources getResources() {
		return RESOURCES;
	}

}
