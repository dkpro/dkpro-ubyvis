package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.d3;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class D3Reference extends JavaScriptResourceReference{
	
	public static final D3Reference D3_JS = new D3Reference();
	
	public D3Reference()
    {
        super(D3Reference.class, "d3.min.js");
    }

}
