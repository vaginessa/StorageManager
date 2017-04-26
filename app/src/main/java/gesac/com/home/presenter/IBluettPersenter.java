package gesac.com.home.presenter;

import java.util.List;

import gesac.com.home.model.Bluetooth;

/**
 * Created by GE11522 on 2017/4/26.
 */

public interface IBluettPersenter {

    List<Bluetooth> getAllbluett();
    boolean bindBluett(String bluettaddr);
}
