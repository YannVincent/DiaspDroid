package fr.android.scaron.diaspdroid.activity;

import android.app.ListActivity;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.TinyDB;

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

    @ViewById
    ListView pod_listview;
//    ExpandableListView pod_listview;

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
    void setupAdapterListPod(){
        String methodName = ".setupAdapterListPod : ";
        LOG.d(methodName+ "Entrée");
        final ArrayList<String> list = new ArrayList<String>();
        LOG.d(methodName+ "add values in list");
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        LOG.d(methodName+ "create adapter for list with android.R.layout.simple_list_item_1");
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        LOG.d(methodName+ "create setAdapter in post_listview");
        pod_listview.setAdapter(adapter);
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
