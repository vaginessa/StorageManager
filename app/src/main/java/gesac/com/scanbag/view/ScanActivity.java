package gesac.com.scanbag.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import gesac.com.R;
import gesac.com.databinding.ActivityScanBinding;
import gesac.com.pickitem.model.Journal;
import gesac.com.scanbag.presenter.IScanPresenter;
import gesac.com.scanbag.presenter.ScanPresenterCompl;
import gesac.com.uitity.LoadDialog;

public class ScanActivity extends Activity implements IScanView {

    private IScanPresenter iScanPresenter;
    private List<Journal> iJournals;

    ActivityScanBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        iScanPresenter = new ScanPresenterCompl(this);
    }

    private void bindViews() {
        bind = DataBindingUtil.setContentView(this, R.layout.activity_scan);

        bind.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 通过webservice搜索日记账
                String jid = bind.jourid.getText().toString();
                LoadDialog.showDialog(ScanActivity.this, "请稍等");
                new getERPTask().execute("*" + jid + "*");
            }
        });

        bind.jourlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(ScanActivity.this, ItemActivity.class);
                String jstr = new Gson().toJson(iJournals.get(i));
                Log.d("debug", jstr);
                it.putExtra("jour", jstr);
                startActivity(it);
            }
        });
    }

    @Override
    public void showAlert(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .show();
    }

    class getERPTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = new String();
            try {
                result = iScanPresenter.getERPinfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将结果返回给onPostExecute方法
            return result;
        }

        @Override
        //此方法可以在主线程改变UI
        protected void onPostExecute(String result) {
            try {
                iJournals = new Gson().fromJson(result, new TypeToken<List<Journal>>() {
                }.getType());
                JourAdapter jourAdapter = new JourAdapter(ScanActivity.this, iJournals);
                bind.jourlist.setAdapter(jourAdapter);
            } catch (JsonSyntaxException e) {
                LoadDialog.cancelDialog();
                showAlert(result);
            }
            LoadDialog.cancelDialog();
        }
    }


}