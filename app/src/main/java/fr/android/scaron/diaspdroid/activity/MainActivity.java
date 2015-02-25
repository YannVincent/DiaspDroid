package fr.android.scaron.diaspdroid.activity;

import android.app.Activity;
import android.util.Log;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.vues.fragment.MainDrawerFragment;

/**
 * Created by Sébastien on 25/02/2015.
 */
@EActivity(R.layout.main_activity)
public class MainActivity extends Activity {
    private static Logger LOGGEUR = LoggerFactory.getLogger(MainActivity_.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);

    @FragmentById(R.id.drawerFragment)
    MainDrawerFragment drawerFragement;

    @AfterViews
    void init(){
        try{
            DiasporaConfig.addActivity(this);
            DiasporaConfig.init(this.getApplication(), this);
        }catch(Throwable thr) {
            LOG.e("Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
    @Trace(tag="MainActivity",level= Log.DEBUG)
    void trace(){

    }
}
