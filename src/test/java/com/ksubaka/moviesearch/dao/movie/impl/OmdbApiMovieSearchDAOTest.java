package com.ksubaka.moviesearch.dao.movie.impl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.JsonObject;
import com.ksubaka.moviesearch.dao.movie.AbstractHttpMovieSearchDAO;
import com.ksubaka.moviesearch.dao.movie.impl.OmdbApiMovieSearchDAO.Result;
import com.ksubaka.moviesearch.dao.movie.impl.OmdbApiMovieSearchDAO.SearchResponse;
import com.ksubaka.moviesearch.domain.Movie;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class OmdbApiMovieSearchDAOTest {
	@Test
	@PrepareForTest(AbstractHttpMovieSearchDAO.class)
	public void shouldReturnMovies() throws Exception {
		OmdbApiMovieSearchDAO spy = PowerMockito.spy(new OmdbApiMovieSearchDAO());
		spy.setUrlSearch("");
		spy.setUrlImdbID("");
		spy.setResultsPerPage(10);

		SearchResponse searchResponse = new OmdbApiMovieSearchDAO.SearchResponse();
		searchResponse.setResponse(true);
		searchResponse.setTotalResults(2);

		Collection<Movie> expected = new LinkedList<>();
		expected.add(new Movie("Raiders of the Lost Ark", "1981", "Steven Spielberg"));

		Collection<Result> results = new LinkedList<>();
		Result result = new OmdbApiMovieSearchDAO.Result();
		result.setImdbID("1");
		result.setTitle("Raiders of the Lost Ark");
		results.add(result);

		searchResponse.setResults(results);

		PowerMockito.doReturn(searchResponse).when(spy, "fetchObject", Matchers.<Class<?>>any(), anyString());

		Movie movie = new Movie("Raiders of the Lost Ark", "1981", "Steven Spielberg");

		PowerMockito.doReturn(movie).when(spy, "fetchObject", Matchers.<Class<?>>any(), anyString(), anyObject());

		Collection<Movie> actual = spy.search("   Indiana Jones    ");

		Assert.assertTrue("Collections not equal", CollectionUtils.isEqualCollection(expected, actual));
	}

	@Test
	@PrepareForTest(AbstractHttpMovieSearchDAO.class)
	public void shouldDeserialize() throws Exception {
		JsonObject jsonElement = new JsonObject();
		jsonElement.addProperty("Title", "Indiana Jones and the Last Crusade");
		jsonElement.addProperty("Year", "1989");
		jsonElement.addProperty("Runtime", "127 min");
		jsonElement.addProperty("Director", "Steven Spielberg");
		jsonElement.addProperty("imdbRating", "8.3");
		jsonElement.addProperty("Response", "True");

		OmdbApiMovieSearchDAO omdbApi = new OmdbApiMovieSearchDAO();

		Movie movie = omdbApi.deserialize(jsonElement, null, null);

		Assert.assertEquals("Title not Indiana Jones and the Last Crusade", "Indiana Jones and the Last Crusade", movie.getTitle());
		Assert.assertEquals("Year not 1989", "1989", movie.getYear());
		Assert.assertEquals("Director not Steven Spielberg", "Steven Spielberg", movie.getDirector());
	}
}
