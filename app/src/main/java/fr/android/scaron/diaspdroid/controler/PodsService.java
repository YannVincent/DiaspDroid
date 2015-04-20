package fr.android.scaron.diaspdroid.controler;

import com.google.gson.JsonObject;

import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresCookie;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.SetsCookie;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.util.List;

import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.model.Pods;
import fr.android.scaron.diaspdroid.model.UploadResult;

/**
 * Created by Sébastien on 11/03/2015.
 */
@Rest(converters={GsonHttpMessageConverter.class, StringHttpMessageConverter.class,  ByteArrayHttpMessageConverter.class, FormHttpMessageConverter.class})
public interface PodsService extends RestClientErrorHandling {

    @Get("http://podupti.me/api.php?key=4r45tg&format=json")
    Pods getPods();
}
