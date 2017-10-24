package gesac.com.pickitem.model;

import android.databinding.BaseObservable;

import java.util.List;

import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/4/17.
 */

public class Journal extends BaseObservable implements IJournal {
    String jourid;
    List<Item> items;

    public Journal() {
    }

    public Journal(String jourid, List<Item> items) {
        this.jourid = jourid;
        this.items = items;
    }

    public boolean setJourid(String jourid) {
        this.jourid = jourid;
        return true;
    }

    public boolean setItemlist(List<Item> items) {
        this.items = items;
        return true;
    }

    public String getJourid() {
        return jourid;
    }

    public List<Item> getItemlist() {
        return items;
    }

    @Override
    public Integer isInJour(IBag iBag, IJournal ijournal) {
        for (int i = 0; i < ijournal.getItemlist().size(); i++) {
            if (ijournal.getItemlist().get(i).getItemid().equalsIgnoreCase(iBag.getPctid())
                    && ijournal.getItemlist().get(i).getItembc().equalsIgnoreCase(iBag.getPctbc())
                    && ijournal.getItemlist().get(i).getItemqlty().equalsIgnoreCase(iBag.getPctqlty())
                    && ijournal.getItemlist().get(i).getItemtol().equalsIgnoreCase(iBag.getPcttol())
                    && ijournal.getItemlist().get(i).getItemqty().equalsIgnoreCase(iBag.getPctqty())
                    && ijournal.getItemlist().get(i).getItemwt().equalsIgnoreCase(iBag.getPcthv())) {
                return i;
            }
        }
        return -1;
    }
}
