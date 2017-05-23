package gesac.com.chkstock;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import java.util.List;

import gesac.com.BR;
import gesac.com.R;
import gesac.com.databinding.ActivityChkStockBinding;
import gesac.com.scanbag.model.Item;

public class ChkStockActivity extends Activity {
    List<Item> itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    void initView(){
        ActivityChkStockBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_chk_stock);
        binding.setStockadapter(new StockAdapter(this,itemList, BR.item));

    }
}
