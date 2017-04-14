package gesac.com.scanbag.model;

/**
 * Created by GE11522 on 2017/4/14.
 */

public class Item {
    String itemid,itemqlty,itemtol,itembc,itemseri,itemqty,itemwt,itemst,itemslc;

    public Item(String itemid, String itemqlty, String itemtol, String itembc, String itemseri, String itemqty, String itemwt, String itemst, String itemslc) {
        this.itemid = itemid;
        this.itemqlty = itemqlty;
        this.itemtol = itemtol;
        this.itembc = itembc;
        this.itemseri = itemseri;
        this.itemqty = itemqty;
        this.itemwt = itemwt;
        this.itemst = itemst;
        this.itemslc = itemslc;
    }

    public Item() {
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemqlty() {
        return itemqlty;
    }

    public void setItemqlty(String itemqlty) {
        this.itemqlty = itemqlty;
    }

    public String getItemtol() {
        return itemtol;
    }

    public void setItemtol(String itemtol) {
        this.itemtol = itemtol;
    }

    public String getItembc() {
        return itembc;
    }

    public void setItembc(String itembc) {
        this.itembc = itembc;
    }

    public String getItemseri() {
        return itemseri;
    }

    public void setItemseri(String itemseri) {
        this.itemseri = itemseri;
    }

    public String getItemqty() {
        return itemqty;
    }

    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    public String getItemwt() {
        return itemwt;
    }

    public void setItemwt(String itemwt) {
        this.itemwt = itemwt;
    }

    public String getItemst() {
        return itemst;
    }

    public void setItemst(String itemst) {
        this.itemst = itemst;
    }

    public String getItemslc() {
        return itemslc;
    }

    public void setItemslc(String itemslc) {
        this.itemslc = itemslc;
    }
}
