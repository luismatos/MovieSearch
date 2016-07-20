package com.ksubaka.moviesearch.business.service;

import java.util.Collection;

import com.ksubaka.moviesearch.business.exception.SearchException;

/**
 * Search Manager interface.
 * 
 * @author Luis Matos
 */
public interface SearchManager<T> {
	/**
	 * Searches based on a given criteria.
	 * 
	 * @param criteria Search criteria
	 * @return {@link Collection} of {@link T}s found.
	 * @throws SearchException If an error is found during the execution of the method.
	 */
	Collection<T> search(String criteria) throws SearchException;
}
