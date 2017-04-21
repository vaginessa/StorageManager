package gesac.com.splitbag.model;

/**
 * Created by GE11522 on 2017/4/14.
 */

public class Bag implements IBag {
    String pctid, pcttol, pctqlty, pctbc, pctqty, pcthv;

    public Bag(String pctid, String pcttol, String pctqlty, String pctbc, String pctqty, String pcthv) {
        this.pctid = pctid;
        this.pcttol = pcttol;
        this.pctqlty = pctqlty;
        this.pctbc = pctbc;
        this.pctqty = pctqty;
        this.pcthv = pcthv;
    }

    @Override
    public String getPctid() {
        return pctid;
    }

    @Override
    public void setPctid(String pctid) {
        this.pctid = pctid;
    }

    @Override
    public String getPcttol() {
        return pcttol;
    }

    @Override
    public void setPcttol(String pcttol) {
        this.pcttol = pcttol;
    }

    @Override
    public String getPctqlty() {
        return pctqlty;
    }

    @Override
    public void setPctqlty(String pctqlty) {
        this.pctqlty = pctqlty;
    }

    @Override
    public String getPctbc() {
        return pctbc;
    }

    @Override
    public void setPctbc(String pctbc) {
        this.pctbc = pctbc;
    }

    @Override
    public String getPctqty() {
        return pctqty;
    }

    @Override
    public void setPctqty(String pctqty) {
        this.pctqty = pctqty;
    }

    @Override
    public String getPcthv() {
        return pcthv;
    }

    @Override
    public void setPcthv(String pcthv) {
        this.pcthv = pcthv;
    }

}
