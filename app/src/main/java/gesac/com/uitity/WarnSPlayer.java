package gesac.com.uitity;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by GE11522 on 2017/5/18.
 */

public class WarnSPlayer extends MediaPlayer {

    public static void playsound(Context context, int resid){
        MediaPlayer mp = MediaPlayer.create(context,resid);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }
}