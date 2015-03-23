package fr.android.scaron.diaspdroid.controler;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.RequiresCookie;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.List;

import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by Sébastien on 11/03/2015.
 */
@Rest(converters={GsonHttpMessageConverter.class})
@Accept(MediaType.APPLICATION_JSON)
public interface StreamPostsService extends RestClientErrorHandling {

    @Get("/stream")
    @RequiresCookie("remember_user_token")
    @RequiresHeader("x-csrf-token")
    List<Post> getStream();

    void setRootUrl(String rootUrl);
    void setCookie(String name, String value);
    String getCookie(String name);
    void setHttpBasicAuth(String username, String password);
    void setHeader(String name, String value);
}