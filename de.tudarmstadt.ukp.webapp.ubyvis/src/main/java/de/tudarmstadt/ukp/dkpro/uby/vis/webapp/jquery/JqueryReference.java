package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.jquery;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class JqueryReference extends JavaScriptResourceReference{
	
	private static final long serialVersionUID = 1L;
	
	public static final JqueryReference jQuery_JS = new JqueryReference();

	public JqueryReference() {
		super(JqueryReference.class, "jquery-2.0.3.min.js");
	}
	
}
