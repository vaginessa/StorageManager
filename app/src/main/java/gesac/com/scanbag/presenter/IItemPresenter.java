package gesac.com.scanbag.presenter;

import gesac.com.scanbag.model.IJournal;
import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/4/18.
 */

public interface IItemPresenter {
    Integer isInJour(IBag iBag, IJournal ijournal);
    IBag subString(String str);
    boolean initBag(IBag iBag);
    String finBDAddress();

    String OpenPrinter();

    String Print1(String divnum, String str);

    String initCode(String divnum);

    String doPrint(String divnum);

}
