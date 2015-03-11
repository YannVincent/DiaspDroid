package fr.android.scaron.diaspdroid.controler;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
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

    @Bean
    AuthenticationStore authenticationStore;


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        StringBuilder sbBody = new StringBuilder();//new String(body, Charset.forName("UTF-8")));
        sbBody.append("user[username]="+authenticationStore.getUsername());
        sbBody.append("&user[password]="+authenticationStore.getPassword());
        sbBody.append("&commit=Connexion");
        sbBody.append("&utf-8=✓");
        sbBody.append("&authenticity_token="+authenticationStore.getToken());
        sbBody.append("&user[remember_me]=1");
        byte[] bodyEncoded = URLEncoder.encode(sbBody.toString()).getBytes(Charset.forName("UTF-8"));
        return execution.execute(request, bodyEncoded);

//        HttpAuthentication authentication = new HttpAuthentication() {
//            @Override
//            public String getHeaderValue() {
//                return null;
//            }
//        }
//        return execution.execute(request, body);
    }
}
