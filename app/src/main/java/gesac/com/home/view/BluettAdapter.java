package gesac.com.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gesac.com.R;
import gesac.com.home.model.Bluetooth;

/**
 * Created by GE11522 on 2017/4/26.
 */

public class BluettAdapter extends BaseAdapter {
    List<Bluetooth> bluetoothList;
    private LayoutInflater mInflater;
    private Context context;

    public BluettAdapter(Context context, List<Bluetooth> bluetoothList) {
        this.bluetoothList = bluetoothList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bluetoothList.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetoothList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHold hold;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = mInflater.inflate(R.layout.bluettlist, null);
            hold.nameView = (TextView) convertView.findViewById(R.id.bluettname);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        return convertView;
    }

    class ViewHold {
        TextView nameView;
    }
}
