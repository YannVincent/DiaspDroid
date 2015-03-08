package fr.android.scaron.diaspdroid.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.vues.fragment.FluxFrag_;
import fr.android.scaron.diaspdroid.vues.fragment.ParamsFragment_;

/**
 * Created by Sébastien on 25/02/2015.
 */
@EActivity(R.layout.diaspora_main)
public class MainActivity extends ActionBarActivity {
    private static Logger LOGGEUR = LoggerFactory.getLogger(MainActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static final String TAG = "MainActivity";

    boolean listItemClicked=false;
    String[] drawerArray;
    private List<String> drawerItems;

    @ViewById(R.id.diaspora_main)
    DrawerLayout diasporaMain;

    @ViewById(R.id.diaspora_main_drawer)
    ListView diasporaMainDrawer;

    @ViewById(R.id.diaspora_main_content)
    FrameLayout diaporaMainContent;

    @AfterViews
    void init(){
        String TAG_METHOD = ".init -> ";
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LOG.d(TAG + TAG_METHOD + "entrée");
        try{
            LOG.d(TAG + TAG_METHOD + "Add Activity in Config : " + this);
            DiasporaConfig.addActivity(this);
            LOG.d(TAG + TAG_METHOD + "Init Config with application and context");
            DiasporaConfig.init(this.getApplication(), this);

            drawerArray = getResources().getStringArray(R.array.drawer_array);
            drawerItems = Arrays.asList(drawerArray);
            // Set the adapter for the list view
            diasporaMainDrawer.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, drawerArray));
            //On selectionne la vue Flux par défaut
            listItemClicked(drawerItems.get(0));
        }catch(Throwable thr) {
            LOG.e(TAG + TAG_METHOD + "Erreur : " + thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);

            throw thr;
        }
    }
    @Trace(tag="MainActivity",level= Log.DEBUG)
    void trace(){
        String TAG_METHOD = ".trace -> ";
        LOG.d(TAG + TAG_METHOD + "Test @Trace with Tag and Level");
    }

    @ItemClick(R.id.diaspora_main_drawer)
    void listItemClicked(String itemClicked) {
        String TAG_METHOD = ".listItemClicked -> ";
        LOG.d(TAG + TAG_METHOD + "list item clicked ? "+itemClicked);
        listItemClicked  = true;
        int itemPosition = drawerItems.indexOf(itemClicked);
        diasporaMainDrawer.setItemChecked(itemPosition, true);
        setTitle(itemClicked);
        switch (itemPosition){
            case 0 : //Flux
                setFluxFragment(itemClicked, itemPosition);
                break;
            case 1 : //Mes Amis
                break;
            case 2 : //Mon Profil
                break;
            case 3 : //Paramètres
                setParamsFragment(itemClicked, itemPosition);
                break;
            default :
                break;
        }
        diasporaMain.closeDrawer(diasporaMainDrawer);
    }

    void setFluxFragment(String title, int position){
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, new FluxFrag_())
                .commit();
    }
    void setParamsFragment(String title, int position){
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, new ParamsFragment_())
                .commit();
    }


}