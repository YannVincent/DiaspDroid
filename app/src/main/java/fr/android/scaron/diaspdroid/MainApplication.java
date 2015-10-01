package fr.android.scaron.diaspdroid;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.androidannotations.annotations.EApplication;


@ReportsCrashes(
        formUri = "https://collector.tracepot.com/301ca578",
        logcatFilterByPid = true,
//        mode = ReportingInteractionMode.DIALOG,
//        applicationLogFile = "/sdcard/Android/data/fr.scaron.diaspdroid/logs/mainapplication.log",
        logcatArguments = { "-t", "100", "-v", "long", "ActivityManager:I", "MyApp:D", "*:S" }
//        logcatArguments = { "-t", "100", "-v", "long", "ActivityManager:I", "*:S" }
)
@EApplication
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
//        ACRA.getErrorReporter().checkReportsOnApplicationStart();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ACRA.getErrorReporter().checkReportsOnApplicationStart();
    }
}
