package fr.android.scaron.diaspdroid.vues.finder;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.ItemDrawer;

/**
 * Created by Sébastien on 06/03/2015.
 */
@EBean
public class InMemoryItemDrawerFinder implements ItemDrawerFinder{

    private static final String TAG = "InMemoryItemDrawerFinder";
    private static Logger LOGGEUR = LoggerFactory.getLogger(InMemoryItemDrawerFinder.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    List<ItemDrawer> itemDrawers;
    @RootContext
    Context context;

    @Override
    public List<ItemDrawer> findAll() {
        String TAG_METHOD = ".findAll -> ";
        LOG.d(TAG + TAG_METHOD + "entrée");
        if (itemDrawers==null){
            LOG.d(TAG + TAG_METHOD + "initList");
            initList();
        }
        LOG.d(TAG + TAG_METHOD + "sortie");
        return itemDrawers;
    }


    void initList(){
        String TAG_METHOD = ".initList -> ";
        LOG.d(TAG + TAG_METHOD + "entrée");
        itemDrawers = new ArrayList<>();
        ItemDrawer itemDrawer = new ItemDrawer(context.getString(R.string.title_section1));
        itemDrawers.add(itemDrawer);
        LOG.d(TAG + TAG_METHOD + "section1 added");
        itemDrawer = new ItemDrawer(context.getString(R.string.title_section2));
        itemDrawers.add(itemDrawer);
        LOG.d(TAG + TAG_METHOD + "section2 added");
        itemDrawer = new ItemDrawer(context.getString(R.string.title_section3));
        itemDrawers.add(itemDrawer);
        LOG.d(TAG + TAG_METHOD + "section3 added");
        itemDrawer = new ItemDrawer(context.getString(R.string.title_section4));
        itemDrawers.add(itemDrawer);
        LOG.d(TAG + TAG_METHOD + "section4 added");
        LOG.d(TAG + TAG_METHOD + "sortie");
    }
}
