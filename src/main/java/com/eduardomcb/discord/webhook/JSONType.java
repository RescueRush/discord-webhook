package com.eduardomcb.discord.webhook;

import org.json.JSONArray;
import org.json.JSONObject;

public interface JSONType {

	public interface Object {
		public JSONObject toJson();
	}

	public interface Arraye {
		public JSONArray toJson();
	}
}
