package fr.android.scaron.diaspdroid.vues.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.ItemDrawer;
import fr.android.scaron.diaspdroid.vues.finder.InMemoryItemDrawerFinder;
import fr.android.scaron.diaspdroid.vues.finder.ItemDrawerFinder;
import fr.android.scaron.diaspdroid.vues.view.ItemDrawerView;
import fr.android.scaron.diaspdroid.vues.view.ItemDrawerView_;

/**
 * Created by Sébastien on 06/03/2015.
 */
@EBean
public class DrawerListAdapter extends BaseAdapter {
    private static final String TAG = "DrawerListAdapter";
    private static Logger LOGGEUR = LoggerFactory.getLogger(DrawerListAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);

    List<ItemDrawer> itemDrawers;

    @Bean(InMemoryItemDrawerFinder.class)
    ItemDrawerFinder itemDrawerFinder;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        itemDrawers = itemDrawerFinder.findAll();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String TAG_METHOD = ".getView -> ";
        LOG.d(TAG + TAG_METHOD + "entrée");
        ItemDrawerView itemDrawerView;
        if (convertView == null) {
            itemDrawerView = ItemDrawerView_.build(context);
        } else {
            itemDrawerView = (ItemDrawerView) convertView;
        }

        itemDrawerView.bind(getItem(position));

        LOG.d(TAG + TAG_METHOD + "sortie");
        return itemDrawerView;
    }

    @Override
    public int getCount() {
        String TAG_METHOD = ".getCount -> ";
        LOG.d(TAG + TAG_METHOD + "entrée/sortie");
        return itemDrawers.size();
    }

    @Override
    public ItemDrawer getItem(int position) {
        String TAG_METHOD = ".getItem -> ";
        LOG.d(TAG + TAG_METHOD + "entrée/sortie");
        return itemDrawers.get(position);
    }

    @Override
    public long getItemId(int position) {
        String TAG_METHOD = ".getItemId -> ";
        LOG.d(TAG + TAG_METHOD + "entrée/sortie");
        return position;
    }
}