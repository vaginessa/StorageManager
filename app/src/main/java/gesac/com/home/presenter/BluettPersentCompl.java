package gesac.com.home.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gesac.com.home.model.Bluetooth;
import gesac.com.home.view.IBluettView;
import gesac.com.uitity.BHTApplication;

/**
 * Created by GE11522 on 2017/4/26.
 */

public class BluettPersentCompl implements IBluettPersenter{
    private static BluetoothAdapter myBluetoothAdapter;

    IBluettView iBluettView;

    public BluettPersentCompl(IBluettView iBluettView) {
        this.iBluettView = iBluettView;
    }

    @Override
    public List<Bluetooth> getAllbluett() {
        return null;
    }

    @Override
    public boolean bindBluett(String bluettaddr) {
        return false;
    }
}
