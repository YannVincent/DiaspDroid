package fr.android.scaron.diaspdroid.controler;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;

/**
 * Created by Sébastien on 11/03/2015.
 */
@EBean
public class StreamPostsBean {
    @RootContext
    Context context;

    @RestService
    StreamPostsService streamPostsService;

    @AfterInject
    public void init(){
        if (DiasporaControler.COOKIE_REMEMBER!=null && !DiasporaControler.COOKIE_REMEMBER.isEmpty()) {
            streamPostsService.setCookie("remember_user_token", DiasporaControler.COOKIE_REMEMBER);
        }else if (DiasporaControler.COOKIE_SESSION_STREAM!=null && !DiasporaControler.COOKIE_SESSION_STREAM.isEmpty()) {
            streamPostsService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_STREAM);
        }else if (DiasporaControler.COOKIE_SESSION_LOGIN!=null && !DiasporaControler.COOKIE_SESSION_LOGIN.isEmpty()) {
            streamPostsService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_LOGIN);
        } else if (DiasporaControler.COOKIE_SESSION_TOKEN!=null && !DiasporaControler.COOKIE_SESSION_TOKEN.isEmpty()) {
            streamPostsService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_TOKEN);
        }
        if (DiasporaControler.TOKEN!=null && !DiasporaControler.TOKEN.isEmpty()){
            streamPostsService.setHeader("x-csrf-token", DiasporaControler.TOKEN);
        }
    }

}
