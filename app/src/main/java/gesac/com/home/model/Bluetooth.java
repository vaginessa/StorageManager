package gesac.com.home.model;

/**
 * Created by GE11522 on 2017/4/26.
 */

public class Bluetooth {
    String btaddr, btname;
    int bindstate;

    public Bluetooth(String btaddr, String btname, int bindstate) {
        this.btaddr = btaddr;
        this.btname = btname;
        this.bindstate = bindstate;
    }

    public String getBtaddr() {
        return btaddr;
    }

    public void setBtaddr(String btaddr) {
        this.btaddr = btaddr;
    }

    public String getBtname() {
        return btname;
    }

    public void setBtname(String btname) {
        this.btname = btname;
    }

    public int getBindstate() {
        return bindstate;
    }

    public void setBindstate(int bindstate) {
        this.bindstate = bindstate;
    }
}
