package gesac.com.uitity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import gesac.com.scanbag.model.Item;
import gesac.com.splitbag.model.Bag;
import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/5/24.
 */

public class CodeUtil {
    public static Pattern pattern = Pattern.compile("([,][a-zA-Z0-9-.]*[,])+");

    public static IBag subItemCode(String str) {
        IBag iBag;
        String[] sourceStrArray = str.split(",,");
        try {
            iBag = new Bag(sourceStrArray[0].replaceAll(",", ""),
                    sourceStrArray[1].replaceAll(",", ""),
                    sourceStrArray[2].replaceAll(",", ""),
                    sourceStrArray[3].replaceAll(",", ""),
                    String.valueOf((int) Math.round(Double.parseDouble(sourceStrArray[5].replaceAll(",", "")))),
                    sourceStrArray[6].replaceAll(",", ""));
        } catch (Exception e) {
            return null;
        }
        return iBag;
    }

    public static Integer isInItemls(IBag ib, List<Item> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemid().equalsIgnoreCase(ib.getPctid())
                    && itemList.get(i).getItembc().equalsIgnoreCase(ib.getPctbc())
                    && itemList.get(i).getItemqlty().equalsIgnoreCase(ib.getPctqlty())
                    && itemList.get(i).getItemtol().equalsIgnoreCase(ib.getPcttol())
                    )
                return i;
        }
        return -1;
    }

    public static Map<String, String> subStCode(String str) {
        Map<String, String> maps = new HashMap<>();
        String[] sourceStrArray = str.split(",,");
        try {
            maps.put("st", sourceStrArray[0].replaceAll(",", ""));
            maps.put("slc", sourceStrArray[1].replaceAll(",", ""));
        } catch (Exception e) {
            return null;
        }
        return maps;
    }

    public static Map<String, String> subHwCode(String str) {
        Map<String, String> maps = new HashMap<>();
        String[] sourceStrArray = str.split(",,");
        try {
            maps.put("hwid", sourceStrArray[0].replaceAll(",", ""));
            maps.put("hwqty", sourceStrArray[1].replaceAll(",", ""));
        } catch (Exception e) {
            return null;
        }
        return maps;
    }
}
