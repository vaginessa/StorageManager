package gesac.com.scanbag.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import gesac.com.R;
import gesac.com.pickitem.model.IJournal;
import gesac.com.pickitem.model.Journal;
import gesac.com.scanbag.presenter.IItemPresenter;
import gesac.com.scanbag.presenter.ItemPresenterCompl;
import gesac.com.splitbag.model.IBag;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.LoadDialog;
import gesac.com.uitity.WarnSPlayer;

public class ItemActivity extends Activity implements IItemVIew {
    private static String TAG = "ItemActivity debug";
    private EditText mItemstr;
    private TextView mJourid;
    private ListView mItemlist;

    private IItemPresenter iItemPresenter;
    private IBag iBag;
    private IJournal iJournal;
    private String strcode;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent it = getIntent();
        iJournal = new Gson().fromJson(it.getStringExtra("jour"), Journal.class);
        Log.d(TAG, String.valueOf(iJournal.getItemlist().size()));
        Log.d(TAG, iJournal.getJourid());

        iItemPresenter = new ItemPresenterCompl(this);
        bindViews();
    }

    private void bindViews() {
        mItemstr = (EditText) findViewById(R.id.itemstr_et);
        mJourid = (TextView) findViewById(R.id.jourid_tv);
        mItemlist = (ListView) findViewById(R.id.itemlist);
        mJourid.setText(iJournal.getJourid());

        adapter = new ItemAdapter(this, iItemPresenter, iJournal.getItemlist());
        mItemlist.setAdapter(adapter);
        mItemstr.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            mItemstr.requestFocus();
            mItemstr.selectAll();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            // 1、获取二维码2、拆分字符串3、匹配物料编号
            mItemstr.postInvalidate();
            strcode = mItemstr.getText().toString();
            iBag = CodeUtil.subIBagCode(strcode);
            mItemstr.setText("");
            //TODO iBag与list比较
            if (iBag != null) {
                int position = CodeUtil.isBgInItemls(iBag, iJournal.getItemlist());
                Log.i(TAG, "onKeyUp: " + "position: " + position);
//                int isin = 1;
                if (position != -1) {
                    //TODO 若匹配则按钮可按
                    WarnSPlayer.playsound(this, R.raw.matchscd);
                    adapter.setIn(position);
                    mItemlist.setSelection(position - 1);
                } else {
                    WarnSPlayer.playsound(this, R.raw.error);
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void showLoad(String str) {
        LoadDialog.showDialog(this, str);
    }

    @Override
    public void closeLoad() {
        LoadDialog.cancelDialog();
    }

    @Override
    public void showAlert(String str) {
        new AlertDialog.Builder(this)
                .setMessage(str)
                .setPositiveButton("确定", null)
                .setCancelable(false)
                .show();
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openBluetooth(Intent it) {
        startActivityForResult(it, 2);
    }
}
