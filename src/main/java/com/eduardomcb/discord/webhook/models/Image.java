package com.eduardomcb.discord.webhook.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduardomcb.discord.webhook.JSONType;

public class Image implements JSONType.Object {

	private String url;

	public Image(String url) {
		this.url = url;
	}

	@Override
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("url", this.url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
