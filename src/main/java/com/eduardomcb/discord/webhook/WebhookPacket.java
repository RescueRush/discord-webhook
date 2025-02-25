package com.eduardomcb.discord.webhook;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduardomcb.discord.webhook.models.Embed;
import com.eduardomcb.discord.webhook.models.Message;

public class WebhookPacket implements JSONType.Object {

	private Embed[] embeds = null;

	private Message message;

	public WebhookPacket() {
	}

	/**
	 * Sets the message content and other details.
	 *
	 * @param message The message object containing username, avatar URL, and
	 *                content.
	 * @return An instance of WebhookManager with the updated message details.
	 */
	public WebhookPacket setMessage(Message message) {
		this.message = message;
		return this;
	}

	public Embed[] getEmbeds() {
		return embeds;
	}

	/**
	 * Sets the array of embeds for the webhook message.
	 *
	 * @param embeds The array of embeds to be attached to the message.
	 * @return An instance of WebhookManager with the updated array of embeds.
	 */
	public WebhookPacket setEmbeds(Embed[] embeds) {
		this.embeds = embeds;
		return this;
	}

	/**
	 * Creates a JSON object representing the message content and embeds.
	 *
	 * @return The JSON object containing the message details.
	 */
	@Override
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("username", message.getUsername());
			obj.put("avatar_url", message.getAvatarUrl());
			obj.put("content", message.getContent());

			JSONArray embedsArr = new JSONArray();

			if (embeds != null) {
				for (Embed item : this.embeds) {
					embedsArr.put(item.toJson());
				}

				if (embedsArr.length() != 0) {
					obj.put("embeds", embedsArr);
				}
			}
		} catch (JSONException e) {
			handleJsonException(e);
		}
		return obj;
	}

	/**
	 * Handles JSON-related exceptions by logging them and invoking the onFailure
	 * callback.
	 *
	 * @param e The JSONException that occurred.
	 */
	private void handleJsonException(JSONException e) {
		Logger logger = Logger.getLogger(WebhookPacket.class.getName());
		logger.log(Level.SEVERE, "JSON Error: ", e);
	}
}
