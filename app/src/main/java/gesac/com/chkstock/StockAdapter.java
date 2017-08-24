package gesac.com.chkstock;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gesac.com.R;
import gesac.com.scanbag.model.Item;
import gesac.com.uitity.WarnSPlayer;

/**
 * Created by GE11522 on 2017/5/23.
 */

public class StockAdapter extends BaseAdapter {
    private Context context;
    private int variableId;
    private List<Item> itemList = new ArrayList<>();

    public StockAdapter(Context context, List<Item> itemList, int variableId) {
        this.context = context;
        this.variableId = variableId;
        this.itemList.addAll(itemList);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chk_itemlist, parent, false);
            convertView = binding.getRoot();
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        ((TextView)(convertView.findViewById(R.id.itemqty_tv))).setText(itemList.get(position).getItemtqty());
        binding.setVariable(variableId, itemList.get(position));
        return convertView;
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyDataSetChanged();
    }

    public void cutItemqty(int position, String i) {
        if (Integer.parseInt(itemList.get(position).getItemqty()) >= Integer.parseInt(i))
            itemList.get(position).setItemqty(String.valueOf
                    (Integer.parseInt(itemList.get(position).getItemqty()) - Integer.parseInt(i)
                    )
            );
        else WarnSPlayer.playsound(context,R.raw.chkerror);
        notifyDataSetChanged();
    }

    public void swapItem(int s, int e) {
        Collections.swap(itemList, s, e);
        notifyDataSetChanged();
    }
}
