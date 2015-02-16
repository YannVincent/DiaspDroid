package fr.android.scaron.diaspdroid.vues.adapter;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.model.Image;
import fr.android.scaron.diaspdroid.model.People;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.fragment.DetailPostFragment;
import fr.android.scaron.diaspdroid.vues.fragment.YoutubePlayerFragment;
import fr.android.scaron.diaspdroid.vues.view.PostView;

/**
 * Created by Sébastien on 16/01/2015.
 */
public class DetailPostFragmentAdapter extends ArrayAdapter<Post> { // implements MediaPlayer.OnErrorListener { for test textureview

    private static Logger LOGGEUR = LoggerFactory.getLogger(DetailPostFragmentAdapter.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    LayoutInflater inflater;
    FragmentActivity follower;
    private List<Post> posts = new ArrayList<Post>();



    public DetailPostFragmentAdapter(FragmentActivity follower, int ressource, List<Post> posts){
        super(follower, ressource);
        try{
            LOG.d(".DetailPostFragmentAdapter entrée");
            LOG.d(".DetailPostFragmentAdapter get inflator");
            inflater = LayoutInflater.from(follower.getApplicationContext());
            this.follower = follower;
            this.posts = posts;
            LOG.d(".DetailPostFragmentAdapter call setPosts with "+posts);
            setPosts(posts);
            LOG.d("DetailPostFragmentAdapter sortie");
        } catch (Throwable thr) {
            LOG.e(".DetailPostFragmentAdapter Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public void setPosts(List<Post> posts){
        try{
            LOG.d(".setPosts entrée");
            if (posts==null) {
                LOG.d(".setPosts initialize this.posts");
                this.posts = new ArrayList<Post>();
            }else{
                LOG.d(".setPosts set this.posts with"+posts);
                this.posts = posts;
            }
            LOG.d(".setPosts notifyDataSetChanged");
            super.notifyDataSetChanged();
            LOG.d(".setPosts sortie");
        } catch (Throwable thr) {
            LOG.e(".setPosts Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public int getCount() {
        try{
            LOG.d(".getCount entrée");
            LOG.d(".getCount sortie return size = "+posts.size());
            return posts.size();
        } catch (Throwable thr) {
            LOG.e(".getCount Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public Post getItem(int position) {
        LOG.d(".getItem entrée with position = "+position);
        LOG.d(".getItem sortie with item = "+posts.get(position));
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        try{
            LOG.d(".getItemId entrée with position = "+position);
            LOG.d(".getItemId sortie with id = "+posts.get(position).getId().longValue());
            return posts.get(position).getId().longValue();
        } catch (Throwable thr) {
            LOG.e(".getItemId Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DetailPostFragment detailFragment;
        FragmentManager supportFragmentManager;
        try{
            LOG.d(".getView entrée with position = "+position);
            LOG.d(".getView posts.get with position = "+position);
            final Post post = posts.get(position);
            LOG.d(".getView create DetailPostFragment");
            detailFragment = new DetailPostFragment();
            LOG.d(".getView create Bundle");
            Bundle bundle = new Bundle();
            LOG.d(".getView putString post");
            bundle.putString("post", new Gson().toJson(post));
            LOG.d(".getView setArguments bundle in detailFragment");
            detailFragment.setArguments(bundle);

            LOG.d(".getView inflate R.layout.fragment_detail in convertView");
            convertView = inflater.inflate(R.layout.fragment_detail, null);
            LOG.d(".getView getting supportFragmentManager");
            supportFragmentManager =  follower.getSupportFragmentManager();
            LOG.d(".getView supportFragmentManager present for replace R.id.fragPost ? "+(supportFragmentManager!=null));
            if (supportFragmentManager!=null){
                LOG.d(".getView beginTransaction on supportFragmentManager");
                FragmentTransaction sfmTx = supportFragmentManager.beginTransaction();
                LOG.d(".getView replace R.id.fragPost with detailFragment in FragmentTransaction");
                sfmTx.replace(R.id.fragPost, detailFragment);
                LOG.d(".getView commit on supportFragmentManager");
                sfmTx.commit();
            }
            LOG.d(".getView setTag in convertview with convertView.findViewById(R.id.fragPost)");
            convertView.setTag(convertView.findViewById(R.id.fragPost));

//            LOG.d(".getView sortie with super.getView(position, convertView, parent)");
//            return super.getView(position, convertView, parent);
            LOG.d(".getView sortie with convertView = "+convertView);
            return convertView;
        } catch (Throwable thr) {
            LOG.e("getView Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
}
