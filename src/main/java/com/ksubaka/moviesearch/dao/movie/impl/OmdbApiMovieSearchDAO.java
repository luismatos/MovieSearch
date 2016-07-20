package com.ksubaka.moviesearch.dao.movie.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.dao.movie.AbstractHttpMovieSearchDAO;
import com.ksubaka.moviesearch.domain.Movie;

/**
 * Implementation for OMDb API - The Open Movie Database.
 * 
 * @see <a href="http://www.omdbapi.com">http://www.omdbapi.com</a>
 * @author Luis Matos
 */
public class OmdbApiMovieSearchDAO extends AbstractHttpMovieSearchDAO implements JsonDeserializer<Movie> {
	protected static final Logger LOG = LogManager.getLogger(OmdbApiMovieSearchDAO.class.getSimpleName());

	protected String urlImdbID;
	protected int resultsPerPage;

	/**
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 * 
	 * <p>
	 * With OMDb API we need to access two URLs to return all the details we need (e.g. director). The parameter "t" only returns one result (with director). The parameter "s" returns many results
	 * (without director). The combination of both will return Title + Year + Director.
	 * </p>
	 */
	@Override
	public Collection<Movie> search(String criteria) throws SearchException {
		Collection<Movie> results = new LinkedList<>();

		int page = 1;
		int totalPages = 0;

		LOG.debug("Searching '{}' in '{}'", criteria, this.getClass().getSimpleName());

		do {
			// get movies from 'urlSearch'. This call will return a maximum of results that do not include 'director'. We need another call to 'urlImdbID' - for each movie - to get the 'director'
			SearchResponse searchResponse;
			String url = null;

			try {
				// create search URL based on criteria and current page
				url = String.format(this.urlSearch, prepareCriteria(criteria), page);

				LOG.trace("Fetching movies from '{}'", url);

				searchResponse = fetchObject(SearchResponse.class, url);
			} catch (Exception ex) {
				LOG.error("Unable to read from '{}'", url, ex);
				throw new SearchException("Unable to read from " + url, ex);
			}

			if (!searchResponse.response) {
				// just abort operation in case of error
				LOG.error("Error processing response from '{}'; '{}'", url, searchResponse.error);
				throw new SearchException("Error processing response from " + url + "; " + searchResponse.error);
			}

			// calculate the number of pages (API returns a maximum of 10 results per page)
			// we repeat this operation for each page but math operations are cheaper than conditional jumps
			totalPages = (int) Math.ceil((float) searchResponse.totalResults / (float) resultsPerPage);

			LOG.trace("Processing page '{}' of '{}'", page, totalPages);

			try {
				// get all the IMDB IDs from response and then load movie details
				for (Result result : searchResponse.results) {
					url = String.format(this.urlImdbID, result.imdbID);

					LOG.trace("Fetching movie details from '{}'; IMDB ID '{}'; page '{}' of '{}'", url, result.imdbID, page, totalPages);

					Movie movie = getMovieDetails(url, result.imdbID);

					results.add(movie);
				}
			} catch (Exception ex) {
				LOG.error("Unable to read from '{}'", url, ex);
				throw new SearchException("Unable to read from " + url, ex);
			}
		} while (page++ < totalPages);

		return results;
	}

	/**
	 * Loads {@link Movie} details;
	 * 
	 * @param url The URL to fetch {@link Movie} details.
	 * @param imdbID The IMDB ID of the {@link Movie}.
	 * @return A {@link Movie} with all the details.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SearchException
	 */
	protected Movie getMovieDetails(String url, String imdbID) throws MalformedURLException, IOException, SearchException {
		return fetchObject(Movie.class, url, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Movie deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = jsonElement.getAsJsonObject();

		return new Movie(jsonObject.get("Title").getAsString(), jsonObject.get("Year").getAsString(), jsonObject.get("Director").getAsString());
	}

	/**
	 * Helper class used only to parse internal responses.
	 * 
	 * @author Luis Matos
	 */
	public static class SearchResponse {
		@SerializedName("Search")
		private Collection<Result> results;
		@SerializedName("totalResults")
		private int totalResults;
		@SerializedName("Response")
		private boolean response;
		@SerializedName("Error")
		private String error;

		public Collection<Result> getResults() {
			return results;
		}

		public void setResults(Collection<Result> results) {
			this.results = results;
		}

		public int getTotalResults() {
			return totalResults;
		}

		public void setTotalResults(int totalResults) {
			this.totalResults = totalResults;
		}

		public boolean isResponse() {
			return response;
		}

		public void setResponse(boolean response) {
			this.response = response;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}
	}

	/**
	 * Helper class used only to parse internal responses.
	 * 
	 * @author Luis Matos
	 */
	public static class Result {
		@SerializedName("Title")
		private String title;
		@SerializedName("Year")
		private String year;
		@SerializedName("imdbID")
		private String imdbID;
		@SerializedName("Type")
		private String type;
		@SerializedName("Poster")
		private String poster;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getImdbID() {
			return imdbID;
		}

		public void setImdbID(String imdbID) {
			this.imdbID = imdbID;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getPoster() {
			return poster;
		}

		public void setPoster(String poster) {
			this.poster = poster;
		}
	}

	public String getUrlImdbID() {
		return urlImdbID;
	}

	public void setUrlImdbID(String urlImdbID) {
		this.urlImdbID = urlImdbID;
	}

	public int getResultsPerPage() {
		return resultsPerPage;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}
}
