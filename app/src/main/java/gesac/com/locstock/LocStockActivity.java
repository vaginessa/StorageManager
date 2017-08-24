package gesac.com.locstock;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gesac.com.BR;
import gesac.com.R;
import gesac.com.databinding.ActivityLocStockBinding;
import gesac.com.splitbag.model.IBag;
import gesac.com.uitity.Alertdlg;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.LoadDialog;
import gesac.com.uitity.SoapUtil;
import gesac.com.uitity.WarnSPlayer;

public class LocStockActivity extends Activity {
    private final String TAG = "LocStockActivity.debug";
    private ActivityLocStockBinding binding;
    private WareAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loc_stock);
        binding.codestrEt.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            binding.codestrEt.requestFocus();
            binding.codestrEt.selectAll();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            String s = binding.codestrEt.getText().toString();
            binding.codestrEt.setText("");
            if (!s.isEmpty()) {
                Log.i(TAG, "onKeyUp: " + s);
                IBag iBag = CodeUtil.subCode(s);
                if (iBag != null) {
                    new WareAsync().execute(iBag.getPctid(),
                            iBag.getPctbc(),
                            iBag.getPctqlty(),
                            iBag.getPcttol());
                    LoadDialog.showDialog(this, "请稍等");
                } else WarnSPlayer.playsound(this, R.raw.codeerr);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private List<Ware> parseInfo(SoapObject result) {
        List<Ware> wareList = new ArrayList<>();
        try {
            SoapObject wares = (SoapObject) result.getProperty("GetInvetnSum_SimpleByItemResult");
            for (int i = 0; i < wares.getPropertyCount(); i++) {
                SoapObject we = (SoapObject) wares.getProperty(i);
                wareList.add(new Ware(
                        we.getPrimitiveProperty("InventLocationId").toString(),
                        we.getPrimitiveProperty("WMSLocationId").toString(),
                        we.getPrimitiveProperty("InventSizeId").toString(),
                        we.getPrimitiveProperty("InventColorId").toString(),
                        we.getPrimitiveProperty("OnHander").toString()
                ));
                Log.i(TAG, "parseInfo: " + we.getPrimitiveProperty("InventLocationId").toString());
            }
        } catch (ClassCastException e) {
            Alertdlg.showDialog(LocStockActivity.this, "请检查服务器设置");
        }
        return wareList;
    }

    class WareAsync extends AsyncTask<String, Void, SoapObject> {
        @Override
        protected SoapObject doInBackground(String... params) {
            Map<String, String> request = new HashMap<>();
            request.put("itemid", params[0]);
            request.put("inventBatchId", params[1]);
            request.put("inventSizeId", params[2]);
            request.put("inventColorid", params[3]);
            SoapObject so = SoapUtil.getERPinfo("GetInvetnSum_SimpleByItem", request);
            return so;
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            super.onPostExecute(result);
            if (result != null) {
                List<Ware> wareList = parseInfo(result);
                adapter = new WareAdapter(LocStockActivity.this, BR.ware, wareList);
                binding.setWareadapter(adapter);
            } else Alertdlg.showDialog(LocStockActivity.this, "请检查服务器设置");
            LoadDialog.cancelDialog();
        }
    }
}
