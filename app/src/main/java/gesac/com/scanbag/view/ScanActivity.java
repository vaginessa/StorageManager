package gesac.com.scanbag.view;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import gesac.com.R;
import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.presenter.IScanPresenter;
import gesac.com.splitbag.model.IBag;
import gesac.com.splitbag.presenter.ISplitPresenter;

public class ScanActivity extends AppCompatActivity implements IScanView {
    private EditText mJourid, mItemstr;
    private Button mSearch;
    private ListView mItemlist;

    private String strcode;
    private IScanPresenter iScanPresenter;
    private ISplitPresenter iSplitPresenter;
    private IBag iBag;
    private IJournal iJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        bindViews();
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
            iBag = iSplitPresenter.subString(strcode);
            //TODO iBag与list比较
            if (iScanPresenter.isInJour(iBag, iJournal)) {
                //TODO 若匹配则变色
            } else new AlertDialog.Builder(this)
                    .setMessage("匹配错误")
                    .setPositiveButton("确定", null)
                    .show();

            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

    private void bindViews() {
        mJourid = (EditText) findViewById(R.id.jourid);
        mSearch = (Button) findViewById(R.id.search);
        mItemstr = (EditText) findViewById(R.id.itemstr);
        mItemlist = (ListView) findViewById(R.id.itemlist);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 通过webservice搜索日记账

            }
        });


    }
}
