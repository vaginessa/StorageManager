package gesac.com.pickitem.prod

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import com.google.gson.Gson
import gesac.com.R
import gesac.com.databinding.ActivityItemBinding
import gesac.com.pickitem.model.Journal
import gesac.com.uitity.BaseActivity
import gesac.com.uitity.CodeUtil
import gesac.com.uitity.WarnSPlayer

class ItemActivity : BaseActivity<ActivityItemBinding>() {
    private lateinit var journal: Journal
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(R.layout.activity_item)
    }

    override fun initialize(LayoutId: Int) {
        super.initialize(LayoutId)
        val it = intent //获取得到的日记账数据Intent
        journal = Gson().fromJson(it.getStringExtra("jour"), Journal::class.java)
        Log.d(tag, journal.itemlist.size.toString())
        Log.d(tag, journal.jourid)

        bind.jouridTv.text = journal.jourid
        adapter = ItemAdapter(this, journal.itemlist)
        bind.itemlist.adapter = adapter
        bind.itemstrEt.inputType = InputType.TYPE_NULL
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            bind.itemstrEt.requestFocus()
            bind.itemstrEt.selectAll()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F8) {
            val strcode = bind.itemstrEt.text.toString()
            bind.itemstrEt.setText("")
            //转化扫描到的二维码
            val iBag = CodeUtil.subCode(strcode)
            adapter.iBag = iBag
            //判断二维码的类型
            val codetype = CodeUtil.whichType(strcode)
            adapter.type = codetype
            if (adapter.iBag != null) {//判断物料是否在该日记账内，在则打印按钮可按
                val position = CodeUtil.isInItems(codetype, adapter.iBag, journal.itemlist)
                if (position != -1) {
                    WarnSPlayer.playsound(this, R.raw.matchscd)
                    adapter.setIn(position)
                    bind.itemlist.setSelection(position - 1)
                } else {
                    WarnSPlayer.playsound(this, R.raw.error)
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }
}
