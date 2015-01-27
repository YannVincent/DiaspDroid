package fr.android.scaron.diaspdroid.controler;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by CARON-08651 on 26/01/2015.
 */
public class LogControler {
    private static LogControler Loggeur = new LogControler();

    public static LogControler getInstance() {
        return Loggeur;
    }

    private LogControler() {
    }

    public void i(Class classe, String message){
        Logger log = LoggerFactory.getLogger(classe);
        log.info(classe.getName(), message);
        Log.i(classe.getName(), message);
    }
    public void d(Class classe, String message){
        Logger log = LoggerFactory.getLogger(classe);
        log.debug(classe.getName(), message);
        Log.d(classe.getName(), message);
    }
    public void v(Class classe, String message){
        Logger log = LoggerFactory.getLogger(classe);
        log.trace(classe.getName(), message);
        Log.v(classe.getName(), message);
    }
    public void e(Class classe, String message){
        Logger log = LoggerFactory.getLogger(classe);
        log.error(classe.getName(), message);
        Log.e(classe.getName(), message);
    }
}
