package com.github.gwtd3.js.client;

import com.github.gwtd3.js.client.resources.D3Initializer;
import com.google.gwt.core.client.EntryPoint;

public class D3Js implements EntryPoint {

	@Override
	public void onModuleLoad() {
		D3Initializer.configure();
	}

}
