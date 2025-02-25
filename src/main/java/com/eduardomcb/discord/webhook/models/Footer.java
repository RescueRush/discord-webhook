package com.eduardomcb.discord.webhook.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduardomcb.discord.webhook.JSONType;

public class Footer implements JSONType.Object{
	
	private String text;
	private String icon_url;
	
	public Footer(String text, String icon_url) {
		this.text = text;
		this.icon_url = icon_url;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("text", this.text);
			obj.put("icon_url", this.icon_url);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
