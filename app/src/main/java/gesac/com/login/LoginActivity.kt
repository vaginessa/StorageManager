package gesac.com.login

import android.content.Intent
import android.os.Bundle
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import gesac.com.R
import gesac.com.databinding.ActivityLoginBinding
import gesac.com.home.view.HomeActivity
import gesac.com.uitity.Alertdlg
import gesac.com.uitity.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(R.layout.activity_login)
    }

    override fun initialize(LayoutId: Int) {
        super.initialize(LayoutId)

        bind.loginBt.setOnClickListener({
            val id = bind.userEt.text.toString()
            val pw = bind.pwEt.text.toString()
            if (!id.isEmpty() && !pw.isEmpty()) {
                user = User("GE" + id, pw)
                LoginRetrofit(user)
            } else {
                bind.userEt.error = "不可为空"
                bind.userEt.requestFocus()
            }
        })

    }

    private fun LoginRetrofit(user: User) {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.30.100.22:3066/AdVerify/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val login = retrofit.create(User.Login::class.java)
        login.signUp("Ge\\" + user.id, user.pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if ("身份验证成功!" == s) {
                        startActivity(Intent(this@LoginActivity,
                                HomeActivity::class.java))
                        sApp.user = user
                    } else {
                        bind.userEt.error = s
                        bind.userEt.requestFocus()
                    }
                }) { throwable ->
                    Alertdlg.showDialog(this@LoginActivity,
                            throwable.message)
                }
    }

}
