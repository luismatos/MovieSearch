package com.ksubaka.moviesearch.dao.movie;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.ksubaka.common.utils.http.Response;
import com.ksubaka.common.utils.http.UrlReader;
import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.dao.movie.impl.MoviesApiMovieSearchDAO;
import com.ksubaka.moviesearch.domain.Movie;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class AbstractHttpMovieSearchDAOTest {
	@Test
	public void testPrepareCriteria1() throws Exception {
		MoviesApiMovieSearchDAO moviesApi = new MoviesApiMovieSearchDAO();

		String criteria = Whitebox.invokeMethod(moviesApi, "prepareCriteria", "   Indiana Jones    ");

		Assert.assertEquals("Criteria not prepared", "indiana+jones", criteria);
	}

	@Test
	public void testPrepareCriteria2() throws Exception {
		MoviesApiMovieSearchDAO moviesApi = new MoviesApiMovieSearchDAO();

		String criteria = Whitebox.invokeMethod(moviesApi, "prepareCriteria", "   ");

		Assert.assertEquals("Criteria not empty", "", criteria);
	}

	@Test
	public void testPrepareCriteria3() throws Exception {
		MoviesApiMovieSearchDAO moviesApi = new MoviesApiMovieSearchDAO();

		String criteria = Whitebox.invokeMethod(moviesApi, "prepareCriteria", "");

		Assert.assertEquals("Criteria not empty", "", criteria);
	}

	@Test
	public void testPrepareCriteria4() throws Exception {
		MoviesApiMovieSearchDAO moviesApi = new MoviesApiMovieSearchDAO();

		String criteria = Whitebox.invokeMethod(moviesApi, "prepareCriteria", isNull(String.class));

		Assert.assertEquals("Criteria not empty", "", criteria);
	}

	@Test
	@PrepareForTest({ UrlReader.class })
	public void shouldFetchData() throws Exception {
		PowerMockito.mockStatic(UrlReader.class);

		PowerMockito.when(UrlReader.readFromUrl(anyString())).thenReturn(new Response(1, "OK", "{\"title\": \"Raiders of the Lost Ark\", \"year\": \"1981\"}".getBytes()));

		MoviesApiMovieSearchDAO moviesApi = new MoviesApiMovieSearchDAO();

		Movie movie = Whitebox.invokeMethod(moviesApi, "fetchObject", Movie.class, "");

		Assert.assertEquals("title not Raiders of the Lost Ark", "Raiders of the Lost Ark", movie.getTitle());
		Assert.assertEquals("year not 1981", "1981", movie.getYear());

	}

	@Test(expected = SearchException.class)
	@PrepareForTest({ UrlReader.class })
	public void shouldThrowException() throws Exception {
		PowerMockito.mockStatic(UrlReader.class);

		PowerMockito.when(UrlReader.readFromUrl(anyString())).thenReturn(new Response(500, "Internal Server Error"));

		MoviesApiMovieSearchDAO moviesApi = new MoviesApiMovieSearchDAO();

		Whitebox.invokeMethod(moviesApi, "fetchObject", Movie.class, "");

		Assert.fail("Exception not thrown");
	}

	@Test
	@PrepareForTest({ UrlReader.class, MoviesApiMovieSearchDAO.class })
	public void shouldCallDeserialize() throws Exception {
		PowerMockito.mockStatic(UrlReader.class);

		PowerMockito.when(UrlReader.readFromUrl(anyString())).thenReturn(new Response(1, "OK", "[{\"title\": \"Raiders of the Lost Ark\", \"year\": \"1981\"}]".getBytes()));

		MoviesApiMovieSearchDAO moviesApi = Mockito.spy(MoviesApiMovieSearchDAO.class);

		moviesApi.setUrlSearch("");
		Collection<Movie> movies = moviesApi.search("");

		Mockito.verify(moviesApi, times(1)).deserialize(anyObject(), anyObject(), anyObject());
		verifyPrivate(moviesApi, times(1)).invoke("fetchCollection", Matchers.<Class<?>>any(), anyString(), anyObject());

		Assert.assertEquals("movies size not 1", 1, movies.size());

		Movie movie = movies.toArray(new Movie[1])[0];

		Assert.assertEquals("title not Raiders of the Lost Ark", "Raiders of the Lost Ark", movie.getTitle());
		Assert.assertEquals("year not 1981", "1981", movie.getYear());
	}
}
