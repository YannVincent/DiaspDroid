package fr.android.scaron.diaspdroid.controler;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.model.UploadResult;

/**
 * Created by Sébastien on 11/03/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class DiasporaBean {
    private static Logger LOGGEUR = LoggerFactory.getLogger(DiasporaBean.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "DiasporaBean";
    @RootContext
    Context context;

    @RestService
    DiasporaService diasporaService;
    @Bean
    DiasporaErrorHandlerBean diasporaErrorHandlerBean;

    @AfterInject
    public void init(){
        diasporaService.setRestErrorHandler(diasporaErrorHandlerBean);
//        if (DiasporaControler.COOKIE_SESSION_STREAM!=null && !DiasporaControler.COOKIE_SESSION_STREAM.isEmpty()) {
//            diasporaService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_STREAM);
//        }else if (DiasporaControler.COOKIE_SESSION_LOGIN!=null && !DiasporaControler.COOKIE_SESSION_LOGIN.isEmpty()) {
//            diasporaService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_LOGIN);
//        } else if (DiasporaControler.COOKIE_SESSION_TOKEN!=null && !DiasporaControler.COOKIE_SESSION_TOKEN.isEmpty()) {
//            diasporaService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_TOKEN);
//        }
//        if (DiasporaControler.TOKEN!=null && !DiasporaControler.TOKEN.isEmpty()){
//            diasporaService.setHeader("x-csrf-token", DiasporaControler.TOKEN);
//        }
    }

    public boolean seLogguer(){
        String TAG_METHOD = TAG + ".seLogguer : ";
        LOG.d(TAG_METHOD+ "Entrée");
        boolean resultOK = true;
        boolean resultKO = false;
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean getTokenOK = getToken(diasporaService.getLoginHTML());
        LOG.d(TAG_METHOD+ "Token crsf obtenu ? "+getTokenOK);
        if (!getTokenOK){
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            return resultKO;
        }
//        diasporaService.setHeader("x-csrf-token", DiasporaControler.TOKEN);
        DiasporaControler.COOKIE_SESSION_TOKEN = diasporaService.getCookie("_diaspora_session");
        if (DiasporaControler.COOKIE_SESSION_TOKEN!=null && !DiasporaControler.COOKIE_SESSION_TOKEN.isEmpty()) {
            diasporaService.setCookie("_diaspora_session", DiasporaControler.COOKIE_SESSION_TOKEN);
        }
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        String loggued = diasporaService.postLogin();

        LOG.d(TAG_METHOD+ "Login obtenu ? "+loggued);
        LOG.d(TAG_METHOD+ "Sortie");
        return resultOK;
    }

    public UploadResult uploadFile(String fileName, String localPath){
        String TAG_METHOD = TAG + ".uploadFile : ";
        LOG.d(TAG_METHOD+ "Entrée");

        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            MultiValueMap<String, Object> mvMap = new LinkedMultiValueMap<String, Object>();
            LOG.d(TAG_METHOD+ "add part file");
            mvMap.add("file", new FileSystemResource(localPath));
            LOG.d(TAG_METHOD+ "call diasporaService.uploadFile");
            UploadResult uploadResult = diasporaService.uploadFile(mvMap, fileName);
            LOG.d(TAG_METHOD+ "uploadResult is null ? "+(uploadResult==null));
            if (uploadResult!=null){
                LOG.d(TAG_METHOD+ "uploadResult is success ? "+uploadResult.getSuccess());
                LOG.d(TAG_METHOD+ "uploadResult is error ? "+uploadResult.getError());
            }
            LOG.d(TAG_METHOD+ "Sortie");
            return uploadResult;
        }
        return null;
    }

    public List<Post> getInfo(String userName){
        String TAG_METHOD = TAG + ".getInfo : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD+ "diasporaService.setRootUrl");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        LOG.d(TAG_METHOD+ "appel de diasporaService.getInfo");
        List<Post> infos = diasporaService.getInfo(userName);
        LOG.d(TAG_METHOD+ "Sortie");
        return infos;
    }

    public List<Post> getStream(){
        String TAG_METHOD = TAG + ".getStream : ";
        LOG.d(TAG_METHOD+ "Entrée");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getStream");
            List<Post> posts = diasporaService.getStream();
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;//streamPostsService.getStream();
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = new Post();
        empty.setText("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    private boolean getToken(String response){
        String TAG_METHOD = TAG + ".getToken : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean resultOK = true;
        boolean resultKO = false;
        if (response==null || response.isEmpty()){
            return resultKO;
        }

        int indexTokenName = response.indexOf("<meta content=\"authenticity_token\" name=\"csrf-param\" />",0);
        if (indexTokenName<=0) {
            LOG.d(TAG_METHOD+ "\t**\tIMPOSSIBLE de trouver le token");
            LOG.d(TAG_METHOD+ "Sortie");
            return resultKO;
        }
        int indexToken = response.indexOf("<meta content=\"", indexTokenName + 1);
        LOG.d(TAG_METHOD+ "**	token found in "+response.substring(indexToken, response.indexOf("/>", indexToken)));
        indexToken = indexToken+"<meta content=\"".length();
        int indexEndToken = response.indexOf("\" name=\"csrf-token\"", indexToken+1);
        if (DiasporaControler.TOKEN.isEmpty()) {
            DiasporaControler.TOKEN = response.substring(indexToken, indexEndToken);
            LOG.d(TAG_METHOD+ "\t**\ttoken récolté '" + DiasporaControler.TOKEN + "'");
        }else{
            LOG.d(TAG_METHOD+ "\t**\ttoken déjà récolté '" + DiasporaControler.TOKEN + "'");
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return resultOK;
    }

}
