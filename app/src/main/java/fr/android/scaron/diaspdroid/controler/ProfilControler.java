package fr.android.scaron.diaspdroid.controler;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;

/**
 * Created by Maison on 11/01/2015.
 */
public class ProfilControler {

    public static Logger log = LoggerFactory.getLogger(ProfilControler.class);
    static String POD = "framasphere.org";
    static String POD_URL = "https://"+POD;
    static String LOGIN_URL = POD_URL+"/users/sign_in";
    static String TOKEN = "";
    static Context theContext;
    Fragment follower;

    public static void putImage(ImageView imageView, String imagePath){
        try{
        Ion.with(imageView)
            .placeholder(R.drawable.placeholder_image)
//          .error(R.drawable.placeholder_image)
            .load(imagePath);
        }catch(Throwable thr){
            log.error("Erreur : " + thr.toString());
            Log.e(ProfilControler.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public static void getToken(Context context, FutureCallback<Response<String>> callback){
        log.debug(ProfilControler.class.getName()+".get token : Entrée");
        Log.d(DataControler.class.getName(), ".get token : Entrée");
        try{
            theContext = context;
            Ion ion=Ion.getDefault(context);
            Ion.with(context)
                    .load("GET", LOGIN_URL)
                    .followRedirect(false)
                    .asString()
                    .withResponse()
                    .setCallback(callback);
        }catch(Throwable thr){
            log.error("Erreur : " + thr.toString());
            Log.e(ProfilControler.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        log.debug(ProfilControler.class.getName()+".get token : Sortie");
        Log.d(DataControler.class.getName(), ".get token : Sortie");
    }

    public static void login(Context context, String username, String password, String token, FutureCallback<Response<String>> callback){
        log.debug(ProfilControler.class.getName()+".login : Entrée (username="+username+" , password="+password+", token="+token+")");
        Log.d(DataControler.class.getName(), ".login : Entrée (username="+username+" , password="+password+", token="+token+")");

        try{
            TOKEN = token;
            theContext = context;
//            Ion ion=Ion.getDefault(context);//.Config.configure().setTag();
//            ion.configure().createSSLContext("TLS");
            Ion.with(context)
                .load("POST", LOGIN_URL)
//                .followRedirect(true)
//                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setBodyParameter("user[username]", username)
                .setBodyParameter("user[password]", password)
                .setBodyParameter("user[remember_me]", "1")
                .setBodyParameter("authenticity_token",token)//"vzXnKbMHg96jEsDO289+fic6Kl2wDGz76dFtUYK98dM=")
                .setBodyParameter("utf8","&#x2713;")
                .setBodyParameter("commit","Connexion")
                .asString()
                .withResponse()
                .setCallback(callback);
        }catch(Throwable thr){
            log.error("Erreur : " + thr.toString());
            Log.e(ProfilControler.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        log.debug(ProfilControler.class.getName()+".login : Sortie (username="+username+" , password="+password+", token="+token+")");
        Log.d(DataControler.class.getName(), ".login : Sortie (username="+username+" , password="+password+", token="+token+")");
    }





    public static void post(Context context, String post, String username, String password, FutureCallback<Response<String>> callback){
        log.debug(ProfilControler.class.getName()+".post : Entrée (post="+post+"username="+username+" , password="+password+", token="+TOKEN+")");
        Log.d(DataControler.class.getName(), ".post : Entrée (username="+username+" , password="+password+", token="+TOKEN+")");
        try{
            theContext = context;
//            Ion ion=Ion.getDefault(context);//.Config.configure().setTag();
//            ion.configure().createSSLContext("TLS");
            Ion.with(context)
                    .load("POST", LOGIN_URL)
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .setBodyParameter("user[username]", username)
                    .setBodyParameter("user[password]", password)
                    .setBodyParameter("status_message[fake_text]", post)
                    .setBodyParameter("status_message[text]", post)
                    .setBodyParameter("user[remember_me]", "1")
                    .setBodyParameter("authenticity_token",TOKEN)
                    .setBodyParameter("aspect_ids[]", "public")//public, all_aspects
                    .setBodyParameter("utf8","&#10003;")
                    .setBodyParameter("commit","Share")
                    .asString()
                    .withResponse()
                    .setCallback(callback);
        }catch(Throwable thr){
            log.error("Erreur : " + thr.toString());
            Log.e(ProfilControler.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        log.debug(ProfilControler.class.getName()+".post : Sortie (post="+post+"username="+username+" , password="+password+", token="+TOKEN+")");
        Log.d(DataControler.class.getName(), ".post : Sortie (post="+post+"username="+username+" , password="+password+", token="+TOKEN+")");
    }
}
