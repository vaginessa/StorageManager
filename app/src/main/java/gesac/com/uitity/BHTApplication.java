package gesac.com.uitity;

import android.app.Application;
import android.content.Context;

/**
 * Created by GE11522 on 2017/4/26.
 */

public class BHTApplication extends Application {
    private static Context context;
    private String bhtaddr;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        bhtaddr = null;
    }

    public  String getBhtaddr() {
        return bhtaddr;
    }

    public  void setBhtstr(String bhtaddr) {
        this.bhtaddr = bhtaddr;
    }
}
