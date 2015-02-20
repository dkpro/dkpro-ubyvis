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

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import de.tudarmstadt.ukp.dkpro.uby.vis.webapp.page.ApplicationPageBase;
import de.tudarmstadt.ukp.lmf.api.UbyQuickAPI;

public class TryUby extends ApplicationPageBase {

	private static final long serialVersionUID = 1L;

	SenseAlign senseAlignVis;
	TextPanel senseAlignText;

	@SpringBean(name = "ubyQuickAPI")
	private static UbyQuickAPI ubyQuick;

	private String param;
	private final Form searchForm;
	private final TextField<String> searchInput;
	private static final CssResourceReference TRYUBY_CSS = new CssResourceReference(
			TryUby.class, "TryUby.css");

	private static final List<String> order_list = Arrays.asList(new String[] {
			"1", "2" });

	// make order 1 selected by default
	private String selected = "1";
	private final DropDownChoice<String> dropDownChoice = new DropDownChoice<String>(
			"order", new PropertyModel<String>(this, "selected"), order_list);

	public TryUby(PageParameters parameters) throws SQLException,
			FileNotFoundException {

		this.param = parameters.get("lemma").toString();

		if (this.param == null) {
			this.param = "";
		}

		this.searchInput = new TextField<String>("textbox", Model.of(param));
		this.searchForm = new Form("search") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {

				PageParameters pageParameters = new PageParameters();
				selected = "1";
				if (searchInput.getValue() != null && selected != null) {
					String text = searchInput.getValue();
					pageParameters.add("lemma", text);

					String orderNo = selected;
					pageParameters.add("order", orderNo);
					setResponsePage(TryUby.class, pageParameters);
				}
			}
		};

		dropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

				String orderNo = selected;
				senseAlignVis.setOrder(orderNo);
				target.add(senseAlignVis);

			}
		});

		searchForm.add(searchInput);
		searchForm.add(dropDownChoice);
		add(searchForm);

		initialization();

	}

	/**
	 * Creates visual and text view.
	 *
	 */
	public void initialization() {

		initVisualView();
		initTextView();
	}

	public void initVisualView() {

		senseAlignVis = new SenseAlign("visualPanel") {

			private static final long serialVersionUID = 1L;

			@Override
            protected void onClicked(IRequestParameters request,
					AjaxRequestTarget target) {

				String tab = (request.getParameterValue("tab")).toString();

				if (tab.contains("2")) {

					StringValue word = request.getParameterValue("word");
					StringValue senseId = request.getParameterValue("senseId");
					StringValue senseDefinition = request
							.getParameterValue("senseDefination");
					StringValue synsetDefinition = request
							.getParameterValue("synsetDefinition");
					StringValue senseExample = request
							.getParameterValue("senseExample");

					Map<String, StringValue> SenseDetails = new TreeMap<String, StringValue>();
					SenseDetails.put("word", word);
					SenseDetails.put("senseId", senseId);
					SenseDetails.put("senseDefinition", senseDefinition);
					SenseDetails.put("synsetDefinition", synsetDefinition);
					SenseDetails.put("senseExample", senseExample);

					List<Map<String, StringValue>> testList = new ArrayList<Map<String, StringValue>>();
					testList.add(SenseDetails);

					IModel textData;

					textData = new ListModel<Map<String, StringValue>>(testList);

					senseAlignText.setDetailSense(textData);
					senseAlignText.tabpanel.setSelectedTab(1);
					target.add(senseAlignText);

				}

				else if (tab.contains("3")) {

					StringValue firstSenseId = request
							.getParameterValue("firstSenseId");
					StringValue firstSynsetDefinition = request
							.getParameterValue("firstSynsetDefinition");
					StringValue firstSenseDefination = request
							.getParameterValue("firstSenseDefination");
					StringValue firstSenseExample = request
							.getParameterValue("firstSenseExample");
					StringValue secondSenseId = request
							.getParameterValue("secondSenseId");
					StringValue secondSynsetDefinition = request
							.getParameterValue("secondSynsetDefinition");
					StringValue secondSenseDefination = request
							.getParameterValue("secondSenseDefination");
					StringValue secondSenseExample = request
							.getParameterValue("secondSenseExample");

					Map<String, StringValue> firstSenseDetails = new TreeMap<String, StringValue>();
					firstSenseDetails.put("senseId", firstSenseId);
					firstSenseDetails.put("senseDefinition",
							firstSenseDefination);
					firstSenseDetails.put("synsetDefinition",
							firstSynsetDefinition);
					firstSenseDetails.put("senseExample", firstSenseExample);

					Map<String, StringValue> secondSenseDetails = new TreeMap<String, StringValue>();
					secondSenseDetails.put("senseId", secondSenseId);
					secondSenseDetails.put("senseDefinition",
							secondSenseDefination);
					secondSenseDetails.put("synsetDefinition",
							secondSynsetDefinition);
					secondSenseDetails.put("senseExample", secondSenseExample);

					List<Map<String, StringValue>> testList = new ArrayList<Map<String, StringValue>>();
					testList.add(firstSenseDetails);
					testList.add(secondSenseDetails);

					IModel textData;

					textData = new ListModel<Map<String, StringValue>>(testList);

					senseAlignText.setCompareData(textData);
					senseAlignText.tabpanel.setSelectedTab(2);
					target.add(senseAlignText);

				}

			}

		};

		senseAlignVis.setLemma(this.param);
		senseAlignVis.loadSenseData();
		senseAlignVis.setOrder(this.selected);

		add(senseAlignVis);
	}

	public void initTextView() {

		senseAlignText = new TextPanel("textPanel");
		senseAlignText.setParam(param);
		add(senseAlignText);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(CssContentHeaderItem.forReference(TRYUBY_CSS));
	}
}
