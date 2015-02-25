package fr.android.scaron.diaspdroid.activity;

import android.support.v4.app.FragmentActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.vues.fragment.MainDrawerFragement;

/**
 * Created by Sébastien on 25/02/2015.
 */
@EActivity(R.layout.activity_diasp)
public class MainActivity extends FragmentActivity{
    private static Logger LOGGEUR = LoggerFactory.getLogger(MainActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);

    @FragmentById(R.id.navigation_drawer)
    MainDrawerFragement drawerFragement;

    @AfterViews
    void init(){
        DiasporaConfig.addActivity(this);
        DiasporaConfig.init(this.getApplication(), this);
    }
}
