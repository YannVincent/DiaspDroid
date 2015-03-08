package fr.android.scaron.diaspdroid.vues.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.activity.YoutubeActivity;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.model.Image;
import fr.android.scaron.diaspdroid.model.OpenGraphCache;
import fr.android.scaron.diaspdroid.model.People;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.fragment.YoutubePlayerFragment;
import fr.android.scaron.diaspdroid.vues.view.DetailPostView;

/**
 * Created by Sébastien on 16/01/2015.
 */
public class DetailPostViewAdapter extends ArrayAdapter<Post> { // implements MediaPlayer.OnErrorListener { for test textureview

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static Logger LOGGEUR = LoggerFactory.getLogger(DetailPostViewAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    LayoutInflater inflater;
    FragmentActivity follower;
    private List<Post> posts = new ArrayList<Post>();
    private final ThumbnailListener thumbnailListener;



    public DetailPostViewAdapter(FragmentActivity follower, int ressource, List<Post> posts){
        super(follower, ressource);
        try{
            LOG.d(".DetailPostFragmentAdapter entrée");
            LOG.d(".DetailPostFragmentAdapter get inflator");
            inflater = LayoutInflater.from(follower.getApplicationContext());
            this.follower = follower;
            thumbnailListener = new ThumbnailListener();
            thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
            this.posts = posts;
            LOG.d(".DetailPostFragmentAdapter call setPosts with "+posts);
            setPosts(posts);
            LOG.d("DetailPostFragmentAdapter sortie");
        } catch (Throwable thr) {
            LOG.e(".DetailPostFragmentAdapter Erreur : " + thr.toString(), thr);
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
            LOG.e(".setPosts Erreur : "+thr.toString(), thr);
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
            LOG.e(".getCount Erreur : "+thr.toString(), thr);
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
            LOG.e(".getItemId Erreur : "+thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{
            LOG.d(".getView entrée with position = "+position);
            LOG.d(".getView posts.get with position = "+position);
            final Post post = posts.get(position);
            LOG.d(".getView videos from post");
            Map<String, String> videos = getVideo(post);
            LOG.d(".getView videos found for post "+post+" : "+videos);
            DetailPostView detailPostView;

//            if (convertView==null){
                detailPostView = new DetailPostView();
                convertView = inflater.inflate(R.layout.fragment_detailpostvids, null);

                LOG.d(".onActivityCreated find TextView with id  R.id.detailPostText");
                detailPostView.texte = (TextView) convertView.findViewById(R.id.detailPostText);
                LOG.d(".onActivityCreated find ImageView with id  R.id.detailPostAvatar");
                detailPostView.avatar = (ImageView)convertView.findViewById(R.id.detailPostAvatar);
                LOG.d(".onActivityCreated find TextView with id  R.id.detailPostUser");
                detailPostView.user = (TextView)convertView.findViewById(R.id.detailPostUser);
                LOG.d(".onActivityCreated find TextView with id  R.id.detailPostDatetime");
                detailPostView.datetime = (TextView)convertView.findViewById(R.id.detailPostDatetime);
                LOG.d(".onActivityCreated find ImageView with id  R.id.detailPostImage");
                detailPostView.image = (ImageView)convertView.findViewById(R.id.detailPostImage);
                LOG.d(".onActivityCreated find ImageView with id  R.id.webvideo");
                detailPostView.webvideo = (WebView)convertView.findViewById(R.id.webvideo);
                LOG.d(".onActivityCreated find YouTubeThumbnailView with id  R.id.ytb_thumbnail");
                detailPostView.thumbnail = (YouTubeThumbnailView) convertView.findViewById(R.id.ytb_thumbnail);
                LOG.d(".onActivityCreated find ImageView with id  R.id.imgplay");
                detailPostView.imgplay = (ImageView)convertView.findViewById(R.id.imgplay);
                View.OnClickListener youtubeclickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (post.asYoutubeVideo && post.idYoutubeVideo != null && !post.idYoutubeVideo.isEmpty()) {
                            // Launching YoutubeActivity Screen
                            Intent i = new Intent(follower, YoutubeActivity.class);
                            i.putExtra("idYoutubeVideo", post.idYoutubeVideo);
                            follower.startActivity(i);
                        }
                    }
                };
                detailPostView.thumbnail.setOnClickListener(youtubeclickListener);
                detailPostView.imgplay.setOnClickListener(youtubeclickListener);

                LOG.d(".onActivityCreated find RelativeLayout with id  R.id.detailOpenGraph");
                detailPostView.detailOpenGraph= (RelativeLayout)convertView.findViewById(R.id.detailOpenGraph);
                LOG.d(".onActivityCreated find ImageView with id  R.id.detailOpenGraphImg");
                detailPostView.detailOpenGraphImg= (ImageView)convertView.findViewById(R.id.detailOpenGraphImg);
                LOG.d(".onActivityCreated find TextView with id  R.id.detailOpenGraphTitle");
                detailPostView.detailOpenGraphTitle= (TextView)convertView.findViewById(R.id.detailOpenGraphTitle);
                LOG.d(".onActivityCreated find TextView with id  R.id.detailOpenGraphTxt");
                detailPostView.detailOpenGraphTxt= (TextView)convertView.findViewById(R.id.detailOpenGraphTxt);
                LOG.d(".onActivityCreated find TextView with id  R.id.detailOpenGraphWebSite");
                detailPostView.detailOpenGraphWebSite= (TextView)convertView.findViewById(R.id.detailOpenGraphWebSite);
                View.OnClickListener urlclickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (post.asWebSiteUrl && post.webSiteUrl!=null && !post.webSiteUrl.isEmpty()){
                            // Launching Browser Screen
                            Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                            myWebLink.setData(Uri.parse(post.webSiteUrl));
                            follower.startActivity(myWebLink);
                        }
                    }
                };
            detailPostView.detailOpenGraph.setOnClickListener(urlclickListener);


            LOG.d(".onActivityCreated find TextView with id  R.id.detailIndicsReshareText");
            detailPostView.detailIndicsReshareText= (TextView)convertView.findViewById(R.id.detailIndicsReshareText);
            LOG.d(".onActivityCreated find TextView with id  R.id.detailIndicsLikeText");
            detailPostView.detailIndicsLikeText= (TextView)convertView.findViewById(R.id.detailIndicsLikeText);
            LOG.d(".onActivityCreated find TextView with id  R.id.detailIndicsCommentaireText");
            detailPostView.detailIndicsCommentaireText= (TextView)convertView.findViewById(R.id.detailIndicsCommentaireText);

            detailPostView.detailLike = (LinearLayout)convertView.findViewById(R.id.detailLike);
            detailPostView.detailComment = (LinearLayout)convertView.findViewById(R.id.detailComment);
            detailPostView.detailRepublish = (LinearLayout)convertView.findViewById(R.id.detailRepublish);

            // On crée la fonction pour le bouton like
            View.OnClickListener likeclickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (post!=null && post.getGuid()!=null){
                        FutureCallback<Response<String>> likeCallBack = new FutureCallback<Response<String>>() {
                            @Override
                            public void onCompleted(Exception e, Response<String> result) {
                                String methodName = ".getView likeCallBack onCompleted: ";
                                boolean resultOK = DiasporaControler.onCompleteRepartager(e, result);
                                if (!resultOK){
                                    if (follower==null){
                                        LOG.e(methodName + "Le contexte est vide et empêche un traitement");
                                        return;
                                    }
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(follower);
                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.setIcon(R.drawable.ic_launcher);
                                    alertDialog.setTitle("PB Accès");
                                    alertDialog.setMessage("Le like est impossible");
                                    alertDialog.show();
                                    return;
                                }
                            }
                        };
                        DiasporaControler.aimer(post.getId(), likeCallBack, false);
                    }
                }
            };
            // On attache la fonction au bouton like
            detailPostView.detailLike.setOnClickListener(likeclickListener);

            // On crée la fonction pour le bouton reshare
            View.OnClickListener reshareclickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (post!=null && post.getGuid()!=null){
                        FutureCallback<Response<String>> reshareCallBack = new FutureCallback<Response<String>>() {
                            @Override
                            public void onCompleted(Exception e, Response<String> result) {
                                String methodName = ".getView reshareCallBack onCompleted: ";
                                boolean resultOK = DiasporaControler.onCompleteRepartager(e, result);
                                if (!resultOK){
                                    if (follower==null){
                                        LOG.e(methodName + "Le contexte est vide et empêche un traitement");
                                        return;
                                    }
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(follower);
                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.setIcon(R.drawable.ic_launcher);
                                    alertDialog.setTitle("PB Accès");
                                    alertDialog.setMessage("Le repartage est impossible");
                                    alertDialog.show();
                                    return;
                                }
                            }
                        };
                        DiasporaControler.repartager(post.getGuid(), reshareCallBack, false);
                    }
                }
            };
            // On attache la fonction au bouton reshare
            detailPostView.detailRepublish.setOnClickListener(reshareclickListener);


                convertView.setTag(detailPostView);
//            } else {
//                detailPostView = (DetailPostView) convertView.getTag();
////                // Make link from youtubeVideo
////                if (videos.containsKey("youtube")){
////                    YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(detailPostView.thumbnail);
////                    if (loader == null) {
////                        // 2) The view is already created, and is currently being initialized. We store the
////                        //    current videoId in the tag.
////                        detailPostView.thumbnail.setTag(videos.get("youtube"));
////                    } else {
////                        // 3) The view is already created and already initialized. Simply set the right videoId
////                        //    on the loader.
////                        detailPostView.thumbnail.setImageResource(R.drawable.loading_thumbnail);
////                        loader.setVideo(videos.get("youtube"));
////                    }
////                }
//            }




            setPost(post,detailPostView);

            if (videos.containsKey("youtube")){
                // Initialize youtubeVideo with youtube thumbnail
                // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
                post.asYoutubeVideo = true;
                post.idYoutubeVideo = videos.get("youtube");
                LOG.d(".getView video youtube found ? "+post.asYoutubeVideo+" with ID ? "+post.idYoutubeVideo + " (url is '"+videos.get("youtubeurl")+"')");
                detailPostView.thumbnail.setTag(videos.get("youtube"));
                detailPostView.thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);


                LOG.i(".getView video youtube found ? " + post.asYoutubeVideo + " with ID ? " + post.idYoutubeVideo + " (url is '" + videos.get("youtubeurl") + "')");

//                    // Initialize youtubeVideo with youtube player
//                    FragmentManager supportFragmentManager = detailPostView.getFragmentManager();
//                    if (supportFragmentManager==null){
//                        LOG.e(".setYoutubeVide impossible to get supportFragmentManager to add Youtube video");
//                    }else {
//
//                        LOG.d(".setYoutubeVideo create FragmentTransaction");
//                        FragmentTransaction sfmTx = supportFragmentManager.beginTransaction();
//                        LOG.d(".setYoutubeVideo fragment 'detailPostObjectData' to create");
//                        detailPostView.youtubePlayerFragment = new YoutubePlayerFragment();
//                        LOG.d(".setYoutubeVideo attach video and listeners on youtubeplauer");
//                        setYoutubeVideo(videos.get("youtube"), sfmTx, detailPostView.youtubePlayerFragment);
//                    }
            }


            LOG.d(".getView sortie with convertView = "+convertView);
            return convertView;
        } catch (Throwable thr) {
            LOG.e("getView Erreur : "+thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }




    public void setPost(Post post, DetailPostView detailPostView) {
        LOG.d(".setPost entree with post : "+post);
        try {
            LOG.d(".setPost getSettings set params");
//                texte.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//                texte.getSettings().setUseWideViewPort(true);
////                texte.getSettings().setLoadWithOverviewMode(true);
//                texte.getSettings().setJavaScriptEnabled(true);
//                // Remplissage de l'objet web
//                StringBuilder sbHtml = new StringBuilder();
//                //Ajout du style
//               // sbHtml.append("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"stylePost.css\" /></head><body>");
//
//                //AJOUT D'UN POST TEST dans la texte
//                sbHtml.append("<div class=\"stream_element loaded\" data-template=\"stream-element\"><div class=\"media\">\n" +
//                        "\n" +
//                        "  \n" +
//                        "    <a href=\"/people/4d1388e030d8013219742a0000053625\" class=\"img hovercardable\">\n" +
//                        "      <img src=\"https://framasphere.org/uploads/images/thumb_small_a5ffc454c286191bfa26.jpg\" class=\"avatar small\" title=\"S4mdf0o1\">\n" +
//                        "    </a>\n" +
//                        "  \n" +
//                        "\n" +
//                        "  <div class=\"bd\">\n" +
//                        "    \n" +
//                        "      <div class=\"controls\">\n" +
//                        "        \n" +
//                        "          <a href=\"#\" rel=\"nofollow\" data-type=\"post\" class=\"post_report\" title=\"Rapport\">\n" +
//                        "            <div class=\"icons-report control_icon\"></div>\n" +
//                        "          </a>\n" +
//                        "          <a href=\"#\" rel=\"nofollow\" class=\"block_user\" data-original-title=\"Ignorer\">\n" +
//                        "            <div class=\"icons-ignoreuser control_icon\"></div>\n" +
//                        "          </a>\n" +
//                        "          <a href=\"#\" rel=\"nofollow\" class=\"delete hide_post\" data-original-title=\"Masquer\">\n" +
//                        "            <div class=\"icons-deletelabel delete control_icon\" data-original-title=\"\"></div>\n" +
//                        "          </a>\n" +
//                        "        \n" +
//                        "      </div>\n" +
//                        "    \n" +
//                        "    \n" +
//                        "    <div>\n" +
//                        "      \n" +
//                        "        <a href=\"/people/4d1388e030d8013219742a0000053625\" class=\"author hovercardable\">S4mdf0o1</a>\n" +
//                        "      \n" +
//                        "\n" +
//                        "      <span class=\"details grey\">\n" +
//                        "        -\n" +
//                        "        <a href=\"/posts/372274\">\n" +
//                        "          <time class=\"timeago\" data-original-title=\"4/2/2015 05:17:13\" datetime=\"2015-02-04T04:17:13Z\">il y a environ 6 heures</time>\n" +
//                        "        </a>\n" +
//                        "\n" +
//                        "        \n" +
//                        "      </span>\n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    \n" +
//                        "        \n" +
//                        "      <div class=\"post-content\"><div data-template=\"status-message\">\n" +
//                        "\n" +
//                        "<div class=\"collapsible opened\" style=\"height: auto;\">\n" +
//                        "  <p>Saluzatous, <a href=\"/tags/nouveauici\" class=\"tag\">#nouveauici</a>, surtout branché <a href=\"/tags/logiciellibre\" class=\"tag\">#logiciellibre</a> et <a href=\"/tags/diy\" class=\"tag\">#diy</a> dans un monde propriétaire, impérialiste, concurrent et hypocrite.</p>\n" +
//                        "\n" +
//                        "<p>J'aime à dire : \"Les dysfonctionnements assurent l'Emploi\" et \"l'Évolution ne s'arrête pas à la suffisance\" (déclaré à un proviseur de lycée, lors d'un entretien d'embauche, suite à sa déclaration : \"Ici on ne fait pas de Freeware\", avec de grands gestes...)</p>\n" +
//                        "\n" +
//                        "<p>En passe de créer une asso locale \"Transition Libre\", dont l'émergence trouve comme tout premier obstacle -en grandes discussions sur la méthode-, la difficulté d'amener les gens à juste comprendre, sans les brusquer, ni les froisser, ni les choquer dans leurs fondements...</p>\n" +
//                        "\n" +
//                        "<p>Bref plein de volonté, d'auto-critique et par là-même d'auto-censure, cherchant la voie de la lumière...</p>\n" +
//                        "\n" +
//                        "<p>Que nos vœux nous libèrent ! \\o/ LOL</p>\n" +
//                        "  <div class=\"oembed\"><div data-template=\"oembed\">\n" +
//                        "</div></div>\n" +
//                        "  <div class=\"opengraph\"><div data-template=\"opengraph\">\n" +
//                        "  \n" +
//                        "\n" +
//                        "\n" +
//                        "</div></div>\n" +
//                        "  <div class=\"poll\"><div data-template=\"poll\">\n" +
//                        "</div></div>\n" +
//                        "<div class=\"expander\" style=\"display: none;\">Voir plus</div></div>\n" +
//                        "</div></div>\n" +
//                        "      <div class=\"status-message-location\"><div data-template=\"status-message-location\">\n" +
//                        "</div></div>\n" +
//                        "\n" +
//                        "      <div class=\"feedback\"><div class=\"info\" data-template=\"feedback\"><span class=\"post_scope grey\" data-original-title=\"\">\n" +
//                        "  \n" +
//                        "    Public\n" +
//                        "  \n" +
//                        "\n" +
//                        "  \n" +
//                        "  –\n" +
//                        "</span>\n" +
//                        "\n" +
//                        "\n" +
//                        "<a href=\"#\" class=\"like\" rel=\"nofollow\">\n" +
//                        "  \n" +
//                        "    J'aime\n" +
//                        "  \n" +
//                        "</a>\n" +
//                        "·\n" +
//                        "\n" +
//                        "\n" +
//                        "  <a href=\"#\" class=\"reshare\" rel=\"nofollow\">\n" +
//                        "    Repartager\n" +
//                        "  </a>\n" +
//                        "  ·\n" +
//                        "\n" +
//                        "\n" +
//                        "<a href=\"#\" class=\"focus_comment_textarea\" rel=\"nofollow\">\n" +
//                        "  Commenter\n" +
//                        "</a>\n" +
//                        "</div></div>\n" +
//                        "      <div class=\"likes\"><div data-template=\"likes-info\">\n" +
//                        "  <div class=\"comment\">\n" +
//                        "    <div class=\"media\">\n" +
//                        "      <div alt=\"Heart\" class=\"icons-heart\"></div>\n" +
//                        "\n" +
//                        "      <div class=\"bd\">\n" +
//                        "        \n" +
//                        "          <a href=\"#\" class=\"expand_likes grey\">\n" +
//                        "            2 J'aime\n" +
//                        "          </a>\n" +
//                        "\n" +
//                        "        \n" +
//                        "      </div>\n" +
//                        "    </div>\n" +
//                        "  </div>\n" +
//                        "\n" +
//                        "</div></div>\n" +
//                        "      <div class=\"comments\"><div class=\"comment_stream\" data-template=\"comment-stream\">\n" +
//                        "  <div class=\"show_comments comment  hidden \">\n" +
//                        "    <div class=\"media\">\n" +
//                        "      <a href=\"/posts/372274#comments\" class=\"toggle_post_comments\">\n" +
//                        "        Montrer 0 commentaire supplémentaire\n" +
//                        "      </a>\n" +
//                        "    </div>\n" +
//                        "  </div>\n" +
//                        "\n" +
//                        "\n" +
//                        "<div class=\"comments\"> <div class=\"comment media\" data-template=\"comment\"><div id=\"8b3fdc308e61013233dc2a0000053625\">\n" +
//                        "  <div class=\"img\">\n" +
//                        "    <a href=\"/people/cd6984d07e2b0132ce3a2a0000053625\" class=\"author-name hovercardable\">\n" +
//                        "      <img src=\"https://framasphere.org/uploads/images/thumb_small_45bea538502256380c74.jpg\" class=\"avatar small\" title=\"Pulce 2081\">\n" +
//                        "    </a>\n" +
//                        "  </div>\n" +
//                        "\n" +
//                        "  <div class=\"bd\">\n" +
//                        "    <div class=\"controls\">\n" +
//                        "    \n" +
//                        "      \n" +
//                        "        <a href=\"#\" data-type=\"comment\" class=\"comment_report\" title=\"Rapport\">\n" +
//                        "          <div class=\"icons-report\"></div>\n" +
//                        "        </a>\n" +
//                        "      \n" +
//                        "    \n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    \n" +
//                        "      <a href=\"/people/cd6984d07e2b0132ce3a2a0000053625\" class=\"author author-name hovercardable\">\n" +
//                        "        Pulce 2081\n" +
//                        "      </a>\n" +
//                        "    \n" +
//                        "\n" +
//                        "    <div class=\"collapsible comment-content\">\n" +
//                        "      <p>Bienvenue parmi nous :)</p>\n" +
//                        "    </div>\n" +
//                        "    \n" +
//                        "    <div class=\"info\">\n" +
//                        "      <a href=\"/posts/372274#8b3fdc308e61013233dc2a0000053625\" class=\"permalink_comment\">\n" +
//                        "        <time class=\"timeago\" data-original-title=\"4/2/2015 07:04:03\" datetime=\"2015-02-04T06:04:03Z\">il y a environ 4 heures</time>\n" +
//                        "      </a>\n" +
//                        "    </div>\n" +
//                        "  </div>\n" +
//                        "</div>\n" +
//                        "</div><div class=\"comment media\" data-template=\"comment\"><div id=\"a6ed6d808e77013233de2a0000053625\">\n" +
//                        "  <div class=\"img\">\n" +
//                        "    <a href=\"/people/04385b502a77013283742a0000053625\" class=\"author-name hovercardable\">\n" +
//                        "      <img src=\"https://framasphere.org/uploads/images/thumb_small_746b9583058f3185f690.jpg\" class=\"avatar small\" title=\"Sébastien Adam\">\n" +
//                        "    </a>\n" +
//                        "  </div>\n" +
//                        "\n" +
//                        "  <div class=\"bd\">\n" +
//                        "    <div class=\"controls\">\n" +
//                        "    \n" +
//                        "      \n" +
//                        "        <a href=\"#\" data-type=\"comment\" class=\"comment_report\" title=\"Rapport\">\n" +
//                        "          <div class=\"icons-report\"></div>\n" +
//                        "        </a>\n" +
//                        "      \n" +
//                        "    \n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    \n" +
//                        "      <a href=\"/people/04385b502a77013283742a0000053625\" class=\"author author-name hovercardable\">\n" +
//                        "        Sébastien Adam\n" +
//                        "      </a>\n" +
//                        "    \n" +
//                        "\n" +
//                        "    <div class=\"collapsible comment-content\">\n" +
//                        "      <p>Bienvenue ici S4mdf0o1 (heu... ça se prononce comment? ;-) ). J'espère que ton projet verra le jour.</p>\n" +
//                        "\n" +
//                        "<p>Si tu as besoin d'aide pour t'y retrouver ici, tu peux utiliser les liens sur la droite de la page (<a href=\"https://framasphere.org/help\" target=\"_blank\">aide</a>, <a href=\"https://diasporafoundation.org/tutorials\" target=\"_blank\">tutoriels</a> et <a href=\"https://wiki.diasporafoundation.org/Main_Page/fr\" target=\"_blank\">wiki</a>), le fameux <a href=\"https://diaspora-fr.org/posts/683275\" target=\"_blank\">kit de base</a> de <a href=\"https://diaspora-fr.org/people/a67fe237dabfccac\" target=\"_blank\">@SpF</a> ou le tag <a href=\"/tags/question\" class=\"tag\">#question</a>.</p>\n" +
//                        "    </div>\n" +
//                        "    \n" +
//                        "    <div class=\"info\">\n" +
//                        "      <a href=\"/posts/372274#a6ed6d808e77013233de2a0000053625\" class=\"permalink_comment\">\n" +
//                        "        <time class=\"timeago\" data-original-title=\"4/2/2015 09:42:18\" datetime=\"2015-02-04T08:42:18Z\">il y a environ une heure</time>\n" +
//                        "      </a>\n" +
//                        "    </div>\n" +
//                        "  </div>\n" +
//                        "</div>\n" +
//                        "</div><div class=\"comment media\" data-template=\"comment\"><div id=\"a91346c08e7b013233dc2a0000053625\">\n" +
//                        "  <div class=\"img\">\n" +
//                        "    <a href=\"/people/de224dd03084013219702a0000053625\" class=\"author-name hovercardable\">\n" +
//                        "      <img src=\"/assets/user/default.png\" class=\"avatar small\" title=\"Antoine\">\n" +
//                        "    </a>\n" +
//                        "  </div>\n" +
//                        "\n" +
//                        "  <div class=\"bd\">\n" +
//                        "    <div class=\"controls\">\n" +
//                        "    \n" +
//                        "      \n" +
//                        "        <a href=\"#\" data-type=\"comment\" class=\"comment_report\" title=\"Rapport\">\n" +
//                        "          <div class=\"icons-report\"></div>\n" +
//                        "        </a>\n" +
//                        "      \n" +
//                        "    \n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    \n" +
//                        "      <a href=\"/people/de224dd03084013219702a0000053625\" class=\"author author-name hovercardable\">\n" +
//                        "        Antoine\n" +
//                        "      </a>\n" +
//                        "    \n" +
//                        "\n" +
//                        "    <div class=\"collapsible comment-content\">\n" +
//                        "      <p>\"Ici on ne fait pas de freeware\". SO CUTE</p>\n" +
//                        "    </div>\n" +
//                        "    \n" +
//                        "    <div class=\"info\">\n" +
//                        "      <a href=\"/posts/372274#a91346c08e7b013233dc2a0000053625\" class=\"permalink_comment\">\n" +
//                        "        <time class=\"timeago\" data-original-title=\"4/2/2015 10:11:00\" datetime=\"2015-02-04T09:11:00Z\">il y a environ une heure</time>\n" +
//                        "      </a>\n" +
//                        "    </div>\n" +
//                        "  </div>\n" +
//                        "</div>\n" +
//                        "</div></div>\n" +
//                        "\n" +
//                        "\n" +
//                        "  <div class=\"comment no-border media new_comment_form_wrapper \">\n" +
//                        "    \n" +
//                        "      <a href=\"/people/1f9bd9b030fc013219722a0000053625\" class=\"img\">\n" +
//                        "        <img src=\"https://framasphere.org/uploads/images/thumb_small_93779d6fd285331e91dd.jpg\" class=\"avatar small\" title=\"Sebastien Caron\">\n" +
//                        "      </a>\n" +
//                        "    \n" +
//                        "\n" +
//                        "    <div class=\"bd\">\n" +
//                        "      <form accept-charset=\"UTF-8\" action=\"/posts/372274/comments\" class=\"new_comment\" id=\"new_comment_on_372274\" method=\"post\">\n" +
//                        "        <textarea class=\"comment_box\" id=\"comment_text_on_372274\" name=\"text\" rows=\"2\" placeholder=\"Commenter\" style=\"resize: none; overflow-y: hidden;\"></textarea>\n" +
//                        "        <div class=\"submit_button\">\n" +
//                        "          <input class=\"button creation\" id=\"comment_submit_372274\" name=\"commit\" type=\"submit\" value=\"Commenter\">\n" +
//                        "        </div>\n" +
//                        "      </form>\n" +
//                        "    </div>\n" +
//                        "  </div>\n" +
//                        "\n" +
//                        "</div></div>\n" +
//                        "    \n" +
//                        "\n" +
//                        "  </div>\n" +
//                        "</div></div></body></html>");




/**/
            // *** Entete
            // Remplissage de la partie auteur
            People author = post.getAuthor();
            if (author != null){
                Image iavatar = author.getAvatar();
                // Remplissage de l'avatar
                if (iavatar!=null){
                    ProfilControler.putImage(detailPostView.avatar, iavatar.getLarge());
                }
                // Remplissage du nom
                detailPostView.user.setText(author.getName());
            }
            // Remplissage de la date
            detailPostView.datetime.setText(post.getCreated_at_str());


            // *** Detail
            // Remplissage de la partie texte
            detailPostView.texte.setText(post.getText());
            // Remplissage de l'image
            String imageURL = post.getImage_url();
            if (imageURL!=null && !imageURL.isEmpty()){
                ProfilControler.putImage(detailPostView.image, imageURL);
            }
            // Remplissage des contenus objets web
            Map<String, String> videoData = getVideo(post);
            if (!videoData.isEmpty()){
                if (videoData.containsKey("web")) {
                    LOG.d(".setPost set web video");
                    detailPostView.setWebVideo(videoData.get("web"));
                    detailPostView.thumbnail.setVisibility(View.GONE);
                    detailPostView.imgplay.setVisibility(View.GONE);
//                    sbHtml.append(videoData.get("web"));
                }else if (videoData.containsKey("youtube")){
                    LOG.d(".setPost set youtube video");
                    detailPostView.webvideo.setVisibility(View.GONE);
                }else{
                    detailPostView.thumbnail.setVisibility(View.GONE);
                    detailPostView.imgplay.setVisibility(View.GONE);
                    detailPostView.webvideo.setVisibility(View.GONE);
                }
            }else{
                detailPostView.thumbnail.setVisibility(View.GONE);
                detailPostView.imgplay.setVisibility(View.GONE);
                detailPostView.webvideo.setVisibility(View.GONE);
            }

            // Remplissage du open_graph_cache
            OpenGraphCache opengraph = post.getOpen_graph_cache();
            if (opengraph!=null){
                // Affichage de l'image
                String opengraphimg = opengraph.getImage();
                if (opengraphimg!=null && !opengraphimg.isEmpty()){
                    ProfilControler.putImage(detailPostView.detailOpenGraphImg, opengraphimg);
                }
                detailPostView.detailOpenGraphTitle.setText(opengraph.getTitle());
                detailPostView.detailOpenGraphTxt.setText(opengraph.getDescription());
                detailPostView.detailOpenGraphWebSite.setText(opengraph.getUrl());
                post.asWebSiteUrl=true;
                post.webSiteUrl=opengraph.getUrl();
            }else{
                detailPostView.detailOpenGraph.setVisibility(View.GONE);
            }

            // Remplissage des indicateurs reshare/like/comments
            detailPostView.detailIndicsReshareText.setText(""+post.getInteractions().getReshares_count());
            detailPostView.detailIndicsLikeText.setText(""+post.getInteractions().getLikes_count());
            detailPostView.detailIndicsCommentaireText.setText(""+post.getInteractions().getComments_count());


//            texte.loadDataWithBaseURL("file:///android_asset/", sbHtml.toString(), mimeType, encoding, "");
            LOG.d(".setPost sortie en succès");
        }catch(Throwable thr) {
            LOG.e(".setPost sortie en Erreur ("+thr.toString()+")", thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }



    public String getVideoType(Post post) {
        if (post.getO_embed_cache() != null && post.getO_embed_cache().getData() != null
                && post.getO_embed_cache().getData().getHtml() != null){
            if (post.getO_embed_cache().getData().getHtml().contains("youtube") ) {
                return "youtube";
            }
            return "web";
        }
        return "none";
    }

    public Map<String, String> getVideo(Post post) {
        Map mapVideo = new HashMap<String, String>();
        String videoType = getVideoType(post);
        if ("youtube".equals(videoType)) {

            String objectHtml = post.getO_embed_cache().getData().getHtml();
            String vId = null;
            Pattern pattern = Pattern.compile(".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*");
            Matcher matcher = pattern.matcher(objectHtml);
            if (matcher.matches()){
                vId = matcher.group(1);
            }
//            int indexSrcBegin = objectHtml.indexOf("src=\"");
//            indexSrcBegin = indexSrcBegin + "src=\"".length();
//            int indexSrcEnd = objectHtml.indexOf("?feature=oembed", indexSrcBegin);
//            String urlSrc = objectHtml.substring(indexSrcBegin, indexSrcEnd);
//            indexSrcBegin = urlSrc.lastIndexOf("/") + 1;
//            urlSrc = urlSrc.substring(indexSrcBegin);
            mapVideo.put("youtube",vId);
            mapVideo.put("youtubeurl", objectHtml);
        }else if ("web".equals(videoType)) {
            mapVideo.put("web",post.getO_embed_cache().getData().getHtml());
        }
        return mapVideo;
    }

    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            thumbnailViewToLoaderMap.put(view, loader);
            view.setImageResource(R.drawable.loading_thumbnail);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.no_thumbnail);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.no_thumbnail);
        }
    }

}
