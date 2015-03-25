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
import org.springframework.util.MultiValueMap;

import java.util.List;

import fr.android.scaron.diaspdroid.model.UploadResult;

/**
 * Created by Sébastien on 11/03/2015.
 */
@Rest(converters={GsonHttpMessageConverter.class, StringHttpMessageConverter.class}, interceptors = { AuthenticationInterceptor.class })
public interface DiasporaService extends RestClientErrorHandling {

    @Get("/users/sign_in")
    @Accept(MediaType.TEXT_HTML)
    @SetsCookie("_diaspora_session")
    String getLoginHTML();


    @Post("/users/sign_in")
    @Accept(MediaType.TEXT_HTML)
    @RequiresCookie("_diaspora_session")
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    String postLogin();


    @Get("/stream")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    List<fr.android.scaron.diaspdroid.model.Post> getStream();


    @Get("/u/{userName}")
    List<fr.android.scaron.diaspdroid.model.Post> getInfo(String userName);


    @Post("/photos?photo%5Bpending%5D=true&set_profile_image=&qqfile={fileName}")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    UploadResult uploadFile(MultiValueMap<String, Object> mapParts, String fileName);

    void setRootUrl(String rootUrl);
    void setCookie(String name, String value);
    String getCookie(String name);
    void setHeader(String name, String value);
}
