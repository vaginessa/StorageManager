package gesac.com.chkstock;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;

import gesac.com.R;
import gesac.com.databinding.ActivityHdwStockBinding;

public class HdwStockActivity extends Activity {
    ActivityHdwStockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hdw_stock);
        binding.stcodeEt.setInputType(InputType.TYPE_NULL);

    }
}
