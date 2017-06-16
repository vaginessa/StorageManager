package gesac.com.uitity;

import android.content.Context;
import android.view.WindowManager;

import dmax.dialog.SpotsDialog;

/**
 * Created by GE11522 on 2016/10/31.
 */

public class LoadDialog {
    private static volatile SpotsDialog spotsdialog;

    public static void showDialog(Context context, String msg) {
        if (spotsdialog == null) {
            synchronized (LoadDialog.class) {
                if (spotsdialog == null)
                    spotsdialog = new SpotsDialog(context, msg);
            }
        }
        spotsdialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        spotsdialog.show();
    }


    public static void cancelDialog() {
        spotsdialog.cancel();
    }
}
