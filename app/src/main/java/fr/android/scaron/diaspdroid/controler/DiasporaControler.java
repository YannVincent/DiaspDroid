package fr.android.scaron.diaspdroid.controler;

import android.app.AlertDialog;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by Sébastien on 24/01/2015.
 */
public class DiasporaControler {

    private static Logger LOGGEUR = LoggerFactory.getLogger(DiasporaControler.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    static String POD = "framasphere.org";
    static String POD_URL = "https://"+POD;
    static String LOGIN_URL = POD_URL+"/users/sign_in";
    static String STREAM_URL = POD_URL+"/stream";
    static String POST_IMAGE = POD_URL + "/photos?photo%5Bpending%5D=true";
    static String TOKEN_VIDE = "";
    static String TOKEN_TEST = "4REWL0RLsEU5edVgVWuZL16XGAQkVuCYyzGirHvXjOI=";
    static String COOKIE_SESSION_LOGIN_VIDE = "";
    static String COOKIE_SESSION_LOGIN_TEST="BAh7B0kiD3Nlc3Npb25faWQGOgZFVEkiJWFkOGU5MjQ0NjRmOTM1MWQ1NTQ3NDZlZDc4MmUwYzA5BjsAVEkiEF9jc3JmX3Rva2VuBjsARkkiMTRSRVdMMFJMc0VVNWVkVmdWV3VaTDE2WEdBUWtWdUNZeXpHaXJIdlhqT0k9BjsARg%3D%3D--0f5956ca762a3f995f5d1dfa8c51db8d384f5e63";
    static String COOKIE_SESSION_STREAM_VIDE = "";
    static String COOKIE_SESSION_STREAM_TEST="BAh7CUkiD3Nlc3Npb25faWQGOgZFVEkiJWQzYTc1MDM4MGZkMGVkZDgxOTAzYjBiZWNmNjY3OGUwBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJFNyaGJkL3JLYkFzOWpZSTlxU1lVT08GOwBUSSIKZmxhc2gGOwBUbzolQWN0aW9uRGlzcGF0Y2g6OkZsYXNoOjpGbGFzaEhhc2gJOgpAdXNlZG86CFNldAY6CkBoYXNoewY6C25vdGljZVQ6DEBjbG9zZWRGOg1AZmxhc2hlc3sGOwpJIihWb3VzIMOqdGVzIMOgIHByw6lzZW50IGNvbm5lY3TDqS1lLgY7AFQ6CUBub3cwSSIQX2NzcmZfdG9rZW4GOwBGSSIxNE5oeVJKdWdqVll2ZDVwaXhRQ1NCZmZlbkl6bzRMZE1SZUx0UEVVVDJFdz0GOwBG--b1db6d257b315bfff3076ee8112f9a75a45d85ef";
    static String COOKIE_REMEMBER_TEST_VIDE = "";
    static String COOKIE_REMEMBER_TEST = "BAhbB1sGaQISCUkiIiQyYSQxMCRTcmhiZC9yS2JBczlqWUk5cVNZVU9PBjoGRVQ%3D--a111fa31a16b0451130d7598978cda8257466368; path=/; expires=Sun, 08-Feb-2015 21:52:35 GMT; HttpOnly; secure";

    static String TOKEN = TOKEN_VIDE;
    static String COOKIE_SESSION_LOGIN = COOKIE_SESSION_LOGIN_VIDE;
    static String COOKIE_SESSION_STREAM = COOKIE_SESSION_STREAM_VIDE;
    static String COOKIE_REMEMBER = COOKIE_REMEMBER_TEST_VIDE;

    static Context contextGlobal;



    public static void getStreamFlow(final Context context, final FutureCallback<List<Post>> fluxCallback){

        contextGlobal = context;
        //Callback d'envoi du formulaire de login
        final FutureCallback<Response<String>> loginCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                boolean resultOK = onCompleteLogin(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(".getStreamFlow : Le contexte est vide est empêche un traitement");
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
                getStream(contextGlobal.getApplicationContext(), fluxCallback);
            }
        };
        //Callback de récupération du formulaire de login et d'accès au authenticity_token
        final FutureCallback<Response<String>> tokenCallback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                boolean resultOK = onCompleteGetToken(e, result);
                if (!resultOK){
                    if (contextGlobal==null){
                        LOG.e(".getStreamFlow : Le contexte est vide est empêche un traitement");
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
                login(contextGlobal, "tilucifer", "Pikifou01", loginCallback);
            }
        };
        //Cas ou nous n'avons pas les informations de connexion déjà en mémoire.
        if (COOKIE_REMEMBER.isEmpty() && COOKIE_SESSION_STREAM.isEmpty()){
            //Cas ou nous n'avons pas les informations pour se connecter.
            if (COOKIE_SESSION_LOGIN.isEmpty() && TOKEN.isEmpty()) {
                getToken(contextGlobal, tokenCallback);
                return;
            }
            //Cas ou nous avons les informations pour se connecter
            login(contextGlobal, "tilucifer", "Pikifou01", loginCallback);
            return;
        }
        if (contextGlobal==null){
            LOG.e(".getStreamFlow : Le contexte est vide est empêche un traitement");
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
                    cookieSessionFound = true;
                    if (COOKIE_SESSION_LOGIN.isEmpty()) {
                        COOKIE_SESSION_LOGIN = headerValue.substring("_diaspora_session=".length());
                        LOG.d(".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN set to " + COOKIE_SESSION_LOGIN + "\t**");
                    } else {
                        LOG.d(".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN already set to " + COOKIE_SESSION_LOGIN + "\t**");
                    }
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
                    .followRedirect(true)
                    .noCache()
                    .setBodyParameter("user[username]", username)
                    .setBodyParameter("user[password]", password)
                    .setBodyParameter("user[remember_me]", "1")
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
                    if (headerValue.length()>COOKIE_SESSION_STREAM.length()) {
                        if (COOKIE_SESSION_STREAM.isEmpty()) {
                            COOKIE_SESSION_STREAM = headerValue.substring("_diaspora_session=".length());
                            LOG.d(".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM set to " + COOKIE_SESSION_STREAM + "\t**");
                        }else{
                            LOG.d(".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM already set to " + COOKIE_SESSION_STREAM + "\t**");
                        }
                    }
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

    public static void getStream(Context context, FutureCallback<List<Post>> callback){
        LOG.d(".getStream : Entrée");
        try{
            try {
                LOG.d(".getStream : On efface les cookies");
                CookieControler cookieControler = CookieControler.getInstance(context);
                cookieControler.clearCookies();
                URI uri = URI.create(POD_URL);
                if (!COOKIE_REMEMBER.isEmpty()) {
                    LOG.d(".getStream : On ajoute le cookie remember_user_token=" + COOKIE_SESSION_LOGIN);
                    cookieControler.storeCookie(uri, "remember_user_token", COOKIE_REMEMBER);
                }else {
                    LOG.d(".getStream : On ajoute le cookie _diaspora_session=" + COOKIE_SESSION_STREAM);
                    cookieControler.storeCookie(uri, "_diaspora_session", COOKIE_SESSION_STREAM);
                }
            }catch (IOException ioex){
                LOG.d(".getStream : Impossible de positioner le cookie _diaspora_session ou remember_user_token");
            }
            LOG.d(".getStream : Construction de la requête d'appel GET à "+STREAM_URL+ " (x-csrf-token="+TOKEN+")");
            Ion.with(context)
                    .load("GET", STREAM_URL)
                    .noCache()
                    .setHeader("x-requested-with", "XMLHttpRequest")
                    .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .setHeader("x-csrf-token", TOKEN)
                    .as(new TypeToken<List<Post>>() {
                    })
                    .setCallback(callback);
        }catch(Throwable thr){
            LOG.e(".getStream Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(".getStream : Sortie");
            throw thr;
        }
        LOG.d(".getStream : Sortie");
    }
}
