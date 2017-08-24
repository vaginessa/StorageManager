package gesac.com.login;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import gesac.com.R;
import gesac.com.databinding.ActivityLoginBinding;
import gesac.com.uitity.CodeUtil;

public class LoginActivity extends Activity {
    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    void initViews(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeUtil.numid = binding.numidEt.getText().toString();
            }
        });
    }
}
