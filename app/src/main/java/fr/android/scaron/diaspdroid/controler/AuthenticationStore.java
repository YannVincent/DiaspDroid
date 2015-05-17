package fr.android.scaron.diaspdroid.controler;

import org.androidannotations.annotations.EBean;

import fr.android.scaron.diaspdroid.model.DiasporaConfig;

/**
 * Created by Sébastien on 11/03/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class AuthenticationStore {
    public String getUsername(){
        return DiasporaConfig.POD_USER;
    }
    public String getPassword(){
        return DiasporaConfig.POD_PASSWORD;
    }
    public String getToken(){
        return DiasporaConfig.AUTHENTICITY_TOKEN;
    }
}
