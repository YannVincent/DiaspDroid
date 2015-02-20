package fr.android.scaron.diaspdroid.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.CookieControler;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.GroupList;
import fr.android.scaron.diaspdroid.model.Pods;
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
        String methodName = ".setupListPod : ";
        LOG.d(methodName+ "Entrée");



//        final ArrayList<String> list = new ArrayList<String>();
//        LOG.d(methodName+ "add values in list");
//        for (int i = 0; i < values.length; ++i) {
//            list.add(values[i]);
//        }
//        LOG.d(methodName+ "create adapter for list with android.R.layout.simple_list_item_1");
//        final ArrayAdapter adapter = new ArrayAdapter(this,
//                android.R.layout.simple_list_item_1, list);

        LOG.d(methodName+ "create adapter for list with PodListViewAdapter");
        podListViewAdapter = new PodListViewAdapter(this,  groups);
        LOG.d(methodName+ "create setAdapter in post_listview");
        pod_listview.setAdapter(podListViewAdapter);

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
                    pods.setName("Liste des Pods");
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

        CookieControler cookieControler = CookieControler.getInstance(ParamsActivity.this);
        cookieControler.clearCookies();

        DiasporaControler.getPodList(ParamsActivity.this, podListCallback);
        LOG.d(methodName + "Sortie");
    }


    @ItemClick(R.id.pod_listview)
    void listItemClicked(String food) {
        String methodName = ".listItemClicked : ";
        LOG.d(methodName+ "Entrée");
        LOG.d(methodName+ "Create Toast message for selected '"+food+"'");
        Toast.makeText(this, "click: " + food, Toast.LENGTH_SHORT).show();
        LOG.d(methodName + "Sortie");
    }
}
