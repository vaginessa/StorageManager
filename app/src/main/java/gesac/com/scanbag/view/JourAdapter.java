package gesac.com.scanbag.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gesac.com.R;
import gesac.com.scanbag.model.Journal;

/**
 * Created by GE11522 on 2017/4/18.
 */

public class JourAdapter extends BaseAdapter {
    List<Journal> iJournals;
    private LayoutInflater mInflater;
    private Context context;

    public JourAdapter(Context context, List<Journal> iJournals) {
        this.iJournals = iJournals;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return iJournals.size();
    }

    @Override
    public Object getItem(int position) {
        return iJournals.get(position);
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
            convertView = mInflater.inflate(R.layout.jourlist, null);
            hold.idView = (TextView) convertView.findViewById(R.id.jourid);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.idView.setText(iJournals.get(position).getJourid());
        return convertView;
    }

    class ViewHold {
        TextView idView;
    }
}
