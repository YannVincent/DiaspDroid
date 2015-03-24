package fr.android.scaron.diaspdroid.controler;

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
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

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
        LOG.d(TAG_METHOD+ "Entrée");
        HttpHeaders httpHeaders = request.getHeaders();
        if (httpHeaders!=null) {
            List<String> cookies = httpHeaders.get("coookie");
            LOG.d(TAG_METHOD + "cookies found ? " + (cookies!=null));
            LOG.d(TAG_METHOD + "cookies : " + cookies);

//        //UnsupportedOperationException donc on commente
//            if (cookies==null){
//                StringBuilder cookieValueSB = new StringBuilder();
//                boolean cookieRememberAdded = false;
//                if (DiasporaControler.COOKIE_REMEMBER!=null){
//                    cookieValueSB.append("remember_user_token="+DiasporaControler.COOKIE_REMEMBER);
//                    cookieRememberAdded = true;
//                }
//                if (DiasporaControler.COOKIE_SESSION_TOKEN!=null){
//                    if (cookieRememberAdded){
//                        cookieValueSB.append(", ");
//                    }
//                    cookieValueSB.append("_diaspora_session="+DiasporaControler.COOKIE_SESSION_TOKEN);
//                }
//                httpHeaders.add("cookie", cookieValueSB.toString());
//            }
//            cookies = httpHeaders.get("coookie");
//            LOG.d(TAG_METHOD + "cookies added found ? " + (cookies!=null));
        }
        StringBuilder sbBody = new StringBuilder();//new String(body, Charset.forName("UTF-8")));
        if (request.getMethod()== HttpMethod.POST) {
            sbBody.append(ulrEncode("user[username]")+"=" + ulrEncode(authenticationStore.getUsername()));
            sbBody.append("&"+ulrEncode("user[password]")+"=" + ulrEncode(authenticationStore.getPassword()));
//            sbBody.append("&commit=Connexion");
//            sbBody.append("&utf-8=✓");
            sbBody.append("&"+ulrEncode("authenticity_token")+"=" + ulrEncode(authenticationStore.getToken()));
            sbBody.append("&"+ulrEncode("user[remember_me]")+"=1");
        }

        LOG.d(TAG_METHOD + "sbBody urlencoded : " + sbBody.toString());
        byte[] bodyEncoded = sbBody.toString().getBytes(Charset.forName("UTF-8"));
        ClientHttpResponse executionResult = execution.execute(request, bodyEncoded);

        LOG.d(TAG_METHOD+ "Sortie");
        return executionResult;

//        HttpAuthentication authentication = new HttpAuthentication() {
//            @Override
//            public String getHeaderValue() {
//                return null;
//            }
//        }
//        return execution.execute(request, body);
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
