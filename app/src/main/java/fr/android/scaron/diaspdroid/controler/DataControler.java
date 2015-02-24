package fr.android.scaron.diaspdroid.controler;

import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.UploadResult;

/**
 * Created by Maison on 13/01/2015.
 */
public class DataControler extends DiasporaControler {


    private static Logger LOGGEUR = LoggerFactory.getLogger(DataControler.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);



    static String GET_STREAM_TEST_DELTA = POD_URL + "/stream?max_time=1421018508&_=1421254891463";
    static String GET_STREAM_TEST = POD_URL + "/stream?_=1421762957984";

    String JSON_TEST = "[{\"id\":309693,\"guid\":\"19246eb07d980132ce362a0000053625\",\"text\":\"Bonjour \\u00e0 tous les framasph\\u00e9rien, faites-moi une petite place, je viens voir ce qui se passe par l\\u00e0....\\r\\n#nouveauici\",\"public\":true,\"created_at\":\"2015-01-13T21:21:44Z\",\"interacted_at\":\"2015-01-13T21:42:00Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":23985,\"guid\":\"51b03d507d960132ce362a0000053625\",\"name\":\"positive_woman@framasphere.org\",\"diaspora_id\":\"positive_woman@framasphere.org\",\"avatar\":{\"small\":\"/assets/user/default.png\",\"medium\":\"/assets/user/default.png\",\"large\":\"/assets/user/default.png\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Bonjour \\u00e0 tous le...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":2,\"likes_count\":0,\"reshares_count\":0,\"comments\":[{\"id\":251362,\"guid\":\"dc5a6fc07d9a0132ce322a0000053625\",\"text\":\"Bonsoir positive woman bienvenue et prends ta place :)\",\"author\":{\"id\":12265,\"guid\":\"428a8cc03f17013250c42a0000053625\",\"name\":\"Sanka SanKa\",\"diaspora_id\":\"sankasanka@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_520559bb0d54ac362ae9.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_520559bb0d54ac362ae9.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_520559bb0d54ac362ae9.jpg\"}},\"created_at\":\"2015-01-13T21:41:31Z\"},{\"id\":251363,\"guid\":\"ee1ec5707d9a0132ce362a0000053625\",\"text\":\"#bienvenueici @positive_woman :) tu verras on s'amuse bien.\",\"author\":{\"id\":114,\"guid\":\"2ff282c0c4c3013152ef2a0000053625\",\"name\":\"Framatophe\",\"diaspora_id\":\"framatophe@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_fcd4c267b73c466025bb.png\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_fcd4c267b73c466025bb.png\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_fcd4c267b73c466025bb.png\"}},\"created_at\":\"2015-01-13T21:42:00Z\"}]}},{\"id\":309589,\"guid\":\"066c5a207d920132a5c1221315d95ef2\",\"text\":\"**BROR GUNNAR JANSSON**\\r\\n\\r\\nA one-man-band from Sweden, a sleek drugstore cowboy, the missing link between Lightnin\\u2019 Hopkins and Kopparm\\u00e4rra.\\r\\n\\r\\nhttps://soundcloud.com/brorgunnarjansson/\\r\\n\\r\\n#musique #blues #one_man_band\",\"public\":true,\"created_at\":\"2015-01-13T20:38:16Z\",\"interacted_at\":\"2015-01-13T20:36:30Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":78,\"guid\":\"a9fa9480b39f013119525b11fd42445c\",\"name\":\"Caps\",\"diaspora_id\":\"caps@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_b9fda0453f1b5de0a4a1.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_b9fda0453f1b5de0a4a1.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_b9fda0453f1b5de0a4a1.jpg\"}},\"o_embed_cache\":{\"data\":{\"version\":1.0,\"type\":\"rich\",\"provider_name\":\"SoundCloud\",\"provider_url\":\"http://soundcloud.com\",\"height\":420,\"width\":420,\"title\":\"Bror Gunnar Jansson\",\"description\":\"A one-man-band from Sweden, a sleek drugstore cowboy, the missing link between Lightnin\\u2019 Hopkins and Kopparm\\u00e4rra.\\n\\nNew album \\\"MOAN SNAKE MOAN\\\" avaliable in France on vinyl, cd \\u0026 download on Normandeep Blues Records.\\nLimited edition of S/T available on ORANGE vinyl!.\\n\\nwww.brorgunnar.com\\n\\n_________________________________\\n\\nUPCOMING SHOWS:\\n\\n2015\\n17/1 | Nefertiti Jazz Club - G\\u00f6teborg, Sverige\\n24/1 | - Oslo, Norge\\n6/02 | New Morning - Paris, France \\n7/02 | Le Chat Vert - Rouen, France\\n11/02 | Le Batolune - Honfleur, France\\n12/02 | Le Pediluve - Ch\\u00e2tenay-Malabry, France\\n13/02 | Le Plan - Ris Orangis, France \\n14/02 | Fuzz'Yon - La Roche sur Yon, France\\n25/2 | Geronimo - Stockholm, Sverige \\n29/5 | Blues Rules Festival - Crissier, Switzerland\\n13/6 | Nostalgifestivalen - V\\u00e5rg\\u00e5rda, Sverige\\n\\nAND SOME EARLIER GIGS!\\nLe Divan du Monde - Paris, France \\nBlues Sur Seine, France\\nL'A\\u00e9ronef - Lille, France\\nLe Chabada - Angers, France\\nAzimut Festival - La Pesse, France\\nL'Antipode - Rennes, France\\nL'Astrolabe - Orl\\u00e9ans, France\\nFestival les Nuits de l'Alligator, France:\\n La Lune des Pirates - Amiens \\n L'Abordage - Evreux\\n La Maroquinerie - Paris\\n 6PAR4, Laval\\nLa M\\u00e9canique Ondulatoire - Paris, France\\nCognac Blues Passions - Cognac, France\\nFestival Blues Autour Du Zinc - Beauvais, France\\nM\\u00f6nster\\u00e5s Bluesfestival\\nWyatt Earps Riders MC \\\"Gun Party\\\"\\nDinos - \\u00c5land\\nSommarrocken - Finsp\\u00e5ng\\nNostalgifestivalen+Live \\u0026 Rockin' - V\\u00e5rg\\u00e5rda\\nAFF - Sthlm\\nMusikens Hus - H\\u00e4ngmattan, Gbg\\nEsso - Varberg\\nSt\\u00e4llet - Norrt\\u00e4lje\\nHonky Tonk Illegal, Tr\\u00fckken - Gbg, Sverige\\nClub Nuggets - Sthlm, Sverige\\nDinos - \\u00c5land\\nNefertiti Jazz Club - Gbg\\nMojo Blues Bar - K\\u00f8benhavn, Denmark\\nLerums Jazzfestival\\nMusikf\\u00f6reningen - V\\u00e4nersborg\\nMetropol - H\\u00e4rn\\u00f6sand.\\nSankt Gertruds - Malm\\u00f6\\nJazzk\\u00f6ket - \\u00d6stersund.\\nPerdido - \\u00d6rnsk\\u00f6ldsvik.\\nDalheimers hus - Gbg.\\nL' Jazz - Ljungskile\\nCC - G\\u00e4vle\\nSundsvalls Jazz \\u0026 blues\\nBurmans Musik - Ume\\u00e5\\nPustervik/Filmfestivalen - G\\u00f6teborg\\nWest Coast Jitterbugs - G\\u00f6teborg\\nGlashuset - Gislaved\\nKlubb Svartedalen\\nRock'n'Roll Bonanza\\nHultsfreed Hayride\\nUme\\u00e5 Jazzfestival \\n\\u00d6stersunds Bluesfestival \\nNefertiti Jazz Club - G\\u00f6teborg\\nFar i Hatten - Malm\\u00f6\\nKaf\\u00e9 De Luxe - V\\u00e4xj\\u00f6\\nGrand - Malm\\u00f6 \\nWyatt Earps Riders MC \\u201cGun Fest 2013\\u201d \\nNostalgifestivalen - V\\u00e5rg\\u00e5rda \\nG\\u00f6teborgs Kulturkalas \\nKlubb Corazon - \\u00d6stersund \\nJonssons Fik \\u0026 Butik - Enviken\\nHugos - Finsp\\u00e5ng\\nLiseberg\\n\\u00c5m\\u00e5ls Blues Fest\\nShowdown, Pustervik\\nGr\\u00e4nna Bluegrassfestival\\nSthlm Blues\\nTorsby Countryfestival \\nMalm\\u00f6festivalen \\nNostalgifestivalen, V\\u00e5rg\\u00e5rda\\nClub Nuggets, Sthlm \\nClub Honky Tonk, Gbg  \\n\\u201cVi drar till landet\\u201d \\nDinos, \\u00c5land \\nTrettonhelgsblues, Musikens hus, G\\u00f6teborg \\nThe Greatest Records tour w/ Old Kerry McKee \\u0026 Jack Day:\\nThe Banshee Labyrinth, Edinburgh, Scotland\\nThe Malt Cross, Nottingham, England \\nThe Lantern Society, London, England \\nDe Perk, Walsorden, Netherlands \\nWhite Trash Fast Food, Berlin, Tyskland\\nG17, Liepzig, Tyskland \\nHasenschaukel, Hamburg, Tyskland\\nTivoli, Helsingborg \\nSofar Sounds, Berlin\\nF\\u00f6rband \\u00e5t Anna von Hauswolff p\\u00e5:\\nStoran, G\\u00f6teborg \\nK\\u00e4gelbanan, Stockholm\\nKB, Malm\\u00f6 \\nVega, K\\u00f8benhavn, DanmarkAmericana Roots Festival, G\\u00f6teborg\\nGbg Blues Party \\n\\u00c5m\\u00e5ls Blues Fest\\nMalm\\u00f6festivalen\\n1000fryd, Aalborg, Danmark\\nAmericana Roots Festival, G\\u00f6teborg\\nNostalgifestivalen, V\\u00e5rg\\u00e5rda\\nNot Quite Roots, Fengersfors \\nMellemrummet, K\\u00f8benhavn, Danmark\\nCookin\\u2019, Bor\\u00e5s \\nF\\u00f6rband \\u00e5t Graveyard p\\u00e5:\\nParkteateret, Oslo, Norge \\nF\\u00f6rband \\u00e5t Bob Wayne \\u0026 The Outlaw Carnies p\\u00e5:\\nTruckstop Alaska\\nLiseberg\\nNefertiti, G\\u00f6teborg\\nUddevalla Bluesfestival\\nN\\u00e4\\u00e4sville Bluegrassfestival\\nClub Nuggets, Sthlm \\nChilifestivalen, \\u00d6rebro\\nSthlm Blues \\nHonky Tonk Illegal\\nEfterband \\u00e5t Appalachian Roots p\\u00e5:\\nS/S Marieholm\\nTors\\u00e5ker Bluegrassfestival\\nObskyrfestival\\nBebopalula, Tranemo\\nKlub Rockin\\u2019 Kats, Gbg\\nKlubb Boris, Gbg\\nClub Nuggets, Sthlm\\nThe Ex Club, Sthlm\\nHonky Tonk Illegal\\nAmericana Roots Festival, G\\u00f6teborg\\nF\\u00f6rband \\u00e5t C.W. Stoneking p\\u00e5:\\nTruckstop Alaska\\nG\\u00f6teborgs kulturkalas\",\"thumbnail_url\":\"http://i1.sndcdn.com/avatars-000081289470-xilxsn-t500x500.jpg\",\"html\":\"\\u003Ciframe width=\\\"420\\\" height=\\\"420\\\" scrolling=\\\"no\\\" frameborder=\\\"no\\\" src=\\\"https://w.soundcloud.com/player/?visual=true\\u0026url=http%3A%2F%2Fapi.soundcloud.com%2Fusers%2F3108133\\u0026show_artwork=true\\u0026frame=1\\u0026maxheight=420\\u0026maxwidth=420\\\"\\u003E\\u003C/iframe\\u003E\",\"author_name\":\"Bror Gunnar Jansson\",\"author_url\":\"http://soundcloud.com/brorgunnarjansson\",\"trusted_endpoint_url\":\"http://soundcloud.com/oembed\"}},\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[{\"id\":76300,\"guid\":\"e618aae07d910132a5c1221315d95ef2\",\"created_at\":\"2015-01-13T20:37:22Z\",\"author\":{\"id\":78,\"guid\":\"a9fa9480b39f013119525b11fd42445c\",\"name\":\"Caps\",\"diaspora_id\":\"caps@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_b9fda0453f1b5de0a4a1.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_b9fda0453f1b5de0a4a1.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_b9fda0453f1b5de0a4a1.jpg\"}},\"sizes\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_1d21378b0a344a0a9d06.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_1d21378b0a344a0a9d06.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/scaled_full_1d21378b0a344a0a9d06.jpg\"},\"dimensions\":{\"height\":2401,\"width\":3608}}],\"root\":null,\"title\":\"BROR GUNNAR JANSS...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":0,\"likes_count\":0,\"reshares_count\":0,\"comments\":[]}},{\"id\":309185,\"guid\":\"130ee1a07d7c0132ce322a0000053625\",\"text\":\"https://www.youtube.com/watch?v=xB_nKpEkILs\\r\\n\\r\\n#TheProdigy #musique #\\u00e9l\\u00e9ctro #rock #non-commercial\",\"public\":true,\"created_at\":\"2015-01-13T18:01:08Z\",\"interacted_at\":\"2015-01-13T18:01:08Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":12875,\"guid\":\"414ca06041c9013250e62a0000053625\",\"name\":\"eliot bernaud\",\"diaspora_id\":\"bigcawot@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_d084a139a25abf44bd52.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_d084a139a25abf44bd52.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_d084a139a25abf44bd52.jpg\"}},\"o_embed_cache\":{\"data\":{\"author_name\":\"TheProdigyVEVO\",\"height\":236,\"version\":\"1.0\",\"thumbnail_width\":480,\"title\":\"The Prodigy - Nasty\",\"author_url\":\"http://www.youtube.com/user/TheProdigyVEVO\",\"provider_name\":\"YouTube\",\"width\":420,\"thumbnail_height\":360,\"thumbnail_url\":\"https://i.ytimg.com/vi/xB_nKpEkILs/hqdefault.jpg\",\"provider_url\":\"http://www.youtube.com/\",\"type\":\"video\",\"html\":\"\\u003Ciframe width=\\\"420\\\" height=\\\"236\\\" src=\\\"https://www.youtube.com/embed/xB_nKpEkILs?feature=oembed\\\" frameborder=\\\"0\\\" allowfullscreen\\u003E\\u003C/iframe\\u003E\",\"trusted_endpoint_url\":\"http://www.youtube.com/oembed?scheme=https\"}},\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"https://www.youtu...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":0,\"likes_count\":0,\"reshares_count\":0,\"comments\":[]}},{\"id\":309173,\"guid\":\"a59185907d7b0132ce362a0000053625\",\"text\":\"http://www.theprodigy.com/\\r\\n\\r\\nPlus que 3 mois avant la sortie de leur nouvel album !!!\\r\\n#TheProdigy #musique #\\u00e9l\\u00e9ctro #rock #non-commercial\",\"public\":true,\"created_at\":\"2015-01-13T17:58:04Z\",\"interacted_at\":\"2015-01-13T17:58:05Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":12875,\"guid\":\"414ca06041c9013250e62a0000053625\",\"name\":\"eliot bernaud\",\"diaspora_id\":\"bigcawot@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_d084a139a25abf44bd52.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_d084a139a25abf44bd52.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_d084a139a25abf44bd52.jpg\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"http://www.thepro...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":0,\"likes_count\":0,\"reshares_count\":0,\"comments\":[]}},{\"id\":309145,\"guid\":\"70d071207d7a0132101700259069449e\",\"text\":\"Conscient, d\\u00e9viant, \\u00e9clectique et Subversif!\\r\\nOn vous pr\\u00e9pare activement le Volume 2, mais le 1er Opus est toujours dispo!\\r\\nD\\u00e9j\\u00e0 t\\u00e9l\\u00e9charg\\u00e9 par plus de 30.000 personnes, \\r\\nLe Pav\\u00e9 dans l'Asphalte, 100 Tracks en Libre T\\u00e9l\\u00e9chargement!\\r\\n\\r\\nhttp://www.unpavedanslasphalte.com/site/index.htm\\r\\n\\r\\n#fr #french #france #fr #french #musica #musique #track #musique #musik #musica #free #gratuit #compilation\",\"public\":true,\"created_at\":\"2015-01-13T17:49:26Z\",\"interacted_at\":\"2015-01-13T20:46:49Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":413,\"guid\":\"f8f3b72f70bc0277\",\"name\":\"Unfamous Resistenza\",\"diaspora_id\":\"el_joker@diasp.org\",\"avatar\":{\"small\":\"https://diasp.org/uploads/images/thumb_small_95da7890c0f866134682.jpg\",\"medium\":\"https://diasp.org/uploads/images/thumb_medium_95da7890c0f866134682.jpg\",\"large\":\"https://diasp.org/uploads/images/thumb_large_95da7890c0f866134682.jpg\"}},\"o_embed_cache\":null,\"open_graph_cache\":{\"title\":\"Un Pav\\u00e9 Dans l'Asphalte - Unfamous Resistenza\",\"ob_type\":\"website\",\"image\":\"https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-ash4/603100_398394743582765_1448253908_n.jpg\",\"description\":\"100 artistes alternatifs - 100 morceaux \\u00e0 t\\u00e9l\\u00e9charger gratuitement !\",\"url\":\"http://www.unpavedanslasphalte.com/\"},\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Conscient, d\\u00e9vian...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":1,\"likes_count\":4,\"reshares_count\":2,\"comments\":[{\"id\":251215,\"guid\":\"141011b07d8b0132a5c3221315d95ef2\",\"text\":\"Cool, m\\u00eame si je suis un peu d\\u00e9\\u00e7u de ne jamais eu de nouvelles (ni oui ni merde) apr\\u00e8s vous avoir envoy\\u00e9 un morceau de Brain Sushi .\",\"author\":{\"id\":44,\"guid\":\"a727118c4188225a\",\"name\":\"FF Shukke\",\"diaspora_id\":\"ff@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_eca91a19490d72d84b58.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_eca91a19490d72d84b58.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_eca91a19490d72d84b58.jpg\"}},\"created_at\":\"2015-01-13T20:46:49Z\"}]}},{\"id\":308351,\"guid\":\"3feb2fe07d4b0132ce322a0000053625\",\"text\":\"Bonjour \\u00e0 vous tous. #nouveauici.\\r\\nMerci de m\\u2019accueillir parmi vous.\",\"public\":true,\"created_at\":\"2015-01-13T12:11:38Z\",\"interacted_at\":\"2015-01-13T19:58:25Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":23924,\"guid\":\"bedd5b607d4a0132ce322a0000053625\",\"name\":\"korsaire@framasphere.org\",\"diaspora_id\":\"korsaire@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_9d9b296b696b63e3cc7c.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_9d9b296b696b63e3cc7c.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_9d9b296b696b63e3cc7c.jpg\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Bonjour \\u00e0 vous to...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":3,\"likes_count\":2,\"reshares_count\":0,\"comments\":[{\"id\":250600,\"guid\":\"e06cb4107d700132ce362a0000053625\",\"text\":\"bienvenue !\",\"author\":{\"id\":12677,\"guid\":\"7c04a5a040e9013250e82a0000053625\",\"name\":\"Pop9\",\"diaspora_id\":\"pop9@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_d96f9fd6acd6906dd04f.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_d96f9fd6acd6906dd04f.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_d96f9fd6acd6906dd04f.jpg\"}},\"created_at\":\"2015-01-13T16:40:59Z\"},{\"id\":250866,\"guid\":\"02506f307d810132ce362a0000053625\",\"text\":\"Bienvenue :D\",\"author\":{\"id\":16796,\"guid\":\"074e746057c801326e8b2a0000053625\",\"name\":\"Mini-Rose\",\"diaspora_id\":\"miniroz@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_bb90a283e84c1e2f7802.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_bb90a283e84c1e2f7802.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_bb90a283e84c1e2f7802.jpg\"}},\"created_at\":\"2015-01-13T18:36:27Z\"},{\"id\":251101,\"guid\":\"75b04c607d8c0132ce362a0000053625\",\"text\":\"bienvenue. il ne te reste qu'\\u00e0 te pr\\u00e9senter via quelques #tag\",\"author\":{\"id\":11795,\"guid\":\"a9a832203cda013250b62a0000053625\",\"name\":\"Sylvain Blandin\",\"diaspora_id\":\"syl20@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_95eb214ecd1ce855c82c.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_95eb214ecd1ce855c82c.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_95eb214ecd1ce855c82c.jpg\"}},\"created_at\":\"2015-01-13T19:58:25Z\"}]}},{\"id\":308168,\"guid\":\"b4acb2307d440132100f00259069449e\",\"text\":\"Pour celles et ceux qui aiment le dancefloor souriant, le kick efficace de la bonne techno du petit matin, free download that stuff \\r\\n\\r\\nhttps://hearthis.at/unfrez/wicked-bitch-set-du-sept2/\\r\\n\\r\\n#fr #french #france #fr #french #blog #set #mix #7 #musica #musique #track #musique #musik #musica #free #gratuit\",\"public\":true,\"created_at\":\"2015-01-13T11:24:47Z\",\"interacted_at\":\"2015-01-13T19:00:43Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":413,\"guid\":\"f8f3b72f70bc0277\",\"name\":\"Unfamous Resistenza\",\"diaspora_id\":\"el_joker@diasp.org\",\"avatar\":{\"small\":\"https://diasp.org/uploads/images/thumb_small_95da7890c0f866134682.jpg\",\"medium\":\"https://diasp.org/uploads/images/thumb_medium_95da7890c0f866134682.jpg\",\"large\":\"https://diasp.org/uploads/images/thumb_large_95da7890c0f866134682.jpg\"}},\"o_embed_cache\":null,\"open_graph_cache\":{\"title\":\"Le Set du Sept n\\u00b02 :  Wicked Bitch\",\"ob_type\":\"song\",\"image\":\"https://hearthis.at/_/cache/images/track/512/59b1310d6ff9d1cc5161d584a4ca6fe6_w512_h512_crop4.jpg\",\"description\":\"Pour cette seconde \\u0026eacute;dition du \\u0026amp;quot;Set du 7\\u0026amp;quot;, on change totalement de vibration par rapport \\u0026agrave; la pr\\u0026eacute;c\\u0026eacute;dente. En effet si le mois dernier nous vous avions propos\\u0026eacute; un set exp\\u0026eacute;rimental et \\u0026agrave; tendance sombre, ce coup ci nous avons opt\\u0026eacute; pour\",\"url\":\"https://hearthis.at/unfrez/wicked-bitch-set-du-sept2/\"},\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Pour celles et ce...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":0,\"likes_count\":1,\"reshares_count\":0,\"comments\":[]}},{\"id\":308041,\"guid\":\"8088b7907d3e0132760456847afe9799\",\"text\":\"#musique  REPRISE DE LA PROGRAMMATION  AL GREEN http://youtu.be/mBA5tV05xAg\",\"public\":true,\"created_at\":\"2015-01-13T10:40:23Z\",\"interacted_at\":\"2015-01-13T11:27:00Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":14891,\"guid\":\"c49b1d404c800132d2ef56847afe9799\",\"name\":\"yvon balkowski\",\"diaspora_id\":\"balkowski1@pod.reseau.nouvelledonne.fr\",\"avatar\":{\"small\":\"http://pod.reseau.nouvelledonne.fr/uploads/images/thumb_small_8ad0c0e983c924e7e5f9.jpg\",\"medium\":\"http://pod.reseau.nouvelledonne.fr/uploads/images/thumb_medium_8ad0c0e983c924e7e5f9.jpg\",\"large\":\"http://pod.reseau.nouvelledonne.fr/uploads/images/thumb_large_8ad0c0e983c924e7e5f9.jpg\"}},\"o_embed_cache\":{\"data\":{\"html\":\"\\u003Ciframe width=\\\"420\\\" height=\\\"236\\\" src=\\\"https://www.youtube.com/embed/mBA5tV05xAg?feature=oembed\\\" frameborder=\\\"0\\\" allowfullscreen\\u003E\\u003C/iframe\\u003E\",\"title\":\"Al Green - The Best Of Al Green [ Full HD ]\",\"thumbnail_height\":360,\"provider_name\":\"YouTube\",\"author_url\":\"http://www.youtube.com/channel/UCFwTN_Is9kBj_UWCHTvt0cw\",\"thumbnail_width\":480,\"height\":236,\"provider_url\":\"http://www.youtube.com/\",\"type\":\"video\",\"width\":420,\"version\":\"1.0\",\"author_name\":\"PTUNITE\",\"thumbnail_url\":\"https://i.ytimg.com/vi/mBA5tV05xAg/hqdefault.jpg\",\"trusted_endpoint_url\":\"http://www.youtube.com/oembed?scheme=https\"}},\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"#musique REPRISE ...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":0,\"likes_count\":1,\"reshares_count\":0,\"comments\":[]}},{\"id\":308281,\"guid\":\"af8767807d380132a5bb221315d95ef2\",\"text\":\"Bonsoir \\u00e0 tous,\\r\\n\\r\\nJe serai sur sc\\u00e8ne ce mois-ci :\\r\\n\\r\\n- **vendredi 16 janvier** \\u00e0 19h et **samedi 17 janvier** \\u00e0 17h \\u00e0 l\\u2019auditorium du CRR de Paris (14 rue de Madrid 75008, m\\u00e9tro Europe) avec l\\u2019orchestre symphonique du CRR de Paris ; au programme : 4e symphonie de Schubert, concerto pour clarinette de Mozart ; entr\\u00e9e libre ;\\r\\n\\r\\n- **dimanche 18 janvier** \\u00e0 16h \\u00e0 l\\u2019\\u00e9glise Saint-Roch \\u00e0 Paris avec les Solistes des Possibles et l\\u2019ensemble vocal Res Lyrica dans un programme Gounod ; libre participation aux frais : http://pecheurs-de-perles.fr/article63.html ;\\r\\n\\r\\n- **dimanche 25 janvier** \\u00e0 16h \\u00e0 l\\u2019\\u00e9glise Saint-Germain-l\\u2019Auxerrois \\u00e0 Paris avec l\\u2019Ensemble des Possibles et le ch\\u0153ur Jubilo Canto dans un programme Vivaldi, Biber (Heinrich, pas *l\\u2019autre* !) et Bach ; entr\\u00e9e libre : http://pecheurs-de-perles.fr/article71.html\\r\\n\\r\\nVenez nombreux !\\r\\n\\r\\n#musique #classique #concert #paris #orchestre #baroque #romantique #schubert #mozart #bach #vivaldi #gounod #biber\",\"public\":true,\"created_at\":\"2015-01-13T09:58:45Z\",\"interacted_at\":\"2015-01-13T13:25:33Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":9,\"guid\":\"332f1a4b27a39b27\",\"name\":\"Nicolas Hussein\",\"diaspora_id\":\"nicoalto@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_1f29f2c5941d0e96fb90.JPG\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_1f29f2c5941d0e96fb90.JPG\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_1f29f2c5941d0e96fb90.JPG\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Bonsoir \\u00e0 tous, J...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":0,\"likes_count\":1,\"reshares_count\":0,\"comments\":[]}},{\"id\":307858,\"guid\":\"75b715f07d330132ce2e2a0000053625\",\"text\":\"Chronique disque: HEADROT [Usa] Gulping the remains CD (Pathos Prods) 2010 http://www.church-ov-ze-dead.com/spip.php?article2909 #french #deadchurch #chronique #musique #deathmetal\",\"public\":true,\"created_at\":\"2015-01-13T09:21:20Z\",\"interacted_at\":\"2015-01-13T09:21:21Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":17177,\"guid\":\"7c89fec05903013249162a0000053625\",\"name\":\"dementedged@framasphere.org\",\"diaspora_id\":\"dementedged@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_bb66739429938e671b3c.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_bb66739429938e671b3c.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_bb66739429938e671b3c.jpg\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Chronique disque:...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":0,\"likes_count\":0,\"reshares_count\":0,\"comments\":[]}},{\"id\":307718,\"guid\":\"ad2cebd07d280132a5b9221315d95ef2\",\"text\":\"#music #musique #cover #reprise Ce matin, un peu d'amour dans ce monde de brutes . (Pour mon tr\\u00e8s cher @{Augier ; augierle42e@diaspora-fr.org} qui semble devancer le printemps .)\\r\\n## [Ukulele Orchestra of Great Britain - Je t'aime, moi non plus](http://grooveshark.com/s/Je+T+aime+Moi+Non+Plus/2UumpI?src=5)\",\"public\":true,\"created_at\":\"2015-01-13T08:04:09Z\",\"interacted_at\":\"2015-01-13T12:11:18Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":44,\"guid\":\"a727118c4188225a\",\"name\":\"FF Shukke\",\"diaspora_id\":\"ff@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_eca91a19490d72d84b58.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_eca91a19490d72d84b58.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_eca91a19490d72d84b58.jpg\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[{\"id\":99,\"guid\":\"6f48214497d8d2c1\",\"name\":\"Augier\",\"diaspora_id\":\"augierle42e@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_1c81c45fbe5d017bc635.jpeg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_1c81c45fbe5d017bc635.jpeg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_1c81c45fbe5d017bc635.jpeg\"}}],\"photos\":[{\"id\":75936,\"guid\":\"a1bc85007d280132a5bb221315d95ef2\",\"created_at\":\"2015-01-13T08:03:50Z\",\"author\":{\"id\":44,\"guid\":\"a727118c4188225a\",\"name\":\"FF Shukke\",\"diaspora_id\":\"ff@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_eca91a19490d72d84b58.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_eca91a19490d72d84b58.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_eca91a19490d72d84b58.jpg\"}},\"sizes\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_fe30b29e35e0bad00f5d.png\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_fe30b29e35e0bad00f5d.png\",\"large\":\"https://diaspora-fr.org/uploads/images/scaled_full_fe30b29e35e0bad00f5d.png\"},\"dimensions\":{\"height\":547,\"width\":626}}],\"root\":null,\"title\":\"#music #musique #...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":7,\"likes_count\":4,\"reshares_count\":1,\"comments\":[{\"id\":249665,\"guid\":\"8ac7ff007d2a0132a5b7221315d95ef2\",\"text\":\"\\u003E non, pas pour le moment\\n\\nJe garde espoir alors ! xD\",\"author\":{\"id\":99,\"guid\":\"6f48214497d8d2c1\",\"name\":\"Augier\",\"diaspora_id\":\"augierle42e@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_1c81c45fbe5d017bc635.jpeg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_1c81c45fbe5d017bc635.jpeg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_1c81c45fbe5d017bc635.jpeg\"}},\"created_at\":\"2015-01-13T11:16:18Z\"},{\"id\":249716,\"guid\":\"1cecb7007d2c0132a5b5221315d95ef2\",\"text\":\"\\u003E Pas dit que ce soit toi, cependant.\\n\\nOn vit un peu loin l'un de l'autre ! :D\",\"author\":{\"id\":99,\"guid\":\"6f48214497d8d2c1\",\"name\":\"Augier\",\"diaspora_id\":\"augierle42e@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_1c81c45fbe5d017bc635.jpeg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_1c81c45fbe5d017bc635.jpeg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_1c81c45fbe5d017bc635.jpeg\"}},\"created_at\":\"2015-01-13T11:28:14Z\"},{\"id\":249731,\"guid\":\"309111f07d2d0132a5b7221315d95ef2\",\"text\":\"Le temps de la dissimulation est \\u00e0 pr\\u00e9sent r\\u00e9volu pour moi . Je n'ai plus rien \\u00e0 cacher .\",\"author\":{\"id\":44,\"guid\":\"a727118c4188225a\",\"name\":\"FF Shukke\",\"diaspora_id\":\"ff@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_eca91a19490d72d84b58.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_eca91a19490d72d84b58.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_eca91a19490d72d84b58.jpg\"}},\"created_at\":\"2015-01-13T11:35:30Z\"}]}},{\"id\":307667,\"guid\":\"54644f407d250132ce2c2a0000053625\",\"text\":\"Bonjour tout le monde !\\r\\nJe suis #nouveauici et j'esp\\u00e8re pouvoir bien m'int\\u00e9grer \\u00e0 ce r\\u00e9seau social.\\r\\nJe commence \\u00e0 avoir un petit peu de mal avec Facebook mais malheureusement il m'est indispensable pour les personnes que je c\\u00f4toie...\",\"public\":true,\"created_at\":\"2015-01-13T07:40:11Z\",\"interacted_at\":\"2015-01-13T21:39:03Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":23597,\"guid\":\"9c0114507af00132ce242a0000053625\",\"name\":\"Christophe LEFEVRE\",\"diaspora_id\":\"kazhan@framasphere.org\",\"avatar\":{\"small\":\"http://graph.facebook.com/10200124573113221/picture\",\"medium\":\"http://graph.facebook.com/10200124573113221/picture\",\"large\":\"http://graph.facebook.com/10200124573113221/picture\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Bonjour tout le m...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":11,\"likes_count\":4,\"reshares_count\":0,\"comments\":[{\"id\":250313,\"guid\":\"a9d23b607d620132a5bf221315d95ef2\",\"text\":\"ha oui s'est bien l\\u00e2ch\\u00e9 le JCF aujourd'hui dans le mode accueil...lol\",\"author\":{\"id\":1664,\"guid\":\"ab61a310e7d30131dfd55ff84df34882\",\"name\":\"Phipe\",\"diaspora_id\":\"phipe@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_9c8b96339d0bee3a4194.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_9c8b96339d0bee3a4194.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_9c8b96339d0bee3a4194.jpg\"}},\"created_at\":\"2015-01-13T14:57:28Z\"},{\"id\":251141,\"guid\":\"fc4ad2f07d8e0132ce362a0000053625\",\"text\":\"Bienvenue, arf tu sais personnellement j'ai le m\\u00eame soucis avec facebook, alors je garde contact juste en utilisant pidgin. Je peux leur parler, et j'ai plus besoin de passer sur facebook pour cela.\",\"author\":{\"id\":21167,\"guid\":\"828034b06bd1013291352a0000053625\",\"name\":\"Raziel\",\"diaspora_id\":\"kingsparda@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_2bd81f9b331ba6887729.png\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_2bd81f9b331ba6887729.png\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_2bd81f9b331ba6887729.png\"}},\"created_at\":\"2015-01-13T20:16:30Z\"},{\"id\":251354,\"guid\":\"8417cd007d9a0132ce362a0000053625\",\"text\":\"Merci pour ces messages, il y a un tr\\u00e8s bon accueil c'est agr\\u00e9able !\",\"author\":{\"id\":23597,\"guid\":\"9c0114507af00132ce242a0000053625\",\"name\":\"Christophe LEFEVRE\",\"diaspora_id\":\"kazhan@framasphere.org\",\"avatar\":{\"small\":\"http://graph.facebook.com/10200124573113221/picture\",\"medium\":\"http://graph.facebook.com/10200124573113221/picture\",\"large\":\"http://graph.facebook.com/10200124573113221/picture\"}},\"created_at\":\"2015-01-13T21:39:03Z\"}]}},{\"id\":307374,\"guid\":\"30c716807cf60132ce302a0000053625\",\"text\":\"https://www.youtube.com/watch?v=Yc__YiipwZg\\r\\n\\r\\nLe th\\u00e8me principale du film d'horreur Phenomena de Dario Argento (1985), une B.O. exceptionnelle. Le film \\u00e0 d\\u2019ailleurs inspirer grandement le jeu vid\\u00e9o survival horreur Clock Tower. \\r\\n\\r\\n#cinema, #horreur, #musique\",\"public\":true,\"created_at\":\"2015-01-13T02:02:45Z\",\"interacted_at\":\"2015-01-13T08:12:35Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":21167,\"guid\":\"828034b06bd1013291352a0000053625\",\"name\":\"Raziel\",\"diaspora_id\":\"kingsparda@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_2bd81f9b331ba6887729.png\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_2bd81f9b331ba6887729.png\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_2bd81f9b331ba6887729.png\"}},\"o_embed_cache\":{\"data\":{\"author_url\":\"http://www.youtube.com/user/themusikmaster\",\"title\":\"phenomena theme song\",\"type\":\"video\",\"provider_url\":\"http://www.youtube.com/\",\"height\":315,\"version\":\"1.0\",\"thumbnail_height\":360,\"thumbnail_width\":480,\"thumbnail_url\":\"https://i.ytimg.com/vi/Yc__YiipwZg/hqdefault.jpg\",\"provider_name\":\"YouTube\",\"width\":420,\"author_name\":\"Raffaelo Carmona Solano Iszera\",\"html\":\"\\u003Ciframe width=\\\"420\\\" height=\\\"315\\\" src=\\\"https://www.youtube.com/embed/Yc__YiipwZg?feature=oembed\\\" frameborder=\\\"0\\\" allowfullscreen\\u003E\\u003C/iframe\\u003E\",\"trusted_endpoint_url\":\"http://www.youtube.com/oembed?scheme=https\"}},\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"https://www.youtu...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":0,\"likes_count\":1,\"reshares_count\":0,\"comments\":[]}},{\"id\":307366,\"guid\":\"1a2d93307cf40132ce2c2a0000053625\",\"text\":\"Bonjour a tous je suis #nouveauici, j'ai d\\u00e9couvert diaspora sur la documentation de #ubuntu.fr, j'en avais marre de facebook et je voulais un r\\u00e9seau social bien confidentiel. J'esp\\u00e8re que je vais bien m'int\\u00e9grer permis vous et d\\u00e9couvrir toute les fonctionnalit\\u00e9 de ce super r\\u00e9seau social.\",\"public\":true,\"created_at\":\"2015-01-13T01:47:48Z\",\"interacted_at\":\"2015-01-13T19:27:38Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":21167,\"guid\":\"828034b06bd1013291352a0000053625\",\"name\":\"Raziel\",\"diaspora_id\":\"kingsparda@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_2bd81f9b331ba6887729.png\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_2bd81f9b331ba6887729.png\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_2bd81f9b331ba6887729.png\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Bonjour a tous je...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":9,\"likes_count\":6,\"reshares_count\":1,\"comments\":[{\"id\":249557,\"guid\":\"9691b9507d3a013280145404a6b20780\",\"text\":\"Bienvenue parmi nous  :-)\",\"author\":{\"id\":681,\"guid\":\"06b87074090ed3e5\",\"name\":\"Cryptie**\",\"diaspora_id\":\"cryptogirl@diasp.eu\",\"avatar\":{\"small\":\"https://diasp.eu/uploads/images/thumb_small_7429af776943411d9f67.png\",\"medium\":\"https://diasp.eu/uploads/images/thumb_medium_7429af776943411d9f67.png\",\"large\":\"https://diasp.eu/uploads/images/thumb_large_7429af776943411d9f67.png\"}},\"created_at\":\"2015-01-13T10:12:22Z\"},{\"id\":250324,\"guid\":\"04f4c4407d630132a5bf221315d95ef2\",\"text\":\"Hello, Kingsparda, willkommen!  \\nPour d\\u00e9couvrir le mieux c'est d'explorer et d ejouer avec les tags. Tu peux aussi passer par une [petite lecture instructive et synth\\u00e9tique du post de l'ami SpF pour aller plus loin avec diaspora*](/posts/e865188033670132f129061787dcf416)  \\n#BienvenueIci  \\n*oui je sais, je n'ai pas plac\\u00e9 mon tag pr\\u00e9f\\u00e9r\\u00e9 de #bi\\u00e8re  \\nha ba si...lol*\",\"author\":{\"id\":1664,\"guid\":\"ab61a310e7d30131dfd55ff84df34882\",\"name\":\"Phipe\",\"diaspora_id\":\"phipe@diaspora-fr.org\",\"avatar\":{\"small\":\"https://diaspora-fr.org/uploads/images/thumb_small_9c8b96339d0bee3a4194.jpg\",\"medium\":\"https://diaspora-fr.org/uploads/images/thumb_medium_9c8b96339d0bee3a4194.jpg\",\"large\":\"https://diaspora-fr.org/uploads/images/thumb_large_9c8b96339d0bee3a4194.jpg\"}},\"created_at\":\"2015-01-13T15:00:01Z\"},{\"id\":251015,\"guid\":\"2866b5907d880132ce362a0000053625\",\"text\":\"Merci tous le monde pour cet accueil chaleureux :) Je vais regarder de plus pr\\u00e8s les liens que vous m'avez envoyer.\",\"author\":{\"id\":21167,\"guid\":\"828034b06bd1013291352a0000053625\",\"name\":\"Raziel\",\"diaspora_id\":\"kingsparda@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_2bd81f9b331ba6887729.png\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_2bd81f9b331ba6887729.png\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_2bd81f9b331ba6887729.png\"}},\"created_at\":\"2015-01-13T19:27:38Z\"}]}},{\"id\":307134,\"guid\":\"62a8e0b07ce30132ce302a0000053625\",\"text\":\"Bonjour \\u00e0 tous ! Je suis #nouveauici\",\"public\":true,\"created_at\":\"2015-01-12T23:48:09Z\",\"interacted_at\":\"2015-01-13T00:28:04Z\",\"provider_display_name\":null,\"post_type\":\"StatusMessage\",\"image_url\":null,\"object_url\":null,\"favorite\":false,\"nsfw\":false,\"author\":{\"id\":23096,\"guid\":\"8171f150773c0132ce102a0000053625\",\"name\":\"Cannelle\",\"diaspora_id\":\"cannelle@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_04b0c393fda46e6318fa.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_04b0c393fda46e6318fa.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_04b0c393fda46e6318fa.jpg\"}},\"o_embed_cache\":null,\"open_graph_cache\":null,\"mentioned_people\":[],\"photos\":[],\"root\":null,\"title\":\"Bonjour \\u00e0 tous ! ...\",\"address\":null,\"poll\":null,\"already_participated_in_poll\":null,\"interactions\":{\"likes\":[],\"reshares\":[],\"comments_count\":1,\"likes_count\":0,\"reshares_count\":0,\"comments\":[{\"id\":248941,\"guid\":\"f64ef4007ce80132ce2c2a0000053625\",\"text\":\"Salut, Cannelle... bienvenue \\u00e0 bord... avant de d\\u00e9coller, je te conseille d'\\u00e9teindre ta cigarette et d'ajouter quelques #tags \\u00e0 ton profil, histoire de nous faire d\\u00e9couvrir tes centres d'int\\u00e9r\\u00eats...\",\"author\":{\"id\":5797,\"guid\":\"f576a0f02b760132b0df2a0000053625\",\"name\":\"\\u2591Z\\u2591\\u00d8\\u2591\\u00d8\\u2591M\\u2591O\\u2591R\\u2591P\\u2591H\\u2591\",\"diaspora_id\":\"zoomorph@framasphere.org\",\"avatar\":{\"small\":\"https://framasphere.org/uploads/images/thumb_small_b8ace15bf4552c943453.jpg\",\"medium\":\"https://framasphere.org/uploads/images/thumb_medium_b8ace15bf4552c943453.jpg\",\"large\":\"https://framasphere.org/uploads/images/thumb_large_b8ace15bf4552c943453.jpg\"}},\"created_at\":\"2015-01-13T00:28:04Z\"}]}}]";


    public static void uploadImage(String localPath, String nameFile, final ProgressBar uploadProgressBar, FutureCallback<Response<String>> callback){

        boolean isTest = false;
        LOG.d(DataControler.class, ".uploadImage : Entrée");
        try{
            //&qqfile=ic_launcher-web.png

            try {
                LOG.d(".uploadImage : On efface les cookies");
                CookieControler cookieControler = CookieControler.getInstance();
                cookieControler.clearCookies();
                URI uri = URI.create(POD_URL);
                if (!COOKIE_REMEMBER.isEmpty()||isTest) {
                    LOG.d(".uploadImage : On ajoute le cookie remember_user_token=" + COOKIE_REMEMBER);
                     if (isTest){
                         //TEST ADD COOKIE EN DUR POUR TEST
//                         BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26; path=/; expires=Fri, 06-Mar-2015 10:19:56 GMT; HttpOnly; secure
                         cookieControler.storeCookie(uri, "remember_user_token", "BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26; path=/; expires=Fri, 06-Mar-2015 10:19:56 GMT; HttpOnly; secure");
//                         cookieControler.storeCookie(uri, "remember_user_token", "BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26");
                     }else{
                         cookieControler.storeCookie(uri, "remember_user_token", COOKIE_REMEMBER);
                     }
                }
//                if (!COOKIE_SESSION_LOGIN.isEmpty()||isTest) {
//                    LOG.d(".uploadImage : On ajoute le cookie _diaspora_session=" + COOKIE_SESSION_LOGIN);
//                    if (isTest){
//                        //TEST ADD COOKIE EN DUR POUR TEST
//                        cookieControler.storeCookie(uri, "_diaspora_session", "BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTViNjdiOGRmZTU3ZTBiMzMzMDlkNDJkYmUzZGQwYzA5BjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJGFGS3lmbXM3NDkyNzVSSmovY2cxVi4GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxT2NQejBsdkYwSG1PaUh2TkRKWW1aYXFObVpxU0ZhdkRtTUV1NDZuZFY2Yz0GOwBG--407bd19b571f9e60899ee0d933c8fcc9a5ee5553");
//                    }else{
//                        cookieControler.storeCookie(uri, "_diaspora_session", COOKIE_SESSION_LOGIN);
//                    }
//                }

                if (isTest) {
                    //TEST ADD COOKIE EN DUR POUR TEST
                    cookieControler.storeCookie(uri, "_pk_id.26.270d", "748f3140b90c4085.1421965980.15.1424126340.1424035050.");
                    cookieControler.storeCookie(uri, "_pk_ses.26.270d", "*");
                }
            }catch (IOException ioex){
                LOG.d(".uploadImage : Impossible de positioner le cookie _diaspora_session ou remember_user_token");
            }
            LOG.d("Test d'accès au fichier que nous souhaitons partager : "+localPath);
            File fichierToShare = new File(localPath);
            if (fichierToShare==null){
                LOG.e("Mal parti le fichier n'est pas accessible");
                LOG.d(DataControler.class, ".uploadImage : Sortie");
                return;
            }
            if (!fichierToShare.exists()){
                LOG.e("Mal parti v2 le fichier n'existe pas");
                LOG.d(DataControler.class, ".uploadImage : Sortie");
                return;
            }
            if (!fichierToShare.isFile()){
                LOG.e("Mal parti v3 le chemin ne défini pas un fichier");
                LOG.d(DataControler.class, ".uploadImage : Sortie");
                return;
            }
            LOG.d(DataControler.class, ".uploadImage : Construction de la requête d'appel POST à "+POST_IMAGE + "&qqfile=" + nameFile+ " (file="+nameFile+")");
            String token = TOKEN;

            if (isTest){
                token = "OcPz0lvF0HmOiHvNDJYmZaqNmZqSFavDmMEu46ndV6c=";
            }
            Ion.with(DiasporaConfig.APPLICATION_CONTEXT)
                    .load("POST", POST_IMAGE + "&qqfile=" + nameFile)
                    .progress(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            float percent = (float) downloaded / total;
                            uploadProgressBar.setProgress((int) percent);

                        }
                    })
                    .noCache()
                    .followRedirect(false)
                    .setHeader("x-requested-with", "XMLHttpRequest")
                    .setHeader("content-type","application/octet-stream")
                    .setHeader("Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .setHeader("accept-encoding", "gzip,deflate")
                    .setHeader("accept-language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4")
                    .setHeader("x-csrf-token", TOKEN)
                    .setHeader("x-file-name", nameFile)
                    .setTimeout(60 * 60 * 1000)
//                    .setMultipartFile(nameFile, "image/png", fichierToShare)
                    .setMultipartFile("qqfile", "image/png", fichierToShare)
                    .asString()
                    .withResponse()
                    .setCallback(callback);
            //x-csrf-token:nEkhdXfaVQIjPGGIHZZKtiOH5qiahOBlN/uIgWZjlp4=
//            x-file-name:ic_launcher-web.png
        }catch(Throwable thr){
            LOG.e(DataControler.class, ".uploadImage Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(DataControler.class, ".uploadImage : Sortie");
            throw thr;
        }
        LOG.d(DataControler.class, ".uploadImage : Sortie");
    }


    public static void uploadImage(String localPath, String nameFile, String message, final ProgressBar uploadProgressBar, FutureCallback<Response<UploadResult>> callback){
/*


Remote Address:78.47.14.84:443
Request URL:https://framasphere.org/photos?photo%5Bpending%5D=true&qqfile=LogoFan3x3_v2014-001_cmjn_16b.png
Request Method:POST
Status Code:200 OK


Request Headers
:host:framasphere.org
:method:POST
:path:/photos?photo%5Bpending%5D=true&qqfile=LogoFan3x3_v2014-001_cmjn_16b.png
:scheme:https
:version:HTTP/1.1
accept:application/json
accept-encoding:gzip,deflate
accept-language:fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4
content-length:177783
content-type:application/octet-stream
cookie:remember_user_token=BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26; _diaspora_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTViNjdiOGRmZTU3ZTBiMzMzMDlkNDJkYmUzZGQwYzA5BjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJGFGS3lmbXM3NDkyNzVSSmovY2cxVi4GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxT2NQejBsdkYwSG1PaUh2TkRKWW1aYXFObVpxU0ZhdkRtTUV1NDZuZFY2Yz0GOwBG--407bd19b571f9e60899ee0d933c8fcc9a5ee5553; _pk_id.26.270d=748f3140b90c4085.1421965980.15.1424126340.1424035050.; _pk_ses.26.270d=*
origin:https://framasphere.org
referer:https://framasphere.org/stream
user-agent:Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2121.0 Safari/537.36
x-csrf-token:OcPz0lvF0HmOiHvNDJYmZaqNmZqSFavDmMEu46ndV6c=
x-file-name:LogoFan3x3_v2014-001_cmjn_16b.png
x-requested-with:XMLHttpRequest

Query String Parameter
photo[pending]:true
qqfile:LogoFan3x3_v2014-001_cmjn_16b.png

Response Headers
cache-control:max-age=0, private, must-revalidate
content-encoding:gzip
content-type:application/json; charset=utf-8
date:Mon, 16 Feb 2015 22:40:02 GMT
etag:"69965147d0f77032df1da9acf1208553"
server:nginx/1.6.2
status:200 OK
status:200 OK
strict-transport-security:max-age=31536000
vary:Accept-Encoding
version:HTTP/1.1
x-diaspora-version:0.4.1.2-pd705150a
x-frame-options:sameorigin
x-git-revision:d705150a3fe0141de6045bdbe63e71a3e665e40a
x-git-update:2014-11-02 19:39:54 +0100
x-rack-cache:invalidate, pass
x-request-id:c7a16df9cfa68ed69fe01c0a9c83743b
x-runtime:0.446032
x-ua-compatible:IE=Edge,chrome=1

 */

        String methodName = ".uploadImage : ";



        LOG.d(methodName + "Entrée");
        try{
            //&qqfile=ic_launcher-web.png

            boolean isTest = false;
            try {
                LOG.d(".uploadImage : On efface les cookies");
                CookieControler cookieControler = CookieControler.getInstance();
                cookieControler.clearCookies();
                URI uri = URI.create(POD_URL);

                if (!COOKIE_REMEMBER.isEmpty()) {
                    LOG.d(methodName+ "On ajoute le cookie remember_user_token=" + COOKIE_REMEMBER);
                    cookieControler.storeCookie(uri, "remember_user_token", COOKIE_REMEMBER);
                }

//                if (!COOKIE_REMEMBER.isEmpty()||isTest) {
//                    String remember = COOKIE_REMEMBER;//.substring(0,COOKIE_REMEMBER.indexOf(";"));
//                    LOG.d(".uploadImage : On ajoute le cookie remember_user_token=" + remember);
//                    if (isTest) {
//                        //TEST ADD COOKIE EN DUR POUR TEST
//                        cookieControler.storeCookie(uri, "remember_user_token", "BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26");
//                    }else{
//                        cookieControler.storeCookie(uri, "remember_user_token", remember);
//                    }
//
//                }
////                if (!COOKIE_SESSION_LOGIN.isEmpty()||isTest) {
////                    String session = COOKIE_SESSION_LOGIN.substring(0,COOKIE_SESSION_LOGIN.indexOf(";"));
////                    LOG.d(".uploadImage : On ajoute le cookie _diaspora_session=" + session);
////                    if (isTest) {
////                        //TEST ADD COOKIE EN DUR POUR TEST
////                        cookieControler.storeCookie(uri, "_diaspora_session", "BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTViNjdiOGRmZTU3ZTBiMzMzMDlkNDJkYmUzZGQwYzA5BjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJGFGS3lmbXM3NDkyNzVSSmovY2cxVi4GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxT2NQejBsdkYwSG1PaUh2TkRKWW1aYXFObVpxU0ZhdkRtTUV1NDZuZFY2Yz0GOwBG--407bd19b571f9e60899ee0d933c8fcc9a5ee5553");
////                    }else {
////                        cookieControler.storeCookie(uri, "_diaspora_session", session);
////                    }
////                }
//
//
//                if (isTest) {
//                //TEST ADD COOKIE EN DUR POUR TEST
//                cookieControler.storeCookie(uri, "_pk_id.26.270d", "748f3140b90c4085.1421965980.15.1424126340.1424035050.");
//                cookieControler.storeCookie(uri, "_pk_ses.26.270d", "*");
//                }
            }catch (IOException ioex){
                LOG.d(methodName + "Impossible de positioner le cookie _diaspora_session ou remember_user_token");
            }
            LOG.d(methodName + "Test d'accès au fichier que nous souhaitons partager : "+localPath);
            File fichierToShare = new File(localPath);
            if (fichierToShare==null){
                LOG.e(methodName + "Mal parti le fichier n'est pas accessible");
                LOG.d(methodName + "Sortie");
                return;
            }
            if (!fichierToShare.exists()){
                LOG.e(methodName + "Mal parti v2 le fichier n'existe pas");
                LOG.d(methodName + "Sortie");
                return;
            }
            if (!fichierToShare.isFile()){
                LOG.e(methodName + "Mal parti v3 le chemin ne défini pas un fichier");
                LOG.d(methodName + "Sortie");
                return;
            }
            LOG.d(methodName + "Construction de la requête d'appel POST à "+POST_IMAGE + "&qqfile=" + nameFile+ " (file="+nameFile+")");
            String token = TOKEN;

            if (isTest){
                token = "OcPz0lvF0HmOiHvNDJYmZaqNmZqSFavDmMEu46ndV6c=";
            }
            Ion.with(DiasporaConfig.APPLICATION_CONTEXT)
                    .load("POST", POST_IMAGE + "&qqfile=" + nameFile)
                    .progress(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            float percent = (float) downloaded / total;
                            uploadProgressBar.setProgress((int) percent);

                        }
                    })
//                    .uploadProgressBar(uploadProgressBar)
                    /*
                    :host:framasphere.org
:method:POST
:path:/photos?photo%5Bpending%5D=true&qqfile=2015-01-21_001131.png
:scheme:https
:version:HTTP/1.1
accept:application/json
accept-encoding:gzip,deflate
accept-language:fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4
content-length:12030
content-type:application/octet-stream
cookie:_pk_id.26.270d=748f3140b90c4085.1421965980.4.1422743713.1422573820.; remember_user_token=BAhbB1sGaQISCUkiIiQyYSQxMCRTcmhiZC9yS2JBczlqWUk5cVNZVU9PBjoGRVQ%3D--a111fa31a16b0451130d7598978cda8257466368; _diaspora_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTM5NjFlZWY5OTI3ODUwYWQ0YzcwNmM5NDEyMzI5ODg0BjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJFNyaGJkL3JLYkFzOWpZSTlxU1lVT08GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxNzF6Ujd4VUoxT2djTXRBcXFQczBWWis5Szk0QUU0SWFlakx6d0drZmJKdz0GOwBG--4457c24f363ef1afcc886b9ca04466e58871fc53
origin:https://framasphere.org
referer:https://framasphere.org/bookmarklet?title=%20%E2%80%A2%20chrome-extension%3A%2F%2Fpgblolfhnddeobmfgjhckddncbbfnlim%2Foptions.html%3Fpodurl%3Dhttps%253A%252F%252Fframasphere.org%23%20%3Cbr%3E%3Csub%3E&url=[via%20Diaspora*%20Publisher](http%3A%2F%2Fgoo.gl%2FtmeFB)%20-%3C/sub%3E&v=1&noui=1&jump=doclose
user-agent:Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2121.0 Safari/537.36
x-csrf-token:71zR7xUJ1OgcMtAqqPs0VZ+9K94AE4IaejLzwGkfbJw=
x-file-name:2015-01-21_001131.png
x-requested-with:XMLHttpRequest
                    */

                    .noCache()
                    .setHeader("x-requested-with", "XMLHttpRequest")
                    .setHeader("User-Agent", USER_AGENT)
//                    .setHeader("content-type", "application/octet-stream")
                    .setHeader("Accept", "application/json")
                    .setHeader("Cache-Control", "no-cache")
//                    .setHeader("x-file-name", nameFile)
//                    .setHeader("x-csrf-token", token)
//                    .setHeader("authenticity_token", token)
//                    .noCache()
//                    .followRedirect(false)
//                    .setHeader("x-requested-with", "XMLHttpRequest")
//                    .setHeader("content-type","application/octet-stream")
//                    .setHeader("Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//                    .setHeader("accept-encoding", "gzip,deflate")
//                    .setHeader("accept-language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4")
//                    .setHeader("x-csrf-token", TOKEN)
//                    .setHeader("x-file-name", nameFile)
//                    .setTimeout(60 * 60 * 1000)
////                    .setMultipartFile(nameFile, "image/png", fichierToShare)
                    .setMultipartParameter("photo[pending]","true")
                    .setMultipartParameter("set_profile_image","")
                    .setMultipartParameter("qqFile",nameFile)
                    .setMultipartFile("file", "image/png", fichierToShare)
                    .as(new TypeToken<UploadResult>() {
                    })
                    .withResponse()
                    .setCallback(callback);
            //x-csrf-token:nEkhdXfaVQIjPGGIHZZKtiOH5qiahOBlN/uIgWZjlp4=
//            x-file-name:ic_launcher-web.png
        }catch(Throwable thr){
            LOG.e(methodName + "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(methodName + "Sortie");
            throw thr;
        }
        LOG.d(methodName + "Sortie");
    }
//
//    public static void getStream(Context DiasporaConfig.APPLICATION_CONTEXT, FutureCallback<List<Post>> callback){
//        LOG.d(DataControler.class, ".getStream : Entrée");
//        try{
//            try {
//                LOG.d(ProfilControler.class, ".getStream : On efface les cookies");
//                CookieControler cookieControler = CookieControler.getInstance(DiasporaConfig.APPLICATION_CONTEXT);
//                cookieControler.clearCookies();
//                URI uri = URI.create(POD_URL);
//                if (!COOKIE_REMEMBER.isEmpty()) {
//                    LOG.d(DataControler.class, ".getStream : On ajoute le cookie remember_user_token=" + COOKIE_SESSION_TOKEN);
//                    cookieControler.storeCookie(uri, "remember_user_token", COOKIE_REMEMBER);
//                }else {
//                    LOG.d(DataControler.class, ".getStream : On ajoute le cookie _diaspora_session=" + COOKIE_SESSION_LOGIN);
//                    cookieControler.storeCookie(uri, "_diaspora_session", COOKIE_SESSION_LOGIN);
//                }
//            }catch (IOException ioex){
//                LOG.d(DataControler.class, ".getStream : Impossible de positioner le cookie _diaspora_session ou remember_user_token");
//            }
//            LOG.d(DataControler.class, ".getStream : Construction de la requête d'appel GET à "+STREAM_URL+ " (x-csrf-token="+TOKEN+")");
//            Ion.with(DiasporaConfig.APPLICATION_CONTEXT)
//                    .load("GET", STREAM_URL)
//                    .noCache()
//                    .setHeader("x-requested-with", "XMLHttpRequest")
//                    .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
//                    .setHeader("x-csrf-token", TOKEN)
////                    .setHeader("Set-Cookie", COOKIE_SESSION)
////                    .setHeader("Cookie", COOKIE_SESSION)
//                    .as(new TypeToken<List<Post>>() {
//                    })
//                    .setCallback(callback);
//        }catch(Throwable thr){
//            LOG.e(DataControler.class, ".getStream Erreur : " + thr.toString());
//            ACRA.getErrorReporter().handleException(thr);
//            LOG.d(DataControler.class, ".getStream : Sortie");
//            throw thr;
//        }
//        LOG.d(DataControler.class, ".getStream : Sortie");
//    }
}
