package com.ksubaka.common.utils.http;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Helper to read data from a URL.
 * 
 * @author Luis Matos
 */
public class UrlReader {
	private static final String HEADER_USER_AGENT = "User-Agent";
	private static final String USER_AGENT = "Ksubaca";

	/**
	 * Reads data as {@link String} from a given URL.
	 * 
	 * @param url The URL to read from.
	 * @return A {@link Response} with content.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Response readFromUrl(String url) throws MalformedURLException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add User-Agent header
		request.addHeader(HEADER_USER_AGENT, USER_AGENT);
		HttpResponse httpResponse = client.execute(request);

		Response response = new Response(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());

		if (httpResponse.getEntity().getContent() != null) {
			// get content in bytes to be generic
			byte[] responseContent = IOUtils.toByteArray(httpResponse.getEntity().getContent());
			response.setContent(responseContent);
		}

		return response;
	}
}
