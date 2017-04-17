package gesac.com.scanbag.presenter;

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
    boolean isInJour(IBag iBag, IJournal ijournal);
}
