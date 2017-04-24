package gesac.com.scanbag.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import gesac.com.R;
import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.model.Item;
import gesac.com.scanbag.presenter.IItemPresenter;
import gesac.com.scanbag.presenter.ItemPresenterCompl;
import gesac.com.splitbag.model.Bag;
import gesac.com.splitbag.model.IBag;
import gesac.com.splitbag.view.SplitActivity;
import gesac.com.uitity.LoadDialog;

import static gesac.com.R.id.num;

/**
 * Created by GE11522 on 2017/4/17.
 */

public class ItemAdapter extends BaseAdapter {
    List<Item> itemlist;
    private IItemPresenter iItemPresenter;
    private LayoutInflater mInflater;
    private Context context;

    public ItemAdapter(Context context, IItemPresenter iItemPresenter, List<Item> itemlist) {
        this.itemlist = itemlist;
        this.iItemPresenter = iItemPresenter;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return itemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHold {
        TextView idView, bcView, seriView, qltyView, tolView, qtyView, wtView, stView, slcView;
        Button splitBt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold hold;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = mInflater.inflate(R.layout.itemlist, null);
            hold.idView = (TextView) convertView.findViewById(R.id.itemid);
            hold.bcView = (TextView) convertView.findViewById(R.id.itembc);
            hold.seriView = (TextView) convertView.findViewById(R.id.itemseri);
            hold.qltyView = (TextView) convertView.findViewById(R.id.itemqlty);
            hold.tolView = (TextView) convertView.findViewById(R.id.itemtol);
            hold.qtyView = (TextView) convertView.findViewById(R.id.itemqty);
            hold.wtView = (TextView) convertView.findViewById(R.id.itemwt);
            hold.stView = (TextView) convertView.findViewById(R.id.itemst);
            hold.slcView = (TextView) convertView.findViewById(R.id.itemslc);
            hold.splitBt = (Button) convertView.findViewById(R.id.splitbt);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.idView.setText(itemlist.get(position).getItemid());
        hold.bcView.setText(itemlist.get(position).getItembc());
        hold.seriView.setText(itemlist.get(position).getItemseri());
        hold.qltyView.setText(itemlist.get(position).getItemqlty());
        hold.tolView.setText(itemlist.get(position).getItemtol());
        hold.qtyView.setText(itemlist.get(position).getItemqty());
        hold.wtView.setText(itemlist.get(position).getItemwt());
        hold.stView.setText(itemlist.get(position).getItemst());
        hold.slcView.setText(itemlist.get(position).getItemslc());
        if (itemlist.get(position).getIsin() != 0) {
            IBag bag = new Bag(itemlist.get(position).getItemid(),
                    itemlist.get(position).getItemtol(),
                    itemlist.get(position).getItemqlty(),
                    itemlist.get(position).getItembc(),
                    itemlist.get(position).getItemqty(),
                    "");
            iItemPresenter.initBag(bag);
            hold.splitBt.setEnabled(true);
            hold.splitBt.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    //TODO 拆包操作
                    AlertDialog.Builder aldg = new AlertDialog.Builder(context);
                    final View v = mInflater.inflate(R.layout.splitdialog, null);
                    final TextView tv = (TextView) v.findViewById(R.id.num);
                    final EditText et = (EditText) v.findViewById(R.id.divnum);
                    tv.setText(itemlist.get(position).getItemqty());
                    aldg.setCancelable(false)
                            .setTitle("拆包")
                            .setView(v);
                    aldg.setPositiveButton("打印", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String div = et.getText().toString();
                            if (!div.isEmpty()) {
                                new splitBagTask().execute(div);
                                LoadDialog.showDialog(context, "打印中");
                            } else
                                Toast.makeText(context, "请输入拆分数量", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                }
            });
        } else hold.splitBt.setEnabled(false);


        return convertView;
    }

    class splitBagTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                result = iItemPresenter.doPrint(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将结果返回给onPostExecute方法
            return result;
        }

        @Override
        //此方法可以在主线程改变UI
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            LoadDialog.cancelDialog();
        }
    }
}
