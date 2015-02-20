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
	private final List<Sense> sensesList;

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
