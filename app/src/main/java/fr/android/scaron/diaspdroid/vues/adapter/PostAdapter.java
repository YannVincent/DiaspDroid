package fr.android.scaron.diaspdroid.vues.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.model.Data;
import fr.android.scaron.diaspdroid.model.Image;
import fr.android.scaron.diaspdroid.model.OEmbedCache;
import fr.android.scaron.diaspdroid.model.People;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.view.PostView;

/**
 * Created by CARON-08651 on 16/01/2015.
 */
public class PostAdapter extends ArrayAdapter<Post> {

    public static Logger log = LoggerFactory.getLogger(PostAdapter.class);
    LayoutInflater inflater;
    Activity follower;
    private List<Post> posts = new ArrayList<Post>();


    public PostAdapter(Activity follower, int ressource, List<Post> posts){
        super(follower, ressource);
        try{
            inflater = LayoutInflater.from(follower.getApplicationContext());
            this.follower = follower;
            this.posts = posts;
            setPosts(posts);
        } catch (Throwable thr) {
            Log.e(this.getClass().getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public void setPosts(List<Post> posts){
        try{
            //follower.updateTitle(String.valueOf(downloads.size()));
            if (posts==null) {
                this.posts = new ArrayList<Post>();
            }else{
                this.posts = posts;
            }
            super.notifyDataSetChanged();
        } catch (Throwable thr) {
            Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public int getCount() {
        try{
            return posts.size();
        } catch (Throwable thr) {
            Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public Post getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        try{
            return posts.get(position).getId().longValue();
        } catch (Throwable thr) {
            Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try{
                PostView postView;
                if(convertView == null) {
                    postView = new PostView();
                    convertView = inflater.inflate(R.layout.flux_list_item, null);
                    postView.flux_list_item_post_avatar = (ImageView)convertView.findViewById(R.id.flux_list_item_post_avatar);
                    postView.flux_list_item_post_user = (TextView)convertView.findViewById(R.id.flux_list_item_post_user);
                    postView.flux_list_item_post_datetime = (TextView)convertView.findViewById(R.id.flux_list_item_post_datetime);
                    postView.flux_list_item_post_detail = (TextView)convertView.findViewById(R.id.flux_list_item_post_detail);
                    postView.flux_list_item_post_detail_picture = (ImageView)convertView.findViewById(R.id.flux_list_item_post_detail_picture);
                    postView.flux_list_item_post_detail_video = (VideoView)convertView.findViewById(R.id.flux_list_item_post_detail_video);
                    postView.flux_list_item_post_detail_video_web = (WebView)convertView.findViewById(R.id.flux_list_item_post_detail_video_web);
                    convertView.setTag(postView);
                } else {
                    postView = (PostView) convertView.getTag();
                }
                Post post = posts.get(position);
                postView.post = post;
                // *** Entete
                // Remplissage de la partie auteur
                People author = post.getAuthor();
                if (author != null){
                    Image avatar = author.getAvatar();
                    // Remplissage de l'avatar
                    if (avatar!=null){
                        ProfilControler.putImage(postView.flux_list_item_post_avatar, avatar.getLarge());
                    }
                    // Remplissage du nom
                    postView.flux_list_item_post_user.setText(author.getName());
                }
                // Remplissage de la date
                postView.flux_list_item_post_datetime.setText(post.getCreated_at_str());

                // *** Detail
                // Remplissage de la partie texte
                postView.flux_list_item_post_detail.setText(post.getText());
                // Remplissage de l'image
                String imageURL = post.getImage_url();
                if (imageURL!=null && !imageURL.isEmpty()){
                    ProfilControler.putImage(postView.flux_list_item_post_detail_picture, imageURL);
                }

                // Remplissage de l'objet web(vidéo ou autre) et de la vue vidéo
                OEmbedCache object = post.getO_embed_cache();
                if (object!=null) {
                    Data objectData = object.getData();
                    if (objectData!=null) {
                        String objectHtml = objectData.getHtml();
                        if (objectHtml != null && !objectHtml.isEmpty()) {
                            String objectHtmlDroid = objectHtml;//"<div style=\"width: 100%;\">" + objectHtml + "</div>";

                            //Cas de l'objet web
                            postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);
                            postView.flux_list_item_post_detail_video_web.getSettings().setLoadWithOverviewMode(true);
                            postView.flux_list_item_post_detail_video_web.getSettings().setJavaScriptEnabled(true);
                            final String mimeType = "text/html";
                            final String encoding = "utf-8";
                            postView.flux_list_item_post_detail_video_web.loadDataWithBaseURL(null, objectHtmlDroid, mimeType, encoding, "");
                            //test on masque la video
                            postView.flux_list_item_post_detail_video_web.setVisibility(View.INVISIBLE);

                            //Cas de la vidéo
                            int indexSrcBegin = objectHtml.indexOf("src=\"");
                            indexSrcBegin = indexSrcBegin+1+"src=\"".length();
                            int indexSrcEnd = objectHtml.indexOf("\"", indexSrcBegin);
                            String urlSrc = objectHtml.substring(indexSrcBegin, indexSrcEnd);
                            log.debug(PostAdapter.class.getName() + "Url de la vidéo récoltée '" + urlSrc + "'");
                            Log.d(this.getClass().getName(),"Url de la vidéo récoltée '" + urlSrc + "'");
                            postView.flux_list_item_post_detail_video.setVideoPath(urlSrc);
                        }
                    }
                }
                return convertView;
            } catch (Throwable thr) {
                Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
                ACRA.getErrorReporter().handleException(thr);
                throw thr;
            }
        }
}
