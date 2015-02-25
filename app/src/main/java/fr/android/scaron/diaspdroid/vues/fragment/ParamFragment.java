package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.ActivityManager;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.DiaspDroid;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.GroupList;
import fr.android.scaron.diaspdroid.vues.adapter.PodListViewAdapter;

/**
 * Created by Sébastien on 25/02/2015.
 */
@EFragment(R.layout.pod_list)
public class ParamFragment extends Fragment {
    private static Logger LOGGEUR = LoggerFactory.getLogger(ParamFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);

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
    @App
    DiaspDroid customApplication;

    @SystemService
    ActivityManager activityManager;

    @AfterInject
    void calledAfterInjection() {
    }

    @AfterViews
    void calledAfterViewInjection() {
    }

    @Receiver(actions = "org.androidannotations.ACTION_1")
    protected void onAction1() {

    }
}
