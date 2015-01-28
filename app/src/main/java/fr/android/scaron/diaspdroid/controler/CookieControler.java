package fr.android.scaron.diaspdroid.controler;

import android.content.Context;

import com.koushikdutta.async.http.Headers;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.cookie.CookieMiddleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by CARON-08651 on 26/01/2015.
 */
public class CookieControler {

    private static Logger LOGGEUR = LoggerFactory.getLogger(CookieControler.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);

    static CookieControler instance;
    CookieManager manager;
    CookieMiddleware middleware;

    public static CookieControler getInstance(Context context) {
        if (instance==null){
            instance = new CookieControler(context);
        }
        return instance;
    }
    private CookieControler(Context context) {
        Ion ion = Ion.getDefault(context);
        middleware = ion.getCookieMiddleware();
        manager = middleware.getCookieManager();
    }

    public void clearCookies(){
        middleware.clear();
    }

    public void storeCookies(URLConnection conn) throws IOException {

        List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
        URI uri = URI.create(conn.getURL().toString());
        if(cookies != null) {
            storeCookies(uri, cookies);
        }
    }

    public void storeCookies(URI uri, List<String> cookies) throws IOException {
        Headers headers = new Headers();
        headers.addAll("Set-Cookie", cookies);
        manager.put(uri, headers.getMultiMap());
    }

    public void storeCookie(URI uri, String cookieName, String cookieValue) throws IOException {
        List<String> cookie = new ArrayList<String>();
        cookie.add(String.format("%s=%s", cookieName, cookieValue));
        storeCookies(uri, cookie);
    }
}
