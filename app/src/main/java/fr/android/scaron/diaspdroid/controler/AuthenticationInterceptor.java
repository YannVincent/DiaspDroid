package fr.android.scaron.diaspdroid.controler;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by CARON-08651 on 11/03/2015.
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
        StringBuilder sbBody = new StringBuilder();//new String(body, Charset.forName("UTF-8")));
        sbBody.append("user[username]="+authenticationStore.getUsername());
        sbBody.append("&user[password]="+authenticationStore.getPassword());
        sbBody.append("&commit=Connexion");
        sbBody.append("&utf-8=✓");
        sbBody.append("&authenticity_token="+authenticationStore.getToken());
        sbBody.append("&user[remember_me]=1");

        LOG.d(TAG_METHOD+ "sbBody created : "+sbBody.toString());
        byte[] bodyEncoded = URLEncoder.encode(sbBody.toString()).getBytes(Charset.forName("UTF-8"));

        LOG.d(TAG_METHOD+ "sbBody urlencoded : "+bodyEncoded);
        LOG.d(TAG_METHOD+ "sbBody urlencoded (str) : "+new String(bodyEncoded,Charset.forName("UTF-8")));
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
}
