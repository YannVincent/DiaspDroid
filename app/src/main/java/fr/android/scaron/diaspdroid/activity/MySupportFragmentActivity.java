package fr.android.scaron.diaspdroid.activity;

import android.support.v4.app.FragmentActivity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.FragmentByTag;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.vues.fragment.MyListFragment;
import fr.android.scaron.diaspdroid.vues.fragment.MySupportFragment;
import fr.android.scaron.diaspdroid.vues.fragment.beans.BeanWithSupportFragments;

/**
 * Created by Sébastien on 26/02/2015.
 */
@EActivity(R.layout.support_fragments)
public class MySupportFragmentActivity extends FragmentActivity {

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

    @Bean
    public BeanWithSupportFragments beanWithSupportFragments;

}