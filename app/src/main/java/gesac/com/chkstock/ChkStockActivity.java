package gesac.com.chkstock;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gesac.com.BR;
import gesac.com.R;
import gesac.com.databinding.ActivityChkStockBinding;
import gesac.com.scanbag.model.Item;
import gesac.com.splitbag.model.IBag;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.LoadDialog;
import gesac.com.uitity.SoapUtil;
import gesac.com.uitity.WarnSPlayer;

public class ChkStockActivity extends Activity {
    private ActivityChkStockBinding binding;
    private StockAdapter skadapter;

    private Map<String, String> stmap;
    private List<Item> itemList;
    private IBag iBag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chk_stock);
        Intent it = getIntent();
        stmap = new Gson().fromJson(it.getStringExtra("stinfo"), Map.class);
        binding.codestrEt.setInputType(InputType.TYPE_NULL);
        new SoapAsync().execute(stmap.get("st"), stmap.get("slc"));
        LoadDialog.showDialog(ChkStockActivity.this, "请稍等");
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
            String str = binding.codestrEt.getText().toString();
            iBag = CodeUtil.subItemCode(str);
            binding.codestrEt.setText("");
            if (iBag == null)
                WarnSPlayer.playsound(this, R.raw.codeerr);
            else {
                int i = CodeUtil.isInItemls(iBag, skadapter.getItemList());
                if (i != -1) {
                    WarnSPlayer.playsound(this, R.raw.matchscd);
                    skadapter.cutItemqty(i, iBag.getPctqty());
                    if (i != 0) skadapter.swapItem(0, i);
                } else {
                    WarnSPlayer.playsound(this, R.raw.error);
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private List<Item> parseInfo(SoapObject result) {
        List<Item> itemList = new ArrayList<>();
        if (result != null) {
            try {
                SoapObject items = (SoapObject) result.getProperty(0);
                for (int i = 0; i < items.getPropertyCount(); i++) {
                    SoapObject it = (SoapObject) items.getProperty(i);
                    itemList.add(new Item(
                            it.getPrimitiveProperty("ItemId").toString(),
                            it.getPrimitiveProperty("InventColorId").toString(),
                            it.getPrimitiveProperty("InventSizeId").toString(),
                            it.getPrimitiveProperty("inventBatchId").toString(),
                            "",
                            it.getPrimitiveProperty("OnHander").toString(),
                            "",
                            it.getPrimitiveProperty("InventLocationId").toString(),
                            it.getPrimitiveProperty("WMSLocationId").toString()
                    ));
                }
            } catch (ClassCastException e) {
                Toast.makeText(this, "请检查服务器设置", Toast.LENGTH_LONG);
            }
            return itemList;
        } else return null;
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
            itemList = parseInfo(result);

            skadapter = new StockAdapter(ChkStockActivity.this, itemList, BR.item);
            binding.setStockadapter(skadapter);
            LoadDialog.cancelDialog();
        }
    }
}
