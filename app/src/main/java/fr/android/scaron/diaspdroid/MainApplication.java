package fr.android.scaron.diaspdroid;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.androidannotations.annotations.EApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.controler.LogControler;


@ReportsCrashes(
        formKey = "",
        formUri = "https://collector.tracepot.com/301ca578",
        applicationLogFile = "/sdcard/Android/data/fr.scaron.diaspdroid/logs/mainapplication.log",
        logcatArguments = { "-t", "100", "-v", "long", "ActivityManager:I", "DiaspDroid:D", "*:S" }
)
@EApplication
public class MainApplication extends Application {
    private static Logger LOGGEUR = LoggerFactory.getLogger(MainApplication.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    @Override
    public void onCreate() {
        try {
            super.onCreate();
            ACRA.init(this);
            ACRA.getErrorReporter().checkReportsOnApplicationStart();
        } catch (Throwable thr) {
            LOG.e("Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;

        }
    }
}
