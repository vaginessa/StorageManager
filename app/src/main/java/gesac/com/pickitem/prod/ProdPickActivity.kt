package gesac.com.pickitem.prod

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import gesac.com.R
import gesac.com.databinding.ActivityScanBinding
import gesac.com.pickitem.JourAdapter
import gesac.com.pickitem.model.Item
import gesac.com.pickitem.model.Journal
import gesac.com.uitity.Alertdlg
import gesac.com.uitity.BaseActivity
import gesac.com.uitity.LoadDialog
import gesac.com.uitity.SoapUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.ksoap2.serialization.SoapObject
import java.util.*


class ProdPickActivity : BaseActivity<ActivityScanBinding>() {
    private var iJournals: List<Journal>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(R.layout.activity_scan)
    }

    override fun initialize(LayoutId: Int) {
        super.initialize(LayoutId)

        bind.search.setOnClickListener({
            val jid = bind.jourid.text.toString()
            LoadDialog.showDialog(this@ProdPickActivity, "请稍等")
            getJourList("*$jid*")
        })

        bind.jourlist.setOnItemClickListener { _, _, position, _ ->
            val it = Intent(this@ProdPickActivity, ItemActivity::class.java)
            val jstr = Gson().toJson(iJournals!![position])
            Log.d("debug", jstr)
            it.putExtra("jour", jstr)
            startActivity(it)
        }
    }

    private fun parseInfo(result: SoapObject?): List<Journal>? { //将获取到的数据转化为日记账List
        val journals = ArrayList<Journal>()
        var items: MutableList<Item>
        if (result != null) {
            try {
                val sjourlist = result.getProperty("GetJournalTransResult") as SoapObject
                for (i in 0 until sjourlist.propertyCount) {
                    val jourlist = sjourlist.getProperty(i) as SoapObject
                    val itemlist = jourlist.getProperty("items") as SoapObject
                    items = (0 until itemlist.propertyCount)
                            .map { itemlist.getProperty(it) as SoapObject }
                            .mapTo(ArrayList()) {
                                Item(it.getPrimitiveProperty("itemid").toString(),
                                        it.getPrimitiveProperty("itemqlty").toString(),
                                        it.getPrimitiveProperty("itemtol").toString(),
                                        it.getPrimitiveProperty("itembc").toString(),
                                        it.getPrimitiveProperty("itemseri").toString(),
                                        it.getPrimitiveProperty("itemqty").toString().replace("-".toRegex(), ""),
                                        it.getPrimitiveProperty("itemrqty").toString(),
                                        it.getPrimitiveProperty("itemst").toString(),
                                        it.getPrimitiveProperty("itemslc").toString())
                            }
                    journals.add(Journal(jourlist.getProperty("jourid").toString(), items))
                }
            } catch (e: ClassCastException) {
                Toast.makeText(this, "请检查服务器设置", Toast.LENGTH_LONG)
            }
            return journals
        } else
            return null
    }

    fun getJourList(jourId: String) {//获取远程数据
        Observable.create(ObservableOnSubscribe<SoapObject> { e ->
            val map = mapOf(Pair("journalId", jourId))
            val so: SoapObject? = SoapUtil.getERPinfo("GetJournalTrans", map)
            if (so != null)
                e.onNext(so)
            else Alertdlg.showDialog(this@ProdPickActivity, "获取日记账号失败")
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ so ->
                    iJournals = parseInfo(so)
                    val jourAdapter = JourAdapter(this@ProdPickActivity, iJournals)
                    bind.jourlist.adapter = jourAdapter
                    LoadDialog.cancelDialog()
                })
    }
}