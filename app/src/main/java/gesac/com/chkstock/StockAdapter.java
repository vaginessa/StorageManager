package gesac.com.chkstock;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import gesac.com.R;
import gesac.com.scanbag.model.Item;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding = null;
        if (convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chk_itemlist,parent,false);
        }else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(variableId,itemList.get(position));
        return binding.getRoot();
    }
}
