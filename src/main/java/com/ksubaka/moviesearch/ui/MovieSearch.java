package com.ksubaka.moviesearch.ui;

import java.util.Collection;

import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.domain.Movie;

/**
 * Interface for all UI implementations.
 * 
 * @author Luis Matos
 */
public interface MovieSearch {
	/**
	 * Searches for movies against a collection of APIs, based on a given criteria.
	 * 
	 * @param apis Collection of APIs to search.
	 * @param criteria Search criteria.
	 * @return {@link Collection} of {@link Movie}s found in all APIs.
	 * @throws SearchException If an error is found during the execution of the method.
	 */
	Collection<Movie> search(Collection<String> apis, String criteria) throws SearchException;
}
