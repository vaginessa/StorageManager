package gesac.com.pickitem;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gesac.com.BR;
import gesac.com.R;
import gesac.com.databinding.ActivityHdwPickBinding;
import gesac.com.scanbag.model.Item;
import gesac.com.scanbag.model.Journal;
import gesac.com.uitity.LoadDialog;
import gesac.com.uitity.SoapUtil;

public class HdwPickActivity extends Activity {
    ActivityHdwPickBinding binding;
    String jourid;

    List<Journal> journals;
    JourAdapter jouradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hdw_pick);

        binding.searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jourid = binding.jouridEt.getText().toString();
                LoadDialog.showDialog(HdwPickActivity.this, "请稍等");
                new JourAsync().execute("*" + jourid + "*");
            }
        });

        binding.jourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(HdwPickActivity.this, ItemActivity.class);
                String jstr = new Gson().toJson(journals.get(position));
                it.putExtra("jour", jstr);
                startActivity(it);
            }
        });
    }

    private List<Journal> parseInfo(SoapObject result) {
        List<Journal> journals = new ArrayList<>();
        List<Item> items;
        if (result != null) {
            try {
                SoapObject sjourlist = (SoapObject) result.getProperty("GetJournalTransResult");
                for (int i = 0; i < sjourlist.getPropertyCount(); i++) {
                    SoapObject jourlist = (SoapObject) sjourlist.getProperty(i);
                    SoapObject itemlist = (SoapObject) jourlist.getProperty("items");
                    items = new ArrayList<>();
                    for (int j = 0; j < itemlist.getPropertyCount(); j++) {
                        SoapObject item = (SoapObject) itemlist.getProperty(j);
                        items.add(new Item(item.getPrimitiveProperty("itemid").toString(),
                                item.getPrimitiveProperty("itemqlty").toString(),
                                item.getPrimitiveProperty("itemtol").toString(),
                                item.getPrimitiveProperty("itembc").toString(),
                                item.getPrimitiveProperty("itemseri").toString(),
                                item.getPrimitiveProperty("itemqty").toString().replaceAll("-", ""),
                                item.getPrimitiveProperty("itemrqty").toString(),
                                item.getPrimitiveProperty("itemst").toString(),
                                item.getPrimitiveProperty("itemslc").toString()));
                    }
                    journals.add(new Journal(jourlist.getProperty("jourid").toString(), items));
                }
            } catch (ClassCastException e) {
                Toast.makeText(this, "请检查服务器设置", Toast.LENGTH_LONG);
            }
            return journals;
        } else
            return null;
    }

    class JourAsync extends AsyncTask<String, Void, SoapObject> {
        @Override
        protected SoapObject doInBackground(String[] params) {
            Map<String, String> map = new HashMap<>();
            map.put("journalId", params[0]);
            SoapObject so = SoapUtil.getERPinfo("GetJournalTrans", map);
            return so;
        }

        @Override
        protected void onPostExecute(SoapObject s) {
            super.onPostExecute(s);
            journals = parseInfo(s);
            jouradapter = new JourAdapter(HdwPickActivity.this, BR.journal, journals);
            binding.setJouradapter(jouradapter);
            LoadDialog.cancelDialog();
        }
    }
}
