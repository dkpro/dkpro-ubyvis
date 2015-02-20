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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import de.tudarmstadt.ukp.dkpro.uby.vis.webapp.d3.D3Reference;
import de.tudarmstadt.ukp.dkpro.uby.vis.webapp.jquery.JqueryReference;
import de.tudarmstadt.ukp.lmf.api.UbyQuickAPI;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.meta.SemanticLabel;
import de.tudarmstadt.ukp.lmf.model.multilingual.SenseAxis;
import de.tudarmstadt.ukp.lmf.model.semantics.SenseExample;

public class SenseAlign extends Panel {

	private static final long serialVersionUID = 551014051352333974L;

	public static final JavaScriptResourceReference SENSE_ALIGN_JS = new JavaScriptResourceReference(
			SenseAlign.class, "SenseAlign.js");

	public static final CssResourceReference SENSE_ALIGN_CSS = new CssResourceReference(
			SenseAlign.class, "SenseAlign.css");

	String lemma;
	String order;

	private static ArrayList<List<Map<String, String>>> mapAlign = new ArrayList<List<Map<String, String>>>();

	ListModel<List<Map<String, String>>> displayData;

	private AbstractAjaxBehavior controller;

	@SpringBean(name = "jsonConverter")
	private MappingJacksonHttpMessageConverter jsonConverter;

	@SpringBean(name = "ubyQuickAPI")
	private static UbyQuickAPI ubyQuick;

	private IModel senseItemModel;

	public SenseAlign(String id) {
		super(id);
		initialize();
	}

	public void initialize() {

		controller = new AbstractAjaxBehavior() {
			private static final long serialVersionUID = -4779657570720954212L;

			@Override
			public void onRequest() {
				// Convert model to JSON
				StringWriter out = new StringWriter();
				try {
					Object model = senseItemModel.getObject();
					jsonConverter.getObjectMapper().getJsonFactory()
							.createJsonGenerator(out).writeObject(model);
				} catch (IOException e) {
					// FIXME handle errors somehow!!!
					e.printStackTrace();
				}

				// Send JSON result
				String result = out.toString();
				RequestCycle requestCycle = RequestCycle.get();
				requestCycle
						.scheduleRequestHandlerAfterCurrent(new TextRequestHandler(
								"application/json", "utf-8", result));
			}

		};

		add(controller);

		final AbstractDefaultAjaxBehavior behave = new AbstractDefaultAjaxBehavior() {

			@Override
            public void renderHead(Component component,
					IHeaderResponse aResponse) {

				super.renderHead(component, aResponse);

				aResponse.render(JavaScriptReferenceHeaderItem.forReference(D3Reference.D3_JS));
				aResponse.render(JavaScriptReferenceHeaderItem.forReference(JqueryReference.jQuery_JS));
				aResponse.render(JavaScriptReferenceHeaderItem.forReference(SENSE_ALIGN_JS));
				aResponse.render(CssReferenceHeaderItem.forReference(SENSE_ALIGN_CSS));

				String callbackUrl = getCallbackUrl().toString();

				// Tell D3 to initiate the AJAX request
				String[] annotatorScript1 = new String[] {
						"d3.json(\"" + controller.getCallbackUrl()
								+ "\", function(json) {",
						"  draw(\"" + getMarkupId() + "\", json,\""
								+ getLemma() + "\",\"" + getOrder() + "\",\""
								+ callbackUrl + "\");", "});" };

				if (getLemma() != "") {
					aResponse.render(OnDomReadyHeaderItem.forScript(StringUtils
							.join(annotatorScript1, "\n")));
				}

			}

			@Override
			protected void respond(AjaxRequestTarget target) {

				IRequestParameters request = RequestCycle.get().getRequest()
						.getRequestParameters();
				onClicked(request, target);
			}

		};

		add(behave);
		setOutputMarkupId(true);
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	/**
	 * Generates sense clusters and retrieves sense details.
	 *
	 */
	public void loadSenseData() {
		senseItemModel = new LoadableDetachableModel() {

			private static final long serialVersionUID = 1L;
			ArrayList<List<Map<String, String>>> alignmentsListMap = new ArrayList<List<Map<String, String>>>();

			@Override
			protected Object load() {

				if (getLemma() != null && getLemma().trim().length() > 0) {
					List<Sense> senses = new ArrayList<Sense>();

					for (LexicalEntry le : ubyQuick.getLexicalEntries(lemma,
							null, null)) {
						try {
							if (le.getSenses() != null
									&& le.getSenses().size() > 0) {
								for (Sense sense : le.getSenses()) {
									senses.add(sense);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

					List<SenseAxis> sas = ubyQuick.lightSenseAxes(senses);

					List<List<Sense>> clusterList = new ArrayList<List<Sense>>();

					for (SenseAxis sa : sas) {
						if (clusterList.size() == 0) {
							List<Sense> senseList = new ArrayList<Sense>();
							clusterList.add(senseList);

							if (sa.getSenseOne().getLexicalEntry()
									.getLemmaForm().equals(lemma)
									&& sa.getSenseTwo().getLexicalEntry()
											.getLemmaForm().equals(lemma)) {
								senseList.add(sa.getSenseOne());
								senseList.add(sa.getSenseTwo());
							}
						} else {
							boolean match = false;

							for (List<Sense> senseGroup : clusterList) {

								if (senseGroup.contains(sa.getSenseOne())
										|| senseGroup
												.contains(sa.getSenseTwo())) {
									match = true;

									if (sa.getSenseOne().getLexicalEntry()
											.getLemmaForm().equals(lemma)
											&& sa.getSenseTwo()
													.getLexicalEntry()
													.getLemmaForm()
													.equals(lemma)) {

										if (!senseGroup.contains(sa
												.getSenseOne())) {
											senseGroup.add(sa.getSenseOne());
										}

										if (!senseGroup.contains(sa
												.getSenseTwo())) {
											senseGroup.add(sa.getSenseTwo());
										}

									}

									break;
								}
							}

							if (!match) {
								List<Sense> senseList = new ArrayList<Sense>();
								if (sa.getSenseOne().getLexicalEntry()
										.getLemmaForm().equals(lemma)
										&& sa.getSenseTwo().getLexicalEntry()
												.getLemmaForm().equals(lemma)) {
									senseList.add(sa.getSenseOne());
									senseList.add(sa.getSenseTwo());
								}

								clusterList.add(senseList);
							}
						}

					}

					for (Sense sense : senses) {
						boolean match = false;

						for (List<Sense> senseGroup : clusterList) {
							if (senseGroup.contains(sense)) {
								match = true;
								break;
							}
						}
						if (!match) {
							List<Sense> senseList = new ArrayList<Sense>();
							senseList.add(sense);
							clusterList.add(senseList);
						}
					}

					List<Map<String, String>> finalData = new ArrayList<Map<String, String>>();

					Map<String, List<Map<String, String>>> map = new TreeMap<String, List<Map<String, String>>>();

					for (List<Sense> printList : clusterList) {
						List<Sense> senseList = printList;

						List<String> senseListString = new ArrayList<String>();
						List<Map<String, String>> senseMapList = new ArrayList<Map<String, String>>();

						for (Sense sense : senseList) {

							Map<String, String> senseMap = new TreeMap<String, String>();

							Collection<SenseExample> senseExamples = sense
									.getSenseExamples();
							Collection<SemanticLabel> semanticLabels = sense
									.getSemanticLabels();

							senseMap.put("senseId", sense.getId());

							if (!(sense.getDefinitionText() == null)) {
								senseMap.put("senseDefination",
										sense.getDefinitionText());
							} else {
								senseMap.put("senseDefination",
										"No sense defination");
							}

							if (!(sense.getSynset() == null)
									&& !(sense.getSynset().getDefinitionText() == null)) {
								senseMap.put("synsetDefinition", sense
										.getSynset().getDefinitionText());
							} else {
								senseMap.put("synsetDefinition",
										"No synset defination");
							}

							if (!senseExamples.isEmpty()) {
								senseMap.put("senseExample", sense
										.getSenseExamples().get(0)
										.getTextRepresentations().get(0)
										.getWrittenText());
							} else {
								senseMap.put("senseExample", "No sense example");
							}

							if (!semanticLabels.isEmpty()) {
								String label = sense.getSemanticLabels().get(0)
										.getLabel();
								senseMap.put("semanticLabel", label);
							} else {
								senseMap.put("semanticLabel",
										"No semantic Label");
							}

							senseListString.add(sense.getId());
							senseMapList.add(senseMap);

						}

						alignmentsListMap.add(senseMapList);

					}

					setMapAlignments(alignmentsListMap);

				}

				return alignmentsListMap;
			}

		};

	}

	protected void onClicked(IRequestParameters request,
			AjaxRequestTarget target) {
	}

	public void setMapAlignments(ArrayList<List<Map<String, String>>> mapData) {
		this.mapAlign = mapData;
	}

	public ArrayList<List<Map<String, String>>> getMapAlignments() {
		return this.mapAlign;
	}
}
