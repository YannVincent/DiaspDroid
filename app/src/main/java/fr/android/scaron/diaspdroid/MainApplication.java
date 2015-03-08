package fr.android.scaron.diaspdroid;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.androidannotations.annotations.EApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.controler.LogControler;


@EApplication
@ReportsCrashes(
        formUri = "https://collector.tracepot.com/301ca578",
        logcatFilterByPid = true,
//        mode = ReportingInteractionMode.DIALOG,
//        applicationLogFile = "/sdcard/Android/data/fr.scaron.diaspdroid/logs/mainapplication.log",
        logcatArguments = { "-t", "100", "-v", "long", "ActivityManager:I", "MyApp:D", "*:S" }
//        logcatArguments = { "-t", "100", "-v", "long", "ActivityManager:I", "*:S" }
)
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
//        ACRA.getErrorReporter().checkReportsOnApplicationStart();
    }

    @Override
    public void onTerminate() {
        ACRA.getErrorReporter().checkReportsOnApplicationStart();
    }
}
