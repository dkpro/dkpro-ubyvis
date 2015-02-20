/*******************************************************************************
 * Copyright 2015
 * Ubiquitous Knowledge Processing (UKP) Lab and FG Language Technology
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
package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.page;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import de.agilecoders.wicket.core.Bootstrap;


/**
 * @author Richard Eckart de Castilho
 */
public abstract class ApplicationPageBase
    extends WebPage
{
    private final static Log LOG = LogFactory.getLog(ApplicationPageBase.class);

    private static final long serialVersionUID = -1690130604031181803L;

    private Label versionLabel;
    private Label embeddedDbWarning;

    protected ApplicationPageBase()
    {
        commonInit();
    }

    protected ApplicationPageBase(final PageParameters parameters)
    {
        super(parameters);
        commonInit();
    }


    @Override
    public void renderHead(IHeaderResponse response) {
    	//Session.get().setStyle("amelia");
    	super.renderHead(response);
    	response.render(JavaScriptHeaderItem.forReference(Bootstrap.getSettings().getJsResourceReference()));
    	response.render(CssHeaderItem.forReference(Bootstrap.getSettings().getCssResourceReference()));

    	/*response.render(JavaScriptHeaderItem.forReference(Bootstrap.getSettings().getJsResourceReference()));
    	response.render(CssHeaderItem.forReference(Bootstrap.getSettings().getCssResourceReference()));*/


    }

    @SuppressWarnings({ "serial" })
    private void commonInit()
    {
        getSession().setLocale(Locale.ENGLISH);

        Properties props = getVersionProperties();
        String versionString = props.getProperty("version") + " (" + props.getProperty("timestamp")
                + ")";
        versionLabel = new Label("version", versionString);

        embeddedDbWarning = new Label("embeddedDbWarning",
                "USE THIS INSTALLATION FOR TESTING ONLY -- "
                + "AN EMBEDDED DATABASE IS NOT SUPPORTED FOR PRODUCTION USE");
        embeddedDbWarning.setVisible(false);
//        try {
//            String driver = repository.getDatabaseDriverName();
//            embeddedDbWarning.setVisible(StringUtils.contains(driver.toLowerCase(Locale.US),
//                    "hsql"));
//        }
//        catch (Throwable e) {
//            LOG.warn("Unable to determine which database is being used", e);
//        }

        add(versionLabel);
        add(embeddedDbWarning);
    }

//    @Override
//    protected void onConfigure()
//    {
//        super.onConfigure();
//        logoutPanel.setVisible(AuthenticatedWebSession.get().isSignedIn());
//    }

     public Properties getVersionProperties()
    {
        try {
            return PropertiesLoaderUtils.loadAllProperties("/META-INF/version.properties");
        }
        catch (IOException e) {
            LOG.error("Unable to load version information", e);
            return new Properties();
        }
    }
}
