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
package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.page.welcome;

import org.apache.wicket.markup.html.link.Link;

import de.tudarmstadt.ukp.dkpro.uby.vis.webapp.page.ApplicationPageBase;

/**
 * Start page
 */
public class WelcomePage
	extends ApplicationPageBase
{
    private static final long serialVersionUID = -8053898420101543293L;

	private Link<Void> drawLink;
	private Link<Void> RadioExample;

    public WelcomePage()
    {
     /*drawLink = new BookmarkablePageLink<Void>("drawSome", drawSomething.class);
     add(drawLink);*/

     /*RadioExample = new BookmarkablePageLink<Void>("RadioEx", RadioChoicePage.class);
     add(RadioExample);*/

    }

}
