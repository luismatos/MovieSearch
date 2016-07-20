package com.ksubaka.common.utils.http;

/**
 * Represents a HTTP Response.
 * 
 * @author Luis Matos
 *
 */
public class Response {
	private int statusCode;
	private String reasonPhrase;
	private byte[] content;

	public Response(int statusCode, String reasonPhrase) {
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	public Response(int statusCode, String reasonPhrase, byte[] content) {
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
		this.content = content;
	}

	/**
	 * Checks if the response was successful.
	 * 
	 * @return true if the response was successful; false otherwise.
	 */
	public boolean isSuccess() {
		return this.statusCode >= 200 && this.statusCode < 300;
	}

	/**
	 * Checks if the response was unsuccessful.
	 * 
	 * @return true if the response was unsuccessful; false otherwise.
	 */
	public boolean isError() {
		return isClientError() || isServerError();
	}

	/**
	 * Checks if the error was on client end.
	 * 
	 * @return true if the error was on client end; false otherwise.
	 */
	public boolean isClientError() {
		return this.statusCode >= 400 && this.statusCode < 500;
	}

	/**
	 * Checks if the error was on server end.
	 * 
	 * @return true if the error was on server end; false otherwise.
	 */
	public boolean isServerError() {
		return this.statusCode >= 500 && this.statusCode < 600;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
