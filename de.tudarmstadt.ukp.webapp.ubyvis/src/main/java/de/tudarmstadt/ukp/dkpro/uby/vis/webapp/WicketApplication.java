/*******************************************************************************
 * Copyright 2015
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
