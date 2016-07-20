package com.ksubaka.moviesearch.integration.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.business.service.movie.MovieSearchManager;
import com.ksubaka.moviesearch.domain.Movie;
import com.ksubaka.moviesearch.integration.SearchFacade;

/**
 * Default implementation of Search facade.
 * 
 * @author Luis Matos
 */
public class DefaultSearchFacade implements SearchFacade {
	private static final Logger LOG = LogManager.getLogger(DefaultSearchFacade.class.getSimpleName());

	private Map<String, MovieSearchManager> movieSearchManagers;

	/**
	 * {@inheritDoc}
	 */
	public Collection<Movie> movieSearch(Collection<String> apis, String criteria) throws SearchException {
		Collection<Movie> results = new LinkedList<>();

		// loop over all APIs and load corresponding MovieSearchManager to search
		for (String api : apis) {
			LOG.info("Loading '{}' API", api);

			MovieSearchManager movieSearchManager = movieSearchManagers.get(api);

			if (movieSearchManager == null) {
				LOG.warn("'{}' not initialised, ignoring...", api);
				continue;
			}

			// add results to the collection
			results.addAll(movieSearchManager.search(criteria));
		}

		return results;
	}

	public Map<String, MovieSearchManager> getMovieSearchManagers() {
		return movieSearchManagers;
	}

	public void setMovieSearchManagers(Map<String, MovieSearchManager> searchManagers) {
		this.movieSearchManagers = searchManagers;
	}
}
