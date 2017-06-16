package gesac.com.scanbag.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gesac.com.R;
import gesac.com.scanbag.model.Item;
import gesac.com.scanbag.presenter.IItemPresenter;
import gesac.com.uitity.LoadDialog;
import gesac.com.uitity.WarnSPlayer;

/**
 * Created by GE11522 on 2017/4/17.
 */

public class ItemAdapter extends BaseAdapter {
    List<Item> itemlist = new ArrayList<>();
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

    public boolean setIn(int position) {
        itemlist.get(position).setIsin(1);
        notifyDataSetChanged();
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHold hold;
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
            hold.splitBt = (Button) convertView.findViewById(R.id.split_bt);
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
//                            Map<String, String> map = new HashMap<>();
                            if (!div.isEmpty()) {
//                                map.put("div", div);
//                                map.put("position", String.valueOf(position));
                                splitBagTask sptask = new splitBagTask();
                                sptask.execute(div);
                                sptask.setOnAsyncRespones( new AsyncRespones() {
                                    @Override
                                    public void onDataReceivedSuccess(int result) {
                                        if (result == 0) removeItem(position);
                                    }
                                });
                                LoadDialog.showDialog(context, "打印中");
                            } else
                                Toast.makeText(context, "请输入拆分数量", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .setNegativeButton("取消", null)
                            .setCancelable(false)
                            .create()
                            .show();
                }
            });
        } else hold.splitBt.setEnabled(false);
        return convertView;
    }

    public void removeItem(int position) {
        itemlist.remove(position);
        notifyDataSetChanged();
    }

    private interface AsyncRespones {
        void onDataReceivedSuccess(int result);
    }

    class ViewHold {
        TextView idView, bcView, seriView, qltyView, tolView, qtyView, wtView, stView, slcView;
        Button splitBt;
    }

    class splitBagTask extends AsyncTask<String, Integer, Integer> {

        private AsyncRespones asyncRespones;

        public void setOnAsyncRespones(AsyncRespones asyncRespones) {
            this.asyncRespones = asyncRespones;
        }

        @Override
        protected Integer doInBackground(String... params) {
            int result = 9999;
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
        protected void onPostExecute(Integer result) {
            asyncRespones.onDataReceivedSuccess(result);
            String msg = "";
            switch (result) {
                case 0:
                    msg = "打印成功";
                    WarnSPlayer.playsound(context, R.raw.printscd);
                    break;
                case -1:
                    msg = "打印失败！请检查与打印机的连接是否正常";
                    WarnSPlayer.playsound(context, R.raw.printerr);
                    break;
                case 1:
                    msg = "打印失败！打印机纸仓盖开";
                    WarnSPlayer.playsound(context, R.raw.printerr);
                    break;
                case 2:
                    msg = "打印失败！打印机缺纸";
                    WarnSPlayer.playsound(context, R.raw.printerr);
                    break;
                case 3:
                    msg = "创建打印页面失败";
                    WarnSPlayer.playsound(context, R.raw.printerr);
                    break;
                case 4:
                    msg = "打印失败！打印头过热";
                    WarnSPlayer.playsound(context, R.raw.printerr);
                    break;
                case 5:
                    msg = "请连接打印机";
                    WarnSPlayer.playsound(context, R.raw.printerr);
                    break;
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            LoadDialog.cancelDialog();
        }
    }
}
