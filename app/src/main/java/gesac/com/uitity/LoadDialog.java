package gesac.com.uitity;

import android.content.Context;
import android.content.Intent;

import dmax.dialog.SpotsDialog;

/**
 * Created by GE11522 on 2016/10/31.
 */

public class LoadDialog {
    private static SpotsDialog spotsdialog;
    public static void showDialog(Context context){
        spotsdialog = new SpotsDialog(context,"请稍等");
        spotsdialog.show();
    }

    public static void cancelDialog(){
        spotsdialog.cancel();
    }
}
