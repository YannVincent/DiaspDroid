package fr.android.scaron.diaspdroid.controler;

import android.content.Context;

import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.model.Comment;
import fr.android.scaron.diaspdroid.model.Contact;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.NewPost;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.model.UploadResult;
import fr.android.scaron.diaspdroid.model.User;

/**
 * Created by Sébastien on 11/03/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class DiasporaBean {
    private static Logger LOGGEUR = LoggerFactory.getLogger(DiasporaBean.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "DiasporaBean";

    private static DiasporaBean instance;
    private static String ERREUR_LOGIN = "L'authentification sur votre POD a échouée.\nVeuillez vérifier vos paramètres ou que votre POD n'est pas en opération de maintenace !";
    @RootContext
    Context context;

    @RestService
    DiasporaService diasporaService;
    @RestService
    PodsService podsService;
    @Bean
    DiasporaErrorHandlerBean diasporaErrorHandlerBean;

//    private static void setInstance(DiasporaBean instance){
//        DiasporaBean.instance = instance;
//    }
//    public static DiasporaBean getInstance(){
//        return DiasporaBean.instance;
//    }

    public void setRootUrl(final String rootUrl){
        diasporaService.setRootUrl(rootUrl);
    }

    @AfterInject
    public void init(){
//        setInstance(this);
        diasporaService.setRestErrorHandler(diasporaErrorHandlerBean);
    }





    public List<Pod> getPods(){
        return podsService.getPods().getPods();
    }

    public boolean seLogguer(){
        String TAG_METHOD = TAG + ".seLogguer : ";
        LOG.d(TAG_METHOD+ "Entrée");
        boolean resultOK = true;
        boolean resultKO = false;
        if (!DiasporaConfig.COOKIE_AUTH.isEmpty()){
            LOG.d(TAG_METHOD + "Vous vous êtes déjà authentifié...");
            LOG.d(TAG_METHOD + "------- Rappel des paramètres -----------");
            LOG.d(TAG_METHOD + "\tDiasporaConfig.COOKIE_AUTH : "+DiasporaConfig.COOKIE_AUTH);
            LOG.d(TAG_METHOD + "\tDiasporaConfig.AUTHENTICITY_TOKEN : "+DiasporaConfig.AUTHENTICITY_TOKEN);
            LOG.d(TAG_METHOD + "\tDiasporaConfig.X_CSRF_TOKEN : "+DiasporaConfig.X_CSRF_TOKEN);
            LOG.d(TAG_METHOD + "-----------------------------------------");
            LOG.d(TAG_METHOD+ "Sortie");
            return resultOK;
        }
        boolean getTokenOK = false;
        try {
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            getTokenOK = getToken(diasporaService.getLoginHTML());
        }catch(Throwable thr){
            LOG.e(TAG_METHOD + "Token authenticity non obtenu, pour cause d'erreur " + thr.getMessage(),thr);
        }
        LOG.d(TAG_METHOD+ "Token authenticity obtenu ? "+getTokenOK);
        if (!getTokenOK){
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            return resultKO;
        }
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        String loggued = diasporaService.postLogin();

        boolean loginOK = "<html><body>You are being <a href=\"https://framasphere.org/stream\">redirected</a>.</body></html>".equals(loggued);
        LOG.d(TAG_METHOD + "Login obtenu ? " + loginOK);

        if (!loginOK){
            LOG.d(TAG_METHOD + "Sortie en erreur");
            return resultKO;
        }

        getTokenOK = false;
        try {
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            getTokenOK = getCsrfToken(diasporaService.getBookmarkletHTML());
        }catch(Throwable thr){
            LOG.e(TAG_METHOD + "Token crsf non obtenu, pour cause d'erreur " + thr.getMessage());
        }
        LOG.d(TAG_METHOD+ "Token crsf obtenu ? "+getTokenOK);
        if (!getTokenOK){
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            return resultKO;
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return resultOK;
    }

    public Post reshare(String rootGuid){
        String TAG_METHOD = TAG + ".reshare : ";
        LOG.d(TAG_METHOD + "Entrée");
        Post reponseReshare=null;
        boolean logged = seLogguer();
        if (logged) {
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaConfig.X_CSRF_TOKEN != null && !DiasporaConfig.X_CSRF_TOKEN.isEmpty()) {
                diasporaService.setHeader("x-csrf-token", DiasporaConfig.X_CSRF_TOKEN);
                JsonObject jsonParam = new JsonObject();
                LOG.d(TAG_METHOD + "Ajout du root_guid=" + rootGuid);
                jsonParam.addProperty("root_guid", rootGuid);
                diasporaService.setRootUrl(DiasporaConfig.POD_URL);
                reponseReshare = diasporaService.reshare(jsonParam);
                LOG.d(TAG_METHOD + "réponse is null ? : " + (reponseReshare==null));
                if (reponseReshare!=null) {
                    LOG.d(TAG_METHOD + "réponse : " + reponseReshare.toString());
                }
            }
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return reponseReshare;
    }

    public Post like(Integer postID){
        String TAG_METHOD = TAG + ".like : ";
        LOG.d(TAG_METHOD + "Entrée");
        Post reponseLike=null;
        boolean logged = seLogguer();
        if (logged) {
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaConfig.X_CSRF_TOKEN != null && !DiasporaConfig.X_CSRF_TOKEN.isEmpty()) {
                diasporaService.setHeader("x-csrf-token", DiasporaConfig.X_CSRF_TOKEN);
                diasporaService.setRootUrl(DiasporaConfig.POD_URL);
                reponseLike = diasporaService.like(postID);
                LOG.d(TAG_METHOD + "réponse is null ? : " + (reponseLike==null));
                if (reponseLike!=null) {
                    LOG.d(TAG_METHOD + "réponse : " + reponseLike.toString());
                }
            }
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return reponseLike;
    }

    public Post getPost(Integer postID){
        String TAG_METHOD = TAG + ".like : ";
        LOG.d(TAG_METHOD + "Entrée");
        Post reponseGetPost=null;
        boolean logged = seLogguer();
        if (logged) {
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaConfig.X_CSRF_TOKEN != null && !DiasporaConfig.X_CSRF_TOKEN.isEmpty()) {
                diasporaService.setHeader("x-csrf-token", DiasporaConfig.X_CSRF_TOKEN);
                diasporaService.setRootUrl(DiasporaConfig.POD_URL);
                reponseGetPost = diasporaService.getPost(postID);
                LOG.d(TAG_METHOD + "réponse is null ? : " + (reponseGetPost==null));
                if (reponseGetPost!=null) {
                    LOG.d(TAG_METHOD + "réponse : " + reponseGetPost.toString());
                }
            }
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return reponseGetPost;
    }



    public Post comment(Integer postID, String text){
        String TAG_METHOD = TAG + ".like : ";
        LOG.d(TAG_METHOD + "Entrée");
        Post reponseComment=null;
        boolean logged = seLogguer();
        if (logged) {
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaConfig.X_CSRF_TOKEN != null && !DiasporaConfig.X_CSRF_TOKEN.isEmpty()) {
                diasporaService.setHeader("x-csrf-token", DiasporaConfig.X_CSRF_TOKEN);
                JsonObject jsonParam = new JsonObject();
                jsonParam.addProperty("text", text);
                diasporaService.setRootUrl(DiasporaConfig.POD_URL);
                reponseComment = diasporaService.comment(postID, jsonParam);
                LOG.d(TAG_METHOD + "réponse : " + reponseComment.toString());
            }
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return reponseComment;
    }

    public Post sendPost(NewPost newPost){
        String TAG_METHOD = TAG + ".sendPost : ";
        LOG.d(TAG_METHOD + "Entrée");

        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaConfig.X_CSRF_TOKEN!=null && !DiasporaConfig.X_CSRF_TOKEN.isEmpty()){
                diasporaService.setHeader("x-csrf-token", DiasporaConfig.X_CSRF_TOKEN);
            }
            diasporaService.setHeader("content-type", "application/json");

            LOG.d(TAG_METHOD + "call diasporaService.uploadFile");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            Post postResult = diasporaService.post(newPost);
            LOG.d(TAG_METHOD+ "postResult is null ? "+(postResult==null));
            LOG.d(TAG_METHOD+ "Sortie");
            return postResult;
        }
        LOG.d(TAG_METHOD + "logged failure");
        return null;
    }

    public UploadResult uploadFile(byte[] photoBytes){
        String TAG_METHOD = TAG + ".uploadFile : ";
        LOG.d(TAG_METHOD + "Entrée");

        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaConfig.X_CSRF_TOKEN!=null && !DiasporaConfig.X_CSRF_TOKEN.isEmpty()){
                diasporaService.setHeader("x-csrf-token", DiasporaConfig.X_CSRF_TOKEN);
            }
            diasporaService.setHeader("content-type", "application/octet-stream");
            LOG.d(TAG_METHOD + "call diasporaService.uploadFile");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            UploadResult uploadResult = diasporaService.uploadFile(photoBytes);
            LOG.d(TAG_METHOD+ "uploadResult is null ? "+(uploadResult==null));
            if (uploadResult!=null){
                LOG.d(TAG_METHOD+ "uploadResult is success ? "+uploadResult.getSuccess());
                LOG.d(TAG_METHOD+ "uploadResult is error ? "+uploadResult.getError());
            }
            LOG.d(TAG_METHOD+ "Sortie");
            return uploadResult;
        }
        LOG.d(TAG_METHOD + "logged failure");
        return null;
    }

    public UploadResult uploadFile(String localPath){
        String TAG_METHOD = TAG + ".uploadFile : ";
        LOG.d(TAG_METHOD + "Entrée");

        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "logged successfully");
            if (DiasporaConfig.X_CSRF_TOKEN!=null && !DiasporaConfig.X_CSRF_TOKEN.isEmpty()){
                diasporaService.setHeader("x-csrf-token", DiasporaConfig.X_CSRF_TOKEN);
            }
            diasporaService.setHeader("content-type", "application/octet-stream");
            LOG.d(TAG_METHOD + "On crée l'entité photo à partir des données brutes");
            final byte[] photoBytes = getImageBytes(localPath);
            LOG.d(TAG_METHOD+ "call diasporaService.uploadFile");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            UploadResult uploadResult = diasporaService.uploadFile(photoBytes);
            LOG.d(TAG_METHOD+ "uploadResult is null ? "+(uploadResult==null));
            if (uploadResult!=null){
                LOG.d(TAG_METHOD+ "uploadResult is success ? "+uploadResult.getSuccess());
                LOG.d(TAG_METHOD+ "uploadResult is error ? "+uploadResult.getError());

                LOG.d(TAG_METHOD+ "uploadResult url image ? "+uploadResult.getData().getPhoto().getUnprocessed_image());
            }
            LOG.d(TAG_METHOD+ "Sortie");
            return uploadResult;
        }
        LOG.d(TAG_METHOD + "logged failure");
        return null;
    }


    private byte[] getImageBytes(String filePath){
        try {
            InputStream is = new FileInputStream(filePath);
            byte[] bytes = IOUtils.toByteArray(is);
            return bytes;
        }catch(FileNotFoundException fnfe){
            return ("FileNotFoundException : "+fnfe.getMessage()).getBytes(Charset.forName("UTF-8"));
        }catch(IOException ioe){
            return ("IOException : "+ioe.getMessage()).getBytes(Charset.forName("UTF-8"));
        }
    }

    public User getInfo(String userName){
        String TAG_METHOD = TAG + ".getInfo : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD + "diasporaService.setRootUrl");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        LOG.d(TAG_METHOD + "appel de diasporaService.getInfo");
        User infos = diasporaService.getInfo(userName);
        LOG.d(TAG_METHOD+ "Sortie");
        return infos;
    }

    public List<Contact> getContacts(){
        String TAG_METHOD = TAG + ".getContacts : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "appel de diasporaService.getContacts");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Contact> contacts = diasporaService.getContacts();
            LOG.d(TAG_METHOD+ "Sortie");
            return contacts;
        }
        List<Contact> emptyError =  new ArrayList<Contact>();
        Contact empty = Contact.createContactErreur("L'authentification sur votre POD a échouée.\nVeuillez vérifier vos paramètres ou que votre POD n'est pas en opération de maintenace !");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }


    public List<Comment> getComments(Integer postID){
        String TAG_METHOD = TAG + ".getComments : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "appel de diasporaService.getComments");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Comment> comments = diasporaService.getComments(postID);
            LOG.d(TAG_METHOD+ "Sortie");
            return comments;
        }
        List<Comment> emptyError =  new ArrayList<Comment>();
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getStream(){
        String TAG_METHOD = TAG + ".getStream : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "appel de diasporaService.getStream");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getStream();
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = Post.createPostErreur(ERREUR_LOGIN);
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getMoreStream(long timestampStreamMax, long timestampStreamInit){
        String TAG_METHOD = TAG + ".getMoreStream : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "appel de diasporaService.getMoreStream");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getMoreStream(timestampStreamMax, timestampStreamInit);
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = Post.createPostErreur(ERREUR_LOGIN);
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getTagSuivis(){
        String TAG_METHOD = TAG + ".getTagSuivis : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getTagSuivis");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getTagSuivis();
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = Post.createPostErreur(ERREUR_LOGIN);
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getMoreTagSuivis(long timestampStreamMax, long timestampStreamInit){
        String TAG_METHOD = TAG + ".getMoreTagSuivis : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getMoreTagSuivis");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getMoreTagSuivis(timestampStreamMax, timestampStreamInit);
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = Post.createPostErreur(ERREUR_LOGIN);
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getActivity(){
        String TAG_METHOD = TAG + ".getActivity : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getActivity");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getActivity();
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = Post.createPostErreur(ERREUR_LOGIN);
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getMoreActivity(long timestampStreamMax, long timestampStreamInit){
        String TAG_METHOD = TAG + ".getMoreActivity : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getMoreActivity");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getMoreActivity(timestampStreamMax, timestampStreamInit);
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = Post.createPostErreur(ERREUR_LOGIN);
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public byte[] getImageFile(String fileUrl){
        String TAG_METHOD = TAG + ".getImageFile : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (fileUrl==null || fileUrl.isEmpty()){
            LOG.d(TAG_METHOD+ "Sortie en erreur fileUrl is null");
            return null;
        }
        if (fileUrl.startsWith("/")){
            fileUrl=DiasporaConfig.POD_URL + fileUrl;
        }
        diasporaService.setRootUrl(fileUrl);
        byte[] imageFile = diasporaService.getImageFile();

        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        LOG.d(TAG_METHOD+ "Sortie");
        return imageFile;
    }

    private boolean getToken(String response){
        String TAG_METHOD = TAG + ".getToken : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean resultOK = true;
        boolean resultKO = false;
        if (response==null || response.isEmpty()){
            return resultKO;
        }

        //Evolution Diaspora Code 0.5.0.1-p6a5597e2
        int indexToken = -1;
        int indexEndToken = -1;
//        //<input type="hidden" name="authenticity_token" value="UQxFDSvaZcTB6357CiajoZRN9/bKFM+tQNyBX+MiUNw93o84z426aHyxzgn+9nv3IHkVMRsTdotC3h1HJEnWjQ==">
        String tagBegin = "<input type=\"hidden\" name=\"authenticity_token\" value=\"";
        int indexTokenName = response.indexOf(tagBegin,0);
        if (indexTokenName>0) {
            indexToken = indexTokenName + tagBegin.length();
            indexEndToken = response.indexOf("\"", indexToken + 1);
                DiasporaConfig.AUTHENTICITY_TOKEN = response.substring(indexToken, indexEndToken);
                LOG.d(TAG_METHOD+ "\t**\tAUTHENTICITY_TOKEN récolté '" + DiasporaConfig.AUTHENTICITY_TOKEN + "'");
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return resultOK;
    }




    private boolean getCsrfToken(String response){
        String TAG_METHOD = TAG + ".getCsrfToken : ";
        LOG.d(TAG_METHOD + "Entrée");
        boolean resultOK = true;
        boolean resultKO = false;
        if (response==null || response.isEmpty()){
            return resultKO;
        }

        //Evolution Diaspora Code 0.5.0.1-p6a5597e2
        int indexToken = -1;
        int indexEndToken = -1;
//        //<input type="hidden" name="authenticity_token" value="UQxFDSvaZcTB6357CiajoZRN9/bKFM+tQNyBX+MiUNw93o84z426aHyxzgn+9nv3IHkVMRsTdotC3h1HJEnWjQ==">
        String tagBegin = "<input type=\"hidden\" name=\"authenticity_token\" value=\"";
        int indexTokenName = response.indexOf(tagBegin,0);
        if (indexTokenName>0) {
            indexToken = indexTokenName + tagBegin.length();
            indexEndToken = response.indexOf("\"", indexToken + 1);
                DiasporaConfig.X_CSRF_TOKEN = response.substring(indexToken, indexEndToken);
                LOG.d(TAG_METHOD+ "\t**\tX_CSRF_TOKEN récolté '" + DiasporaConfig.X_CSRF_TOKEN + "'");
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return resultOK;
    }

}
