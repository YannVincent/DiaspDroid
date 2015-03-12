package fr.android.scaron.diaspdroid.controler;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.RequiresCookie;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.SetsCookie;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

//import org.androidannotations.annotations.rest.Post;

/**
 * Created by Sébastien on 11/03/2015.
 */
@Rest(converters={GsonHttpMessageConverter.class, StringHttpMessageConverter.class})
public interface LoginService {

    @Get("/users/sign_in")
    @Accept(MediaType.TEXT_HTML)
    @SetsCookie("_diaspora_session")
    String getLoginHTML();


    @org.androidannotations.annotations.rest.Post("/users/sign_in")
    @Accept(MediaType.APPLICATION_FORM_URLENCODED)
    @RequiresHeader("x-csrf-token")
    @RequiresCookie("_diaspora_session")
    String postLogin();



    void setRootUrl(String rootUrl);
    void setCookie(String name, String value);
    String getCookie(String name);
    void setHeader(String name, String value);
}
