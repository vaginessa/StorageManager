package gesac.com.scanbag.view;

import android.content.Intent;

/**
 * Created by GE11522 on 2017/4/18.
 */

public interface IItemVIew {
    void showLoad(String str);

    void closeLoad();

    void showToast(String str);

    void openBluetooth(Intent it);
}
