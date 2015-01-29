package fr.android.scaron.diaspdroid.vues.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.controler.YoutubeControler;
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

    private static Logger LOGGEUR = LoggerFactory.getLogger(PostAdapter.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
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
            LOG.e("Erreur : " + thr.toString());
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
            LOG.e("Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public int getCount() {
        try{
            return posts.size();
        } catch (Throwable thr) {
            LOG.e("Erreur : "+thr.toString());
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
            LOG.e("Erreur : "+thr.toString());
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
                    ProfilControler.putImage(postView.flux_list_item_post_avatar, avatar.getMedium());
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

                        LOG.d("Object HTML récolté '" + objectHtml + "'");
                        //Cas de la vidéo Youtube
                        if (objectHtml.contains("youtube")) {
                            int indexSrcBegin = objectHtml.indexOf("src=\"");
                            indexSrcBegin = indexSrcBegin + "src=\"".length();
                            int indexSrcEnd = objectHtml.indexOf("?feature=oembed", indexSrcBegin);
                            String urlSrc = objectHtml.substring(indexSrcBegin, indexSrcEnd);
                            LOG.d("Url de la vidéo récoltée '" + urlSrc + "'");
//                        postView.flux_list_item_post_detail_video.setVideoPath(urlSrc);
//                        postView.flux_list_item_post_detail_video.setVisibility(View.VISIBLE);

//                        postView.flux_list_item_post_detail_video.setVideoURI(Uri.parse("rtsp://v4.cache1.c.youtube.com/CiILENy73wIaGQk4RDShYkdS1BMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp"));
//                        postView.flux_list_item_post_detail_video.setMediaController(new MediaController(AlertDetail.this));
//                        postView.flux_list_item_post_detail_video.requestFocus();
//                        postView.flux_list_item_post_detail_video.start();

                            // Le lien rtsp est déjà en mémoire ?
                            if (!YoutubeControler.rtspMapping.containsKey(urlSrc)){
                                // Non donc on tente de le trouver
                                String rtspVideo = YoutubeControler.getUrlVideoRTSP(follower,urlSrc, postView.flux_list_item_post_detail_video);
                                LOG.d("Url de la vidéo rtsp en court de recherche ...");
                            }else{
                                // Oui donc on affiche directement
                                String rtspVideo = YoutubeControler.rtspMapping.get(urlSrc);
                                LOG.d("Url de la vidéo rtsp déjà en mémoire : '" + rtspVideo + "'");
                                postView.flux_list_item_post_detail_video.setVideoURI(Uri.parse(rtspVideo));
                                MediaController mc = new MediaController(follower);
                                postView.flux_list_item_post_detail_video.setMediaController(mc);
                                postView.flux_list_item_post_detail_video.requestFocus();
                                postView.flux_list_item_post_detail_video.start();
                                mc.show();
                                postView.flux_list_item_post_detail_video.setVisibility(View.VISIBLE);
                            }
//                            postView.flux_list_item_post_detail_video.setVideoURI(Uri.parse(rtspVideo));
//                            MediaController mc = new MediaController(follower);
//                            postView.flux_list_item_post_detail_video.setMediaController(mc);
//                            postView.flux_list_item_post_detail_video.requestFocus();
//                            postView.flux_list_item_post_detail_video.start();
//                            mc.show();
//                            postView.flux_list_item_post_detail_video.setVisibility(View.VISIBLE);
                        }else{
                            //Cas de l'objet web
                            int indexSrcBegin = objectHtml.indexOf("src=\"");
                            indexSrcBegin = indexSrcBegin + "src=\"".length();
                            int indexSrcEnd = objectHtml.indexOf("\"", indexSrcBegin);
                            String urlSrc = objectHtml.substring(indexSrcBegin, indexSrcEnd);
                            LOG.d("Url de l'objet récoltée '" + urlSrc + "'");
                            String objectHtmlDroid = objectHtml;//"<iframe scrolling=\"no\" frameborder=\"no\" src=\""+urlSrc+"\">";//objectHtml;//"<div style=\"width: 100%;\">" + objectHtml + "</div>";
                            postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);
                            postView.flux_list_item_post_detail_video_web.getSettings().setLoadWithOverviewMode(true);
                            postView.flux_list_item_post_detail_video_web.getSettings().setJavaScriptEnabled(true);
                            postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);//TEST
                            final String mimeType = "text/html";
                            final String encoding = "utf-8";
                            postView.flux_list_item_post_detail_video_web.loadDataWithBaseURL(null, objectHtmlDroid, mimeType, encoding, "");
                            //test on masque la video
                            postView.flux_list_item_post_detail_video_web.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            return convertView;
        } catch (Throwable thr) {
            LOG.e("Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
}
