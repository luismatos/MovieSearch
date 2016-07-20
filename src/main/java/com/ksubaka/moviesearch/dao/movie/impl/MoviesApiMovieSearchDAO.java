package com.ksubaka.moviesearch.dao.movie.impl;

import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.dao.movie.AbstractHttpMovieSearchDAO;
import com.ksubaka.moviesearch.domain.Movie;

/**
 * Implementation for SimAPI Movies - Simple Movies API.
 * 
 * @see <a href="https://www.moviesapi.com">https://www.moviesapi.com</a>
 * @author Luis Matos
 */
public class MoviesApiMovieSearchDAO extends AbstractHttpMovieSearchDAO implements JsonDeserializer<Movie> {
	private static final Logger LOG = LogManager.getLogger(MoviesApiMovieSearchDAO.class.getSimpleName());

	/**
	 * {@inheritDoc}
	 */
	public Collection<Movie> search(String criteria) throws SearchException {
		Collection<Movie> results = null;

		LOG.debug("Searching '{}' in '{}'", criteria, this.getClass().getSimpleName());

		String url = null;

		try {
			// create search URL based on criteria
			url = String.format(this.urlSearch, prepareCriteria(criteria));

			LOG.trace("Fetching movies from '{}'", url);

			results = fetchCollection(Movie.class, url, this);
		} catch (Exception ex) {
			LOG.error("Unable to read from '{}'", url, ex);
			throw new SearchException("Unable to read from " + url, ex);
		}

		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = jsonElement.getAsJsonObject();

		// this API has no director, pass null
		return new Movie(jsonObject.get("title").getAsString(), jsonObject.get("year").getAsString(), null);
	}
}
