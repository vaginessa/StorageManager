package gesac.com.scanbag.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import gesac.com.R;
import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.presenter.IScanPresenter;
import gesac.com.scanbag.presenter.ScanPresenterCompl;
import gesac.com.splitbag.model.IBag;
import gesac.com.splitbag.presenter.ISplitPresenter;
import gesac.com.uitity.LoadDialog;

public class ScanActivity extends AppCompatActivity implements IScanView {
    private EditText mJourid, mItemstr;
    private Button mSearch;
    private ListView mJourlist;

    private String strcode;
    private IScanPresenter iScanPresenter;
    private IBag iBag;
    private List<IJournal> iJournals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        bindViews();
        iScanPresenter = new ScanPresenterCompl(this);
    }


    private void bindViews() {
        mJourid = (EditText) findViewById(R.id.jourid);
        mSearch = (Button) findViewById(R.id.search);
        mItemstr = (EditText) findViewById(R.id.itemstr);
        mJourlist = (ListView) findViewById(R.id.jourlist);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 通过webservice搜索日记账
                String jid = mJourid.getText().toString();
                new getERPTask().execute(jid);
                LoadDialog.showDialog(ScanActivity.this, "请稍等");
            }
        });

        mJourlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(ScanActivity.this, ItemActivity.class);
                Gson gson = new Gson();
                String jstr = gson.toJson(iJournals.get(i));
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

    class getERPTask extends AsyncTask<String, Integer, List<IJournal>> {
        @Override
        protected List<IJournal> doInBackground(String... params) {
            try {
                iJournals = iScanPresenter.getERPinfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将结果返回给onPostExecute方法
            return iJournals;
        }

        @Override
        //此方法可以在主线程改变UI
        protected void onPostExecute(List<IJournal> result) {
            JourAdapter jourAdapter = new JourAdapter(ScanActivity.this, result);
            mJourlist.setAdapter(jourAdapter);
            LoadDialog.cancelDialog();
        }
    }
}
