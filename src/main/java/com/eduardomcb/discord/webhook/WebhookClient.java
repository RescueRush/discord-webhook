package com.eduardomcb.discord.webhook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

public class WebhookClient {

	private String webhookUrl;

	public WebhookClient() {
	}

	public WebhookClient(String webhookUrl) {
		this.webhookUrl = webhookUrl;
	}

	/**
	 * Sets the webhook URL for a channel.
	 *
	 * @param webhookUrl The webhook URL to be set for the channel.
	 * @return An instance of WebhookManager with the updated webhook URL if the
	 *         provided URL is valid.
	 */
	public WebhookClient setChannelUrl(String webhookUrl) {
		/**
		 * Checks if the provided webhook URL is in a valid format. The valid format is
		 * "https://discord.com/api/webhooks/{CHANNEL_ID}/{TOKEN}".
		 *
		 * @param webhookUrl The webhook URL to be checked.
		 * @return true if the URL is in a valid format, false otherwise.
		 */
		if (!webhookUrl.matches("https://discord\\.com/api/webhooks/[0-9]+/[A-Za-z0-9_\\-]+")) {
			throw new IllegalArgumentException("Link isn't a discord.com webhook.");
		}

		// If the provided URL is valid, update the channel's webhook URL.
		this.webhookUrl = webhookUrl;

		// Return the updated instance of WebhookManager.
		return this;
	}

	public WebhookClient send(WebhookPacket packet) {
		return send(packet.toJson(), this::onFailure, this::onSuccess);
	}

	public WebhookClient send(WebhookPacket packet, Consumer<Exception> failure, Consumer<String> success) {
		return send(packet.toJson(), failure, success);
	}

	/**
	 * Sends a message to a specified webhook URL.
	 *
	 * @param webhookUrl The URL of the webhook to send the message to.
	 * @param message    The JSON message to be sent.
	 */
	protected WebhookClient send(JSONObject message, Consumer<Exception> failure, Consumer<String> success) {
		try {
			// Create a URI object from the provided webhook URL
			URI uri = new URI(webhookUrl);

			// Open a connection to the webhook URL
			HttpsURLConnection connection = (HttpsURLConnection) uri.toURL().openConnection();
			connection.setRequestProperty("Content-Type", "application/json");
			String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
			connection.setRequestProperty("User-Agent", userAgent);

			// Enable output for sending POST data
			connection.setDoOutput(true);

			// Write the JSON message to the connection's output stream
			try (OutputStream stream = connection.getOutputStream()) {
				stream.write(message.toString().getBytes(StandardCharsets.UTF_8));
			}

			// Get the HTTP response code
			int responseCode = connection.getResponseCode();
			// System.out.println("Response Code: " + responseCode);

			// Handle response based on response code
			if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_NO_CONTENT) {
				handleSuccessfulResponse(connection, success);
			} else {
				handleErrorResponse(connection, responseCode, failure);
			}
		} catch (IOException | URISyntaxException e) {
			// System.err.println("I/O Error: " + e.getMessage());
			failure.accept(e);
		}

		return this;
	}

	protected void onFailure(Exception e) {
	}

	protected void onSuccess(String message) {
	}

	/**
	 * Handles successful responses from the webhook server.
	 *
	 * @param connection The connection from which to read the response.
	 * @param success
	 * @throws IOException If an I/O error occurs while reading the response.
	 */
	private void handleSuccessfulResponse(HttpsURLConnection connection, Consumer<String> success) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			success.accept(response.toString());
		}
	}

	/**
	 * Handles error responses from the webhook server.
	 *
	 * @param connection   The connection from which to read the error response.
	 * @param responseCode The HTTP response code indicating the type of error.
	 * @param failure
	 * @throws IOException If an I/O error occurs while reading the error response.
	 */
	private void handleErrorResponse(HttpsURLConnection connection, int responseCode, Consumer<Exception> failure) throws IOException {
		try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(responseCode == HttpsURLConnection.HTTP_NOT_FOUND ? connection.getErrorStream() : connection.getInputStream()))) {

			String errorInputLine;
			StringBuilder errorResponse = new StringBuilder();

			while ((errorInputLine = errorReader.readLine()) != null) {
				errorResponse.append(errorInputLine);
			}

			failure.accept(new HTTPException(responseCode, errorResponse.toString()));
		}
	}

	@Override
	public String toString() {
		return "WebhookClient [webhookUrl=" + webhookUrl + "]";
	}

}
