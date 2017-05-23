package gesac.com.scanbag.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

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
    private IItemVIew iItemVIew;
    private IBag iBag;

    public ItemPresenterCompl(IItemVIew iItemVIew) {
        this.iItemVIew = iItemVIew;
        SelectedBDAddress = new String();
    }

    @Override
    public Integer isInJour(IBag iBag, IJournal ijournal) {
        for (int i = 0; i < ijournal.getItemlist().size(); i++) {
            if (ijournal.getItemlist().get(i).getItemid().equalsIgnoreCase(iBag.getPctid())
                    && ijournal.getItemlist().get(i).getItemqlty().equalsIgnoreCase(iBag.getPctqlty())
                    && ijournal.getItemlist().get(i).getItemtol().equalsIgnoreCase(iBag.getPcttol())
                    ) {
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
        } catch (Exception e) {
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

    public int Print1(String divnum) {
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
            String str = "," + iBag.getPctid() + ",,"
                    + iBag.getPcttol() + ",,"
                    + iBag.getPctqlty() + ",,"
                    + iBag.getPctbc() + ",,,," + divnum
                    + ".0000,," + iBag.getPcthv() + ",";
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
            String str = "," + iBag.getPctid()
                    + ",," + iBag.getPcttol()
                    + ",," + iBag.getPctqlty()
                    + ",," + iBag.getPctbc() + ",,,,"
                    + (Integer.parseInt(iBag.getPctqty()) - Integer.parseInt(divnum))
                    + ".0000,," + iBag.getPcthv() + ",";
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

    @Override
    public String initCode(String divnum) {
        String str = new String();
        str = "," + iBag.getPctid() + ",," + iBag.getPcttol() + ",," + iBag.getPctqlty() + ",," + iBag.getPctbc() + ",,,," + divnum
                + ".0000,," + iBag.getPcthv() + ",";
        return str;
    }

    @Override
    public int doPrint(String divnum) {
        //TODO 打印
        if (finBDAddress().equalsIgnoreCase("bluetooth success"))
            return 5; //"请连接打印机"
        else {
            int sign = Print1(divnum);
            return sign;
        }
    }


}
