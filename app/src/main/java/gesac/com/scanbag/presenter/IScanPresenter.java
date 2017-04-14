package gesac.com.scanbag.presenter;

import gesac.com.scanbag.model.Item;

/**
 * Created by GE11522 on 2017/4/14.
 */

public interface IScanPresenter {

    boolean doCheck(Item item);
    void doScan();
}
