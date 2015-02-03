package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.tryUby;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;
import de.tudarmstadt.ukp.dkpro.uby.vis.webapp.model.Lexicon_Senses;
import de.tudarmstadt.ukp.lmf.api.UbyQuickAPI;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;

public class TextPanel extends Panel {

	private static final long serialVersionUID = 1L;
	boolean initialize = true;
	private IModel allsenses;
	private IModel detailSense;
	boolean detailSenseIsSet = false;
	private IModel compareData;
	boolean compareIsSet = false;
	boolean allsensesIsSet = false;
	int count = 0;

	AnalyzePanel analyze;

	@SpringBean(name = "ubyQuickAPI")
	private static UbyQuickAPI ubyQuick;

	private String param;

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	// Bootstrap Panel
	public BootstrapTabbedPanel<ITab> tabpanel = null;
	private Integer defaultTab = 0;

	public IModel getCompareData() {
		return compareData;
	}

	public void setCompareData(IModel compareData) {
		this.compareIsSet = true;
		this.compareData = compareData;
	}

	public boolean isCompareIsSet() {
		return compareIsSet;
	}

	public void setCompareIsSet(boolean compareIsSet) {
		this.compareIsSet = compareIsSet;
	}

	public IModel getDetailSense() {
		return detailSense;
	}

	public void setDetailSense(IModel detailSense) {
		this.detailSenseIsSet = true;
		this.detailSense = detailSense;
	}

	public IModel getAllsenses() {
		return allsenses;
	}

	public void setAllsenses(IModel allsenses) {
		this.allsenses = allsenses;
	}

	@SuppressWarnings("unchecked")
	public TextPanel(String id) {

		super(id);

		final IModel allSenseDataList = new LoadableDetachableModel() {

			@Override
			protected Object load() {

				List<Sense> allSenseList = new ArrayList<Sense>();
				List<Lexicon_Senses> list = new ArrayList<Lexicon_Senses>();

				if (param.trim().length() > 0) {
					List<Lexicon> lexicons = ubyQuick.lightLexicons();

					for (Lexicon lexicon : lexicons) {

						List<LexicalEntry> lexicalEntries = ubyQuick
								.getLexicalEntries(param, null, lexicon);
						if (lexicalEntries.size() > 0) {
							List<Sense> senses = new ArrayList<Sense>();
							List<String> checkDuplicate = new ArrayList<String>();
							for (LexicalEntry lexicalEntry : lexicalEntries) {
								try {
									List<Sense> sensesList = lexicalEntry
											.getSenses();
									for (Sense sense : sensesList) {
										if (!checkDuplicate.contains(sense
												.getId())) {
											checkDuplicate.add(sense.getId());
											if (sense.getLexicalEntry()
													.getLemmaForm()
													.equals(param)) {
												senses.add(sense);
											}
										}
									}
								} catch (Exception ex) {
									System.out
											.println("Log-Information: This LexicalEntry has no sense!"
													+ lexicalEntry.getId());
								}
							}
							if (senses.size() != 0) {
								Lexicon_Senses resource = new Lexicon_Senses(
										lexicon.getName(), senses);
								list.add(resource);
							} else {
								System.out.println(":((");
							}
						}
					}
				}

				if (param.trim().length() > 0 && list.size() == 0) {
					List<Sense> senses = new ArrayList<Sense>();
					list.add(new Lexicon_Senses("The word: '" + param
							+ "' does not exist in our Database", senses));
				}

				for (Lexicon_Senses ls : list) {

					List<Sense> senseList = ls.getSensesList();
					for (Sense s : senseList) {
						allSenseList.add(s);
					}

				}

				return allSenseList;

			}

		};

		ArrayList<ITab> tabs = new ArrayList<ITab>();

		tabpanel = new BootstrapTabbedPanel<ITab>("tabs", tabs,
				Model.of(defaultTab));

		tabs.add(new AbstractTab(Model.of("All")) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {

				return new AllPanel(panelId, allSenseDataList);
			}

		});

		tabs.add(new AbstractTab(Model.of("Detail")) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {

				IModel detailSenseData = new LoadableDetachableModel() {

					@Override
					protected Object load() {

						if (detailSenseIsSet) {
							return getDetailSense().getObject();
						} else {
							return getDetailSense();
						}
					}
				};
				return new DetailPanel(panelId, detailSenseData);
			}

		});

		tabs.add(new AbstractTab(Model.of("Compare")) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {

				IModel compareSenses = new LoadableDetachableModel() {

					@Override
					protected Object load() {

						if (compareIsSet) {
							return getCompareData().getObject();
						} else {
							return getCompareData();
						}
					}
				};
				return new ComparePanel(panelId, compareSenses);
			}

		});

		tabs.add(new AbstractTab(Model.of("Analyze")) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {

				analyze = new AnalyzePanel(panelId);
				analyze.setLemma(param);
				analyze.loadSenseData();
				return analyze;
			}

		});

		add(tabpanel);
		setOutputMarkupId(true);

	}

}
