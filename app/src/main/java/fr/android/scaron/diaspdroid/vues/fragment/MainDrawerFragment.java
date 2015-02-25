package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.ActivityManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.acra.ACRA;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.MainApplication;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;

/**
 * Created by Sébastien on 25/02/2015.
 */
@EFragment(R.layout.main_drawer_fragment)
public class MainDrawerFragment extends ListFragment {
    private static Logger LOGGEUR = LoggerFactory.getLogger(MainDrawerFragment_.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);



    @ViewById(value=android.R.id.list)
    ListView mDrawerListView;

    @App
    MainApplication mainApplication;

    @SystemService
    ActivityManager activityManager;


//    boolean forceLayoutInjection=true;
    String[] sections = {
            getString(R.string.title_section1),
            getString(R.string.title_section2),
            getString(R.string.title_section3),
            getString(R.string.title_section4),
    };

    boolean	listItemClicked = false;
    ArrayAdapter<CharSequence> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);

            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.drawer_array, android.R.layout.simple_list_item_1);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mDrawerListView.setAdapter(adapter);
        }catch(Throwable thr) {
            LOG.e("Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @ItemClick
    void listItemClicked(String string) {
        listItemClicked  = true;
    }

////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        return null;
////    }
//    @AfterViews
//    public void init(){
//        try {
//            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, sections);
//            mDrawerListView.setAdapter(adapter);
//        }catch(Throwable thr) {
//                LOG.e("Erreur : " + thr.toString());
//                ACRA.getErrorReporter().handleException(thr);
//                throw thr;
//        }
//    }

}
