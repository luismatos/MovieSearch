package com.ksubaka.moviesearch.business.service.movie;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.dao.movie.MovieSearchDAO;
import com.ksubaka.moviesearch.domain.Movie;

public abstract class AbstractMovieSearchManager implements MovieSearchManager {
	private static final Logger LOG = LogManager.getLogger(AbstractMovieSearchManager.class.getSimpleName());

	private MovieSearchDAO movieSearchDAO;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Movie> search(String criteria) throws SearchException {
		LOG.info("Searching '{}' in '{}'", criteria, this.getClass().getSimpleName());

		return this.movieSearchDAO.search(criteria);
	}

	public MovieSearchDAO getMovieSearchDAO() {
		return movieSearchDAO;
	}

	public void setMovieSearchDAO(MovieSearchDAO movieSearchDAO) {
		this.movieSearchDAO = movieSearchDAO;
	}
}
