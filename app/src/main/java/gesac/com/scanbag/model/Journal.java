package gesac.com.scanbag.model;

import java.util.List;

/**
 * Created by GE11522 on 2017/4/17.
 */

public class Journal implements IJournal {
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
}
