package gesac.com.uitity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import gesac.com.pickitem.model.Item;
import gesac.com.splitbag.model.Bag;
import gesac.com.splitbag.model.IBag;

/**
 * Created by GE11522 on 2017/5/24.
 */

public class CodeUtil {
    public static Pattern pattern = Pattern.compile("([,][a-zA-Z0-9-.]*[,])+");
    public static String numid = "";

    public static String initCode(IBag iBag, String divnum) {
        String str = "," + iBag.getPctid() + ",,"
                + iBag.getPctqlty() + ",,"
                + iBag.getPcttol() + ",,"
                + iBag.getPctbc() + ",,,," + divnum
                + ".0000,," + iBag.getPcthv() + ",";
        return str;
    }

    public static Integer isBgInItemls(IBag ib, List<Item> itemList) {
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

    public static Integer isHwInItemls(IBag ib, List<Item> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemid().equalsIgnoreCase(ib.getPctid())
                    )
                return i;
        }
        return -1;
    }

    public static Integer isInItems(int type, IBag ib, List<Item> itemList) {
        if (type == 0 || type == 2)
            return isBgInItemls(ib, itemList);
        else if (type == 1) return isHwInItemls(ib, itemList);
        else return -1;
    }

    public static IBag subCode(String str) {
        if (whichType(str) == 1)
            return subHwCode(str);
        else if (whichType(str) == 2)
            return subUsaCode(str);
        else if (whichType(str) == 0)
            return subIBagCode(str);
        else return null;
    }

    public static int whichType(String str) {
        String[] sourceStrArray = str.split(",,");
        String firstStr = sourceStrArray[0];
        if (sourceStrArray.length < 4)
            return 1;
        else if (sourceStrArray.length > 3 && ",C01".equalsIgnoreCase(firstStr))
            return 2;
        else if (sourceStrArray.length > 3 && !",C01".equalsIgnoreCase(firstStr))
            return 0;
        else return -1;
    }

    public static IBag subIBagCode(String str) {
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

    public static IBag subUsaCode(String str) {
        IBag iBag;
        String[] sourceStrArray = str.split(",,");
        try {
            iBag = new Bag(sourceStrArray[1].replaceAll(",", ""),
                    sourceStrArray[2].replaceAll(",", ""),
                    sourceStrArray[3].replaceAll(",", ""),
                    sourceStrArray[4].replaceAll(",", ""),
                    String.valueOf((int) Math.round(Double.parseDouble(sourceStrArray[6].replaceAll(",", "")))),
                    sourceStrArray[7].replaceAll(",", ""));
        } catch (Exception e) {
            return null;
        }
        return iBag;
    }

    public static IBag subHwCode(String str) {
        IBag iBag;
        String[] sourceStrArray = str.split(",,");
        try {
            iBag = new Bag(sourceStrArray[0].replaceAll(",", ""),
                    "",
                    "",
                    "",
                    String.valueOf((int) Math.round(Double.parseDouble(sourceStrArray[1].replaceAll(",", "")))),
                    "");
        } catch (Exception e) {
            return null;
        }
        return iBag;
    }

    public static Map<String, String> subStCode(String str) {
        Map<String, String> maps = new HashMap<>();
        String[] sourceStrArray = str.split(",,");
        if (sourceStrArray.length < 4) {
            try {
                maps.put("st", sourceStrArray[0].replaceAll(",", ""));
                maps.put("slc", sourceStrArray[1].replaceAll(",", ""));
            } catch (Exception e) {
                return null;
            }
        } else
            return null;
        return maps;
    }
}