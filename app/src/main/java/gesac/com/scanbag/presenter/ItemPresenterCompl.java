package gesac.com.scanbag.presenter;

import android.util.Log;

import gesac.com.scanbag.model.IJournal;
import gesac.com.scanbag.view.IItemVIew;
import gesac.com.splitbag.model.Bag;
import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/4/18.
 */

public class ItemPresenterCompl implements IItemPresenter {
    private IItemVIew iItemVIew;
    private IBag iBag;
    public ItemPresenterCompl(IItemVIew iItemVIew) {
        this.iItemVIew = iItemVIew;
    }

    @Override
    public Integer isInJour(IBag iBag, IJournal ijournal) {
        for (int i = 0; i < ijournal.getItemlist().size(); i++) {
            if (ijournal.getItemlist().get(i).equals(iBag.getPctid())){
                return i;
            }
        }
        return 0;
    }

    public IBag subString(String str) {
        String[] sourceStrArray = str.split(",,");
        try {
            iBag = new Bag(sourceStrArray[0], sourceStrArray[1], sourceStrArray[2], sourceStrArray[3],
                    String.valueOf((int) Math.round(Double.parseDouble(sourceStrArray[5]))),
                    sourceStrArray[6]);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.i("message", "subString: " + e.toString());
            return null;
        }
        return iBag;
    }
}
