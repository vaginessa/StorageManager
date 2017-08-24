package gesac.com.uitity;

import android.app.AlertDialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by GE11522 on 2017/8/14.
 */

public class Alertdlg {
    private static volatile AlertDialog ad;

    public static void showDialog(Context context, String msg) {
        if (ad == null) {
            synchronized (Alertdlg.class) {
                if (ad == null)
                    ad = new AlertDialog.Builder(context)
                            .setMessage(msg)
                            .create();
            }
        }
        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        ad.show();
    }
}
