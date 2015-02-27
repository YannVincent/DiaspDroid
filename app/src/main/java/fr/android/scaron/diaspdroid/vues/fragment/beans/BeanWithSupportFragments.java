package fr.android.scaron.diaspdroid.vues.fragment.beans;

/**
 * Created by Sébastien on 26/02/2015.
 */

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.FragmentByTag;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.vues.fragment.MyListFragment;
import fr.android.scaron.diaspdroid.vues.fragment.MySupportFragment;

@EBean
public class BeanWithSupportFragments {

    @FragmentById
    public MySupportFragment mySupportFragment;

    @FragmentById(R.id.mySupportFragment)
    public MySupportFragment mySupportFragment2;

    @FragmentByTag
    public MySupportFragment mySupportFragmentTag;

    @FragmentByTag("mySupportFragmentTag")
    public MySupportFragment mySupportFragmentTag2;



    @FragmentById
    public MyListFragment myListFragment;

    @FragmentById(R.id.myListFragment)
    public MyListFragment myListFragment2;

    @FragmentByTag
    public MyListFragment myListFragmentTag;

    @FragmentByTag("myListFragmentTag")
    public MyListFragment myListFragmentTag2;

}
