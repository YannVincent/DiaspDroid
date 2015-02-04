package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Data;
import fr.android.scaron.diaspdroid.model.OEmbedCache;
import fr.android.scaron.diaspdroid.model.Post;

//import com.google.android.youtube.player.YouTubePlayerFragment;
//import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by CARON-08651 on 03/02/2015.
 */
public class DetailPostFragment extends Fragment {
    private static Logger LOGGEUR = LoggerFactory.getLogger(DetailPostFragment.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    private View rootview;
    private WebView webview;
    private YoutubePlayerFragment youTubePlayerFragment;
    final String mimeType = "text/html";
    final String encoding = "utf-8";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayer.OnInitializedListener youtubeListener;
    FragmentActivity listener;
    private Post post;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (FragmentActivity) activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String jsonMyObject;
            jsonMyObject = savedInstanceState.getString("post");
            post = new Gson().fromJson(jsonMyObject, Post.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            LOG.d(".onCreateView entree with savedInstanceState : " + savedInstanceState);
            if (container!=null) {
                LOG.d(".onCreateView clear all views");
                container.removeAllViews();
                LOG.d(".onCreateView create view with layout  R.layout.fragment_detailpostyoutube");
                rootview = inflater.inflate(R.layout.fragment_detailpostyoutube, container, false);
                LOG.d(".onCreateView sortie with view : " + rootview);
                LOG.d(".onCreateView find web view with id  R.id.detailPostTextYoutube");
                webview = (WebView) rootview.findViewById(R.id.detailPostTextYoutube);
                LOG.d(".onCreateView webview found ? " + (webview != null));
                LOG.d(".onCreateView getting supportFragmentManager");
                FragmentManager supportFragmentManager =  getActivity().getSupportFragmentManager();
                LOG.d(".onCreateView find youtube fragment with id  R.id.detailPostVideoYoutube");
                youTubePlayerFragment =
                        (YoutubePlayerFragment) supportFragmentManager.findFragmentById(R.id.detailPostVideoYoutube);
                LOG.d(".onCreateView youTubePlayerFragment found ? " + (youTubePlayerFragment != null));
                if (youTubePlayerFragment==null){
                    LOG.d(".onCreate fragment 'detailPostVideoYoutube' to create");
                    youTubePlayerFragment = new YoutubePlayerFragment();
                    LOG.d(".onCreateView create FragmentTransaction");
                    FragmentTransaction sfmTx = supportFragmentManager.beginTransaction();
                    LOG.d(".onCreateView add add(R.id.detailPostVideoYoutube, youTubePlayerFragment) in FragmentTransaction");
                    sfmTx.add(R.id.detailPostVideoYoutube, youTubePlayerFragment);
                    LOG.d(".onCreateView commit FragmentTransaction");
                    sfmTx.commit();
                }
            }
//            LOG.d(".onCreateView sortie with view : " + container.getRootView());
//            return container.getRootView();
        }catch(Throwable thr) {
            LOG.e(".onCreateView sortie en Erreur ("+thr.toString()+")");
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
        return rootview;
//        webview = new WebView(getActivity());
//        LOG.d(".onCreateView sortie with webview : "+webview);
//        return webview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            LOG.d(".onActivityCreated entree with savedInstanceState : " + savedInstanceState);


            Bundle bundle = getArguments();
            LOG.d(".onActivityCreated bundle is null ? : " + (bundle == null));
            if (bundle != null) {

//                LOG.d(".onCreateView create view with layout  R.layout.fragment_detailpostyoutube");
//                getActivity().setContentView(R.layout.fragment_detailpostyoutube);
                String jsonMyObject;
                jsonMyObject = bundle.getString("post");
                final Post post = new Gson().fromJson(jsonMyObject, Post.class);
                LOG.d(".onActivityCreated setText with text : " + bundle.getString("link"));
                setPost(post);

//BLOC COPY oncreateview
                LOG.d(".onActivityCreated getting supportFragmentManager");
                FragmentManager supportFragmentManager =  getActivity().getSupportFragmentManager();
                LOG.d(".onActivityCreated find web view with id  R.id.detailPostTextYoutube");
                webview = (WebView) getActivity().findViewById(R.id.detailPostTextYoutube);
                LOG.d(".onActivityCreated webview found ? " + (webview != null));
                if (webview ==null){
                    LOG.d(".onActivityCreated webview 'detailPostTextYoutube' to create");
                    webview = new WebView(getActivity());
                }
                LOG.d(".onActivityCreated find youtube fragment with id  R.id.detailPostVideoYoutube");
                youTubePlayerFragment =
                        (YoutubePlayerFragment) supportFragmentManager.findFragmentById(R.id.detailPostVideoYoutube);
                LOG.d(".onActivityCreated youTubePlayerFragment found ? " + (youTubePlayerFragment != null));
                if (youTubePlayerFragment==null){
                    LOG.d(".onActivityCreated fragment 'detailPostVideoYoutube' to create");
                    youTubePlayerFragment = new YoutubePlayerFragment();
                    LOG.d(".onActivityCreated create FragmentTransaction");
                    FragmentTransaction sfmTx = supportFragmentManager.beginTransaction();
                    LOG.d(".onActivityCreated add add(R.id.detailPostVideoYoutube, youTubePlayerFragment) in FragmentTransaction");
                    sfmTx.add(R.id.detailPostVideoYoutube, youTubePlayerFragment);
                    LOG.d(".onActivityCreated commit FragmentTransaction");
                    sfmTx.commit();
                }

//BLOC COPY oncreateview FIN


                LOG.d(".onActivityCreated find web view with id  R.id.detailPostTextYoutube");
//                webview = (WebView) getActivity().findViewById(R.id.detailPostTextYoutube);
                LOG.d(".onActivityCreated webview found ? " + (webview != null));
//                LOG.d(".onActivityCreated find youtube fragment with id  R.id.detailPostVideoYoutube");
//                youTubePlayerFragment =
//                        (YoutubePlayerFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detailPostVideoYoutube);
                LOG.d(".onActivityCreated youTubePlayerFragment found ? " + (youTubePlayerFragment != null));
//        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                LOG.d(".onActivityCreated create youtubeListener");
                youtubeListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                        boolean wasRestored) {

                        if (!wasRestored) {
                            // Remplissage de l'objet web(vidéo ou autre) et de la vue vidéo
                            OEmbedCache object = post.getO_embed_cache();
                            if (object != null) {
                                Data objectData = object.getData();
                                if (objectData != null) {
                                    String objectHtml = objectData.getHtml();
                                    if (objectHtml != null && !objectHtml.isEmpty()) {

                                        LOG.d(".onInitializationSuccess Object HTML récolté '" + objectHtml + "'");
                                        //Cas de la vidéo Youtube
                                        if (objectHtml.contains("youtube")) {
                                            int indexSrcBegin = objectHtml.indexOf("src=\"");
                                            indexSrcBegin = indexSrcBegin + "src=\"".length();
                                            int indexSrcEnd = objectHtml.indexOf("?feature=oembed", indexSrcBegin);
                                            String urlSrc = objectHtml.substring(indexSrcBegin, indexSrcEnd);
                                            indexSrcBegin = urlSrc.lastIndexOf("/") + 1;
                                            urlSrc = urlSrc.substring(indexSrcBegin);
                                            LOG.d(".onInitializationSuccess ID de la vidéo récoltée '" + urlSrc + "'");
                                            player.cueVideo(urlSrc);
                                        }
                                    }
                                }
                            }
//                        player.cueVideo("nCgQDjiotG0");
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult errorReason) {
                        if (errorReason.isUserRecoverableError()) {
                            errorReason.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
                        } else {
                            String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
                            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                };
                if (youTubePlayerFragment != null) {
                    LOG.d(".onActivityCreated initialize youTubePlayerFragment");
                    youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, youtubeListener);
                } else {
                    LOG.d(".onActivityCreated initialize youTubePlayerFragment failed");
                }
            }
            LOG.d(".onActivityCreated entree with savedInstanceState : " + savedInstanceState);
        }catch(Throwable thr) {
            LOG.e(".onActivityCreated sortie en Erreur ("+thr.toString()+")");
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public void setPost(Post post) {
        LOG.d(".setPost entree with post : "+post);
        try {
            if (webview != null) {
//            LOG.d(".setText setInitialScale");
//            webview.setInitialScale(50);
//            LOG.d(".setText loadUrl with : "+item);
//            webview.loadUrl(item);
                LOG.d(".setPost getSettings set params");
                webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webview.getSettings().setUseWideViewPort(true);
                webview.getSettings().setLoadWithOverviewMode(true);
                webview.getSettings().setJavaScriptEnabled(true);
                // Remplissage de l'objet web
                StringBuilder sbHtml = new StringBuilder();
                //Ajout du style
                sbHtml.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"stylePost.css\" />");

                //AJOUT D'UN POST TEST dans la webview
                sbHtml.append("<div class=\"stream_element loaded\" data-template=\"stream-element\"><div class=\"media\">\n" +
                        "\n" +
                        "  \n" +
                        "    <a href=\"/people/4d1388e030d8013219742a0000053625\" class=\"img hovercardable\">\n" +
                        "      <img src=\"https://framasphere.org/uploads/images/thumb_small_a5ffc454c286191bfa26.jpg\" class=\"avatar small\" title=\"S4mdf0o1\">\n" +
                        "    </a>\n" +
                        "  \n" +
                        "\n" +
                        "  <div class=\"bd\">\n" +
                        "    \n" +
                        "      <div class=\"controls\">\n" +
                        "        \n" +
                        "          <a href=\"#\" rel=\"nofollow\" data-type=\"post\" class=\"post_report\" title=\"Rapport\">\n" +
                        "            <div class=\"icons-report control_icon\"></div>\n" +
                        "          </a>\n" +
                        "          <a href=\"#\" rel=\"nofollow\" class=\"block_user\" data-original-title=\"Ignorer\">\n" +
                        "            <div class=\"icons-ignoreuser control_icon\"></div>\n" +
                        "          </a>\n" +
                        "          <a href=\"#\" rel=\"nofollow\" class=\"delete hide_post\" data-original-title=\"Masquer\">\n" +
                        "            <div class=\"icons-deletelabel delete control_icon\" data-original-title=\"\"></div>\n" +
                        "          </a>\n" +
                        "        \n" +
                        "      </div>\n" +
                        "    \n" +
                        "    \n" +
                        "    <div>\n" +
                        "      \n" +
                        "        <a href=\"/people/4d1388e030d8013219742a0000053625\" class=\"author hovercardable\">S4mdf0o1</a>\n" +
                        "      \n" +
                        "\n" +
                        "      <span class=\"details grey\">\n" +
                        "        -\n" +
                        "        <a href=\"/posts/372274\">\n" +
                        "          <time class=\"timeago\" data-original-title=\"4/2/2015 05:17:13\" datetime=\"2015-02-04T04:17:13Z\">il y a environ 6 heures</time>\n" +
                        "        </a>\n" +
                        "\n" +
                        "        \n" +
                        "      </span>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    \n" +
                        "        \n" +
                        "      <div class=\"post-content\"><div data-template=\"status-message\">\n" +
                        "\n" +
                        "<div class=\"collapsible opened\" style=\"height: auto;\">\n" +
                        "  <p>Saluzatous, <a href=\"/tags/nouveauici\" class=\"tag\">#nouveauici</a>, surtout branché <a href=\"/tags/logiciellibre\" class=\"tag\">#logiciellibre</a> et <a href=\"/tags/diy\" class=\"tag\">#diy</a> dans un monde propriétaire, impérialiste, concurrent et hypocrite.</p>\n" +
                        "\n" +
                        "<p>J'aime à dire : \"Les dysfonctionnements assurent l'Emploi\" et \"l'Évolution ne s'arrête pas à la suffisance\" (déclaré à un proviseur de lycée, lors d'un entretien d'embauche, suite à sa déclaration : \"Ici on ne fait pas de Freeware\", avec de grands gestes...)</p>\n" +
                        "\n" +
                        "<p>En passe de créer une asso locale \"Transition Libre\", dont l'émergence trouve comme tout premier obstacle -en grandes discussions sur la méthode-, la difficulté d'amener les gens à juste comprendre, sans les brusquer, ni les froisser, ni les choquer dans leurs fondements...</p>\n" +
                        "\n" +
                        "<p>Bref plein de volonté, d'auto-critique et par là-même d'auto-censure, cherchant la voie de la lumière...</p>\n" +
                        "\n" +
                        "<p>Que nos vœux nous libèrent ! \\o/ LOL</p>\n" +
                        "  <div class=\"oembed\"><div data-template=\"oembed\">\n" +
                        "</div></div>\n" +
                        "  <div class=\"opengraph\"><div data-template=\"opengraph\">\n" +
                        "  \n" +
                        "\n" +
                        "\n" +
                        "</div></div>\n" +
                        "  <div class=\"poll\"><div data-template=\"poll\">\n" +
                        "</div></div>\n" +
                        "<div class=\"expander\" style=\"display: none;\">Voir plus</div></div>\n" +
                        "</div></div>\n" +
                        "      <div class=\"status-message-location\"><div data-template=\"status-message-location\">\n" +
                        "</div></div>\n" +
                        "\n" +
                        "      <div class=\"feedback\"><div class=\"info\" data-template=\"feedback\"><span class=\"post_scope grey\" data-original-title=\"\">\n" +
                        "  \n" +
                        "    Public\n" +
                        "  \n" +
                        "\n" +
                        "  \n" +
                        "  –\n" +
                        "</span>\n" +
                        "\n" +
                        "\n" +
                        "<a href=\"#\" class=\"like\" rel=\"nofollow\">\n" +
                        "  \n" +
                        "    J'aime\n" +
                        "  \n" +
                        "</a>\n" +
                        "·\n" +
                        "\n" +
                        "\n" +
                        "  <a href=\"#\" class=\"reshare\" rel=\"nofollow\">\n" +
                        "    Repartager\n" +
                        "  </a>\n" +
                        "  ·\n" +
                        "\n" +
                        "\n" +
                        "<a href=\"#\" class=\"focus_comment_textarea\" rel=\"nofollow\">\n" +
                        "  Commenter\n" +
                        "</a>\n" +
                        "</div></div>\n" +
                        "      <div class=\"likes\"><div data-template=\"likes-info\">\n" +
                        "  <div class=\"comment\">\n" +
                        "    <div class=\"media\">\n" +
                        "      <div alt=\"Heart\" class=\"icons-heart\"></div>\n" +
                        "\n" +
                        "      <div class=\"bd\">\n" +
                        "        \n" +
                        "          <a href=\"#\" class=\"expand_likes grey\">\n" +
                        "            2 J'aime\n" +
                        "          </a>\n" +
                        "\n" +
                        "        \n" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "\n" +
                        "</div></div>\n" +
                        "      <div class=\"comments\"><div class=\"comment_stream\" data-template=\"comment-stream\">\n" +
                        "  <div class=\"show_comments comment  hidden \">\n" +
                        "    <div class=\"media\">\n" +
                        "      <a href=\"/posts/372274#comments\" class=\"toggle_post_comments\">\n" +
                        "        Montrer 0 commentaire supplémentaire\n" +
                        "      </a>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "\n" +
                        "\n" +
                        "<div class=\"comments\"> <div class=\"comment media\" data-template=\"comment\"><div id=\"8b3fdc308e61013233dc2a0000053625\">\n" +
                        "  <div class=\"img\">\n" +
                        "    <a href=\"/people/cd6984d07e2b0132ce3a2a0000053625\" class=\"author-name hovercardable\">\n" +
                        "      <img src=\"https://framasphere.org/uploads/images/thumb_small_45bea538502256380c74.jpg\" class=\"avatar small\" title=\"Pulce 2081\">\n" +
                        "    </a>\n" +
                        "  </div>\n" +
                        "\n" +
                        "  <div class=\"bd\">\n" +
                        "    <div class=\"controls\">\n" +
                        "    \n" +
                        "      \n" +
                        "        <a href=\"#\" data-type=\"comment\" class=\"comment_report\" title=\"Rapport\">\n" +
                        "          <div class=\"icons-report\"></div>\n" +
                        "        </a>\n" +
                        "      \n" +
                        "    \n" +
                        "    </div>\n" +
                        "\n" +
                        "    \n" +
                        "      <a href=\"/people/cd6984d07e2b0132ce3a2a0000053625\" class=\"author author-name hovercardable\">\n" +
                        "        Pulce 2081\n" +
                        "      </a>\n" +
                        "    \n" +
                        "\n" +
                        "    <div class=\"collapsible comment-content\">\n" +
                        "      <p>Bienvenue parmi nous :)</p>\n" +
                        "    </div>\n" +
                        "    \n" +
                        "    <div class=\"info\">\n" +
                        "      <a href=\"/posts/372274#8b3fdc308e61013233dc2a0000053625\" class=\"permalink_comment\">\n" +
                        "        <time class=\"timeago\" data-original-title=\"4/2/2015 07:04:03\" datetime=\"2015-02-04T06:04:03Z\">il y a environ 4 heures</time>\n" +
                        "      </a>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "</div>\n" +
                        "</div><div class=\"comment media\" data-template=\"comment\"><div id=\"a6ed6d808e77013233de2a0000053625\">\n" +
                        "  <div class=\"img\">\n" +
                        "    <a href=\"/people/04385b502a77013283742a0000053625\" class=\"author-name hovercardable\">\n" +
                        "      <img src=\"https://framasphere.org/uploads/images/thumb_small_746b9583058f3185f690.jpg\" class=\"avatar small\" title=\"Sébastien Adam\">\n" +
                        "    </a>\n" +
                        "  </div>\n" +
                        "\n" +
                        "  <div class=\"bd\">\n" +
                        "    <div class=\"controls\">\n" +
                        "    \n" +
                        "      \n" +
                        "        <a href=\"#\" data-type=\"comment\" class=\"comment_report\" title=\"Rapport\">\n" +
                        "          <div class=\"icons-report\"></div>\n" +
                        "        </a>\n" +
                        "      \n" +
                        "    \n" +
                        "    </div>\n" +
                        "\n" +
                        "    \n" +
                        "      <a href=\"/people/04385b502a77013283742a0000053625\" class=\"author author-name hovercardable\">\n" +
                        "        Sébastien Adam\n" +
                        "      </a>\n" +
                        "    \n" +
                        "\n" +
                        "    <div class=\"collapsible comment-content\">\n" +
                        "      <p>Bienvenue ici S4mdf0o1 (heu... ça se prononce comment? ;-) ). J'espère que ton projet verra le jour.</p>\n" +
                        "\n" +
                        "<p>Si tu as besoin d'aide pour t'y retrouver ici, tu peux utiliser les liens sur la droite de la page (<a href=\"https://framasphere.org/help\" target=\"_blank\">aide</a>, <a href=\"https://diasporafoundation.org/tutorials\" target=\"_blank\">tutoriels</a> et <a href=\"https://wiki.diasporafoundation.org/Main_Page/fr\" target=\"_blank\">wiki</a>), le fameux <a href=\"https://diaspora-fr.org/posts/683275\" target=\"_blank\">kit de base</a> de <a href=\"https://diaspora-fr.org/people/a67fe237dabfccac\" target=\"_blank\">@SpF</a> ou le tag <a href=\"/tags/question\" class=\"tag\">#question</a>.</p>\n" +
                        "    </div>\n" +
                        "    \n" +
                        "    <div class=\"info\">\n" +
                        "      <a href=\"/posts/372274#a6ed6d808e77013233de2a0000053625\" class=\"permalink_comment\">\n" +
                        "        <time class=\"timeago\" data-original-title=\"4/2/2015 09:42:18\" datetime=\"2015-02-04T08:42:18Z\">il y a environ une heure</time>\n" +
                        "      </a>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "</div>\n" +
                        "</div><div class=\"comment media\" data-template=\"comment\"><div id=\"a91346c08e7b013233dc2a0000053625\">\n" +
                        "  <div class=\"img\">\n" +
                        "    <a href=\"/people/de224dd03084013219702a0000053625\" class=\"author-name hovercardable\">\n" +
                        "      <img src=\"/assets/user/default.png\" class=\"avatar small\" title=\"Antoine\">\n" +
                        "    </a>\n" +
                        "  </div>\n" +
                        "\n" +
                        "  <div class=\"bd\">\n" +
                        "    <div class=\"controls\">\n" +
                        "    \n" +
                        "      \n" +
                        "        <a href=\"#\" data-type=\"comment\" class=\"comment_report\" title=\"Rapport\">\n" +
                        "          <div class=\"icons-report\"></div>\n" +
                        "        </a>\n" +
                        "      \n" +
                        "    \n" +
                        "    </div>\n" +
                        "\n" +
                        "    \n" +
                        "      <a href=\"/people/de224dd03084013219702a0000053625\" class=\"author author-name hovercardable\">\n" +
                        "        Antoine\n" +
                        "      </a>\n" +
                        "    \n" +
                        "\n" +
                        "    <div class=\"collapsible comment-content\">\n" +
                        "      <p>\"Ici on ne fait pas de freeware\". SO CUTE</p>\n" +
                        "    </div>\n" +
                        "    \n" +
                        "    <div class=\"info\">\n" +
                        "      <a href=\"/posts/372274#a91346c08e7b013233dc2a0000053625\" class=\"permalink_comment\">\n" +
                        "        <time class=\"timeago\" data-original-title=\"4/2/2015 10:11:00\" datetime=\"2015-02-04T09:11:00Z\">il y a environ une heure</time>\n" +
                        "      </a>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "</div>\n" +
                        "</div></div>\n" +
                        "\n" +
                        "\n" +
                        "  <div class=\"comment no-border media new_comment_form_wrapper \">\n" +
                        "    \n" +
                        "      <a href=\"/people/1f9bd9b030fc013219722a0000053625\" class=\"img\">\n" +
                        "        <img src=\"https://framasphere.org/uploads/images/thumb_small_93779d6fd285331e91dd.jpg\" class=\"avatar small\" title=\"Sebastien Caron\">\n" +
                        "      </a>\n" +
                        "    \n" +
                        "\n" +
                        "    <div class=\"bd\">\n" +
                        "      <form accept-charset=\"UTF-8\" action=\"/posts/372274/comments\" class=\"new_comment\" id=\"new_comment_on_372274\" method=\"post\">\n" +
                        "        <textarea class=\"comment_box\" id=\"comment_text_on_372274\" name=\"text\" rows=\"2\" placeholder=\"Commenter\" style=\"resize: none; overflow-y: hidden;\"></textarea>\n" +
                        "        <div class=\"submit_button\">\n" +
                        "          <input class=\"button creation\" id=\"comment_submit_372274\" name=\"commit\" type=\"submit\" value=\"Commenter\">\n" +
                        "        </div>\n" +
                        "      </form>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "\n" +
                        "</div></div>\n" +
                        "    \n" +
                        "\n" +
                        "  </div>\n" +
                        "</div></div>");


                // Ajout du titre
                sbHtml.append("<h1>" + post.getTitle() + "</h1>");
                // Ajout du text
                sbHtml.append("<div>" + post.getText() + "</div>");
                // Remplissage de l'objet web avec l'objet
                OEmbedCache object = post.getO_embed_cache();
                if (object != null) {
                    Data objectData = object.getData();
                    if (objectData != null) {
                        String objectHtml = objectData.getHtml();
                        if (objectHtml != null && !objectHtml.isEmpty()) {

                            LOG.d(".setPost Object HTML récolté '" + objectHtml + "'");
                            sbHtml.append(objectHtml);
                        }
                    }
                }
                webview.loadDataWithBaseURL("file:///android_asset/", sbHtml.toString(), mimeType, encoding, "");
//            webview.loadDataWithBaseURL(null, sbHtml.toString(), mimeType, encoding, "");
                LOG.d(".setPost sortie en succès");
                return;
            }
        }catch(Throwable thr) {
            LOG.e(".setPost sortie en Erreur ("+thr.toString()+")");
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
        LOG.d(".setPost not possible webview not found");
        LOG.d(".setPost sortie en erreur");
    }
}
