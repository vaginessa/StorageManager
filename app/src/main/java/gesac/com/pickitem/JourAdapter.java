package gesac.com.pickitem;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import gesac.com.R;
import gesac.com.databinding.JourlistBinding;
import gesac.com.pickitem.model.Journal;

/**
 * Created by GE11522 on 2017/6/13.
 */

public class JourAdapter extends BaseAdapter {
    private Context context;
    private List<Journal> jourList = new ArrayList<>();

    public JourAdapter(Context context, List<Journal> jourList) {
        this.context = context;
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
        JourlistBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.jourlist, parent, false);
            convertView = binding.getRoot();
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setJournal(jourList.get(position));
        return convertView;
    }
}
