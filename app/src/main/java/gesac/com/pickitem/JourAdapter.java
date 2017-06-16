package gesac.com.pickitem;

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
import gesac.com.scanbag.model.Journal;

/**
 * Created by GE11522 on 2017/6/13.
 */

public class JourAdapter extends BaseAdapter {
    private Context context;
    private int variableId;
    private List<Journal> jourList = new ArrayList<>();

    public JourAdapter(Context context, int variableId, List<Journal> jourList) {
        this.context = context;
        this.variableId = variableId;
        this.jourList.addAll(jourList);
    }

    @Override
    public int getCount() {
        return jourList.size();
    }

    @Override
    public Object getItem(int position) {
        return jourList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.jourlist, parent, false);
            convertView = binding.getRoot();
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(variableId, jourList.get(position));
        return convertView;
    }
}
