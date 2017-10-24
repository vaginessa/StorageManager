package gesac.com.pickitem.prod

import android.app.AlertDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import gesac.com.R
import gesac.com.databinding.ItemlistBinding
import gesac.com.databinding.SplitdialogBinding
import gesac.com.model.RespPOJO
import gesac.com.pickitem.model.Item
import gesac.com.splitbag.model.IBag
import gesac.com.uitity.LoadDialog
import gesac.com.uitity.PrintUtil
import gesac.com.uitity.WarnSPlayer
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by GE11522 on 2017/9/26.
 */
class ItemAdapter(val context: Context, private var itemList: MutableList<Item>) : BaseAdapter() {
    private val tag = "ItemAdapter.debug"
    var type: Int = 0
    var iBag: IBag? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val bind: ItemlistBinding
        if (view == null) {
            bind = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.itemlist, parent, false)
            view = bind.root
        } else {
            bind = DataBindingUtil.getBinding(view)
        }
        bind.item = itemList[position]
        bind.setOnPickBtClickListener { _ ->
            if (iBag != null) {
                val bindsp: SplitdialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.splitdialog, null, false)
                val qty = finSame(itemList[position], position).sumBy { itemList[it].itemqty.toInt() }
                val scanQty = iBag!!.pctqty
                bindsp.qty = qty.toString()
                bindsp.scanqty = scanQty.toString()
                AlertDialog.Builder(context)
                        .setTitle("拆包")
                        .setView(bindsp.root)
                        .setCancelable(false)
                        .setPositiveButton("打印", ({ _, _ ->
                            val div = bindsp.divnum.text.toString()
                            if (!div.isEmpty()) {
                                LoadDialog.showDialog(context, "打印中")
                                printTask(div, position)
                            } else Toast.makeText(context, "请输入拆分数量", Toast.LENGTH_SHORT).show()
                        }))
                        .setNegativeButton("取消", null)
                        .create()
                        .show()
            } else {
                Toast.makeText(context, "请先扫码", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun finSame(item: Item, position: Int): Set<Int> {
        val list: MutableSet<Int> = mutableSetOf()
        list.add(position)
        (0 until count).filterTo(list) { itemList[it] == item }
        return list
    }

    private fun setSplit(position: Int) {
        itemList[position].isSplit = true
        notifyDataSetChanged()
    }

    fun setIn(position: Int): Boolean {
        itemList[position].isin = 1
        notifyDataSetChanged()
        return true
    }

    private fun printTask(div: String, position: Int) {
        Observable.create(ObservableOnSubscribe<RespPOJO<Any>> { e ->
            val result = PrintUtil.doPickPrint(type, div, iBag)
            e.onNext(result)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    var msg = result.msg
                    if (result.code == 0) {
                        val plist = finSame(itemList[position], position)
                        for (i in plist)
                            setSplit(i)
                        WarnSPlayer.playsound(context, R.raw.printscd)
                    } else
                        WarnSPlayer.playsound(context, R.raw.printerr)
                    LoadDialog.cancelDialog()
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                })
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

}