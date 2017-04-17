package gesac.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import gesac.com.scanbag.view.ScanActivity;
import gesac.com.splitbag.view.SplitActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class HomeActivity extends AppCompatActivity  {
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
                initRetrofit();
            }
        });
    }

    interface initRetrof{
        @GET("XTC_JournalTrans_Service")
        Call<ResponseBody> getstr();
    }
    public void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.1.0.133:8101/DynamicsAx/Services/")
                .build();
        initRetrof realLocGetIF = retrofit.create(initRetrof.class);
        Call<ResponseBody> call = realLocGetIF.getstr();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        testtv.setText(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("aaaaaa", t.toString());
            }
        });
    }


}
