package gesac.com.scanbag.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;

import java.util.Set;

import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.view.IItemVIew;
import gesac.com.splitbag.model.Bag;
import gesac.com.splitbag.model.IBag;
import zpSDK.zpSDK.zpSDK;

/**
 * Created by GE11522 on 2017/4/18.
 */

public class ItemPresenterCompl implements IItemPresenter {
    public static BluetoothAdapter myBluetoothAdapter;
    public String SelectedBDAddress;
    Handler handler;
    private IItemVIew iItemVIew;
    private IBag iBag;

    public ItemPresenterCompl(IItemVIew iItemVIew) {
        this.iItemVIew = iItemVIew;
        SelectedBDAddress = new String();
    }

    @Override
    public Integer isInJour(IBag iBag, IJournal ijournal) {
        for (int i = 0; i < ijournal.getItemlist().size(); i++) {
            if (ijournal.getItemlist().get(i).getItemid().equalsIgnoreCase(iBag.getPctid())) {
                return i;
            }
        }
        return -1;
    }

    public IBag subString(String str) {
        String[] sourceStrArray = str.split(",,");
        try {
            iBag = new Bag(sourceStrArray[0].replaceAll(",", ""),
                    sourceStrArray[1].replaceAll(",", ""),
                    sourceStrArray[2].replaceAll(",", ""),
                    sourceStrArray[3].replaceAll(",", ""),
                    String.valueOf((int) Math.round(Double.parseDouble(sourceStrArray[5].replaceAll(",", "")))),
                    sourceStrArray[6].replaceAll(",", ""));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            iItemVIew.showAlert("条码错误！");
//            return null;
//        }catch (NumberFormatException e){
//            iItemVIew.showAlert("条码错误！");
//            return null;
        }catch (Exception e){
            iItemVIew.showAlert("条码错误！");
            return null;
        }
        return iBag;
    }

    public String finBDAddress() {
        if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
            return "没有找到蓝牙适配器";
        }

        if (!myBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            iItemVIew.openBluetooth(enableBtIntent);
        }
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() <= 0) return "false";
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equalsIgnoreCase("xt4131a"))
                SelectedBDAddress = device.getAddress();
        }
        return "bluetooth success";
    }

    public String OpenPrinter() {
        if (SelectedBDAddress == "" || SelectedBDAddress == null) {
            return "没有选择打印机";
        }
        BluetoothDevice myDevice;
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!myBluetoothAdapter.isEnabled()) {
            return "读取蓝牙设备错误";
        }
        myDevice = myBluetoothAdapter.getRemoteDevice(SelectedBDAddress);
        if (myDevice == null) {
            return "读取蓝牙设备错误";
        }
        if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
            return "打印失败！请检查与打印机的连接是否正常";
        }
//        if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
//            Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
//            return false;
//        }
        return "connect success";
    }

    public String Print1(String divnum, String str) {
//        iItemVIew.showLoad("正在检查与打印机的连接...");
        if (!OpenPrinter().equals("connect success")) {
//            iItemVIew.closeLoad();
            return "打印失败！请检查与打印机的连接是否正常";
        }
//        iItemVIew.closeLoad();
//        iItemVIew.showLoad("正在打印...");
        if (!zpSDK.zp_page_create(80, 34)) { //70,30
//            iItemVIew.showToast("创建打印页面失败");
//            iItemVIew.closeLoad();
            return "打印失败！请检查与打印机的连接是否正常";
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
            return "打印失败！请检查与打印机的连接是否正常";
        }
//        if (zpSDK.zp_printer_status_get(8000) != 0) {
//            Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
//        }
        zpSDK.zp_page_free();
        zpSDK.zp_close();
//        iItemVIew.closeLoad();
        return "打印成功";
    }

    public boolean initBag(IBag iBag) {
        this.iBag = iBag;
        return true;
    }

    @Override
    public String initCode(String divnum) {
        String str = new String();
        str = "," + iBag.getPctid() + ",," + iBag.getPcttol() + ",," + iBag.getPctqlty() + ",," + iBag.getPctbc() + ",,,," + divnum
                + ".0000,," + iBag.getPcthv() + ",";
        return str;
    }

    @Override
    public String doPrint(String divnum) {
        String sstr1 = initCode(divnum);
        String sstr2 = initCode(Integer.parseInt(iBag.getPctqty()) - Integer.parseInt(divnum) + "");

        //TODO 打印
        if (!finBDAddress().equals("bluetooth success"))
            return "请连接打印机";
        else {
            Print1(divnum, sstr1);
            String sign = Print1(String.valueOf(Integer.parseInt(iBag.getPctqty()) - Integer.parseInt(divnum)), sstr2);
            return sign;
        }
    }


}
