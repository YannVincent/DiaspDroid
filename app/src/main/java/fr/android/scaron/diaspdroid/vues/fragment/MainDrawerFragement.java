package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.ActivityManager;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import fr.android.scaron.diaspdroid.R;

/**
 * Created by Sébastien on 25/02/2015.
 */
@EFragment(R.layout.fragment_navigation_diasp_drawer)
public class MainDrawerFragement extends Fragment{

    String[] sections = {
            getString(R.string.title_section1),
            getString(R.string.title_section2),
            getString(R.string.title_section3),
            getString(R.string.title_section4),
    };

    ArrayAdapter<String> adapter;

    @ViewById(R.id.drawerList)
    ListView mDrawerListView;

    @SystemService
    ActivityManager activityManager;

    @AfterInject
    public void init(){
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, sections);
        mDrawerListView.setAdapter(adapter);
    }

}
