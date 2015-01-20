package fr.android.scaron.diaspdroid.controler;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

import fr.android.scaron.diaspdroid.R;

/**
 * Created by Maison on 11/01/2015.
 */
public class ProfilControler {

    public static Logger log = LoggerFactory.getLogger(ProfilControler.class);
    static String POD = "framasphere.org";
    static String POD_URL = "https://"+POD;
    static String LOGIN_URL = POD_URL+"/users/sign_in";
    static Context theContext;
    Fragment follower;

//    public static void getAvatarPath(Fragment follower){
//        Ion.with(follower).load("http://example.com/thing.json")
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        follower.notify();
//                        // do stuff with the result or error
//                    }
//                });
//    }

    public static void putImage(ImageView imageView, String imagePath){
        try{
//        Ion.with(imageView)
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.placeholder_image)
//                .load(imagePath);
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

    public static void login(Context context, String username, String password, FutureCallback<Response<String>> callback){
        log.debug(ProfilControler.class.getName()+".login : Entrée (username="+username+" , password="+password+")");
        Log.d(DataControler.class.getName(), ".login : Entrée (username="+username+" , password="+password+")");
        try{
            theContext = context;
            Ion ion=Ion.getDefault(context);//.Config.configure().setTag();
            ion.configure().createSSLContext("TLS");
            Ion.with(context)
                .load("POST", LOGIN_URL)
                .setBodyParameter("user[username]", username)
                .setBodyParameter("user[password]", password)
                .setBodyParameter("user[remember_me]", "1")
                .asString()
                .withResponse()
                .setCallback(callback);

            /*
            new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    HeadersResponse resultHeaders = result.getHeaders();
                    int code = resultHeaders.code();
                    String message = resultHeaders.message();
                    if (message!=null && !message.isEmpty()){
                        message = message.replaceAll("[\r\n]+", "");

                        log.info(ProfilControler.class.getName() + "Réponse du login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                    }
                    String reponseLoginOk = "OK"; //"<html><body>You are being<a href=\"https://framasphere.org/stream\">redirected</a>.</body></html>";
                    if (reponseLoginOk.equals(message)) {
                        log.info(ProfilControler.class.getName() + "Succès du login sur le pod " + POD);
                        Log.i(DataControler.class.getName(), "Succès du login sur le pod " + POD);
                        Toast.makeText(theContext, "Succès du login sur le pod " + POD, Toast.LENGTH_LONG);
                    } else if (code == 302) {
                        log.info(ProfilControler.class.getName() + "Succès du login sur le pod " + POD);
                        Log.i(DataControler.class.getName(), "Succès du login sur le pod " + POD);
                        Toast.makeText(theContext, "Succès du login sur le pod " + POD, Toast.LENGTH_LONG);
                    } else {
                        log.error(ProfilControler.class.getName() + "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult());
                        Log.e(DataControler.class.getName(), "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult());
                        Toast.makeText(theContext, "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult(), Toast.LENGTH_LONG);
                    }
                }
            }
             */
        }catch(Throwable thr){
            log.error("Erreur : " + thr.toString());
            Log.e(ProfilControler.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        log.debug(ProfilControler.class.getName()+".login : Sortie (username="+username+" , password="+password+")");
        Log.d(DataControler.class.getName(), ".login : Sortie (username="+username+" , password="+password+")");
    }
}
