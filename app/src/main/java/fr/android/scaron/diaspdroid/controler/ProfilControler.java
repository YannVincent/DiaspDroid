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

import fr.android.scaron.diaspdroid.R;

/**
 * Created by Maison on 11/01/2015.
 */
public class ProfilControler {

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
            Log.e(ProfilControler.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public static void login(Context context, String username, String password){
        try{
            theContext = context;
            Ion.with(context)
                .load("POST", LOGIN_URL)
                .setBodyParameter("user[username]", username)
                .setBodyParameter("user[password]", password)
                .setBodyParameter("user[remember_me]", "1")
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        HeadersResponse resultHeaders = result.getHeaders();
                        int code = resultHeaders.code();
                        String message = resultHeaders.message();
                        if (code==302){
                            Toast.makeText(theContext, "Succès du login sur le pod "+POD, Toast.LENGTH_LONG);
                        }else{
                            Toast.makeText(theContext, "Echec du login sur le pod "+POD + "(err:"+code+")\n" + result.getResult(), Toast.LENGTH_LONG);
                        }
                    }
                });
        }catch(Throwable thr){
            Log.e(ProfilControler.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
}
