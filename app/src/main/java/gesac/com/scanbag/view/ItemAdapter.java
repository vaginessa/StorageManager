package gesac.com.scanbag.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import gesac.com.R;
import gesac.com.scanbag.model.Item;

/**
 * Created by GE11522 on 2017/4/17.
 */

public class ItemAdapter extends BaseAdapter {
    List<Item> itemlist;
    private LayoutInflater mInflater;
    private Context context;

    public ItemAdapter(Context context, List<Item> itemlist) {
        this.itemlist = itemlist;
        mInflater = LayoutInflater.from(context);
        this.context = context;
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

    public void changeItemColor(int position){

    }

    class ViewHold{
        TextView idView, bcView, seriView, qltyView, tolView, qtyView, wtView, stView, slcView;
        Button splitBt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        hold.splitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 拆包操作
            }
        });

        return convertView;
    }
}
