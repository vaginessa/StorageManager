package gesac.com.chkstock;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gesac.com.R;
import gesac.com.databinding.ActivityStockBinding;
import gesac.com.scanbag.model.Item;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.LoadDialog;
import gesac.com.uitity.SoapUtil;
import gesac.com.uitity.WarnSPlayer;

public class StockActivity extends Activity {
    private final String TAG = "StockActivity debug";
    private ActivityStockBinding binding;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock);
        binding.stcodeEt.setInputType(InputType.TYPE_NULL);
        type = getIntent().getIntExtra("type", -1);
        Log.i(TAG, "initViews: type: " + type);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            binding.stcodeEt.requestFocus();
            binding.stcodeEt.selectAll();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            String str = binding.stcodeEt.getText().toString();
            binding.stcodeEt.setText("");
            Map<String, String> maps = CodeUtil.subStCode(str);
            if (maps != null) {
                LoadDialog.showDialog(this, "请稍等");
                new SoapAsync().execute(maps.get("st"), maps.get("slc"));
            } else WarnSPlayer.playsound(this, R.raw.codeerr);
        }
        return super.onKeyUp(keyCode, event);
    }

    private List<Item> parseInfo(SoapObject result) {
        List<Item> itemList = new ArrayList<>();
        try {
            SoapObject items = (SoapObject) result.getProperty(0);
            for (int i = 0; i < items.getPropertyCount(); i++) {
                SoapObject it = (SoapObject) items.getProperty(i);
                itemList.add(new Item(
                        it.getPrimitiveProperty("ItemId").toString(),
                        it.getPrimitiveProperty("InventSizeId").toString(),
                        it.getPrimitiveProperty("InventColorId").toString(),
                        it.getPrimitiveProperty("inventBatchId").toString(),
                        "",
                        it.getPrimitiveProperty("OnHander").toString().replaceAll("-", ""),
                        "",
                        it.getPrimitiveProperty("InventLocationId").toString(),
                        it.getPrimitiveProperty("WMSLocationId").toString()
                ));
            }
        } catch (ClassCastException e) {
            Toast.makeText(this, "请检查服务器设置", Toast.LENGTH_LONG);
        }
        return itemList;
    }

    class SoapAsync extends AsyncTask<String, Integer, SoapObject> {
        @Override
        protected SoapObject doInBackground(String... strings) {
            Map<String, String> map = new HashMap<>();
            map.put("InventLocationId", strings[0]);
            map.put("WMSLocationId", strings[1]);
            SoapObject info = SoapUtil.getERPinfo("GetInvetnSum_Simple", map);
            return info;
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            super.onPostExecute(result);
            if (result != null) {
                List<Item> itemList = parseInfo(result);
                Intent it = new Intent(StockActivity.this, ChkStockActivity.class);
                it.putExtra("itlist", new Gson().toJson(itemList));
                it.putExtra("type", type);
                startActivity(it);
            }
            LoadDialog.cancelDialog();
        }
    }
}