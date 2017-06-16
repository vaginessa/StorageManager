package gesac.com.chkstock;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;

import com.google.gson.Gson;

import java.util.Map;

import gesac.com.R;
import gesac.com.databinding.ActivityStockBinding;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.WarnSPlayer;

public class StockActivity extends Activity {
    private ActivityStockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock);
        binding.stcodeEt.setInputType(InputType.TYPE_NULL);
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
            Map<String, String> maps = CodeUtil.subStCode(str);
            if (maps != null) {
                Intent it = new Intent(this, ChkStockActivity.class);
                it.putExtra("stinfo", new Gson().toJson(maps));
                startActivity(it);
                binding.stcodeEt.setText("");
            }else WarnSPlayer.playsound(this,R.raw.codeerr);
        }
        return super.onKeyUp(keyCode, event);
    }
}
