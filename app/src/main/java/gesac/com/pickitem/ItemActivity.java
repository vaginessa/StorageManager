package gesac.com.pickitem;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;

import com.google.gson.Gson;

import gesac.com.BR;
import gesac.com.R;
import gesac.com.databinding.ActivityItemBinding;
import gesac.com.scanbag.model.Journal;

public class ItemActivity extends Activity {
    ActivityItemBinding binding;

    Journal journal;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item);
        Intent it = getIntent();
        journal = new Gson().fromJson(it.getStringExtra("jour"), Journal.class);

        binding.itemstrEt.setInputType(InputType.TYPE_NULL);
        binding.jouridTv.setText(journal.getJourid());
        adapter = new ItemAdapter(this, BR.item, journal.getItemlist());
        binding.setItemadapter(adapter);

    }
}
