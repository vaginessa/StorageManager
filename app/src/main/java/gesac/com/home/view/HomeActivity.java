package gesac.com.home.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import gesac.com.R;
import gesac.com.chkstock.HdwStockActivity;
import gesac.com.chkstock.StockActivity;
import gesac.com.databinding.ActivityHomeBinding;
import gesac.com.locstock.LocStockActivity;
import gesac.com.pickitem.hardware.HdwPickActivity;
import gesac.com.pickitem.prod.ProdPickActivity;
import gesac.com.splitbag.view.SplitActivity;

public class HomeActivity extends Activity {
    // Content View Elements
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        binding.scanHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] params = {"产品领料", "五金领料"};
                new AlertDialog.Builder(HomeActivity.this)
                        .setItems(params, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent it;
                                switch (which) {
                                    case 0:
                                        it = new Intent(HomeActivity.this, ProdPickActivity.class);
                                        startActivity(it);
                                        break;
                                    case 1:
                                        it = new Intent(HomeActivity.this, HdwPickActivity.class);
                                        startActivity(it);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
        binding.chkHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] params = {"产品盘点", "五金盘点"};
                new AlertDialog.Builder(HomeActivity.this)
                        .setItems(params, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent it;
                                switch (which) {
                                    case 0:
                                        it = new Intent(HomeActivity.this, StockActivity.class);
                                        it.putExtra("type",0);
                                        startActivity(it);
                                        break;
                                    case 1:
                                        it = new Intent(HomeActivity.this, HdwStockActivity.class);
                                        it.putExtra("type",1);
                                        startActivity(it);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
        binding.locHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeActivity.this, LocStockActivity.class);
                startActivity(it);
            }
        });

        binding.pickHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeActivity.this, SplitActivity.class);
                startActivity(it);
            }
        });
    }
}