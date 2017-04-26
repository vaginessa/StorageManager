package gesac.com.scanbag.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import gesac.com.R;
import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.model.Journal;
import gesac.com.scanbag.presenter.IItemPresenter;
import gesac.com.scanbag.presenter.ItemPresenterCompl;
import gesac.com.splitbag.model.IBag;
import gesac.com.uitity.LoadDialog;

public class ItemActivity extends AppCompatActivity implements IItemVIew {
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
        Log.d("debug", String.valueOf(iJournal.getItemlist().size()));
        iItemPresenter = new ItemPresenterCompl(this);
        bindViews();
    }

    private void bindViews() {
        mItemstr = (EditText) findViewById(R.id.itemstr);
        mJourid = (TextView) findViewById(R.id.jourid);
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
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            // 1、获取二维码2、拆分字符串3、匹配物料编号
            mItemstr.postInvalidate();
            strcode = mItemstr.getText().toString();
            iBag = iItemPresenter.subString(strcode);
            //TODO iBag与list比较
            if (iBag != null) {
                int position = iItemPresenter.isInJour(iBag, iJournal);
//                int isin = 1;
                if (position != -1) {
                    //TODO 若匹配则按钮可按
                    iJournal.getItemlist().get(position).setIsin(1);
                    adapter.notifyDataSetChanged();
                    mItemlist.setSelection(position);
                } else showAlert("匹配错误");
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
