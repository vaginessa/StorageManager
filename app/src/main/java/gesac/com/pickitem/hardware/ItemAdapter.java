package gesac.com.pickitem.hardware;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gesac.com.BR;
import gesac.com.R;
import gesac.com.model.RespPOJO;
import gesac.com.pickitem.model.Item;
import gesac.com.splitbag.model.IBag;
import gesac.com.uitity.LoadDialog;
import gesac.com.uitity.PrintUtil;
import gesac.com.uitity.WarnSPlayer;

/**
 * Created by GE11522 on 2017/6/14.
 */

public class ItemAdapter extends BaseAdapter {
    private final String TAG = "ItemAdapter debug";
    private Context context;
    private int variableId;
    private List<Item> itemList = new ArrayList<>();
    private int type;

    private IBag iBag = null;

    public ItemAdapter(Context context, int variableId, List<Item> itemList) {
        this.context = context;
        this.variableId = variableId;
        this.itemList = itemList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return itemList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewDataBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.itemlist, parent, false);
            convertView = binding.getRoot();
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(variableId, itemList.get(position));
        binding.setVariable(BR.onPickBtClickListener, new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (iBag != null) {
                    PrintAsync pasnc = new PrintAsync();
                    pasnc.execute(itemList.get(position));
                    pasnc.setAsyncRespones(new AsyncRespones() {
                        @Override
                        public void onDataReceivedSuccess(int result) {
                            if (result == 0) setSplit(position);
                        }
                    });
                    LoadDialog.showDialog(context, "打印中");
                } else Toast.makeText(context, "请先扫码", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyDataSetChanged();
    }

    private void setSplit(int position) {
        itemList.get(position).setSplit(true);
        notifyDataSetChanged();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public IBag getiBag() {
        return iBag;
    }

    public void setiBag(IBag iBag) {
        this.iBag = iBag;
    }

    public void setIn(int position) {
        itemList.get(position).setIsin(position);
        Log.i(TAG, "setIn: " + itemList.get(position).getIsin());
        notifyDataSetChanged();
    }

    private interface AsyncRespones {
        void onDataReceivedSuccess(int result);
    }

    class PrintAsync extends AsyncTask<Object, Object, RespPOJO<Object>> {

        private AsyncRespones asyncRespones;

        private void setAsyncRespones(AsyncRespones asyncRespones) {
            this.asyncRespones = asyncRespones;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param objects The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected RespPOJO<Object> doInBackground(Object... objects) {
            RespPOJO<Object> result = new RespPOJO<>();
            try {
                result = PrintUtil.doPickPrint(getType(), "0", objects[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(RespPOJO<Object> result) {
            super.onPostExecute(result);
            asyncRespones.onDataReceivedSuccess(result.getCode());
            String msg = result.getMsg();
            if (result.getCode() == 0)
                WarnSPlayer.playsound(context, R.raw.printscd);
            else WarnSPlayer.playsound(context, R.raw.printerr);
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            LoadDialog.cancelDialog();
        }
    }
}
