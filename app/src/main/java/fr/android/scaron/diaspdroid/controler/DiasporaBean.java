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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import fr.android.scaron.diaspdroid.model.Contact;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.LikeResult;
import fr.android.scaron.diaspdroid.model.NewPost;
import fr.android.scaron.diaspdroid.model.Pod;
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

    private static DiasporaBean instance;
    @RootContext
    Context context;

    @RestService
    DiasporaService diasporaService;
    @RestService
    PodsService podsService;
    @Bean
    DiasporaErrorHandlerBean diasporaErrorHandlerBean;

    Long timestampStreamMax;
    Long timestampStreamInit;

    private static void setInstance(DiasporaBean instance){
        DiasporaBean.instance = instance;
    }
    public static DiasporaBean getInstance(){
        return DiasporaBean.instance;
    }

    public void setRootUrl(final String rootUrl){
        diasporaService.setRootUrl(rootUrl);
    }

    @AfterInject
    public void init(){
        setInstance(this);
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
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean getTokenOK = getToken(diasporaService.getLoginHTML());
        LOG.d(TAG_METHOD+ "Token crsf obtenu ? "+getTokenOK);
        if (!getTokenOK){
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            return resultKO;
        }
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        String loggued = diasporaService.postLogin();

        LOG.d(TAG_METHOD+ "Login obtenu ? "+loggued);
        LOG.d(TAG_METHOD+ "Sortie");
        return resultOK;
    }

    public Post reshare(String rootGuid){
        String TAG_METHOD = TAG + ".reshare : ";
        LOG.d(TAG_METHOD+ "Entrée");
        Post reponseReshare=null;
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged) {
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaControler.TOKEN != null && !DiasporaControler.TOKEN.isEmpty()) {
                diasporaService.setHeader("x-csrf-token", DiasporaControler.TOKEN);
                JsonObject jsonParam = new JsonObject();
                LOG.d(TAG_METHOD + "Ajout du root_guid=" + rootGuid);
                jsonParam.addProperty("root_guid", rootGuid);
                reponseReshare = diasporaService.reshare(jsonParam);
               LOG.d(TAG_METHOD + "réponse : "+reponseReshare.toString());
            }
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return reponseReshare;
    }

    public LikeResult like(Integer postID){
        String TAG_METHOD = TAG + ".like : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LikeResult reponseLike=null;
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged) {
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaControler.TOKEN != null && !DiasporaControler.TOKEN.isEmpty()) {
                diasporaService.setHeader("x-csrf-token", DiasporaControler.TOKEN);
                reponseLike = diasporaService.like(postID);
                LOG.d(TAG_METHOD + "réponse : " + reponseLike.toString());
            }
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return reponseLike;
    }

    public Post sendPost(NewPost newPost){
        String TAG_METHOD = TAG + ".sendPost : ";
        LOG.d(TAG_METHOD+ "Entrée");

        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "logged successfully");
            if (DiasporaControler.TOKEN!=null && !DiasporaControler.TOKEN.isEmpty()){
                diasporaService.setHeader("x-csrf-token", DiasporaControler.TOKEN);
            }
            diasporaService.setHeader("content-type", "application/json");

            LOG.d(TAG_METHOD+ "call diasporaService.uploadFile");
            Post postResult = diasporaService.post(newPost);
            LOG.d(TAG_METHOD+ "postResult is null ? "+(postResult==null));
            LOG.d(TAG_METHOD+ "Sortie");
            return postResult;
        }
        LOG.d(TAG_METHOD+ "logged failure");
        return null;
    }



    public UploadResult uploadFile(String localPath){
        String TAG_METHOD = TAG + ".uploadFile : ";
        LOG.d(TAG_METHOD+ "Entrée");

        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "logged successfully");
            if (DiasporaControler.TOKEN!=null && !DiasporaControler.TOKEN.isEmpty()){
                diasporaService.setHeader("x-csrf-token", DiasporaControler.TOKEN);
            }
            diasporaService.setHeader("content-type", "application/octet-stream");
            LOG.d(TAG_METHOD + "On crée l'entité photo à partir des données brutes");
            final byte[] photoBytes = getImageBytes(localPath);
            LOG.d(TAG_METHOD+ "call diasporaService.uploadFile");
            UploadResult uploadResult = diasporaService.uploadFile(photoBytes);
            LOG.d(TAG_METHOD+ "uploadResult is null ? "+(uploadResult==null));
            if (uploadResult!=null){
                LOG.d(TAG_METHOD+ "uploadResult is success ? "+uploadResult.getSuccess());
                LOG.d(TAG_METHOD+ "uploadResult is error ? "+uploadResult.getError());
            }
            LOG.d(TAG_METHOD+ "Sortie");
            return uploadResult;
        }
        LOG.d(TAG_METHOD+ "logged failure");
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

    public List<Post> getInfo(String userName){
        String TAG_METHOD = TAG + ".getInfo : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD + "diasporaService.setRootUrl");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        LOG.d(TAG_METHOD + "appel de diasporaService.getInfo");
        List<Post> infos = diasporaService.getInfo(userName);
        LOG.d(TAG_METHOD+ "Sortie");
        return infos;
    }

    public List<Contact> getContacts(){
        String TAG_METHOD = TAG + ".getContacts : ";
        LOG.d(TAG_METHOD+ "Entrée");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "appel de diasporaService.getContacts");
            List<Contact> contacts = diasporaService.getContacts();
            LOG.d(TAG_METHOD+ "Sortie");
            return contacts;
        }
        List<Contact> emptyError =  new ArrayList<Contact>();
        Contact empty = new Contact();
        empty.setName("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getStream(){
        String TAG_METHOD = TAG + ".getStream : ";
        LOG.d(TAG_METHOD+ "Entrée");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD + "appel de diasporaService.getStream");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getStream();
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = new Post();
        empty.setText("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getMoreStream(long timestampStreamMax, long timestampStreamInit){
        String TAG_METHOD = TAG + ".getMoreStream : ";
        LOG.d(TAG_METHOD+ "Entrée");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getMoreStream");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getMoreStream(timestampStreamMax, timestampStreamInit);
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = new Post();
        empty.setText("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getTagSuivis(){
        String TAG_METHOD = TAG + ".getTagSuivis : ";
        LOG.d(TAG_METHOD+ "Entrée");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getTagSuivis");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getTagSuivis();
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = new Post();
        empty.setText("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getMoreTagSuivis(long timestampStreamMax, long timestampStreamInit){
        String TAG_METHOD = TAG + ".getMoreTagSuivis : ";
        LOG.d(TAG_METHOD+ "Entrée");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getMoreTagSuivis");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getMoreTagSuivis(timestampStreamMax, timestampStreamInit);
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = new Post();
        empty.setText("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getActivity(){
        String TAG_METHOD = TAG + ".getActivity : ";
        LOG.d(TAG_METHOD + "Entrée");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getActivity");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getActivity();
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = new Post();
        empty.setText("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public List<Post> getMoreActivity(long timestampStreamMax, long timestampStreamInit){
        String TAG_METHOD = TAG + ".getMoreActivity : ";
        LOG.d(TAG_METHOD+ "Entrée");
        diasporaService.setRootUrl(DiasporaConfig.POD_URL);
        boolean logged = seLogguer();
        if (logged){
            LOG.d(TAG_METHOD+ "appel de diasporaService.getMoreActivity");
            diasporaService.setRootUrl(DiasporaConfig.POD_URL);
            List<Post> posts = diasporaService.getMoreActivity(timestampStreamMax, timestampStreamInit);
            LOG.d(TAG_METHOD+ "Sortie");
            return posts;
        }
        List<Post> emptyError =  new ArrayList<Post>();
        Post empty = new Post();
        empty.setText("Aucune réponse n'a été trouvée");
        emptyError.add(empty);
        LOG.d(TAG_METHOD + "Sortie en erreur de login");
        return emptyError;
    }

    public byte[] getImageFile(String fileUrl){
        String TAG_METHOD = TAG + ".getImageFile : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (fileUrl.startsWith("/")){
            fileUrl=DiasporaConfig.POD_URL + fileUrl;
        }
        diasporaService.setRootUrl(fileUrl);
        byte[] imageFile = diasporaService.getImageFile();
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

        int indexTokenName = response.indexOf("<meta content=\"authenticity_token\" name=\"csrf-param\" />",0);
        if (indexTokenName<=0) {
            LOG.d(TAG_METHOD+ "\t**\tIMPOSSIBLE de trouver le token");
            LOG.d(TAG_METHOD+ "Sortie");
            return resultKO;
        }
        int indexToken = response.indexOf("<meta content=\"", indexTokenName + 1);
        LOG.d(TAG_METHOD+ "**	token found in "+response.substring(indexToken, response.indexOf("/>", indexToken)));
        indexToken = indexToken+"<meta content=\"".length();
        int indexEndToken = response.indexOf("\" name=\"csrf-token\"", indexToken + 1);
        if (DiasporaControler.TOKEN.isEmpty()) {
            DiasporaControler.TOKEN = response.substring(indexToken, indexEndToken);
            LOG.d(TAG_METHOD+ "\t**\ttoken récolté '" + DiasporaControler.TOKEN + "'");
        }else{
            LOG.d(TAG_METHOD+ "\t**\ttoken déjà récolté '" + DiasporaControler.TOKEN + "'");
        }
        LOG.d(TAG_METHOD+ "Sortie");
        return resultOK;
    }

    private void calcTimestampStreamInit(){
        String TAG_METHOD = TAG + ".calcTimestampStreamInit : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (timestampStreamInit ==null) {
            Calendar cal = GregorianCalendar.getInstance();
//          cal.set(Calendar.DAY_OF_MONTH, 23);// I might have the wrong Calendar constant...
//          cal.set(Calendar.MONTH, 8);// -1 as month is zero-based
//          cal.set(Calendar.YEAR, 2009);
            Timestamp tstamp = new Timestamp(cal.getTimeInMillis());
            timestampStreamInit = Long.valueOf(""+tstamp.getTime()/1000);
            LOG.d(TAG_METHOD + "timestampStreamInit calculated : "+ timestampStreamInit);
        }else{
            LOG.d(TAG_METHOD + "timestampStreamInit stored : "+ timestampStreamInit);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    private void calcTimestampStreamMax(){
        String TAG_METHOD = TAG + ".calcTimestampStreamMax : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (timestampStreamMax ==null){
            timestampStreamMax = timestampStreamInit - 3600 * 10;
        }else{
            timestampStreamMax = timestampStreamMax - 3600 * 10;
        }
        LOG.d(TAG_METHOD + "timestampStreamMax calculated : "+ timestampStreamMax);
        LOG.d(TAG_METHOD + "Sortie");
    }

}
