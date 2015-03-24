package fr.android.scaron.diaspdroid.controler;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by Sébastien on 11/03/2015.
 */
@EBean
public class StreamPostsBean {
    @RootContext
    Context context;

    @RestService
    StreamPostsService streamPostsService;
    @Bean
    LoginBean loginBean;
    @Bean
    ErrorHandlerBean errorHandlerBean;

    @AfterInject
    public void init(){
        streamPostsService.setRootUrl(DiasporaConfig.POD_URL);
        streamPostsService.setRestErrorHandler(errorHandlerBean);
        if (DiasporaControler.COOKIE_REMEMBER!=null && !DiasporaControler.COOKIE_REMEMBER.isEmpty()) {
            streamPostsService.setCookie("remember_user_token", DiasporaControler.COOKIE_REMEMBER);
//        }else if (DiasporaControler.COOKIE_SESSION_STREAM!=null && !DiasporaControler.COOKIE_SESSION_STREAM.isEmpty()) {
//            streamPostsService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_STREAM);
        }else if (DiasporaControler.COOKIE_SESSION_LOGIN!=null && !DiasporaControler.COOKIE_SESSION_LOGIN.isEmpty()) {
            streamPostsService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_LOGIN);
        } else if (DiasporaControler.COOKIE_SESSION_TOKEN!=null && !DiasporaControler.COOKIE_SESSION_TOKEN.isEmpty()) {
            streamPostsService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_TOKEN);
        }
        if (DiasporaControler.TOKEN!=null && !DiasporaControler.TOKEN.isEmpty()){
            streamPostsService.setHeader("x-csrf-token", DiasporaControler.TOKEN);
        }
    }

    public List<Post> getStream(){
        boolean logged = loginBean.seLogguer();
        if (logged){
            return streamPostsService.getStream();
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = new Post();
        empty.setText("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        return emptyError;
    }

}
