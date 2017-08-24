package gesac.com.uitity;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

/**
 * Created by GE11522 on 2017/6/7.
 */

public class SoapUtil {
    public static SoapObject getERPinfo(String methodname, Map<String, String> params) {
        String WSDL_URI = "http://10.2.2.67:8099/JournalService.asmx?WSDL";//wsdl 的uri
        String namespace = "http://tempuri.org/";//namespace
        String methodName = methodname;//要调用的方法名称

        SoapObject request = new SoapObject(namespace, methodName);
//         设置需调用WebService接口需要传入的两个参数mobileCode、userId

        for (Map.Entry<String, String> p : params.entrySet())
            request.addProperty(p.getKey(), p.getValue());
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
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        SoapObject object = null;
        try {
            object = (SoapObject) envelope.bodyIn;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return object;
    }
}
