package gesac.com.scanbag.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gesac.com.dividepackage.R;

public class ScanActivity extends AppCompatActivity implements IScanView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }
}
