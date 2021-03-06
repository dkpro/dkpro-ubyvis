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
package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.tryUby;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValue;

public class DetailPanel extends Panel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	ListView detailSenseList;

	public DetailPanel(String id, IModel<?> model) {
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

		detailSenseList = new ListView("detailSense", model) {

			@Override
			protected void populateItem(ListItem item) {

				Map<String, StringValue> detail = (Map<String, StringValue>) item
						.getModelObject();

				String res_id = (detail.get("senseId")).toString();

				String[] result_arr = res_id.split("_");

				String res_color = result_arr[0];

				if (res_id.contains("OW_eng") || res_id.contains("OW_deu")) {
					res_color = result_arr[0].concat("_").concat(result_arr[1]);
				}

				String border = " border : 1px solid " + res_map.get(res_color)
						+ ";" + "border-left-width: 15px";

				item.add(new AttributeAppender("style", border));

				item.add(new Label("word", detail.get("word")));
				item.add(new Label("senseId", detail.get("senseId")));
				item.add(new Label("senseDefinition", detail.get("senseDefinition")));
				item.add(new Label("synsetDefinition", detail.get("synsetDefinition")));
				item.add(new Label("senseExample", detail.get("senseExample")));
			}

		};

		add(detailSenseList);
		setOutputMarkupId(true);
	}

}
