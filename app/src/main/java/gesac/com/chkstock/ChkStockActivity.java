package gesac.com.chkstock;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import gesac.com.BR;
import gesac.com.R;
import gesac.com.databinding.ActivityChkStockBinding;
import gesac.com.pickitem.model.Item;
import gesac.com.splitbag.model.IBag;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.WarnSPlayer;

/**
 *
 */
public class ChkStockActivity extends Activity {
    private final String TAG = "ChkStockActivity debug";
    private ActivityChkStockBinding binding;
    private StockAdapter skadapter;

    private int type; //判断是产品还是五金物料
    private int codetype = -1;
    private List<Item> itemList;
    private IBag iBag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    void initViews() {
        iBag = null;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chk_stock);
        binding.codestrEt.setInputType(InputType.TYPE_NULL);
        Intent it = getIntent();
        itemList = new Gson().fromJson(it.getStringExtra("itlist"), new TypeToken<List<Item>>() {
        }.getType());
        type = it.getIntExtra("type", -1);
        Log.i(TAG, "initViews: type: " + type);
        skadapter = new StockAdapter(ChkStockActivity.this, itemList, BR.item);
        binding.setStockadapter(skadapter);
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
            pctCheck();
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 产品盘点功能
     */
    private void pctCheck() {
        String str = binding.codestrEt.getText().toString();
        binding.codestrEt.setText("");
        if(!str.isEmpty()) {
            codetype = CodeUtil.whichType(str);
            iBag = CodeUtil.subCode(str);
            if (iBag == null)
                WarnSPlayer.playsound(this, R.raw.codeerr);
            else {
                int i = CodeUtil.isInItems(codetype, iBag, skadapter.getItemList());
                if (i != -1) {
                    if (Integer.parseInt(skadapter.getItemList().get(i).getItemqty()) != 0) { //该物料数量未盘点完毕
                        WarnSPlayer.playsound(this, R.raw.matchscd);
                        skadapter.cutItemqty(i, iBag.getPctqty());
                        if (i != 0) skadapter.swapItem(0, i);
                    } else if (Integer.parseInt(skadapter.getItemList().get(i).getItemqty()) == 0)
                        WarnSPlayer.playsound(this, R.raw.chkscd);
                } else {
                    WarnSPlayer.playsound(this, R.raw.error);
                }
            }
        }
    }
}