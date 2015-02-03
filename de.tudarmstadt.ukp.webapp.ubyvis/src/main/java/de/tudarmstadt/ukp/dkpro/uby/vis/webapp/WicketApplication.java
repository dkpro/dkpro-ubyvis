package de.tudarmstadt.ukp.dkpro.uby.vis.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.odlabs.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.tudarmstadt.ukp.dkpro.uby.vis.webapp.page.welcome.WelcomePage;
import de.tudarmstadt.ukp.dkpro.uby.vis.webapp.tryUby.TryUby;

public class WicketApplication
	extends WebApplication
{
	boolean isInitialized = false;

	@Override
	public void init()
	{
		if (!isInitialized) {
			super.init();
			
			BootstrapSettings settings = new BootstrapSettings();
	        Bootstrap.install(this, settings);

			addResourceReplacement(WiQueryCoreThemeResourceReference.get(),
					new WiQueryCoreThemeResourceReference("redlion"));

			getComponentInstantiationListeners().add(new SpringComponentInjector(this));
			
			 IPackageResourceGuard packageResourceGuard = 
					 getResourceSettings().getPackageResourceGuard(); 
					                 if (packageResourceGuard instanceof SecurePackageResourceGuard) 
					                 { 
					                         SecurePackageResourceGuard guard = (SecurePackageResourceGuard) 
					 packageResourceGuard; 
					                         guard.addPattern("+*.js"); 
					                 } 
					                
		 

			mountPage("/welcome.html", getHomePage());
			mountPage("/tryuby.html", TryUby.class);

			isInitialized = true;
		}
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage()
	{
		return WelcomePage.class;
	}

}
