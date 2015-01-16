package fr.android.scaron.diaspdroid.controler;

import android.app.Fragment;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import fr.android.scaron.diaspdroid.R;

/**
 * Created by Maison on 11/01/2015.
 */
public class ProfilControler {

    String DIASP_LOGIN_URL = "https://framasphere.org/users/sign_in";

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
//        Ion.with(imageView)
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.placeholder_image)
//                .load(imagePath);
        Ion.with(imageView)
                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.placeholder_image)
                .load(imagePath);
    }

    public static void login(String username, String password){

    }
}
