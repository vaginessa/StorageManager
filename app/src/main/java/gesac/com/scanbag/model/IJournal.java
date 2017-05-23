package gesac.com.scanbag.model;

import java.util.List;

import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/4/14.
 */

public interface IJournal {
    boolean setJourid(String jourid);
    boolean setItemlist(List<Item> items);

    String getJourid();
    List<Item> getItemlist();

    Integer isInJour(IBag iBag, IJournal ijournal);
}
