package com.eduardomcb.discord.webhook.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduardomcb.discord.webhook.JSONType;

public class Field implements JSONType.Object {

	private String name;
	private String value;
	private boolean inline = false;

	public Field(String name, String value, boolean inline) {
		this.name = name;
		this.value = value;
		this.inline = inline;
	}

	@Override
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", this.name);
			obj.put("value", this.value);
			obj.put("inline", this.inline);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
