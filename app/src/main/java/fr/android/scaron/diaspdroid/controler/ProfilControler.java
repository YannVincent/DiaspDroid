package fr.android.scaron.diaspdroid.controler;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.Headers;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.android.scaron.diaspdroid.R;

/**
 * Created by Maison on 11/01/2015.
 */
public class ProfilControler extends DiaporaControler{

    public static LogControler LOG = LogControler.getInstance(ProfilControler.class);



    public static void putImage(ImageView imageView, String imagePath){
        try{
            Ion.with(imageView)
                    .placeholder(R.drawable.placeholder_image)
//          .error(R.drawable.placeholder_image)
                    .load(imagePath);
        }catch(Throwable thr){
            LOG.e(ProfilControler.class, "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public static void getToken(Context context, FutureCallback<Response<String>> callback){
        LOG.d(ProfilControler.class, ".get token : Entrée");
        try{
            Ion.with(context)
                    .load("GET", LOGIN_URL)
                    .followRedirect(true)
                    .noCache()
                    .asString()
                    .withResponse()
                    .setCallback(callback);
        }catch(Throwable thr){
            LOG.e(ProfilControler.class, "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        LOG.d(DataControler.class, ".get token : Sortie");
    }

    public static boolean onCompleteGetToken(Exception exception, Response<String> response){
        String result = response.getResult();
        boolean resultOK = true;
        boolean resultKO = false;

        if (exception != null) {
            exception.printStackTrace();
            return resultKO;
        }

        if (response==null || response.getHeaders()==null){
            LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\trecherche impossible de COOKIE_SESSION\t**");
            return resultKO;
        }
        Header[] headers = response.getHeaders().getHeaders().toHeaderArray();
        boolean cookieSessionFound = false;
        for(Header header:headers) {
            String headerName = header.getName();
            String headerValue = header.getValue();
//            LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\tHEADER FOUND : "+headerName+" -> "+headerValue + "\t**");
//            for (HeaderElement headerElt:header.getElements()){
//                String headerEltName = headerElt.getName();
//                String headerEltValue = headerElt.getValue();
//                LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\t\tHEADER ELT FOUND : "+headerEltName+" -> "+headerEltValue + "\t**");
//                for (NameValuePair nameValuePair:headerElt.getParameters()){
//                    String valuePairName = nameValuePair.getName();
//                    String valuePairValue = nameValuePair.getValue();
//                    LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\t\t\tVALUE PAIR FOUND : "+valuePairName+" -> "+valuePairValue + "\t**");
//                }
//            }
            if (headerName != null && !headerName.isEmpty() &&
                    headerValue != null && !headerValue.isEmpty() &&
                    headerName.toLowerCase().equals("set-cookie")) {
                if (headerValue.startsWith("remember_user_token=")){
                    COOKIE_REMEMBER = headerValue.substring("remember_user_token=".length());
                    LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\tCOOKIE_REMEMBER set to " + COOKIE_REMEMBER + "\t**");
                }else if (headerValue.startsWith("_diaspora_session=")) {
                    cookieSessionFound = true;
                    if (COOKIE_SESSION_LOGIN.isEmpty()) {
                        COOKIE_SESSION_LOGIN = headerValue.substring("_diaspora_session=".length());
                        LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN set to " + COOKIE_SESSION_LOGIN + "\t**");
                    }else{
                        LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\tCOOKIE_SESSION_LOGIN already set to " + COOKIE_SESSION_LOGIN + "\t**");
                    }
                }
            }
        }
        if (!cookieSessionFound){
            LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\tCOOKIE_SESSION introuvable\t**");
            return resultKO;
        }

        if (result==null || result.isEmpty()){
            LOG.d(ProfilControler.class, ".parseLoginAsync\t**\tRESPONSE introuvable\t**");
            return resultKO;
        }
        int indexTokenName = result.indexOf("<meta content=\"authenticity_token\" name=\"csrf-param\" />",0);
        if (indexTokenName<=0) {
            LOG.d(ProfilControler.class,".onCompleteGetToken\t**\tIMPOSSIBLE de trouver le token");
            return resultKO;
        }
        int indexToken = result.indexOf("<meta content=\"", indexTokenName + 1);
        indexToken = indexToken+1+"<meta content=\"".length();
        int indexEndToken = result.indexOf("\" name=\"csrf-token\"", indexToken);
        if (TOKEN.isEmpty()) {
            TOKEN = result.substring(indexToken, indexEndToken);
            LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\ttoken récolté '" + TOKEN + "'");
        }else{
            LOG.d(ProfilControler.class, ".onCompleteGetToken\t**\ttoken déjà récolté '" + TOKEN + "'");
        }
        return resultOK;
    }

    public static void login(Context context, String username, String password, FutureCallback<Response<String>> callback){
        LOG.d(DataControler.class, ".login : Entrée");

        try{
            CookieControler cookieControler = CookieControler.getInstance(context);
            cookieControler.clearCookies();
            URI uri = URI.create(POD_URL);
            cookieControler.storeCookie(uri, "_diaspora_session", COOKIE_SESSION_LOGIN);
//            Ion ion=Ion.getDefault(context);//.Config.configure().setTag();
//            ion.configure().createSSLContext("TLS");

//            Ion ion = Ion.getDefault(context);
//            ion.getCookieMiddleware().clear();
//            LOG.i(ProfilControler.class, ".getStreamAsync : cookies cleared");
////            Ion.with(context)
////            CookieManager manager = new CookieManager(null, null);
////            Headers newHeaders = new Headers();
////            URI uri = URI.create(POD_URL);
////            Map<String, List<String>> cookies = manager.get(uri, newHeaders.getMultiMap());
////            manager.get(uri, cookies);
////            ion.getCookieMiddleware().addCookies(cookies, newHeaders);
//
//            ion.build(context)
            Ion.with(context)
                    .load("POST", LOGIN_URL)
                    .followRedirect(true)
                    .noCache()
//                    .followRedirect(false)
//                    .setHeader("Cookie", COOKIE_SESSION)
//                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .setBodyParameter("user[username]", username)
                    .setBodyParameter("user[password]", password)
                    .setBodyParameter("user[remember_me]", "1")
                    .setBodyParameter("authenticity_token", TOKEN)//"vzXnKbMHg96jEsDO289+fic6Kl2wDGz76dFtUYK98dM=")
//                .setBodyParameter("utf8","&#x2713;")
//                .setBodyParameter("commit","Connexion")
                    .asString()
                    .withResponse()
                    .setCallback(callback);
        }catch(Throwable thr){
            LOG.e(ProfilControler.class, "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        LOG.d(DataControler.class, ".login : Sortie");
    }


    public static boolean onCompleteLogin(Exception exception, Response<String> response) {
        String result = response.getResult();
        boolean resultOK = true;
        boolean resultKO = false;

        if (exception != null) {
            exception.printStackTrace();
            return resultKO;
        }

        if (response==null || response.getHeaders()==null){
            LOG.d(ProfilControler.class, ".onCompleteLogin\t**\trecherche impossible de COOKIE_SESSION\t**");
            return resultKO;
        }
        Header[] headers = response.getHeaders().getHeaders().toHeaderArray();
        boolean cookieSessionFound = false;
        for(Header header:headers) {
            String headerName = header.getName();
            String headerValue = header.getValue();
//            LOG.d(ProfilControler.class, ".onCompleteLogin\t**\tHEADER FOUND : "+headerName+" -> "+headerValue + "\t**");
//            for (HeaderElement headerElt:header.getElements()){
//                String headerEltName = headerElt.getName();
//                String headerEltValue = headerElt.getValue();
//                LOG.d(ProfilControler.class, ".onCompleteLogin\t**\t\tHEADER ELT FOUND : "+headerEltName+" -> "+headerEltValue + "\t**");
//                for (NameValuePair nameValuePair:headerElt.getParameters()){
//                    String valuePairName = nameValuePair.getName();
//                    String valuePairValue = nameValuePair.getValue();
//                    LOG.d(ProfilControler.class, ".onCompleteLogin\t**\t\t\tVALUE PAIR FOUND : "+valuePairName+" -> "+valuePairValue + "\t**");
//                }
//            }
            if (headerName != null && !headerName.isEmpty() &&
                    headerValue != null && !headerValue.isEmpty() &&
                    headerName.toLowerCase().equals("set-cookie")) {
                if (headerValue.startsWith("remember_user_token=")){
                    COOKIE_REMEMBER = headerValue.substring("remember_user_token=".length());
                    LOG.d(ProfilControler.class, ".onCompleteLogin\t**\tCOOKIE_REMEMBER set to " + COOKIE_REMEMBER + "\t**");
                }else if (headerValue.startsWith("_diaspora_session=")) {
                    cookieSessionFound = true;
                    if (headerValue.length()>COOKIE_SESSION_STREAM.length()) {
                        if (COOKIE_SESSION_STREAM.isEmpty()) {
                            COOKIE_SESSION_STREAM = headerValue.substring("_diaspora_session=".length());
                            LOG.d(ProfilControler.class, ".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM set to " + COOKIE_SESSION_STREAM + "\t**");
                        }else{
                            LOG.d(ProfilControler.class, ".onCompleteLogin\t**\tCOOKIE_SESSION_STREAM already set to " + COOKIE_SESSION_STREAM + "\t**");
                        }
                    }
                }
            }
        }
        if (!cookieSessionFound){
            LOG.d(ProfilControler.class, ".onCompleteLogin\t**\tCOOKIE_SESSION introuvable\t**");
            return resultKO;
        }

        if (result==null || result.isEmpty()){
            LOG.d(ProfilControler.class, ".onCompleteLogin\t**\tRESPONSE introuvable\t**");
            return resultKO;
        }
        return resultOK;
    }



    public static void post(Context context, String post, String username, String password, FutureCallback<Response<String>> callback){
        LOG.d(DataControler.class, ".post : Entrée (username="+username+" , password="+password+", token="+TOKEN+")");
        try{
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
            //log.error("Erreur : " + thr.toString());
            LOG.e(ProfilControler.class, "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
        }
        LOG.d(DataControler.class, ".post : Sortie (post="+post+"username="+username+" , password="+password+", token="+TOKEN+")");
    }
}
