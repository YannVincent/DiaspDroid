package fr.android.scaron.diaspdroid.controler;

import android.net.Uri;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import fr.android.scaron.diaspdroid.model.DiasporaConfig;

/**
 * Created by Sébastien on 11/03/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {
    private static Logger LOGGEUR = LoggerFactory.getLogger(AuthenticationInterceptor.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "AuthenticationInterceptor";

    @Bean
    AuthenticationStore authenticationStore;


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String TAG_METHOD = TAG + ".intercept : ";
        LOG.d(TAG_METHOD+ "Entrée ("+request.getURI()+" ["+request.getMethod()+"])");

        ClientHttpResponse executionResult;
        HttpHeaders httpHeaders = request.getHeaders();
        if (httpHeaders!=null) {
            LOG.d(TAG_METHOD + "httpHeaders : " + httpHeaders);
            List<String> cookies = httpHeaders.get("Cookie");
            LOG.d(TAG_METHOD + "Cookie found ? " + (cookies!=null));
            LOG.d(TAG_METHOD + "Cookie : " + cookies);
            if (cookies!=null) {
                for (String cookie : cookies) {
                    if (DiasporaConfig.COOKIE_AUTH.isEmpty() && cookie!=null && cookie.contains("_diaspora_session") && cookie.contains("remember_user_token")) {
                        LOG.d(TAG_METHOD + "set COOKIE_AUTH with value : " + cookie);
                        DiasporaConfig.COOKIE_AUTH = cookie;
                    }
                }
            }
        }
        if (request.getMethod()== HttpMethod.POST){
            StringBuilder sbBody = new StringBuilder();
            if (request.getURI().toString().endsWith("/users/sign_in")) {
                sbBody.append(ulrEncode("user[username]") + "=" + ulrEncode(authenticationStore.getUsername()));
                sbBody.append("&" + ulrEncode("user[password]") + "=" + ulrEncode(authenticationStore.getPassword()));
//            sbBody.append("&commit=Connexion");
//            sbBody.append("&utf-8=✓");
                sbBody.append("&" + ulrEncode("authenticity_token") + "=" + ulrEncode(authenticationStore.getToken()));
                sbBody.append("&" + ulrEncode("user[remember_me]") + "=1");
                LOG.d(TAG_METHOD + "sbBody urlencoded : " + sbBody.toString());
                byte[] bodyEncoded = sbBody.toString().getBytes(Charset.forName("UTF-8"));
                LOG.d(TAG_METHOD + "execution.execute (" + request.getURI() + " [" + request.getMethod() + "]))");
                executionResult = execution.execute(request, bodyEncoded);
                if (executionResult != null) {
                    LOG.d(TAG_METHOD + "execution.execute (" + request.getURI() + " [" + request.getMethod() + "])), result code " + executionResult.getStatusCode());
                }
            }else {
//                LOG.d(TAG_METHOD + "sbBody : " + new String(body, Charset.forName("UTF-8")));
                LOG.d(TAG_METHOD+ "execution.execute ("+request.getURI()+" ["+request.getMethod()+"]))");
                executionResult = execution.execute(request, body);
                if (executionResult!=null) {
                    LOG.d(TAG_METHOD + "execution.execute ("+request.getURI()+" ["+request.getMethod()+"])), result code "+executionResult.getStatusCode());
                }
            }
        }else {
            LOG.d(TAG_METHOD + "sbBody : " + new String(body, Charset.forName("UTF-8")));
            LOG.d(TAG_METHOD+ "execution.execute ("+request.getURI()+" ["+request.getMethod()+"]))");
            executionResult = execution.execute(request, body);
            if (executionResult!=null) {
                LOG.d(TAG_METHOD + "execution.execute ("+request.getURI()+" ["+request.getMethod()+"])), result code "+executionResult.getStatusCode());
            }
        }
        LOG.d(TAG_METHOD+ "Sortie ("+request.getURI()+" ["+request.getMethod()+"])");
        return executionResult;
    }

    String ulrEncode(String data){
        try {
            return URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return data;
        }
    }
}
