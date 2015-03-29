package fr.android.scaron.diaspdroid.controler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.view.PostView;
import fr.android.scaron.diaspdroid.vues.view.PostView_;

/**
 * Created by Sébastien on 29/03/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class PostsAdapter extends BaseAdapter {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PostsAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "PostsAdapter";

    List<Post> posts;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        posts = new ArrayList<Post>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PostView postView;
        if (convertView == null) {
            postView = PostView_.build(context);
        } else {
            postView = (PostView) convertView;
        }

        postView.bind(getItem(position));

        return postView;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Post getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    public void setPosts(List<Post> posts){
        String TAG_METHOD = TAG + ".setPosts : ";
        try{
            LOG.d(TAG_METHOD + "Entrée");
            if (posts==null) {
                LOG.d(".setPosts initialize this.posts");
                this.posts = new ArrayList<Post>();
            }else{
                LOG.d(TAG_METHOD + "set this.posts with"+posts);
                this.posts = posts;
            }
            LOG.d(TAG_METHOD + "notifyDataSetChanged");
            super.notifyDataSetChanged();
            LOG.d(TAG_METHOD + "sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD + "Erreur : "+thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
}