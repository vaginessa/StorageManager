package gesac.com.uitity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding

/**
 * Created by GE11522 on 2017/9/18.
 */
abstract class BaseActivity<T : ViewDataBinding> : Activity() {
    protected lateinit var tag: String
    protected lateinit var bind: T
    protected lateinit var sApp: SysApplication

    protected open fun initialize(LayoutId: Int) {
        bind = DataBindingUtil.setContentView(this, LayoutId)
        sApp = application as SysApplication
        tag = this.localClassName + ".debug"
    }
}