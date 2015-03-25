package fr.android.scaron.diaspdroid.controler;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by Sébastien on 11/03/2015.
 */
@EBean
public class RestServiceControler {
    @RootContext
    Context context;

//    @Bean
//    LoginBean loginBean;

    @Bean
    StreamPostsBean streamBean;


//    public void seLogguer(){
//        loginBean.seLogguer();
//    }

    public List<Post> getStream(){ return streamBean.getStream();}



}
