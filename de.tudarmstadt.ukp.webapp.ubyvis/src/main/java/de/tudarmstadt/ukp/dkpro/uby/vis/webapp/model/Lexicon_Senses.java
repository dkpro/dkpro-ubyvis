package de.tudarmstadt.ukp.dkpro.uby.vis.webapp.model;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.lmf.model.core.Sense;

/**
 *
 * @author UKP
 * This class is the model using to list all Senses grouped by Resources (WN, Wiki, etc.)
 *
 */
public class Lexicon_Senses
{
	private String resourcesName;
	private List<Sense> sensesList;

	/**
	 * Initializes a newly created Lexicon_Senses object so that it represents an empty resourceName
	 * and an empty sensesList.
	 */
	public Lexicon_Senses()
	{
		this.resourcesName = "";
		this.sensesList = new ArrayList<Sense>();
	}

	/**
	 * Initializes a newly created Lexicon_Senses object so that it represents the same resourceName
	 * and the same list of senses as the argument.
	 * 
	 * @param resourcesName : String
	 * @param senses : List of Sense
	 * 
	 * @throws NullPointerException if senses are null.
	 */
	public Lexicon_Senses(String resourcesName, List<Sense> senses)
	{
		this.resourcesName = resourcesName;
		this.sensesList = new ArrayList<Sense>();
		this.sensesList.addAll(senses);
	}

	/**
	 * This setter adds the given senses to the List.
	 * 
	 * @param senses : List of Sense
	 * 
	 * @throws NullPointerException if senses are null. 
	 */
	public void setSensesList(List<Sense> senses)
	{
		this.sensesList.addAll(senses);
	}

	/**
	 * Set the resourcesName with the given parameter.
	 * 
	 * @param resourcesName : String
	 */
	public void setResourcesName(String resourcesName)
	{
		this.resourcesName = resourcesName;
	}

	/**
	 * Returns the resourcesName. The resourcesName can be null or a string.
	 * 
	 * @return resourcesName : String
	 */
	public String getResourcesName()
	{
		return this.resourcesName;
	}

	/**
	 * Returns the list of senses.
	 * 
	 * @return sensesList : List of Sense
	 */
	public List<Sense> getSensesList()
	{
		return this.sensesList;
	}
}
