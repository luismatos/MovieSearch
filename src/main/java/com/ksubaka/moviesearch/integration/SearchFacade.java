package com.ksubaka.moviesearch.integration;

import java.util.Collection;

import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.domain.Movie;

/**
 * Facade to access Search features.
 * 
 * @author Luis Matos
 */
public interface SearchFacade {
	/**
	 * Searches for movies against a collection of APIs, based on a given criteria.
	 * 
	 * @param apis Collection of APIs to search.
	 * @param criteria Search criteria.
	 * @return {@link Collection} of {@link Movie}s found in all APIs.
	 * @throws SearchException If an error is found during the execution of the method.
	 */
	Collection<Movie> movieSearch(Collection<String> apis, String criteria) throws SearchException;
}
