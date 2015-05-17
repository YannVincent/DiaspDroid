package fr.android.scaron.diaspdroid.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * Created by Sebastien on 13/05/2015.
 * Ajout pour le passage en version 0.5 de diaspora
 //"created_at": "2015-05-13T12:33:30.521Z", evolution diaspora code 0.5
 */
public class CustGsonHttpMessageConverter extends GsonHttpMessageConverter {
    protected static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    protected static Gson buildGson() {
            GsonBuilder gsonBuilder = new GsonBuilder();

            gsonBuilder.setDateFormat(DATE_FORMAT);

            return gsonBuilder.create();
    }

    public CustGsonHttpMessageConverter()  {
            super(buildGson());
    }
}