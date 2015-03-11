package fr.android.scaron.diaspdroid.controler;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.List;

import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by Sébastien on 11/03/2015.
 */
@Rest(converters={GsonHttpMessageConverter.class})
@Accept(MediaType.APPLICATION_JSON)
public interface UserPublicPostsService {

    @Get("/u/{userName}")
    List<Post> getInfo(String userName);

    void setRootUrl(String rootUrl);
}
