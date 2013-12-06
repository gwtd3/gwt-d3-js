/*
 *  Copyright 2012 GWT-Bootstrap
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.gwtd3.js.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * 
 * 
 * @since 2.0.4.0
 * 
 * @author Carlos Alexandro Becker
 */
public class D3Initializer {

	/**
	 * The real created factory depends on the "d3.pretty" property.
	 */
	private static final ResourceFactory RESOURCE_FACTORY = GWT
			.create(ResourceFactory.class);

	// private static final Configurator ADAPTER =
	// GWT.create(Configurator.class);
	// this is for injecting SHIM for nstance
	// private static final InternalResourceInjector INJECTOR =
	// GWT.create(InternalResourceInjector.class);

	/**
	 * Injects the required JavaScript files into the document header.
	 */
	public static void configure() {
		// INJECTOR.preConfigure();

		injectJs(RESOURCE_FACTORY.getResources().d3JsScript());

		// INJECTOR.configure();
	}

	private static void injectJs(final TextResource r) {
		JavaScriptInjector.inject(r.getText());
	}

}
