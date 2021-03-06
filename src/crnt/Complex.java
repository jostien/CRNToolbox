/* CRNToolkit, Copyright (c) 2010-2016 Jost Neigenfind  <jostie@gmx.de>,
 * Sergio Grimbs, Zoran Nikoloski
 * 
 * A Java toolkit for Chemical Reaction Networks
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package crnt;

import java.util.*;

import math.graph.MyNode;
import math.set.MyMultiset;
import math.set.MySet;
import singular.*;
import miscellaneous.*;

public class Complex extends MyNode<MyMultiset<Species>>{
	public Complex(){
		super(new MyMultiset<Species>());
	}
	
	public Complex(Species[] species_array){
		for (int i = 0; i < species_array.length; i++)
			this.getObject().add(species_array[i]);
	}
	
	public Complex(Species[] species_array, Double[] occurences){
		for (int i = 0; i < species_array.length; i++)
			this.getObject().add(species_array[i], occurences[i]);
	}
	
	public Complex addSpecies(Species species){
		if (!species.getName().equals("0"))
			this.getObject().add(species);
		
		return this;
	}
	
	public Complex addSpecies(Species species, double occurences){
		if (!species.getName().equals("0")){
			this.getObject().add(species, occurences);
		}
		
		return this;
	}
	
	public Species getSpecies(int i){
		if (i >= this.getObject().numberOfDistinctElements())
			return null;
		
		Object[] array = this.getObject().getListOfDistinctElements();
		return (Species)array[i];
	}
	
	public MySet<Species> getSpecies(){
		return this.getObject().toMySet();
	}
	
	public String getPsi(){
		String ret = "";

		Iterator<Species> species_iterator = this.getObject().iterator();
		while (species_iterator.hasNext()){
			Species species = species_iterator.next();
			Double c = this.getObject().getNumberOfOccurences(species);
			if (c != 1)
				ret = ret + "c_" + species.getName() + "^" + c + "*";
			else
				ret = ret + "c_" + species.getName() + "*";
		}
		
		if (ret.length() > 0)
			return ret.substring(0, ret.length() - 1);
		
		return "";
	}
	
	public Monomial getMonomial(){
		Monomial ret = new Monomial();
		
		Iterator<Species> species_iterator = this.getObject().iterator();
		while (species_iterator.hasNext()){
			Species species = species_iterator.next();
			Double c = this.getObject().getNumberOfOccurences(species);
			
			Variable variable = new Variable(species.getName(), c.intValue());
			ret.addVariable(variable);
		}

		return ret;
	}
	
	public static boolean isSubstrate(Complex complex, MySet<Reaction> R){
		Iterator<Reaction> iterator = R.iterator();
		while (iterator.hasNext()){
			Reaction reaction = iterator.next();
			if (complex.equals(reaction.getSubstrate()))
				return true;
		}
		
		return false;
	}
	
	public Complex clone(){
		Complex ret = new Complex();
		Iterator<Species> species_iterator = this.getObject().iterator();
		while (species_iterator.hasNext()){
			Species species = species_iterator.next().clone();
			ret.getObject().add(species, this.getObject().getNumberOfOccurences(species));
		}
		return ret;
	}

	public int compareTo(MyNode<MyMultiset<Species>> complex){
		return this.toString().compareTo(complex.toString());
	}	
	
	public boolean equals(Object o){
		return this.toString().equals(((Complex)o).toString());
	}
	
	public int hashCode(){
		return this.toString().hashCode();
	}
	
	public String toString(){
		return this.getObject().toString();
	}

	public static String ID_PREFIX = "C_";
}
