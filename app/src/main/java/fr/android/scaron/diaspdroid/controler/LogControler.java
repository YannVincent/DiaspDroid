package fr.android.scaron.diaspdroid.controler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by CARON-08651 on 26/01/2015.
 */
public class LogControler {
    private static LogControler Loggeur;
    Logger log;

    public static LogControler getInstance(Class classe) {
        if (Loggeur==null){
            Loggeur= new LogControler(classe);
        }
        return Loggeur;
    }


    public static LogControler getInstance(Logger log) {
        if (Loggeur==null){
            Loggeur= new LogControler(log);
        }
        return Loggeur;
    }

    private LogControler(Class classe) {
        log = LoggerFactory.getLogger(classe);
    }
    private LogControler(Logger loggeur) {
        log = loggeur;
    }

    public void i(Class classe, String message){
//        Logger log = LoggerFactory.getLogger(classe);
        log.info(message);
//        Log.i(classe.getName(), message);
    }
    public void d(Class classe, String message){
//        Logger log = LoggerFactory.getLogger(classe);
        log.debug(message);
//        Log.d(classe.getName(), message);
    }
    public void v(Class classe, String message){
//        Logger log = LoggerFactory.getLogger(classe);
        log.trace(message);
//        Log.v(classe.getName(), message);
    }
    public void e(Class classe, String message){
//        Logger log = LoggerFactory.getLogger(classe);
        log.error(message);
//        Log.e(classe.getName(), message);
    }
}
