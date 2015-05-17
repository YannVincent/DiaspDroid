package fr.android.scaron.diaspdroid.model;

import com.google.gson.annotations.SerializedName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.android.scaron.diaspdroid.controler.LogControler;

/**
 * Created by Maison on 11/01/2015.
 *
 *
 *
 *
 */
/*
{
  "id": 303129,
  "guid": "cd7125407bdd0132ce302a0000053625",
  "text": "Que l'on soit #fran\u00e7ais, #arabe, #riche, #pauvre, #drogu\u00e9, #tromp\u00e9, #perverti, #abus\u00e9, #affam\u00e9, #engraiss\u00e9, que l'on aime la #musique ou que l'on pr\u00e9f\u00e8re le #bruit, que l'on soit #Charlie ou bien sa famille, j'ai l'impression que tout le monde oublie que finalement, on est tous dans le m\u00eame panier.\r\n\r\nhttps://soundcloud.com/la-quincaillerie/tous",
  "public": true,
  "created_at": "2015-01-11T16:35:40Z",
  "interacted_at": "2015-01-11T16:35:40Z",
  "provider_display_name": null,
  "post_type": "StatusMessage",
  "image_url": null,
  "object_url": null,
  "favorite": false,
  "nsfw": false,
  "author": {
    "id": 18534,
    "guid": "d09312405c880132492c2a0000053625",
    "name": "Lucas",
    "diaspora_id": "kaptain_koala@framasphere.org",
    "avatar": {
      "small": "https://framasphere.org/uploads/images/thumb_small_99b5f97ca03bc762cb0d.jpg",
      "medium": "https://framasphere.org/uploads/images/thumb_medium_99b5f97ca03bc762cb0d.jpg",
      "large": "https://framasphere.org/uploads/images/thumb_large_99b5f97ca03bc762cb0d.jpg"
    }
  },
  "o_embed_cache": {
    "data": {
      "version": 1.0,
      "type": "rich",
      "provider_name": "SoundCloud",
      "provider_url": "http://soundcloud.com",
      "height": 400,
      "width": 420,
      "title": "Tous by La Quincaillerie",
      "description": "La Quincaillerie fait une promotion sur les lames de scie sauteuse d'\u00e9paisseur 8 (d\u00e9grippant fourni).\n\nAdrien Fossaert : Voix lead, Guitare.\nPaul Jochmans : Percussions, Voix. \nThomas Fossaert : Saxophones, Clavier, Voix.\n\nSite web : www.collectifquincaille.com/\nFacebook : www.facebook.com/pages/La-Quincai\u202619326680?fref=ts\n\nContact :\ntoms_box@hotmail.fr\n0675386501",
      "thumbnail_url": "http://a1.sndcdn.com/images/fb_placeholder.png?1418912302",
      "html": "\u003Ciframe width=\"420\" height=\"400\" scrolling=\"no\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?visual=true\u0026url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F130295814\u0026show_artwork=true\u0026frame=1\u0026maxheight=420\u0026maxwidth=420\"\u003E\u003C/iframe\u003E",
      "author_name": "La Quincaillerie",
      "author_url": "http://soundcloud.com/la-quincaillerie",
      "trusted_endpoint_url": "http://soundcloud.com/oembed"
    }
  },
  "open_graph_cache": null,
  "mentioned_people": [

  ],
  "photos": [

  ],
  "root": null,
  "title": "Que l'on soit #fr...",
  "address": null,
  "poll": null,
  "already_participated_in_poll": null,
  "interactions": {
    "likes": [

    ],
    "reshares": [

    ],
    "comments_count": 0,
    "likes_count": 0,
    "reshares_count": 0,
    "comments": [

    ]
  }
}
 */
public class Post {

    private static Logger LOGGEUR = LoggerFactory.getLogger(Post.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = Post.class.getSimpleName();

    public static Post createPostErreur(String pTextErreur){
        Post postError = new Post();
        postError.setId(0);
        postError.setText(pTextErreur);
        return postError;
    }
    public boolean asYoutubeVideo;
    public String idYoutubeVideo;
    public boolean asWebSiteUrl;
    public String webSiteUrl;
    //"id": 302726,
    Integer id;
    //"guid": "56cacec07bc40132ce282a0000053625",
    String guid;
    //"text": "Bienvenu \u00e0 @{ced hns ; cedhns@framasphere.org} qui est #nouveauici et qui, je l'esp\u00e8re, s'y plaira ! (En encore un prof, un ! ;-)",
    String text;
    //"public": true,
    @SerializedName("public")
    Boolean isPublic;
    //"created_at": "2015-01-11T13:33:23Z",
    //use Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    //"created_at": "2015-05-13T12:33:30.521Z", evolution diaspora code 0.5
    //
    Date created_at;

    //"interacted_at": "2015-01-11T13:36:32Z",
    //use Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    Date interacted_at;

    //"provider_display_name": null,
    String provider_display_name;

    //"post_type": "StatusMessage",
    String post_type;

    //"image_url": null,
    String image_url;

    //"object_url": null,
    String object_url;

    //"favorite": false,
    Boolean favorite;
    //"nsfw": false,

//    Boolean nsfw;
    Object nsfw;
//    "author": {
//        "id": 7449,
//                "guid": "50b8df903074013219742a0000053625",
//                "name": "Julien",
//                "diaspora_id": "julien@framasphere.org",
//                "avatar": {
//            "small": "https://framasphere.org/uploads/images/thumb_small_ab1d6560034de7d0402f.jpg",
//                    "medium": "https://framasphere.org/uploads/images/thumb_medium_ab1d6560034de7d0402f.jpg",
//                    "large": "https://framasphere.org/uploads/images/thumb_large_ab1d6560034de7d0402f.jpg"
//        }
//    },
    People author;

//    "o_embed_cache": {
//        "data": {
//            "author_name": "micka5973",
//                    "provider_url": "http://www.youtube.com/",
//                    "provider_name": "YouTube",
//                    "thumbnail_height": 360,
//                    "height": 315,
//                    "author_url": "http://www.youtube.com/user/micka5973",
//                    "version": "1.0",
//                    "width": 420,
//                    "html": "\u003Ciframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/2aRakmBRukQ?feature=oembed\" frameborder=\"0\" allowfullscreen\u003E\u003C/iframe\u003E",
//                    "title": "hexagone renaud",
//                    "type": "video",
//                    "thumbnail_url": "https://i.ytimg.com/vi/2aRakmBRukQ/hqdefault.jpg",
//                    "thumbnail_width": 480,
//                    "trusted_endpoint_url": "http://www.youtube.com/oembed?scheme=https"
//        }
//    },
    OEmbedCache o_embed_cache;

//    "open_graph_cache": {
//        "title": "Un Pav\u00e9 Dans l'Asphalte - Unfamous Resistenza",
//                "ob_type": "website",
//                "image": "https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-ash4/603100_398394743582765_1448253908_n.jpg",
//                "description": "100 artistes alternatifs - 100 morceaux \u00e0 t\u00e9l\u00e9charger gratuitement !",
//                "url": "http://www.unpavedanslasphalte.com/"
//    }
    OpenGraphCache open_graph_cache;

//    "mentioned_people": [
//    {
//        "id": 37,
//            "guid": "29a1b659217ab9ce",
//            "name": "St\u00e9phane 22decembre",
//            "diaspora_id": "22decembre@diasp.eu",
//            "avatar": {
//        "small": "https://diasp.eu/uploads/images/thumb_small_f876531c8328a8dfd477.jpg",
//                "medium": "https://diasp.eu/uploads/images/thumb_medium_f876531c8328a8dfd477.jpg",
//                "large": "https://diasp.eu/uploads/images/thumb_large_f876531c8328a8dfd477.jpg"
//    }
//    }
//    ],
    ArrayList<People> mentioned_people;

//    "photos": [
//    {
//        "id": 76300,
//        "guid": "e618aae07d910132a5c1221315d95ef2",
//        "created_at": "2015-01-13T20:37:22Z",
//        "author": {
//                "id": 78,
//                "guid": "a9fa9480b39f013119525b11fd42445c",
//                "name": "Caps",
//                "diaspora_id": "caps@diaspora-fr.org",
//                "avatar": {
//                    "small": "https://diaspora-fr.org/uploads/images/thumb_small_b9fda0453f1b5de0a4a1.jpg",
//                    "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_b9fda0453f1b5de0a4a1.jpg",
//                    "large": "https://diaspora-fr.org/uploads/images/thumb_large_b9fda0453f1b5de0a4a1.jpg"
//                }
//        },
//        "sizes": {
//                "small": "https://diaspora-fr.org/uploads/images/thumb_small_1d21378b0a344a0a9d06.jpg",
//                "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_1d21378b0a344a0a9d06.jpg",
//                "large": "https://diaspora-fr.org/uploads/images/scaled_full_1d21378b0a344a0a9d06.jpg"
//        },
//        "dimensions": {
//                "height": 2401,
//                "width": 3608
//        }
//    }
//    ]
    ArrayList<Photo> photos;

//    "root": {
//            "id": 277359,
//            "guid": "21c0d44074ae0132ce062a0000053625",
//            "text": "Pourquoi refuser la L\u00e9gion d'honneur, par l'odieux connard :\r\n\r\n![Pourquoi refuser la L\u00e9gion d'honneur, par l'odieux connard](https://odieuxconnard.files.wordpress.com/2015/01/honneurlegion.jpg)",
//            "public": true,
//            "created_at": "2015-01-02T13:06:47Z",
//            "interacted_at": "2015-01-04T09:43:59Z",
//            "provider_display_name": null,
//            "post_type": "StatusMessage",
//            "image_url": null,
//            "object_url": null,
//            "favorite": false,
//            "nsfw": false,
//            "author": {
//        "id": 5,
//                "guid": "b13eb6b0beac0131e7e32a0000053625",
//                "name": "Framasky",
//                "diaspora_id": "framasky@framasphere.org",
//                "avatar": {
//            "small": "https://framasphere.org/uploads/images/thumb_small_cff201fed872f01be231.png",
//                    "medium": "https://framasphere.org/uploads/images/thumb_medium_cff201fed872f01be231.png",
//                    "large": "https://framasphere.org/uploads/images/thumb_large_cff201fed872f01be231.png"
//        }
//    },
//            "o_embed_cache": null,
//            "open_graph_cache": null,
//            "mentioned_people": [
//
//            ],
//            "photos": [
//
//            ],
//            "root": null,
//            "title": "Pourquoi refuser ...",
//            "address": null,
//            "poll": null,
//            "already_participated_in_poll": null,
//            "interactions": {
//        "likes": [
//
//        ],
//        "reshares": [
//
//        ],
//        "comments_count": 4,
//                "likes_count": 22,
//                "reshares_count": 13
//    }
//}
    Post root;
//    "title": "St\u00e9phane 22decemb...",
    String title;
//    "address": null,
    String adress;
//    "poll": null,
    Poll poll;
//    "already_participated_in_poll": null,
    String already_participated_in_poll;


//    "interactions": {
//        "likes": [
//
//        ],
//        "reshares": [
//
//        ],
//        "comments_count": 7,
//                "likes_count": 1,
//                "reshares_count": 0,
//                "comments": [
//        {
//            "id": 245281,
//                "guid": "677102c07bc00132a5a3221315d95ef2",
//                "text": "Faut pas crier trop fort qu'on se trouve plut\u00f4t pas b\u00eate, c'est toujours la qu'on tombe sur un mec qui va te faire passer pour un idiot fini ! :D",
//                "author": {
//            "id": 103,
//                    "guid": "f4130fc32ed91f6a",
//                    "name": "dada",
//                    "diaspora_id": "dada@diaspora-fr.org",
//                    "avatar": {
//                "small": "https://diaspora-fr.org/uploads/images/thumb_small_47348508d4b08a2b9dd1.png",
//                        "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_47348508d4b08a2b9dd1.png",
//                        "large": "https://diaspora-fr.org/uploads/images/thumb_large_47348508d4b08a2b9dd1.png"
//            }
//        },
//            "created_at": "2015-01-11T13:03:55Z"
//        },
//        {
//            "id": 245285,
//                "guid": "891337b07bc00132a5a3221315d95ef2",
//                "text": "D'ailleurs, si je vais a la manif de tout a l'heure avec cette musique a fond, j'vais me faire massacrer, non ? ^^",
//                "author": {
//            "id": 103,
//                    "guid": "f4130fc32ed91f6a",
//                    "name": "dada",
//                    "diaspora_id": "dada@diaspora-fr.org",
//                    "avatar": {
//                "small": "https://diaspora-fr.org/uploads/images/thumb_small_47348508d4b08a2b9dd1.png",
//                        "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_47348508d4b08a2b9dd1.png",
//                        "large": "https://diaspora-fr.org/uploads/images/thumb_large_47348508d4b08a2b9dd1.png"
//            }
//        },
//            "created_at": "2015-01-11T13:04:29Z"
//        },
//        {
//            "id": 245292,
//                "guid": "c1f940807bc0013280105404a6b20780",
//                "text": "J'en sais rien... Moi je vais pas \u00e0 la manif :D",
//                "author": {
//            "id": 37,
//                    "guid": "29a1b659217ab9ce",
//                    "name": "St\u00e9phane 22decembre",
//                    "diaspora_id": "22decembre@diasp.eu",
//                    "avatar": {
//                "small": "https://diasp.eu/uploads/images/thumb_small_f876531c8328a8dfd477.jpg",
//                        "medium": "https://diasp.eu/uploads/images/thumb_medium_f876531c8328a8dfd477.jpg",
//                        "large": "https://diasp.eu/uploads/images/thumb_large_f876531c8328a8dfd477.jpg"
//            }
//        },
//            "created_at": "2015-01-11T13:07:46Z"
//        }
//        ]
//    }
    Interaction interactions;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getCreated_at_str(){
        DateFormat dateFormat = new SimpleDateFormat(Constant.DATETIME_PATTERN);
        return dateFormat.format(created_at);
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getInteracted_at() {
        return interacted_at;
    }

    public void setInteracted_at(Date interacted_at) {
        this.interacted_at = interacted_at;
    }

    public String getProvider_display_name() {
        return provider_display_name;
    }

    public void setProvider_display_name(String provider_display_name) {
        this.provider_display_name = provider_display_name;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getObject_url() {
        return object_url;
    }

    public void setObject_url(String object_url) {
        this.object_url = object_url;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Object getNsfw() {
        return nsfw;
    }
//    public Boolean getNsfw() {
//        return nsfw;
//    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public People getAuthor() {
        return author;
    }

    public void setAuthor(People author) {
        this.author = author;
    }

    public OEmbedCache getO_embed_cache() {
        return o_embed_cache;
    }

    public void setO_embed_cache(OEmbedCache o_embed_cache) {
        this.o_embed_cache = o_embed_cache;
    }

    public OpenGraphCache getOpen_graph_cache() {
        return open_graph_cache;
    }

    public void setOpen_graph_cache(OpenGraphCache open_graph_cache) {
        this.open_graph_cache = open_graph_cache;
    }

    public ArrayList<People> getMentioned_people() {
        return mentioned_people;
    }

    public void setMentioned_people(ArrayList<People> mentioned_people) {
        this.mentioned_people = mentioned_people;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public Post getRoot() {
        return root;
    }

    public void setRoot(Post root) {
        this.root = root;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getAlready_participated_in_poll() {
        return already_participated_in_poll;
    }

    public void setAlready_participated_in_poll(String already_participated_in_poll) {
        this.already_participated_in_poll = already_participated_in_poll;
    }

    public Interaction getInteractions() {
        return interactions;
    }

    public void setInteractions(Interaction interactions) {
        this.interactions = interactions;
    }


    @Override
    public boolean equals(Object object){
//        String TAG_METHOD = TAG + ".equals : ";
//        LOG.d(TAG_METHOD + "Entree");
        boolean sameId = false;
        if (object != null && object instanceof Post){
//            LOG.d(TAG_METHOD + "Compare this.guid="+this.id+" with object.guid="+((Post) object).id);
            sameId = this.guid.equals(((Post) object).guid);
        }
//        LOG.d(TAG_METHOD + "Sortie with value "+sameId);
        return sameId;
    }
}
