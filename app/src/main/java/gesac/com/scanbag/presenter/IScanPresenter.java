package gesac.com.scanbag.presenter;

import java.util.List;

import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.model.Item;
import gesac.com.scanbag.model.Journal;
import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/4/14.
 */

public interface IScanPresenter {

    boolean doCheck(Item item);
    void doScan();
    List<IJournal> getERPinfo(String jourid);
}
