package fr.android.scaron.diaspdroid.controler;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by CARON-08651 on 26/01/2015.
 */
public class LogControler {
    private static LogControler Loggeur;
    Logger log;
    String className;

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
        className = log.getClass().getName();
    }
    private LogControler(Logger loggeur) {
        log = loggeur;
        className = log.getClass().getName();
    }

    public void i(Class classe, String message){
        log.info(message);
        Log.i(classe.getName(), message);
    }
    public void d(Class classe, String message){
        log.debug(message);
        Log.d(classe.getName(), message);
    }
    public void v(Class classe, String message){
        log.trace(message);
        Log.v(classe.getName(), message);
    }
    public void e(Class classe, String message){
        log.error(message);
        Log.e(classe.getName(), message);
    }

    public void i(String message){
        log.info(message);
        Log.i(className, className+" : "+message);
    }
    public void d(String message){
        log.debug(message);
        Log.d(className, className+" : "+message);
    }
    public void v(String message){
        log.trace(message);
        Log.v(className, className+" : "+message);
    }
    public void e(String message){
        log.error(message);
        Log.e(className, className+" : "+message);
    }
}
