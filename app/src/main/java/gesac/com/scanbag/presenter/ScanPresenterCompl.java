package gesac.com.scanbag.presenter;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.model.Item;
import gesac.com.scanbag.model.Journal;
import gesac.com.scanbag.view.IScanView;

/**
 * Created by GE11522 on 2017/4/18.
 */

public class ScanPresenterCompl implements IScanPresenter {
    IScanView iScanView;

    public ScanPresenterCompl(IScanView iScanView) {
        this.iScanView = iScanView;
    }

    @Override
    public String getERPinfo(String jourid) {
        List<IJournal> iJournals = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        String WSDL_URI = "http://10.2.2.67:8099/JournalService.asmx?WSDL";//wsdl 的uri
        String namespace = "http://tempuri.org/";//namespace
        String methodName = "GetJournalTrans";//要调用的方法名称

        SoapObject request = new SoapObject(namespace, methodName);
//         设置需调用WebService接口需要传入的两个参数mobileCode、userId
        request.addProperty("journalId", jourid);
        request.addProperty("dataAreaId", "ge");
        request.addProperty("userName", "geandroid");
        request.addProperty("password", "WJ82LYVu8fm+vdtIdA0yxA==");
        request.addProperty("dimain", "ge");


        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        try {
            httpTransportSE.call(null, envelope);//调用
        } catch (ConnectException e) {
            return "请检查网络连接";
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "获取时间超时，请确认日记账号!";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        // 获取返回的数据
        try {
            SoapObject object = (SoapObject) ((SoapObject) envelope.bodyIn).getProperty("GetJournalTransResult");
            for (int i = 0; i < object.getPropertyCount(); i++) {
                SoapObject journal = (SoapObject) object.getProperty(i);
                SoapObject itemlist = (SoapObject) journal.getProperty("items");
                items = new ArrayList<>();
                for (int j = 0; j < itemlist.getPropertyCount(); j++) {
                    SoapObject item = (SoapObject) itemlist.getProperty(j);
                    items.add(new Item(item.getPrimitiveProperty("itemid").toString(),
                            item.getPrimitiveProperty("itemqlty").toString(),
                            item.getPrimitiveProperty("itemtol").toString(),
                            item.getPrimitiveProperty("itembc").toString(),
                            item.getPrimitiveProperty("itemseri").toString(),
                            item.getPrimitiveProperty("itemqty").toString().replaceAll("-",""),
                            item.getPrimitiveProperty("itemrqty").toString(),
                            item.getPrimitiveProperty("itemst").toString(),
                            item.getPrimitiveProperty("itemslc").toString()));
                }
                iJournals.add(new Journal(journal.getProperty("jourid").toString(), items));
            }
        } catch (ClassCastException e) {
            return "请检查服务器设置";
        }
        Gson gson = new Gson();
        String result = gson.toJson(iJournals);
        return result;
    }

}
