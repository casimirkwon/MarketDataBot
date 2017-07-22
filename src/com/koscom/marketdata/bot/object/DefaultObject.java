package com.koscom.marketdata.bot.object;

import java.lang.reflect.Type;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public abstract class DefaultObject implements TelegramObject {

	private transient Gson gson;
	
	
	/**
	 * 
	 */
	public DefaultObject() {
		gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	}

	@Override
	public String toJsonString() {
		if( gson == null) 
			gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		return gson.toJson(this);
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}

//	@Override
//	public <T> T fromJsonString(String json, Class<T> clazz) {
//		return gson.fromJson(json, clazz);
//	}
	
	public static <T> T fromJsonString(String json, Class<T> clazz) {
		return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create().fromJson(json, clazz);
	}

	public static <T> T fromJsonString(String json, Type type) {
		return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create().fromJson(json, type);
	}
}
