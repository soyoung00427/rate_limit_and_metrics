package com.icd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectMapper {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String writeValueAsString(Object value) {
        return gson.toJson(value);
    }

    public <T> T readValue(String content, Class<T> valueType) {
        return gson.fromJson(content, valueType);
    }
}
