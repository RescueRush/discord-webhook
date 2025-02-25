package com.eduardomcb.discord.webhook.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduardomcb.discord.webhook.JSONType;

public class Author implements JSONType.Object {
	
	private String name;
	private String url;
	private String icon;
	
	public Author(String name, String url, String icon) {
		this.name = name;
		this.url = url;
		this.icon = icon;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", this.name);
			obj.put("url", this.url);
			obj.put("icon_url", this.icon);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
