package gesac.com.splitbag.presenter;

import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/4/14.
 */

public interface ISplitPresenter {
    IBag subString(String str);

    Boolean finBDAddress();

    boolean OpenPrinter();

    void Print1(String divnum);

    String initCode(String divnum);

    void doPrint(String divnum);
}
