package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.page.welcome;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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
