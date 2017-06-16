package gesac.com.locstock;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;

import java.util.regex.Matcher;

import gesac.com.R;
import gesac.com.databinding.ActivityLocStockBinding;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.WarnSPlayer;

public class LocStockActivity extends Activity {
    ActivityLocStockBinding binding;

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
            Matcher m = CodeUtil.pattern.matcher(s);
            if (m.matches()){
                WarnSPlayer.playsound(this,R.raw.matchscd);
            }else WarnSPlayer.playsound(this,R.raw.error);
//            binding.codestrEt.setText("");
        }
        return super.onKeyUp(keyCode, event);
    }
}
