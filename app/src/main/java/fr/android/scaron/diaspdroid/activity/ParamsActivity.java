package fr.android.scaron.diaspdroid.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.CookieControler;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.GroupList;
import fr.android.scaron.diaspdroid.model.Pods;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.model.TinyDB;
import fr.android.scaron.diaspdroid.vues.adapter.PodListViewAdapter;

/**
 * Created by Sébastien on 20/02/2015.
 */
@EActivity(R.layout.pod_list)
public class ParamsActivity extends ActionBarActivity {
    private static Logger LOGGEUR = LoggerFactory.getLogger(ParamsActivity.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);

    private TinyDB myDB;
    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };


    SparseArray<GroupList> groups = new SparseArray<GroupList>();
    PodListViewAdapter podListViewAdapter;

    @ViewById(R.id.pod_listview)
    ExpandableListView pod_listview;
    @ViewById(R.id.poduser)
    TextView poduser;
    @ViewById(R.id.podpassword)
    TextView podpassword;
    @ViewById(R.id.podselected)
    TextView podselected;

    @Click(R.id.podConfigOK)
    void podConfigOKClicked() {
        String methodName = ".updateBookmarksClicked : ";
        LOG.d(methodName+ "Entrée");
        final String user=poduser.getText().toString();
        final String password=podpassword.getText().toString();
        final String url=podselected.getText().toString();
        DiasporaConfig.setPodAuthenticationValues(url, user, password);
        DiasporaControler.testerConnexion();
        LOG.d(methodName+ "Sortie");
    }


//    ListView pod_listview;

    @AfterViews
    void updateBarIcon() {
        String methodName = ".updateBarIcon : ";
        LOG.d(methodName+ "Entrée");
        LOG.d(methodName+ "getActionBar");
        ActionBar actionBar = getActionBar();
        if (actionBar!=null) {
            LOG.d(methodName+ "actionBar.setTitle");
            actionBar.setTitle("Paramètres");
        }
        LOG.d(methodName+ "Entrée");
    }

    @AfterViews
    void setupListPod(){

        DiasporaConfig.init(this.getApplication(), this);
        String methodName = ".setupListPod : ";

        LOG.d(methodName+ "create adapter for list with PodListViewAdapter");
        podListViewAdapter = new PodListViewAdapter(this,  groups, pod_listview);
        LOG.d(methodName+ "create setAdapter in post_listview");
        pod_listview.setAdapter(podListViewAdapter);
        if (DiasporaConfig.POD_URL!=null && !DiasporaConfig.POD_URL.isEmpty()){
            podselected.setText(DiasporaConfig.POD_URL);
        }
        if (DiasporaConfig.POD_USER!=null && !DiasporaConfig.POD_USER.isEmpty()){
            poduser.setText(DiasporaConfig.POD_USER);
        }
        if (DiasporaConfig.POD_PASSWORD!=null && !DiasporaConfig.POD_PASSWORD.isEmpty()){
            podpassword.setText(DiasporaConfig.POD_PASSWORD);
        }

        //Restore PODList from memory
        if (DiasporaConfig.POD_LIST_JSON!=null && !DiasporaConfig.POD_LIST_JSON.isEmpty()&&
            DiasporaConfig.POD_LIST!=null && DiasporaConfig.POD_LIST.getPods()!=null && DiasporaConfig.POD_LIST.getPodcount()>0){
            LOG.d(methodName + "Chargement des pods en mémoire");
            podListViewAdapter.addGroup(DiasporaConfig.POD_LIST);
            LOG.d(methodName + "Sortie");
            return;
        }

        //Callback de la récupération du flux
        final FutureCallback<Response<Pods>> podListCallback = new FutureCallback<Response<Pods>>() {
            @Override
            public void onCompleted(Exception e, Response<Pods> podsResponse) {

                String methodName = ".setupListPod podListCallback onCompleted : ";
                LOG.d(methodName+ "exception ? " + e);
                if (e!=null){
                    e.printStackTrace();
                }
                if (e!=null) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ParamsActivity.this);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Données");
                    alertDialog.setMessage("La récupétion de la liste des pods a échouée");
                    alertDialog.show();
                    LOG.e(methodName + "Erreur : " + e.toString());
                    if (e.getCause()!=null) {
                        LOG.e(methodName+ "cause exception ? " + e.getCause().getMessage());
                    }

                }
                LOG.d(methodName+ "podsResponse ? " + podsResponse);
                if (podsResponse!=null){
                    Pods pods = podsResponse.getResult();

                    LOG.e(methodName + "Pods ? : " + pods);
                    pods.setName("Liste des Pods");
                    LOG.e(methodName + "Pods.pods ? : " + pods.getPods());
                    if (pods.getPods()!=null) {
                        pods.setPodcount(pods.getPods().size());
                        pods.children.clear();
                        pods.children.addAll(pods.getPods());
                        DiasporaConfig.setPods(pods);
                        LOG.e(methodName + "Nombre de pods trouvés : " + pods.getPodcount());
                    }
                    podListViewAdapter.addGroup(pods);
//                    groups.append(0, pods);
//                    GroupList group = new Pods("Liste des Pods");
//                    group.children.addAll(pods.getPods());
//                    groups.append(0, group);
//                    podListViewAdapter.notifyDataSetChanged();
                    return;
                }
                ACRA.getErrorReporter().handleException(e);
            }
        };

        DiasporaControler.getPodList(podListCallback);
        LOG.d(methodName + "Sortie");
    }


//    @ItemClick(R.id.pod_listview)
//    void listItemClicked(String food) {
//        String methodName = ".listItemClicked : ";
//        LOG.d(methodName+ "Entrée");
//        LOG.d(methodName+ "Create Toast message for selected '"+food+"'");
//        Toast.makeText(this, "click: " + food, Toast.LENGTH_SHORT).show();
//        LOG.d(methodName + "Sortie");
//    }
}
