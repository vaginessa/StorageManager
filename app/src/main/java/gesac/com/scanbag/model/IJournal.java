package gesac.com.scanbag.model;

import java.util.List;

/**
 * Created by GE11522 on 2017/4/14.
 */

public interface IJournal {
    boolean setJourid();
    boolean setItemlist(List<Item> items);

    String getJourid();
    List<Item> getItemlist();
}
