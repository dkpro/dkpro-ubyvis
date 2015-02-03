package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.tryUby;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.tudarmstadt.ukp.lmf.model.core.Sense;

public class AllPanel extends Panel {

	private static final long serialVersionUID = 1L;
	ListView<?> list;
	int count = 0;

	public AllPanel(String id, IModel<?> model) {
		super(id, model);

		final Map<String, String> res_map = new HashMap<String, String>();

		res_map.put("FN", "rgba(148,103,189,0.8)");
		res_map.put("OW_deu", "rgba(255,152,150,0.8)");
		res_map.put("OW_eng", "rgba(214,39,40,0.8)");
		res_map.put("OntoWktDE", "rgba(255,187,120,0.8)");
		res_map.put("OntoWktEN", "rgba(255,127,14,0.8)");
		res_map.put("VN", "rgba(188,189,34,0.8)");
		res_map.put("WikiDE", "rgba(174,199,232,0.8)");
		res_map.put("WikiEN", "rgba(31,119,180,0.8)");
		res_map.put("WktDE", "rgba(152,223,138,0.8)");
		res_map.put("WktEN", "rgba(44,160,44,0.8)");
		res_map.put("WN", "rgba(23,190,207,0.8)");

		if (model.getObject() != null) {

			list = new ListView("listSenses", model) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem item) {

					final Sense sense = (Sense) item.getModelObject();
					int sIndex = item.getIndex();

					if (sIndex == 0) {
						count = 0;
					}

					count++;

					String res_id = sense.toString();
					String[] result_arr = res_id.split("_");
					String res_color = result_arr[0];

					if (res_id.contains("OW_eng") || res_id.contains("OW_deu")) {
						res_color = result_arr[0].concat("_").concat(
								result_arr[1]);
					}

					String border = " border : 1px solid "
							+ res_map.get(res_color) + ";"
							+ "border-left-width: 15px";

					item.add(new AttributeAppender("style", border));
					Label word = new Label("word", "(" + count + ") "
							+ sense.getLexicalEntry().getLemmaForm());

					String postag = (sense.getLexicalEntry().getPartOfSpeech() != null) ? "("
							+ sense.getLexicalEntry().getPartOfSpeech().name()
							+ ")"
							: "";
					Label pos = new Label("POSTAG", postag);

					String defString = sense.getDefinitionText();
					if (defString == null) {
						if (sense.getSynset() != null) {
							defString = sense.getSynset().getDefinitionText();
						}
					}

					if (defString == null || defString.trim().length() == 0) {
						defString = "No definition found!";
					}

					Label definition = new Label("definition",
							new Model<String>(defString.trim()));

					item.add(word);
					item.add(pos);
					item.add(definition);
				}

			};

		}

		add(list);
		setOutputMarkupId(true);
	}

}
