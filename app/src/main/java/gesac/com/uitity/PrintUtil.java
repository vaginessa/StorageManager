package gesac.com.uitity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.Set;

import gesac.com.scanbag.model.Item;
import gesac.com.splitbag.model.IBag;
import zpSDK.zpSDK.zpSDK;


/**
 * Created by GE11522 on 2017/6/15.
 */

public class PrintUtil {
    public static BluetoothAdapter myBluetoothAdapter;
    public static String SelectedBDAddress;


    private static String finBDAddress() {
        if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
            return "没有找到蓝牙适配器";
        }

        if (!myBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        }
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() <= 0) return "false";
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equalsIgnoreCase("xt4131a")) {
                SelectedBDAddress = device.getAddress();
                return "bluetooth success";
            }
        }
        return "false";
    }

    public static int printPick(String divnum, IBag iBag) {
//        iItemVIew.showLoad("正在检查与打印机的连接...");
        if (!OpenPrinter().equals("connect success")) {
//            iItemVIew.closeLoad();
            return -1;//"打印失败！请检查与打印机的连接是否正常";
        }
//        iItemVIew.closeLoad();
//        iItemVIew.showLoad("正在打印...");
        if (!zpSDK.zp_page_create(80, 34)) { //70,30
            return 3;//"创建打印页面失败";

        } else {
            zpSDK.TextPosWinStyle = false;
            zpSDK.zp_draw_text_ex(2, 2.5, iBag.getPctid(), "黑体", 3, 0, true, false, false);
            zpSDK.zp_draw_text_ex(40, 2.5, iBag.getPctbc(), "黑体", 3, 0, true, false, false);
            zpSDK.zp_draw_text_ex(25, 15, divnum, "黑体", 6.0, 0, true, false, false);
            String str = CodeUtil.initCode(iBag,divnum);
            zpSDK.zp_draw_barcode2d(40, 20, str, zpSDK.BARCODE2D_TYPE.BARCODE2D_DATAMATRIX, 3, 3, 90);

            zpSDK.zp_page_print(false);
//            zpSDK.zp_printer_status_detect();
//        zpSDK.zp_goto_mark_label(4);
            switch (zpSDK.zp_printer_status_get(5000)) {
                case 0:
                    break;
                case -1:
                    return -1; //"打印失败！请检查与打印机的连接是否正常";
                case 1:
                    return 1;// "打印失败！打印机纸仓盖开";
                case 2:
                    return 2;//"打印失败！打印机缺纸";
                case 4:
                    return 4;//"打印失败！打印头过热";
                default:
                    zpSDK.zp_page_free();
            }
        }
//        if (zpSDK.zp_printer_status_get(8000) != 0) {
//            Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
//        }
        if (!zpSDK.zp_page_create(80, 34.4)) { //70,30
            return 3;//"创建打印页面失败";

        } else {

            zpSDK.TextPosWinStyle = false;
            zpSDK.zp_draw_text_ex(2, 2.5, iBag.getPctid(), "黑体", 3, 0, true, false, false);
            zpSDK.zp_draw_text_ex(40, 2.5, iBag.getPctbc(), "黑体", 3, 0, true, false, false);
            zpSDK.zp_draw_text_ex(25, 15, (Integer.parseInt(iBag.getPctqty()) - Integer.parseInt(divnum)) + "", "黑体", 6.0, 0, true, false, false);
            String str = CodeUtil.initCode(iBag,(Integer.parseInt(iBag.getPctqty()) - Integer.parseInt(divnum)) + "");
            zpSDK.zp_draw_barcode2d(40, 20, str, zpSDK.BARCODE2D_TYPE.BARCODE2D_DATAMATRIX, 3, 3, 90);

            zpSDK.zp_page_print(false);
            zpSDK.zp_printer_status_detect();
//        zpSDK.zp_goto_mark_label(4);
            switch (zpSDK.zp_printer_status_get(5000)) {
                case 0:
                    break;
                case -1:
                    return -1; //"打印失败！请检查与打印机的连接是否正常";
                case 1:
                    return 1;// "打印失败！打印机纸仓盖开";
                case 2:
                    return 2;//"打印失败！打印机缺纸";
                case 4:
                    return 4;//"打印失败！打印头过热";
                default:
                    zpSDK.zp_page_free();
            }
        }
        zpSDK.zp_close();
        return 0;//"打印成功";
    }

    public static int printHwPick(Item item) {
//        iItemVIew.showLoad("正在检查与打印机的连接...");
        if (!OpenPrinter().equals("connect success")) {
//            iItemVIew.closeLoad();
            return -1;//"打印失败！请检查与打印机的连接是否正常";
        }
//        iItemVIew.closeLoad();
//        iItemVIew.showLoad("正在打印...");
        if (!zpSDK.zp_page_create(80, 34)) { //70,30
            return 3;//"创建打印页面失败";

        } else {
            zpSDK.TextPosWinStyle = false;
            zpSDK.zp_draw_text_ex(2, 2.5, item.getItemid(), "黑体", 3, 0, true, false, false);
            zpSDK.zp_draw_text_ex(25, 15, item.getItemqty().replace("-", ""), "黑体", 6.0, 0, true, false, false);
            String str = "," + item.getItemid() + ",,"
                    + item.getItemqty().replace("-", "") + ",,,";
            zpSDK.zp_draw_barcode2d(40, 20, str, zpSDK.BARCODE2D_TYPE.BARCODE2D_DATAMATRIX, 3, 3, 90);

            zpSDK.zp_page_print(false);
//            zpSDK.zp_printer_status_detect();
//        zpSDK.zp_goto_mark_label(4);
            switch (zpSDK.zp_printer_status_get(5000)) {
                case 0:
                    break;
                case -1:
                    return -1; //"打印失败！请检查与打印机的连接是否正常";
                case 1:
                    return 1;// "打印失败！打印机纸仓盖开";
                case 2:
                    return 2;//"打印失败！打印机缺纸";
                case 4:
                    return 4;//"打印失败！打印头过热";
                default:
                    zpSDK.zp_page_free();
            }
        }
//        if (zpSDK.zp_printer_status_get(8000) != 0) {
//            Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
//        }
        if (!zpSDK.zp_page_create(80, 34.4)) { //70,30
            return 3;//"创建打印页面失败";

        } else {
            String qty = (Integer.parseInt(item.getItemwt()) - Integer.parseInt(item.getItemqty().replace("-", ""))) + "";
            zpSDK.TextPosWinStyle = false;
            zpSDK.zp_draw_text_ex(2, 2.5, item.getItemid(), "黑体", 3, 0, true, false, false);
            zpSDK.zp_draw_text_ex(25, 15, qty, "黑体", 6.0, 0, true, false, false);
            String str = "," + item.getItemid()
                    + ",,"
                    + qty
                    + ",,,";
            zpSDK.zp_draw_barcode2d(40, 20, str, zpSDK.BARCODE2D_TYPE.BARCODE2D_DATAMATRIX, 3, 3, 90);

            zpSDK.zp_page_print(false);
            zpSDK.zp_printer_status_detect();
//        zpSDK.zp_goto_mark_label(4);
            switch (zpSDK.zp_printer_status_get(5000)) {
                case 0:
                    break;
                case -1:
                    return -1; //"打印失败！请检查与打印机的连接是否正常";
                case 1:
                    return 1;// "打印失败！打印机纸仓盖开";
                case 2:
                    return 2;//"打印失败！打印机缺纸";
                case 4:
                    return 4;//"打印失败！打印头过热";
                default:
                    zpSDK.zp_page_free();
            }
        }
        zpSDK.zp_close();
        return 0;//"打印成功";
    }


    public static int doPickPrint(int type, String divnum, Object object) {
        int sign = 9999;
        //TODO 打印
        if (finBDAddress().equalsIgnoreCase("bluetooth success"))
            return 5; //"请连接打印机"
        else {
            switch (type) {
                case 0:
                    sign = printPick(divnum, (IBag) object);
                    break;
                case 1:
                    sign = printHwPick((Item) object);
                    break;
                case 2:
                    sign = printPick(divnum, (IBag) object);
                    break;
            }
            return sign;
        }
    }

    public static String OpenPrinter() {
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
}
