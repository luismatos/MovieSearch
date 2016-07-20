package com.ksubaka.moviesearch.ui.impl.console;

import java.util.Arrays;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ksubaka.moviesearch.business.exception.SearchException;
import com.ksubaka.moviesearch.domain.Movie;
import com.ksubaka.moviesearch.integration.SearchFacade;
import com.ksubaka.moviesearch.ui.MovieSearch;

/**
 * UI Console implementation of Movie Search.
 * 
 * @author Luis Matos
 */
public class ConsoleMovieSearch implements MovieSearch {
	private static final Logger LOG = LogManager.getLogger(ConsoleMovieSearch.class.getSimpleName());
	private static Collection<String> apis;
	private static String criteria;
	private static ConsoleMovieSearch consoleMovieSearch;

	private SearchFacade searchFacade;

	/**
	 * Application entry point.
	 * 
	 * @param args Console arguments.
	 */
	public static void main(String[] args) {
		init();

		LOG.info("Searching results");

		try {
			for (Movie result : consoleMovieSearch.search(apis, criteria)) {
				System.out.println(result);
			}
		} catch (SearchException ex) {
			LOG.error(ex);
		} catch (Exception ex) {
			LOG.info("Unknown error occurred", ex);
		}
	}

	/**
	 * Initialise the application, including Spring context.
	 */
	public static void init() {
		LOG.info("Initialising Application");

		// initialise Spring Context
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		SearchFacade searchFacade = context.getBean("defaultSearchFacade", SearchFacade.class);

		// load VM arguments
		String dApis = System.getProperty("api");
		apis = Arrays.asList(dApis.split(","));
		criteria = System.getProperty("movie");

		consoleMovieSearch = new ConsoleMovieSearch();
		consoleMovieSearch.setSearchFacade(searchFacade);

		LOG.info("Application Initialised");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Movie> search(Collection<String> apis, String criteria) throws SearchException {
		return searchFacade.movieSearch(apis, criteria);
	}

	public SearchFacade getSearchFacade() {
		return searchFacade;
	}

	public void setSearchFacade(SearchFacade searchFacade) {
		this.searchFacade = searchFacade;
	}
}
