package gesac.com.pickitem;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;

import com.google.gson.Gson;

import gesac.com.BR;
import gesac.com.R;
import gesac.com.databinding.ActivityItemBinding;
import gesac.com.scanbag.model.Journal;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.WarnSPlayer;

public class ItemActivity extends Activity {
    ActivityItemBinding binding;

    Journal journal;
    ItemAdapter adapter;

    private int codetype = -1;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            binding.itemstrEt.requestFocus();
            binding.itemstrEt.selectAll();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            // 1、获取二维码2、拆分字符串3、匹配物料编号
            String strcode = binding.itemstrEt.getText().toString();
            binding.itemstrEt.setText("");
            adapter.setiBag(CodeUtil.subCode(strcode));
            codetype = CodeUtil.whichType(strcode);
            adapter.setType(codetype);
            //TODO iBag与list比较
            if (adapter.getiBag() != null) {
                int position = CodeUtil.isInItems(codetype, adapter.getiBag(), journal.getItemlist());
                if (position != -1) {
                    //TODO 若匹配则按钮可按
                    WarnSPlayer.playsound(this, R.raw.matchscd);
                    adapter.setIn(position);
                    binding.itemlist.setSelection(position - 1);
                } else {
                    WarnSPlayer.playsound(this, R.raw.error);
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
