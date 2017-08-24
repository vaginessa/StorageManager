package gesac.com.locstock;

/**
 * Created by GE11522 on 2017/6/19.
 */

public class Ware {
    private String st, slc, qlty, tol, qty;

    public Ware(String st, String slc, String qlty, String tol, String qty) {
        this.st = st;
        this.slc = slc;
        this.qlty = qlty;
        this.tol = tol;
        this.qty = qty;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getSlc() {
        return slc;
    }

    public void setSlc(String slc) {
        this.slc = slc;
    }

    public String getQlty() {
        return qlty;
    }

    public void setQlty(String qlty) {
        this.qlty = qlty;
    }

    public String getTol() {
        return tol;
    }

    public void setTol(String tol) {
        this.tol = tol;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
