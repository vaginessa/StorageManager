package gesac.com.splitbag.presenter;

/**
 * Created by GE11522 on 2017/4/14.
 */

public interface ISplitPresenter {
    void subString(String str);

    Boolean finBDAddress();

    boolean OpenPrinter();

    void Print1(String divnum, String str);

    String initCode(String divnum);

    void doPrint(String divnum);
}
