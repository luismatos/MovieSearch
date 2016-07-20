package com.ksubaka.moviesearch.dao.movie;

import java.util.Collection;

import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.domain.Movie;

public interface MovieSearchDAO {
	/**
	 * Searches for movies based on a given criteria.
	 * 
	 * @param criteria Search criteria.
	 * @return {@link Collection} of {@link Movie}s found in all APIs.
	 * @throws SearchException If an error is found during the execution of the method.
	 */
	Collection<Movie> search(String criteria) throws SearchException;
}
