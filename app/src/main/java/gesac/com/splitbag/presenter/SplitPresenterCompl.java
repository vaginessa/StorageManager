package gesac.com.splitbag.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Set;

import gesac.com.splitbag.model.Bag;
import gesac.com.splitbag.model.IBag;
import gesac.com.splitbag.view.ISplitView;

import zpSDK.zpSDK.zpSDK;

/**
 * Created by GE11522 on 2017/4/14.
 */

public class SplitPresenterCompl implements ISplitPresenter {
    ISplitView iSplitView;
    IBag iBag;
    Handler handler;

    public static BluetoothAdapter myBluetoothAdapter;
    public String SelectedBDAddress;

    public SplitPresenterCompl(ISplitView iSplitView) {
        this.iSplitView = iSplitView;
        this.handler = new Handler(Looper.getMainLooper());
        SelectedBDAddress = new String();
    }

    public IBag subString(String str) {
        String[] sourceStrArray = str.split(",,");
        try {
            iBag = new Bag(sourceStrArray[0], sourceStrArray[1], sourceStrArray[2], sourceStrArray[3],
                    String.valueOf((int) Math.round(Double.parseDouble(sourceStrArray[5]))),
                    sourceStrArray[6]);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.i("message", "subString: " + e.toString());
            iSplitView.clearEdt();
            return null;
        }
        iSplitView.fillEdt(iBag);
        return iBag;
    }

    public Boolean finBDAddress() {
        if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
            iSplitView.showToast("没有找到蓝牙适配器");
            return false;
        }

        if (!myBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            iSplitView.openBluetooth(enableBtIntent);
        }
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() <= 0) return false;
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equalsIgnoreCase("xt4131a"))
                SelectedBDAddress = device.getAddress();
        }
        return true;
    }

    public boolean OpenPrinter() {
        if (SelectedBDAddress == "" || SelectedBDAddress == null) {
            iSplitView.showToast("没有选择打印机");
            return false;
        }
        BluetoothDevice myDevice;
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!myBluetoothAdapter.isEnabled()) {
            iSplitView.showToast("读取蓝牙设备错误");
            return false;
        }
        myDevice = myBluetoothAdapter.getRemoteDevice(SelectedBDAddress);
        if (myDevice == null) {
            iSplitView.showToast("读取蓝牙设备错误");
            return false;
        }
        if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
            iSplitView.showToast("打印失败！请检查与打印机的连接是否正常");
            return false;
        }
//        if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
//            Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }

    public void Print1(String divnum, String str) {
        iSplitView.showStatbox("正在检查与打印机的连接...");
        if (!OpenPrinter()) {
            iSplitView.closeStatbox();
            return;
        }
        iSplitView.closeStatbox();
        iSplitView.showStatbox("正在打印...");
        if (!zpSDK.zp_page_create(80, 34.4)) { //70,30
            iSplitView.showToast("创建打印页面失败");
            iSplitView.closeStatbox();
            return;
        }

        zpSDK.TextPosWinStyle = false;
        zpSDK.zp_draw_text_ex(2, 2.5, iBag.getPctid().substring(1), "黑体", 3, 0, true, false, false);
        zpSDK.zp_draw_text_ex(40, 2.5, iBag.getPctbc(), "黑体", 3, 0, true, false, false);
        zpSDK.zp_draw_text_ex(25, 15, divnum, "黑体", 6.0, 0, true, false, false);
        zpSDK.zp_draw_barcode2d(45, 20, str, zpSDK.BARCODE2D_TYPE.BARCODE2D_DATAMATRIX, 3, 3, 90);

        zpSDK.zp_page_print(false);
        zpSDK.zp_printer_status_detect();
//        zpSDK.zp_goto_mark_right(4);
        if (zpSDK.zp_printer_status_get(8000) != 0) {
            iSplitView.showToast("打印失败！请检查与打印机的连接是否正常");
        }
//        if (zpSDK.zp_printer_status_get(8000) != 0) {
//            Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
//        }
        zpSDK.zp_page_free();
        zpSDK.zp_close();
        iSplitView.closeStatbox();
    }

    @Override
    public String initCode(String divnum) {
        String str = new String();
        str = iBag.getPctid() + ",," + iBag.getPcttol() + ",," + iBag.getPctqlty() + ",," + iBag.getPctbc() + ",,,," + divnum
                + ".0000,," + iBag.getPcthv();
        return str;
    }

    @Override
    public void doPrint(String divnum) {
        String sstr1 = initCode(divnum);
        String sstr2 = initCode(Integer.parseInt(iBag.getPctqty()) - Integer.parseInt(divnum) + "");

        //TODO 打印
        if (!finBDAddress())
            iSplitView.showToast("请连接打印机");
        else {
            Print1(divnum, sstr1);
            Print1(String.valueOf(Integer.parseInt(iBag.getPctqty()) - Integer.parseInt(divnum)), sstr2);
        }
    }
}
