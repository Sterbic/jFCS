package hr.fer.zemris.nenr.fuzzy.domain;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;

/**
 * Sucelje koje predstavlja neku domenu
 * @author Luka Sterbic
 * @version 0.1
 */
public interface IDomain {
	
	/**
	 * Vrati ime domene
	 * @return ime domene
	 */
	public String getName();
	
	/**
	 * Stvori objekt domene iz stringa
	 * @param value string reprezentacija objekta
	 * @return objekt domene
	 */
	public Object[] fromStringRepresentation(String... value);
	
	public String toStringRepresentation(Object... value);
	
	/**
	 * Vrati indeks zadanog elementa
	 * @param value element
	 * @return indeks elementa, -1 ako element nije pronadjen
	 */
	public int getIndexOfElement(Object... value);
	
	/**
	 * Vrati element na zadanom indeksu
	 * @param index trazeni indeks
	 * @return trazeni element
	 */
	public Object[] elementAt(int index);
	
	/**
	 * Vrati kardinalnost domene
	 * @return kardinalnost domene
	 */
	public int getCardinality();
	
	/**
	 * Vrati komponente ove domene
	 * @return ako je domena kartezijev produkt vraca
	 * komponente, inace vraca samu domenu
	 */
	public IDomain[] getDomainComponents();
	
	/**
	 * Konstruiraj domenu na zadanom fuzzy stroju
	 * @param engine fuzzy stroj
	 * @throws FuzzyEngineException 
	 */
	public void build(FuzzyEngine engine) throws FuzzyEngineException;

}
