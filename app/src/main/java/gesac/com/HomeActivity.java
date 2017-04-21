package gesac.com;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import gesac.com.scanbag.view.ScanActivity;
import gesac.com.splitbag.view.SplitActivity;
import gesac.com.uitity.LoadDialog;

public class HomeActivity extends AppCompatActivity {
    // Content View Elements

    private Button mScan_home;
    private Button mSplit_home;
    private Button testbt;
    private TextView testtv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindViews();
    }


    // End Of Content View Elements

    private void bindViews() {

        testbt = (Button) findViewById(R.id.testbt);
        testtv = (TextView) findViewById(R.id.testtv);

        mScan_home = (Button) findViewById(R.id.scan_home);
        mSplit_home = (Button) findViewById(R.id.split_home);

        mScan_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(HomeActivity.this, ScanActivity.class);
                startActivity(it);
            }
        });
        mSplit_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(HomeActivity.this, SplitActivity.class);
                startActivity(it);
            }
        });

        testbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动后台异步线程进行连接webService操作，并且根据返回结果在主线程中改变UI
                QueryAddressTask queryAddressTask = new QueryAddressTask();
                //启动后台任务
                queryAddressTask.execute("*000511");
                LoadDialog.showDialog(HomeActivity.this);
            }
        });
    }

    class QueryAddressTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                result = getERPinfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将结果返回给onPostExecute方法
            return result;
        }

        @Override
        //此方法可以在主线程改变UI
        protected void onPostExecute(String result) {
            // 将WebService返回的结果显示在TextView中
            testtv.setText(result);
            LoadDialog.cancelDialog();
        }
    }

    public String getERPinfo(String jourid) {
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
        } catch (SocketTimeoutException e){
            Toast.makeText(this, "获取时间超时", Toast.LENGTH_SHORT).show();
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
            iJournals.add(new Journal(jourlist.getProperty("jourid").toString(),items));
            items.clear();
            Log.d("debug", String.valueOf(iJournals.get(i).getJourid()));
        }
//        String result = jourlist.getProperty(0).toString();

//        Log.d("debug", String.valueOf(iJournals.get(0).getJourid()));

        return "";
    }

//    interface initRetrof{
//        @GET("XTC_JournalTrans_Service")
//        Call<ResponseBody> getstr();
//    }
//    public void initRetrofit() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.1.0.133:8101/DynamicsAx/Services/")
//                .build();
//        initRetrof realLocGetIF = retrofit.create(initRetrof.class);
//        Call<ResponseBody> call = realLocGetIF.getstr();
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        testtv.setText(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("aaaaaa", t.toString());
//            }
//        });
//    }


}
