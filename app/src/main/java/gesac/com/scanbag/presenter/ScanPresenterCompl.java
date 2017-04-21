package gesac.com.scanbag.presenter;

import android.util.Log;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.model.Item;
import gesac.com.scanbag.model.Journal;
import gesac.com.scanbag.view.IScanView;
import gesac.com.splitbag.model.Bag;
import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/4/18.
 */

public class ScanPresenterCompl implements IScanPresenter {
    IScanView iScanView;
    IBag iBag;

    public ScanPresenterCompl(IScanView iScanView) {
        this.iScanView = iScanView;
    }

    @Override
    public boolean doCheck(Item item) {
        return false;
    }

    @Override
    public void doScan() {

    }



    @Override
    public List<IJournal> getERPinfo(String jourid) {
        List<IJournal> iJournals = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        String WSDL_URI = "http://10.2.2.67:8099/JournalService.asmx?WSDL";//wsdl 的uri
        String namespace = "http://tempuri.org/";//namespace
        String methodName = "GetProdJournalBOM";//要调用的方法名称

        SoapObject request = new SoapObject(namespace, methodName);
//         设置需调用WebService接口需要传入的两个参数mobileCode、userId
        request.addProperty("journalId", jourid);
        request.addProperty("dataAreaId", "ge");
//        request.addProperty("UserName", "ge11522");
//        request.addProperty("PassWord", "Hhl002636");


        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        try {
            httpTransportSE.call(null, envelope);//调用
        } catch (SocketTimeoutException e) {
            iScanView.showAlert("获取时间超时，请确认日记账号!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        // 获取返回的数据
        SoapObject object = (SoapObject) ((SoapObject) envelope.bodyIn).getProperty(0);
        // 获取返回的结果

//        SoapObject item = (SoapObject) itemlist.getPrimitiveProperty("items");
        for (int i = 0; i < object.getPropertyCount(); i++) {
            SoapObject jourlist = (SoapObject) object.getProperty(i);
            SoapObject itemlist = (SoapObject) jourlist.getProperty("items");
            items = new ArrayList<>();
            for (int j = 0; j < itemlist.getPropertyCount(); j++) {
                SoapObject item = (SoapObject) itemlist.getProperty(j);
                items.add(new Item(item.getProperty("itemid").toString(),
                        item.getProperty("itemqlty").toString(),
                        item.getProperty("itemtol").toString(),
                        item.getProperty("itembc").toString(),
                        item.getProperty("itemseri").toString(),
                        item.getProperty("itemqty").toString(),
                        item.getProperty("itemwt").toString(),
                        item.getProperty("itemst").toString(),
                        item.getProperty("itemslc").toString()));
            }
            iJournals.add(new Journal(jourlist.getProperty("jourid").toString(), items));
            Log.d("debug", String.valueOf(iJournals.get(i).getItemlist().size()));

//            Log.d("debug", String.valueOf(iJournals.get(i).getItemlist().size()));
        }
//        String result = jourlist.getProperty(0).toString();

//        Log.d("debug", String.valueOf(iJournals.get(0).getJourid()));

        return iJournals;
    }


}
