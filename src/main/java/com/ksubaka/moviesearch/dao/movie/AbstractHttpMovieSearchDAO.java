package com.ksubaka.moviesearch.dao.movie;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.ksubaka.common.utils.http.Response;
import com.ksubaka.common.utils.http.UrlReader;
import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.domain.Movie;

/**
 * Abstract class to handle HTTP requests/responses.
 * 
 * @author Luis Matos
 */
public abstract class AbstractHttpMovieSearchDAO implements MovieSearchDAO {
	protected String urlSearch;

	/**
	 * Prepares a given criteria to be used as query string parameter, e.g. ' query search ' -> 'query+search'.
	 * 
	 * @param criteria The original criteria.
	 * @return The prepared criteria.
	 */
	protected String prepareCriteria(String criteria) {
		if (criteria == null || criteria.isEmpty()) {
			return "";
		}

		return criteria.trim().replaceAll(" ", "+").toLowerCase();
	}

	/**
	 * Reads data from a given URL and parses the Json content to a {@link T} object.
	 * 
	 * @param classType The class type of the object to return.
	 * @param url The URL to read data from.
	 * @return An instance of {@link T} fully populated.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SearchException
	 */
	protected <T> T fetchObject(Class<T> classType, String url) throws MalformedURLException, IOException, SearchException {
		String json = readStringFromUrl(url);

		return new Gson().fromJson(json, classType);
	}

	/**
	 * Reads data from a given URL and parses the Json content to a {@link T} object.
	 * 
	 * @param classType The class type of the object to return.
	 * @param url The URL to read data from.
	 * @param deserializer The custom deserializer to parse the Json content to {@link T} object.
	 * @return An instance of {@link T} fully populated.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SearchException
	 */
	protected <T> T fetchObject(Class<T> classType, String url, JsonDeserializer<T> deserializer) throws MalformedURLException, IOException, SearchException {
		String json = readStringFromUrl(url);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(classType, deserializer);
		Gson gson = gsonBuilder.create();

		Type listType = new TypeToken<Movie>() {
		}.getType();
		return gson.fromJson(json, listType);
	}

	/**
	 * Reads data from a given URL and parses the Json content to a {@link Collection<T>} object.
	 * 
	 * @param classType The class type of the object to return.
	 * @param url The URL to read data from.
	 * @return An {@link Collection<T>} fully populated.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SearchException
	 */
	protected <T> Collection<T> fetchCollection(Class<T> classType, String url) throws MalformedURLException, IOException, SearchException {
		String json = readStringFromUrl(url);

		Type listType = new TypeToken<Collection<T>>() {
		}.getType();
		return new Gson().fromJson(json, listType);
	}

	/**
	 * Reads data from a given URL and parses the Json content to a {@link Collection<T>} object.
	 * 
	 * @param classType The class type of the object to return.
	 * @param url The URL to read data from.
	 * @param deserializer The custom deserializer to parse the Json content to {@link T} object.
	 * @return An {@link Collection<T>} fully populated.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SearchException
	 */
	protected <T> Collection<T> fetchCollection(Class<T> classType, String url, JsonDeserializer<T> deserializer) throws MalformedURLException, IOException, SearchException {
		String json = readStringFromUrl(url);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(classType, deserializer);
		Gson gson = gsonBuilder.create();

		Type listType = new TypeToken<Collection<Movie>>() {
		}.getType();
		return gson.fromJson(json, listType);
	}

	/**
	 * Reads and returns data from a URL and returns. Assuming HTTP Content-Type is application/json and a known Json schema.
	 * 
	 * @param url The URL to read from.
	 * 
	 * @return Data read from URL.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SearchException
	 */
	private String readStringFromUrl(String url) throws MalformedURLException, IOException, SearchException {
		Response response = UrlReader.readFromUrl(url);

		// on error, abort and pass the HTTP reason phrase as message
		if (response.isError()) {
			throw new SearchException(response.getReasonPhrase());
		}

		return new String(response.getContent(), "UTF-8");
	}

	public String getUrlSearch() {
		return urlSearch;
	}

	public void setUrlSearch(String urlSearch) {
		this.urlSearch = urlSearch;
	}
}
