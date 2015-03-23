package fr.android.scaron.diaspdroid.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.CookieControler;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;

/**
 * Created by Sébastien on 22/02/2015.
 */
public class DiasporaConfig {
    private static Logger LOGGEUR = LoggerFactory.getLogger(DiasporaConfig.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);

    public static String POD_USER;
    public static String POD_PASSWORD;
    public static String POD_URL;
    public static String POD_LIST_JSON;
    public static Pods POD_LIST;

    public static Application APPLICATION;
    public static Context APPLICATION_CONTEXT;
    public static TinyDB DB;

    public static boolean ParamsOK;
    private static final List<Activity> activities = new ArrayList<Activity>();

    public static void init(final Application pApplication, final Context pApplicationContext){
        // Chargement de la configuration sauvegardée
        APPLICATION = pApplication;
        APPLICATION_CONTEXT = pApplicationContext;
        DB = new TinyDB(pApplicationContext);
        final String user=DiasporaConfig.DB.getString("diaspora_user");
        final String password=DiasporaConfig.DB.getString("diaspora_password");
        final String url=DiasporaConfig.DB.getString("diaspora_pod");
        final String podlistJson=DiasporaConfig.DB.getString("diaspora_podlist");
        final Boolean configOK=DiasporaConfig.DB.getBoolean("configOK");

        if (user!=null && !user.isEmpty()){
            POD_USER = user;
        }
        if (password!=null && !password.isEmpty()){
            POD_PASSWORD = password;
        }
        if (url!=null && !url.isEmpty()){
            POD_URL = url;
        }
        if (podlistJson!=null && !podlistJson.isEmpty()){
            Type type = new TypeToken<Pods>(){}.getType();
            POD_LIST = new Gson().fromJson(podlistJson, type);
            POD_LIST_JSON = podlistJson;
        }
        if (configOK!=null){
            ParamsOK = configOK;
        }
        DiasporaControler.initParams();
        CookieControler.init();
    }

    public static void setPods(final Pods pPods){

        if (pPods!=null && pPods.getPodcount()>0 && pPods.getPods()!=null && !pPods.getPods().isEmpty()) {
            POD_LIST = pPods;
            POD_LIST_JSON = new Gson().toJson(pPods);
            DB.putString("diaspora_podlist", POD_LIST_JSON);
            LOG.d("diasporta_podlist = " + POD_LIST_JSON);
        }
    }

    public static void setPodAuthenticationValues(final String pPodUrl, final String pPodUser, final String pPodPassword){
        DB.putString("diaspora_user", pPodUser);
        DB.putString("diaspora_password", pPodPassword);
        DB.putString("diaspora_pod", pPodUrl);
        POD_URL = pPodUrl;
        POD_USER = pPodUser;
        POD_PASSWORD = pPodPassword;
        DiasporaControler.initParams();

        Context context = APPLICATION_CONTEXT;
        if (context==null){
            context = APPLICATION;
        }
//        if (context!=null){
//            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setIcon(R.drawable.ic_launcher);
//            alertDialog.setTitle("Infos de connexion");
//            alertDialog.setMessage("----RAPPEL ---\nurl:"+POD_URL+"\nuser:"+POD_USER+"\npass:"+POD_PASSWORD+"\n--------------");
//            alertDialog.show();
//        }
    }

    public static boolean isValid(){
        if (POD_URL==null || POD_USER==null || POD_PASSWORD==null
                ||POD_URL.isEmpty() || POD_USER.isEmpty() || POD_PASSWORD.isEmpty()){
            return false;
        }
        return true;
    }


    public static void addActivity(Activity pActivity) {
        finishOtherActivities(pActivity);
        activities.add(pActivity);
    }

    public static void finishActivities() {
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }

    public static void finishOtherActivities(Activity pActivity) {
        for (Activity activity : activities) {
            if (activity.getClass() != pActivity.getClass()){
                activity.finish();
            }
            activities.clear();
        }
    }
}
