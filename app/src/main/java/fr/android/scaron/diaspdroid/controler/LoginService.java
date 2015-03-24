package fr.android.scaron.diaspdroid.controler;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresCookie;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.SetsCookie;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * Created by Sébastien on 11/03/2015.
 */
@Rest(converters={GsonHttpMessageConverter.class, StringHttpMessageConverter.class}, interceptors = { AuthenticationInterceptor.class })
public interface LoginService extends RestClientErrorHandling {

    @Get("/users/sign_in")
    @Accept(MediaType.TEXT_HTML)
    @SetsCookie("_diaspora_session")
    String getLoginHTML();


    @Post("/users/sign_in")
    @Accept(MediaType.TEXT_HTML)
//    @RequiresHeader("x-csrf-token")
    @RequiresCookie("_diaspora_session")
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    String postLogin();



    void setRootUrl(String rootUrl);
    void setCookie(String name, String value);
    String getCookie(String name);
    void setHeader(String name, String value);
}
