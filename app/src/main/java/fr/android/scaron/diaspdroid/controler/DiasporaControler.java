package fr.android.scaron.diaspdroid.controler;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.model.Pods;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.model.UploadResult;

/**
 * Created by Sébastien on 24/01/2015.
 */
public class DiasporaControler {

    private static Logger LOGGEUR = LoggerFactory.getLogger(DiasporaControler.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);

    static String PODLIST_URL = "http://podupti.me/api.php?key=4r45tg&format=json";

    static String POD = "framasphere.org";
    static String POD_URL = "https://"+POD;
    static String LOGIN_URL = POD_URL+"/users/sign_in";
    static String STREAM_URL_NORMAL = POD_URL+"/stream";
    static String STREAM_URL_DELTA = POD_URL+"/stream?max_time=1421018508&_=1421254891463";
    static String STREAM_URL = STREAM_URL_NORMAL;
    static String RESHARE_URL = POD_URL+"/reshares";
    static String POSTS_URL = POD_URL+"/posts";
//    static String POST_IMAGE = POD_URL + "/photos?photo%5Baspect_ids%5D=all";//"/photos?photo%5Bpending%5D=true";
//    static String POST_IMAGE = POD_URL + "/photos?photo[pending]=true";
    static String POST_IMAGE = POD_URL + "/photos?photo%5Bpending%5D=true&set_profile_image=&";
    static String TOKEN_VIDE = "";
    static String TOKEN_TEST = "4REWL0RLsEU5edVgVWuZL16XGAQkVuCYyzGirHvXjOI=";
    static String COOKIE_SESSION_LOGIN_VIDE = "";
    static String COOKIE_SESSION_LOGIN_TEST="BAh7B0kiD3Nlc3Npb25faWQGOgZFVEkiJWFkOGU5MjQ0NjRmOTM1MWQ1NTQ3NDZlZDc4MmUwYzA5BjsAVEkiEF9jc3JmX3Rva2VuBjsARkkiMTRSRVdMMFJMc0VVNWVkVmdWV3VaTDE2WEdBUWtWdUNZeXpHaXJIdlhqT0k9BjsARg%3D%3D--0f5956ca762a3f995f5d1dfa8c51db8d384f5e63";
    static String COOKIE_SESSION_STREAM_VIDE = "";
    static String COOKIE_SESSION_STREAM_TEST="BAh7CUkiD3Nlc3Npb25faWQGOgZFVEkiJWQzYTc1MDM4MGZkMGVkZDgxOTAzYjBiZWNmNjY3OGUwBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJFNyaGJkL3JLYkFzOWpZSTlxU1lVT08GOwBUSSIKZmxhc2gGOwBUbzolQWN0aW9uRGlzcGF0Y2g6OkZsYXNoOjpGbGFzaEhhc2gJOgpAdXNlZG86CFNldAY6CkBoYXNoewY6C25vdGljZVQ6DEBjbG9zZWRGOg1AZmxhc2hlc3sGOwpJIihWb3VzIMOqdGVzIMOgIHByw6lzZW50IGNvbm5lY3TDqS1lLgY7AFQ6CUBub3cwSSIQX2NzcmZfdG9rZW4GOwBGSSIxNE5oeVJKdWdqVll2ZDVwaXhRQ1NCZmZlbkl6bzRMZE1SZUx0UEVVVDJFdz0GOwBG--b1db6d257b315bfff3076ee8112f9a75a45d85ef";
//    static String COOKIE_SESSION_STREAM_TEST="BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTM5NjFlZWY5OTI3ODUwYWQ0YzcwNmM5NDEyMzI5ODg0BjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJFNyaGJkL3JLYkFzOWpZSTlxU1lVT08GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxNzF6Ujd4VUoxT2djTXRBcXFQczBWWis5Szk0QUU0SWFlakx6d0drZmJKdz0GOwBG--4457c24f363ef1afcc886b9ca04466e58871fc53";
    static String COOKIE_REMEMBER_TEST_VIDE = "";
    static String COOKIE_REMEMBER_TEST = "BAhbB1sGaQISCUkiIiQyYSQxMCRTcmhiZC9yS2JBczlqWUk5cVNZVU9PBjoGRVQ%3D--a111fa31a16b0451130d7598978cda8257466368; path=/; expires=Sun, 08-Feb-2015 21:52:35 GMT; HttpOnly; secure";
//    static String COOKIE_REMEMBER_TEST = "BAhbB1sGaQISCUkiIiQyYSQxMCRTcmhiZC9yS2JBczlqWUk5cVNZVU9PBjoGRVQ%3D--a111fa31a16b0451130d7598978cda8257466368";

    static String TOKEN = TOKEN_VIDE;
    static String COOKIE_SESSION_LOGIN = COOKIE_SESSION_LOGIN_VIDE;
    static String COOKIE_SESSION_STREAM = COOKIE_SESSION_STREAM_VIDE;
    static String COOKIE_REMEMBER = COOKIE_REMEMBER_TEST_VIDE;

    static String USER_AGENT = "DiaspDroid";

    static Context contextGlobal;

    public static void aimer(final Context context, final int postID, final FutureCallback<Response<String>> fluxCallback, boolean forceRelogin){
        boolean bForceLogin = forceRelogin;
        String methodName = ".aimer : ";
        if (forceRelogin){
            return;
        }
        contextGlobal = context;
        //Callback d'envoi du formulaire de login
        final FutureCallback<Response<String>> loginCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                String methodName = ".aimer loginCallback: ";
                boolean resultOK = onCompleteLogin(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(methodName + "Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Connexion");
                    alertDialog.setMessage("La connexion a Diaspora a échouée");
                    alertDialog.show();
                    return;
                }
                //On est loggué donc on demande le flux
                postLike(contextGlobal, postID, fluxCallback);
            }
        };
        //Callback de récupération du formulaire de login et d'accès au authenticity_token
        final FutureCallback<Response<String>> tokenCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                String methodName = ".aimer tokenCallback: ";
                boolean resultOK = onCompleteGetToken(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(methodName + "Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Accès");
                    alertDialog.setMessage("L'accès a Diaspora est impossible");
                    alertDialog.show();
                    return;
                }
                //On a le token donc on demande le login
                login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            }
        };
        //Cas ou nous n'avons pas les informations de connexion déjà en mémoire.
        if (bForceLogin || COOKIE_REMEMBER.isEmpty()){
            //Cas ou nous n'avons pas les informations pour se connecter.
            if (bForceLogin || (COOKIE_SESSION_LOGIN.isEmpty() || TOKEN.isEmpty())) {
                getToken(contextGlobal, tokenCallback);
                bForceLogin = false;//on ne tente qu'une fois.
                return;
            }
            //Cas ou nous avons les informations pour se connecter
            login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            return;
        }
        if (contextGlobal==null){
            LOG.e(methodName + "Le contexte est vide et empêche un traitement");
            return;
        }
        //Cas ou nous avons les informations de connexion
        postLike(contextGlobal, postID, fluxCallback);
    }


    public static void repartager(final Context context, final String rootGuid, final FutureCallback<Response<String>> fluxCallback, boolean forceRelogin){
        boolean bForceLogin = forceRelogin;
        String methodName = ".repartager : ";
        if (forceRelogin){
            return;
        }
        contextGlobal = context;
        //Callback d'envoi du formulaire de login
        final FutureCallback<Response<String>> loginCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                String methodName = ".repartager loginCallback: ";
                boolean resultOK = onCompleteLogin(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(methodName + "Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Connexion");
                    alertDialog.setMessage("La connexion a Diaspora a échouée");
                    alertDialog.show();
                    return;
                }
                //On est loggué donc on demande le flux
                postReshare(contextGlobal, rootGuid, fluxCallback);
            }
        };
        //Callback de récupération du formulaire de login et d'accès au authenticity_token
        final FutureCallback<Response<String>> tokenCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                String methodName = ".repartager tokenCallback: ";
                boolean resultOK = onCompleteGetToken(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(methodName + "Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Accès");
                    alertDialog.setMessage("L'accès a Diaspora est impossible");
                    alertDialog.show();
                    return;
                }
                //On a le token donc on demande le login
                login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            }
        };
        //Cas ou nous n'avons pas les informations de connexion déjà en mémoire.
        if (bForceLogin || (COOKIE_REMEMBER.isEmpty() && COOKIE_SESSION_STREAM.isEmpty())){
            //Cas ou nous n'avons pas les informations pour se connecter.
            if (bForceLogin || (COOKIE_SESSION_LOGIN.isEmpty() && TOKEN.isEmpty())) {
                getToken(contextGlobal, tokenCallback);
                bForceLogin = false;//on ne tente qu'une fois.
                return;
            }
            //Cas ou nous avons les informations pour se connecter
            login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            return;
        }
        if (contextGlobal==null){
            LOG.e(methodName + "Le contexte est vide et empêche un traitement");
            return;
        }
        //Cas ou nous avons les informations de connexion
        postReshare(contextGlobal, rootGuid, fluxCallback);
    }

    public static void partagerImage(final Context context, final String localPath, final String nameFile, final ProgressBar uploadProgressBar, final FutureCallback<Response<String>> fluxCallback){

        contextGlobal = context;
        //Callback d'envoi du formulaire de login
        final FutureCallback<Response<String>> loginCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                boolean resultOK = onCompleteLogin(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(".partagerImage : Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Connexion");
                    alertDialog.setMessage("La connexion a Diaspora a échouée");
                    alertDialog.show();
                    return;
                }
                //On est loggué donc on demande le flux
                DataControler.uploadImage(contextGlobal.getApplicationContext(), localPath, nameFile, uploadProgressBar, fluxCallback);
            }
        };
        //Callback de récupération du formulaire de login et d'accès au authenticity_token
        final FutureCallback<Response<String>> tokenCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                boolean resultOK = onCompleteGetToken(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(".partagerImage : Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Accès");
                    alertDialog.setMessage("L'accès a Diaspora est impossible");
                    alertDialog.show();
                    return;
                }
                //On a le token donc on demande le login
                login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            }
        };
        //Cas ou nous n'avons pas les informations de connexion déjà en mémoire.
        if (COOKIE_REMEMBER.isEmpty()){
            //Cas ou nous n'avons pas les informations pour se connecter.
            if (COOKIE_SESSION_LOGIN.isEmpty() && TOKEN.isEmpty()) {
                getToken(contextGlobal, tokenCallback);
                return;
            }
            //Cas ou nous avons les informations pour se connecter
            login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            return;
        }
        if (contextGlobal==null){
            LOG.e(".getStreamFlow : Le contexte est vide et empêche un traitement");
            return;
        }
        //Cas ou nous avons les informations de connexion
        DataControler.uploadImage(contextGlobal.getApplicationContext(), localPath, nameFile, uploadProgressBar, fluxCallback);

    }

    public static void partagerImage(final Context context, final String localPath, final String nameFile, final String message, final ProgressBar uploadProgressBar, final FutureCallback<Response<UploadResult>> fluxCallback){

        contextGlobal = context;
        //Callback d'envoi du formulaire de login
        final FutureCallback<Response<String>> loginCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                boolean resultOK = onCompleteLogin(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(".partagerImage : Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Connexion");
                    alertDialog.setMessage("La connexion a Diaspora a échouée");
                    alertDialog.show();
                    return;
                }
                //On est loggué donc on demande le flux
                DataControler.uploadImage(contextGlobal.getApplicationContext(), localPath, nameFile, message, uploadProgressBar, fluxCallback);
            }
        };
        //Callback de récupération du formulaire de login et d'accès au authenticity_token
        final FutureCallback<Response<String>> tokenCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                boolean resultOK = onCompleteGetToken(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(".partagerImage : Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Accès");
                    alertDialog.setMessage("L'accès a Diaspora est impossible");
                    alertDialog.show();
                    return;
                }
                //On a le token donc on demande le login
                login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            }
        };
        //Cas ou nous n'avons pas les informations de connexion déjà en mémoire.
        if (COOKIE_REMEMBER.isEmpty()){
            //Cas ou nous n'avons pas les informations pour se connecter.
            if (COOKIE_SESSION_LOGIN.isEmpty() && TOKEN.isEmpty()) {
                getToken(contextGlobal, tokenCallback);
                return;
            }
            //Cas ou nous avons les informations pour se connecter
            login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            return;
        }
        if (contextGlobal==null){
            LOG.e(".getStreamFlow : Le contexte est vide et empêche un traitement");
            return;
        }
        //Cas ou nous avons les informations de connexion
        DataControler.uploadImage(contextGlobal.getApplicationContext(), localPath, nameFile, message, uploadProgressBar, fluxCallback);

//        //On doit se connecter
//        getToken(contextGlobal, tokenCallback);

    }

    public static void getStreamFlow(final Context context, final FutureCallback<Response<List<Post>>> fluxCallback, boolean forceRelogin){

        boolean bForceLogin = forceRelogin;
        if (forceRelogin){
            return;
        }
        contextGlobal = context;
        //Callback d'envoi du formulaire de login
        final FutureCallback<Response<String>> loginCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                boolean resultOK = onCompleteLogin(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(".getStreamFlow : Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Connexion");
                    alertDialog.setMessage("La connexion a Diaspora a échouée");
                    alertDialog.show();
                    return;
                }
                //On est loggué donc on demande le flux
                getStream(contextGlobal, fluxCallback);
            }
        };
        //Callback de récupération du formulaire de login et d'accès au authenticity_token
        final FutureCallback<Response<String>> tokenCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                boolean resultOK = onCompleteGetToken(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(".getStreamFlow : Le contexte est vide et empêche un traitement");
                        return;
                    }
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextGlobal);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Accès");
                    alertDialog.setMessage("L'accès a Diaspora est impossible");
                    alertDialog.show();
                    return;
                }
                //On a le token donc on demande le login
                login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            }
        };
        //Cas ou nous n'avons pas les informations de connexion déjà en mémoire.
        if (bForceLogin || (COOKIE_REMEMBER.isEmpty() && COOKIE_SESSION_STREAM.isEmpty())){
            //Cas ou nous n'avons pas les informations pour se connecter.
            if (bForceLogin || (COOKIE_SESSION_LOGIN.isEmpty() && TOKEN.isEmpty())) {
                getToken(contextGlobal, tokenCallback);
                bForceLogin = false;//on ne tente qu'une fois.
                return;
            }
            //Cas ou nous avons les informations pour se connecter
            login(contextGlobal, "tilucifer", "tilucifer", loginCallback);
            return;
        }
        if (contextGlobal==null){
            LOG.e(".getStreamFlow : Le contexte est vide et empêche un traitement");
            return;
        }
        //Cas ou nous avons les informations de connexion
        getStream(contextGlobal, fluxCallback);

    }

    public static void getToken(Context context, FutureCallback<Response<String>> callback){
        LOG.d(".getToken : Entrée");
        try{
            LOG.d(".getToken : Construction de la requête d'appel GET à "+LOGIN_URL);
            Ion.with(context)
                    .load("GET", LOGIN_URL)
                    .setHeader("User-Agent", USER_AGENT)
                    .followRedirect(true)
                    .noCache()
                    .asString()
                    .withResponse()
                    .setCallback(callback);
        }catch(Throwable thr){
            LOG.e(".getToken Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        LOG.d(".getToken : Sortie");
    }



    public static boolean onCompleteRepartager(Exception exception, final Response<String> response){
        String methodName = ".onCompleteRepartager : ";
        LOG.d(methodName + "Entrée");


        boolean resultOK = true;
        boolean resultKO = false;

        LOG.d(methodName + ": exception ? "+exception);
        if (exception != null) {
            LOG.d(methodName + "Erreur : "+exception.getMessage());
            exception.printStackTrace();
            ACRA.getErrorReporter().handleException(exception);
            LOG.d(methodName + ": Sortie");
            return resultKO;
        }

        LOG.d(methodName + ": response ? "+response);
        if (response!=null){
            String result = response.getResult();
            LOG.d(methodName + ": reponse result ? "+result);
            HeadersResponse resultHeaders = response.getHeaders();
            LOG.d(methodName + ": reponse headers ? "+resultHeaders);
            if (resultHeaders.getHeaders()!=null){
                LOG.d(methodName + ": all headers ? "+resultHeaders.getHeaders());
                LOG.d(methodName + ": reponse code ? "+resultHeaders.code());
                LOG.d(methodName + ": reponse message ? "+resultHeaders.message());
            }
        }

        LOG.d(methodName + ": Sortie");
        return resultOK;
    }

    public static boolean onCompleteGetToken(Exception exception, Response<String> response){
        LOG.d(".onCompleteGetToken : Entrée");
        String result = response.getResult();
        boolean resultOK = true;
        boolean resultKO = false;

        if (exception != null) {
            LOG.d(".onCompleteGetToken Erreur : "+exception.getMessage());
            exception.printStackTrace();
            ACRA.getErrorReporter().handleException(exception);
            LOG.d(".onCompleteGetToken : Sortie");
            return resultKO;
        }

        if (response==null || response.getHeaders()==null){
            LOG.d(".onCompleteGetToken\t**\trecherche impossible de COOKIE_SESSION_LOGIN\t**");
            LOG.d(".onCompleteGetToken : Sortie");
            return resultKO;
        }
        Header[] headers = response.getHeaders().getHeaders().toHeaderArray();
        boolean cookieSessionFound = false;
        for(Header header:headers) {
            String headerName = header.getName();
            String headerValue = header.getValue();
            if (headerName != null && !headerName.isEmpty() &&
                    headerValue != null && !headerValue.isEmpty() &&
                    headerName.toLowerCase().equals("set-cookie")) {
                if (headerValue.startsWith("_diaspora_session=")) {
                    LOG.d(".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN found in " + headerValue + "\t**");
                    COOKIE_SESSION_LOGIN = headerValue.substring("_diaspora_session=".length());
                    LOG.d(".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN set to " + COOKIE_SESSION_LOGIN + "\t**");
                    cookieSessionFound = true;
//                    if (COOKIE_SESSION_LOGIN.isEmpty()) {
//                        COOKIE_SESSION_LOGIN = headerValue.substring("_diaspora_session=".length());
//                        LOG.d(".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN set to " + COOKIE_SESSION_LOGIN + "\t**");
//                    } else {
//                        LOG.d(".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN already set to " + COOKIE_SESSION_LOGIN + "\t**");
//                    }
                }
            }
        }
        if (!cookieSessionFound){
            LOG.d(".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN introuvable\t**");
            LOG.d(".onCompleteGetToken : Sortie");
            return resultKO;
        }

        if (result==null || result.isEmpty()){
            LOG.d(".parseLoginAsync\t**\tRESPONSE introuvable\t**");
            LOG.d(".onCompleteGetToken : Sortie");
            return resultKO;
        }
        int indexTokenName = result.indexOf("<meta content=\"authenticity_token\" name=\"csrf-param\" />",0);
        if (indexTokenName<=0) {
            LOG.d(".onCompleteGetToken\t**\tIMPOSSIBLE de trouver le token");
            LOG.d(".onCompleteGetToken : Sortie");
            return resultKO;
        }
        int indexToken = result.indexOf("<meta content=\"", indexTokenName + 1);
        LOG.d(".onCompleteGetToken	**	token found in "+result.substring(indexToken, result.indexOf("/>", indexToken)));
        indexToken = indexToken+"<meta content=\"".length();
        int indexEndToken = result.indexOf("\" name=\"csrf-token\"", indexToken+1);
        if (TOKEN.isEmpty()) {
            TOKEN = result.substring(indexToken, indexEndToken);
            LOG.d(".onCompleteGetToken\t**\ttoken récolté '" + TOKEN + "'");
        }else{
            LOG.d(".onCompleteGetToken\t**\ttoken déjà récolté '" + TOKEN + "'");
        }
        LOG.d(".onCompleteGetToken : Sortie");
        return resultOK;
    }

    public static void login(Context context, String username, String password, FutureCallback<Response<String>> callback){
        login(context, username, password, callback, false);
    }



    public static void login(Context context, String username, String password, FutureCallback<Response<String>> callback, boolean followRedirect){
        LOG.d(".login : Entrée");

        try{
            LOG.d(".login : On efface les cookies");
            CookieControler cookieControler = CookieControler.getInstance(context);
            cookieControler.clearCookies();
            LOG.d(".login : On ajoute le cookie _diaspora_session=" + COOKIE_SESSION_LOGIN);
            URI uri = URI.create(POD_URL);
            cookieControler.storeCookie(uri, "_diaspora_session", COOKIE_SESSION_LOGIN);
            LOG.d(".login : Construction de la requête d'appel POST à "+LOGIN_URL+ " (authenticity_token="+TOKEN+")");
            Ion.with(context)
                    .load("POST", LOGIN_URL)
                    .followRedirect(followRedirect)
                    .addHeader("Accept", "*/*")
                    .setHeader("User-Agent", USER_AGENT)
                    .noCache()
//                    .setBodyParameter("utf-8", "✓")
                    .setBodyParameter("user[username]", username)
                    .setBodyParameter("user[password]", password)
                    .setBodyParameter("user[remember_me]", "1")
//                    .setBodyParameter("commit", "Sign+in")
                    .setBodyParameter("authenticity_token", TOKEN)
                    .asString()
                    .withResponse()
                    .setCallback(callback);
        }catch(Throwable thr){
            LOG.e(".login Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        LOG.d(".login : Sortie");
    }



    public static List<Post> onCompleteStream(Exception exception, Response<List<Post>> response) {
        LOG.d(".onCompleteStream : Entrée");
        List<Post> result = response.getResult();
        boolean resultOK = true;
        boolean resultKO = false;

        if (exception != null) {
            LOG.d(".onCompleteStream Erreur : "+exception.getMessage());
            exception.printStackTrace();
            ACRA.getErrorReporter().handleException(exception);
            LOG.d(".onCompleteStream : Sortie");
            return new ArrayList<Post>();
        }

//        if (response==null || response.getHeaders()==null){
//            LOG.d(".onCompleteStream\t**\trecherche impossible de COOKIE_SESSION\t**");
//            return new ArrayList<Post>();
//        }
        Header[] headers = response.getHeaders().getHeaders().toHeaderArray();
//        boolean cookieSessionFound = false;
//        boolean cookieRememberFound = false;
//        for(Header header:headers) {
//            String headerName = header.getName();
//            String headerValue = header.getValue();
//            if (headerName != null && !headerName.isEmpty() &&
//                    headerValue != null && !headerValue.isEmpty() &&
//                    headerName.toLowerCase().equals("set-cookie")) {
//                if (headerValue.startsWith("_diaspora_session=")) {
//                    LOG.d(".onCompleteStream\t**\tCOOKIE_SESSION_STREAM found in " + headerValue + "\t**");
//                    cookieSessionFound = true;
//                    COOKIE_SESSION_STREAM = headerValue.substring("_diaspora_session=".length());
//                    LOG.d(".onCompleteStream\t**\tCOOKIE_SESSION_STREAM replace to " + COOKIE_SESSION_STREAM + "\t**");
//                }
//            }
//        }
//        if (!cookieSessionFound){
//            LOG.d(".onCompleteStream\t**\tCOOKIE_SESSION_STREAM introuvable\t**");
//        }

        if (result==null || result.isEmpty()){
            LOG.d(".onCompleteStream\t**\tRESPONSE introuvable\t**");
            LOG.d(".onCompleteStream : Sortie");
            return new ArrayList<Post>();
        }
        LOG.d(".onCompleteStream : Sortie");
        return result;
    }


    public static boolean onCompleteLogin(Exception exception, Response<String> response) {
        LOG.d(".onCompleteLogin : Entrée");
        String result = response.getResult();
        boolean resultOK = true;
        boolean resultKO = false;

        if (exception != null) {
            LOG.d(".onCompleteLogin Erreur : "+exception.getMessage());
            exception.printStackTrace();
            ACRA.getErrorReporter().handleException(exception);
            LOG.d(".onCompleteLogin : Sortie");
            return resultKO;
        }

        if (response==null || response.getHeaders()==null){
            LOG.d(".onCompleteLogin\t**\trecherche impossible de COOKIE_SESSION\t**");
            return resultKO;
        }
        Header[] headers = response.getHeaders().getHeaders().toHeaderArray();
        boolean cookieSessionFound = false;
        boolean cookieRememberFound = false;
        for(Header header:headers) {
            String headerName = header.getName();
            String headerValue = header.getValue();
            if (headerName != null && !headerName.isEmpty() &&
                    headerValue != null && !headerValue.isEmpty() &&
                    headerName.toLowerCase().equals("set-cookie")) {
                if (headerValue.startsWith("remember_user_token=")){
                    LOG.d(".onCompleteLogin\t**\tCOOKIE_REMEMBER found in " + headerValue + "\t**");
                    cookieRememberFound = true;
                    COOKIE_REMEMBER = headerValue.substring("remember_user_token=".length());
                    LOG.d(".onCompleteLogin\t**\tCOOKIE_REMEMBER set to " + COOKIE_REMEMBER + "\t**");
                }else if (headerValue.startsWith("_diaspora_session=")) {
                    LOG.d(".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM found in " + headerValue + "\t**");
                    cookieSessionFound = true;
                    COOKIE_SESSION_STREAM = headerValue.substring("_diaspora_session=".length());
                    LOG.d(".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM set to " + COOKIE_SESSION_STREAM + "\t**");
//                    if (headerValue.length()>COOKIE_SESSION_STREAM.length()) {
//                        if (COOKIE_SESSION_STREAM.isEmpty()) {
//                            COOKIE_SESSION_STREAM = headerValue.substring("_diaspora_session=".length());
//                            LOG.d(".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM set to " + COOKIE_SESSION_STREAM + "\t**");
//                        }else{
//                            LOG.d(".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM already set to " + COOKIE_SESSION_STREAM + "\t**");
//                        }
//                    }
                }
            }
        }
        if (!cookieSessionFound){
            LOG.d(".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM introuvable\t**");
            LOG.d(".onCompleteLogin : Sortie");
            return resultKO;
        }
        if (!cookieRememberFound){
            LOG.d(".onCompleteLogin\t**\tCOOKIE_REMEMBER introuvable\t**");
//            LOG.d(".onCompleteLogin : Sortie");
//            return resultKO;
        }

        if (result==null || result.isEmpty()){
            LOG.d(".onCompleteLogin\t**\tRESPONSE introuvable\t**");
            LOG.d(".onCompleteLogin : Sortie");
            return resultKO;
        }
        LOG.d(".onCompleteLogin : Sortie");
        return resultOK;
    }



    public static void post(Context context, String post, String username, String password, FutureCallback<Response<String>> callback){
        LOG.d(".post : Entrée (username="+username+" , password="+password+", token="+TOKEN+")");
        try{
            Ion.with(context)
                    .load("POST", LOGIN_URL)
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .setHeader("User-Agent", USER_AGENT)
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
            LOG.e("Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        LOG.d(".post : Sortie (post="+post+"username="+username+" , password="+password+", token="+TOKEN+")");
    }

    public static void getStream(Context context, FutureCallback<Response<List<Post>>> callback){
        LOG.d(".getStream : Entrée");
        try{
            try {
                LOG.d(".getStream : On efface les cookies");
                CookieControler cookieControler = CookieControler.getInstance(context);
                cookieControler.clearCookies();
                URI uri = URI.create(POD_URL);
                if (!COOKIE_REMEMBER.isEmpty()) {
                    LOG.d(".getStream : On ajoute le cookie remember_user_token=" + COOKIE_REMEMBER);
                    cookieControler.storeCookie(uri, "remember_user_token", COOKIE_REMEMBER);
                }
                if (!COOKIE_SESSION_STREAM.isEmpty()) {
                    LOG.d(".getStream : On ajoute le cookie _diaspora_session=" + COOKIE_SESSION_STREAM);
                    cookieControler.storeCookie(uri, "_diaspora_session", COOKIE_SESSION_STREAM);
                }
            }catch (IOException ioex){
                LOG.d(".getStream : Impossible de positioner le cookie _diaspora_session ou remember_user_token");
            }
            LOG.d(".getStream : Construction de la requête d'appel GET à "+STREAM_URL+ " (x-csrf-token="+TOKEN+")");
            Ion.with(context)
                    .load("GET", STREAM_URL)
                    .setHeader("User-Agent", USER_AGENT)
                    .noCache()
                    .setHeader("x-requested-with", "XMLHttpRequest")
                    .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .setHeader("x-csrf-token", TOKEN)
                    .as(new TypeToken<List<Post>>() {
                    })
                    .withResponse()
                    .setCallback(callback);
        }catch(Throwable thr){
            LOG.e(".getStream Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(".getStream : Sortie");
            throw thr;
        }
        LOG.d(".getStream : Sortie");
    }
    public static void getPodList(Context context, FutureCallback<Response<Pods>> callback){
        String methodName = ".postReshare : ";
        LOG.d(methodName + "Entrée");
        try{
            LOG.d(methodName + "Construction de la requête d'appel GET à "+PODLIST_URL+ " (x-csrf-token="+TOKEN+")");
            Ion.with(context)
                    .load("GET", PODLIST_URL)
                    .setHeader("User-Agent", USER_AGENT)
                    .noCache()
                    .setHeader("x-requested-with", "XMLHttpRequest")
                    .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .as(new TypeToken<Pods>() {
                    })
                    .withResponse()
                    .setCallback(callback);
        }catch(Throwable thr){
            LOG.e(methodName + "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(methodName + "Sortie");
            throw thr;
        }
        LOG.d(methodName + "Sortie");
    }

    public static void postReshare(Context context, String rootGuid, FutureCallback<Response<String>> callback){
        String methodName = ".postReshare : ";
        LOG.d(methodName+ "Entrée");
        try{
            try {
                LOG.d(methodName+ "On efface les cookies");
                CookieControler cookieControler = CookieControler.getInstance(context);
                cookieControler.clearCookies();
                URI uri = URI.create(POD_URL);
                if (!COOKIE_REMEMBER.isEmpty()) {
                    LOG.d(methodName+ "On ajoute le cookie remember_user_token=" + COOKIE_REMEMBER);
                    cookieControler.storeCookie(uri, "remember_user_token", COOKIE_REMEMBER);
                }
            }catch (IOException ioex){
                LOG.d(".getStream : Impossible de positioner le cookie remember_user_token");
            }
            JsonObject jsonParam = new JsonObject();
            jsonParam.addProperty("root_guid", rootGuid);
            Ion.with(context)
                .load("POST", RESHARE_URL)
                .setHeader("User-Agent", USER_AGENT)
                .noCache()
                .setHeader("x-requested-with", "XMLHttpRequest")
                .setHeader("content-type", "application/json")
                .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
//                .setHeader("x-csrf-token", TOKEN)
                .setJsonObjectBody(jsonParam)
                .asString()
                .withResponse();

        }catch(Throwable thr){
            LOG.e(methodName+ "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(methodName+ "Sortie");
            throw thr;
        }
        LOG.d(methodName+ "Sortie");
    }

    public static void postLike(Context context, int postID, FutureCallback<Response<String>> callback){
        String methodName = ".postReshare : ";
        LOG.d(methodName+ "Entrée");
        try{
            try {
                LOG.d(methodName+ "On efface les cookies");
                CookieControler cookieControler = CookieControler.getInstance(context);
                cookieControler.clearCookies();
                URI uri = URI.create(POD_URL);
                if (!COOKIE_REMEMBER.isEmpty()) {
                    LOG.d(methodName+ "On ajoute le cookie remember_user_token=" + COOKIE_REMEMBER);
                    cookieControler.storeCookie(uri, "remember_user_token", COOKIE_REMEMBER);
                }
            }catch (IOException ioex){
                LOG.d(".getStream : Impossible de positioner le cookie remember_user_token");
            }
            JsonObject jsonParam = new JsonObject();
            Ion.with(context)
                    .load("POST", POSTS_URL+"/"+postID+"/likes")
                    .setHeader("User-Agent", USER_AGENT)
                    .noCache()
                    .setHeader("x-requested-with", "XMLHttpRequest")
                    .setHeader("content-type", "application/json")
                    .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .setJsonObjectBody(jsonParam)
                    .asString()
                    .withResponse();

        }catch(Throwable thr){
            LOG.e(methodName+ "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(methodName+ "Sortie");
            throw thr;
        }
        LOG.d(methodName+ "Sortie");
    }
}
