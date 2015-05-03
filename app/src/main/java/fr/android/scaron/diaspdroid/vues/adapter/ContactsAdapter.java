package fr.android.scaron.diaspdroid.vues.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Contact;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.view.ContactView;
import fr.android.scaron.diaspdroid.vues.view.ContactView_;
import fr.android.scaron.diaspdroid.vues.view.PostView;
import fr.android.scaron.diaspdroid.vues.view.PostView_;

/**
 * Created by Sébastien on 29/03/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ContactsAdapter extends BaseAdapter {
    private static Logger LOGGEUR = LoggerFactory.getLogger(ContactsAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "ContactsAdapter";
    private SparseArray<View> viewHolder = new SparseArray<View>();
    List<Contact> contacts;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        contacts = new ArrayList<Contact>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        int id = contact.getId();
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView  = ContactView_.build(context);
            ((ContactView)childView).bind(contact);
            viewHolder.put(id, childView);
        }
        return childView;


    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    public void setContacts(List<Contact> contacts){
        String TAG_METHOD = TAG + ".setContacts : ";
        try{
            LOG.d(TAG_METHOD + "Entrée");
            if (contacts==null) {
                LOG.d(".setContacts initialize this.contacts");
                this.contacts = new ArrayList<Contact>();
            }else{
                LOG.d(TAG_METHOD + "set this.contacts with"+contacts);
                this.contacts = Collections.checkedList(contacts, Contact.class);
            }
            LOG.d(TAG_METHOD + "notifyDataSetChanged");
            super.notifyDataSetChanged();
            LOG.d(TAG_METHOD + "sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD + "Erreur : "+thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
}