package com.pepabo.jodo.jodoroid;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        }

        try {
            return ISODateTimeFormat.dateTimeParser().parseDateTime(json.getAsString()).toDate();
        } catch (IllegalArgumentException e) {
            throw new JsonSyntaxException(json.getAsString(), e);
        }
    }
}
