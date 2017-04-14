package gesac.com.splitbag.view;

import android.content.Intent;

import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/4/14.
 */

public interface ISplitView {
    void fillEdt(IBag ibag);

    void clearEdt();

    void showStatbox(String str);

    void closeStatbox();

    void showToast(String str);

    void openBluetooth(Intent it);
}
