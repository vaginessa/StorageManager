package gesac.com.splitbag.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gesac.com.R;
import gesac.com.model.RespPOJO;
import gesac.com.splitbag.model.IBag;
import gesac.com.splitbag.presenter.ISplitPresenter;
import gesac.com.splitbag.presenter.SplitPresenterCompl;
import gesac.com.uitity.Alertdlg;
import gesac.com.uitity.CodeUtil;
import gesac.com.uitity.PrintUtil;
import gesac.com.uitity.StatusBox;

public class SplitActivity extends Activity implements ISplitView {
    StatusBox statusBox;
    ISplitPresenter iSplitPresenter;
    private EditText ecode, epctid, epctbc, epcthv, epctnum, epctdinum;
    private String spctnum = "", spctdinum = "";
    private Button bprint;
    private String codestr;
    private int type;
    private IBag bag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        bindView();
        iSplitPresenter = new SplitPresenterCompl(this);
    }

    private void bindView() {
        ecode = (EditText) findViewById(R.id.code);
        epctid = (EditText) findViewById(R.id.pctid);
        epctbc = (EditText) findViewById(R.id.pctbc);
        epcthv = (EditText) findViewById(R.id.pcthv);
        epctnum = (EditText) findViewById(R.id.pctnum);
        epctdinum = (EditText) findViewById(R.id.pctdinum);

        bprint = (Button) findViewById(R.id.print);
        statusBox = new StatusBox(this, bprint);

        ecode.setInputType(InputType.TYPE_NULL);

        epctdinum.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_F8) {
                    epctdinum.setText("");
                }
                return false;
            }
        });

        bprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spctdinum = epctdinum.getText().toString();
                spctnum = epctnum.getText().toString();
                if (!spctdinum.isEmpty() && !spctnum.isEmpty()) {
                    showStatbox("正在打印......");
                    RespPOJO<Object> result = PrintUtil.doPickPrint(type, spctdinum, bag);
                    closeStatbox();
                    if (result.getCode() != 0)
                        Alertdlg.showDialog(SplitActivity.this, result.getMsg());
//                    iSplitPresenter.doPrint(spctdinum);
                } else
                    Toast.makeText(SplitActivity.this, "请扫码并输入拆分数量", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            ecode.requestFocus();
            ecode.selectAll();
            return super.onKeyUp(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            ecode.postInvalidate();
            codestr = ecode.getText().toString();
            if (!codestr.isEmpty()) {
                Log.i("message", "onKeyDown: " + codestr);
                bag = iSplitPresenter.subString(codestr);
                type = CodeUtil.whichType(codestr);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void fillEdt(IBag ibag) {
        epctid.setText(ibag.getPctid());
        epctbc.setText(ibag.getPctbc());
        epcthv.setText(ibag.getPcthv());
        epctnum.setText(ibag.getPctqty());
        epctdinum.setText("");
        epctid.postInvalidate();
        epctbc.postInvalidate();
        epcthv.postInvalidate();
        epctnum.postInvalidate();
    }

    @Override
    public void clearEdt() {
        epctid.setText("");
        epctbc.setText("");
        epcthv.setText("");
        epctnum.setText("");
        epctdinum.setText("");
        epctid.postInvalidate();
        epctbc.postInvalidate();
        epcthv.postInvalidate();
        epctnum.postInvalidate();
    }

    @Override
    public void showStatbox(String str) {
        statusBox.Show(str);
    }

    @Override
    public void closeStatbox() {
        statusBox.Close();
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
