package fr.android.scaron.diaspdroid.controler;

import com.google.gson.JsonObject;

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

import java.util.List;

import fr.android.scaron.diaspdroid.model.Comment;
import fr.android.scaron.diaspdroid.model.Contact;
import fr.android.scaron.diaspdroid.model.CustGsonHttpMessageConverter;
import fr.android.scaron.diaspdroid.model.NewPost;
import fr.android.scaron.diaspdroid.model.UploadResult;
import fr.android.scaron.diaspdroid.model.User;

/**
 * Created by Sébastien on 11/03/2015.
 */
@Rest(converters={CustGsonHttpMessageConverter.class, StringHttpMessageConverter.class,  ByteArrayHttpMessageConverter.class, FormHttpMessageConverter.class}, interceptors = { AuthenticationInterceptor.class })
public interface DiasporaService extends RestClientErrorHandling {

    @Get("/users/sign_in")
    @Accept(MediaType.TEXT_HTML)
    @SetsCookie("_diaspora_session")
    String getLoginHTML();

    @Get("/bookmarklet")
    @Accept(MediaType.TEXT_HTML)
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    String getBookmarkletHTML();


    @Post("/users/sign_in")
    @Accept(MediaType.TEXT_HTML)
    @RequiresCookie("_diaspora_session")
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    String postLogin();


    @Get("/contacts")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    List<Contact> getContacts();

    @Get("/stream")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    List<fr.android.scaron.diaspdroid.model.Post> getStream();

    @Get("/stream?max_time={timestampStreamMax}&_={timestampStreamInit}")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    List<fr.android.scaron.diaspdroid.model.Post> getMoreStream(long timestampStreamMax, long timestampStreamInit);

    @Get("/followed_tags")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    List<fr.android.scaron.diaspdroid.model.Post> getTagSuivis();

    @Get("/followed_tags?max_time={timestampStreamMax}&_={timestampStreamInit}")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    List<fr.android.scaron.diaspdroid.model.Post> getMoreTagSuivis(long timestampStreamMax, long timestampStreamInit);

    @Get("/activity")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    List<fr.android.scaron.diaspdroid.model.Post> getActivity();

    @Get("/activity?max_time={timestampStreamMax}&_={timestampStreamInit}")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    List<fr.android.scaron.diaspdroid.model.Post> getMoreActivity(long timestampStreamMax, long timestampStreamInit);


    @Get("/u/{userName}")
    User getInfo(String userName);


    @Post("/photos?photo[aspect_ids]=all&qqfile=uploaded.jpg")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    @RequiresHeader({"x-csrf-token", "content-type"})
    @Accept(MediaType.APPLICATION_JSON)
    UploadResult uploadFile(byte[] file);

    @Post("/reshares")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    @RequiresHeader("x-csrf-token")
    @Accept(MediaType.APPLICATION_JSON)
    fr.android.scaron.diaspdroid.model.Post reshare(JsonObject jsonParam);


    @Post("/posts/{postID}/likes")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    @RequiresHeader("x-csrf-token")
    @Accept(MediaType.APPLICATION_JSON)
    fr.android.scaron.diaspdroid.model.Post like(Integer postID);

    @Post("/posts/{postID}")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    @Accept(MediaType.APPLICATION_JSON)
    fr.android.scaron.diaspdroid.model.Post getPost(Integer postID);

    @Get("/posts/{postID}/comments")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    @Accept(MediaType.APPLICATION_JSON)
    List<Comment> getComments(Integer postID);


    @Post("/posts/{postID}/comment")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    @RequiresHeader("x-csrf-token")
    @Accept(MediaType.APPLICATION_JSON)
    fr.android.scaron.diaspdroid.model.Post comment(Integer postID, JsonObject jsonParam);

    @Post("/status_messages")
    @RequiresCookie({"_diaspora_session", "remember_user_token"})
    @SetsCookie({"_diaspora_session", "remember_user_token"})
    @RequiresHeader({"x-csrf-token", "content-type"})
    @Accept(MediaType.APPLICATION_JSON)
    fr.android.scaron.diaspdroid.model.Post post(NewPost post);



    @Get("")
    @Accept(MediaType.IMAGE_JPEG)
    byte[] getImageFile();

    void setRootUrl(String rootUrl);
    void setCookie(String name, String value);
    String getCookie(String name);
    void setHeader(String name, String value);
}
