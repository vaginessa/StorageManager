package gesac.com.scanbag.model;

import java.util.List;

/**
 * Created by GE11522 on 2017/4/17.
 */

public class Journal implements IJournal {
    String jourid;
    List<Item> items;

    public Journal(String jourid, List<Item> items) {
        this.jourid = jourid;
        this.items = items;
    }

    public boolean setJourid() {
        return false;
    }

    public boolean setItemlist(List<Item> items) {
        return false;
    }

    public String getJourid() {
        return null;
    }

    public List<Item> getItemlist() {
        return null;
    }
}
