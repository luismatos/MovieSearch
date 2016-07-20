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
import com.ksubaka.moviesearch.domain.Movie;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class MoviesApiMovieSearchDAOTest {
	@Test
	@PrepareForTest(AbstractHttpMovieSearchDAO.class)
	public void shouldReturnMovies() throws Exception {
		MoviesApiMovieSearchDAO spy = PowerMockito.spy(new MoviesApiMovieSearchDAO());
		spy.setUrlSearch("");

		Collection<Movie> expected = new LinkedList<>();
		expected.add(new Movie("Raiders of the Lost Ark", "1981", "Steven Spielberg"));
		expected.add(new Movie("Indiana Jones and the Temple of Doom", "1984", "Steven Spielberg"));
		expected.add(new Movie("Indiana Jones and the Last Crusade", "1989", "Steven Spielberg"));
		expected.add(new Movie("Indiana Jones and the Kingdom of the Crystal Skull", "2008", "Steven Spielberg"));

		PowerMockito.doReturn(expected).when(spy, "fetchCollection", Matchers.<Class<?>>any(), anyString(), anyObject());

		Collection<Movie> actual = spy.search("   Indiana Jones    ");

		Assert.assertTrue("Collections not equal", CollectionUtils.isEqualCollection(expected, actual));
	}

	@Test
	@PrepareForTest(AbstractHttpMovieSearchDAO.class)
	public void shouldDeserialize() throws Exception {
		JsonObject jsonElement = new JsonObject();
		jsonElement.addProperty("id", "0082971");
		jsonElement.addProperty("title", "Raiders of the Lost Ark");
		jsonElement.addProperty("year", "1981");
		jsonElement.addProperty("poster", "N/A");
		jsonElement.addProperty("type", "movie");

		MoviesApiMovieSearchDAO moviesApi = new MoviesApiMovieSearchDAO();

		Movie movie = moviesApi.deserialize(jsonElement, null, null);

		Assert.assertEquals("Title not Raiders of the Lost Ark", "Raiders of the Lost Ark", movie.getTitle());
		Assert.assertEquals("Year not 1981", "1981", movie.getYear());
		Assert.assertEquals("Director should be null", null, movie.getDirector());
	}
}
