package sanq.ractrl.launcher;

import android.os.Looper;
import android.util.Log;
import sanq.ractrl.core.*;

/**
 * Created with IntelliJ IDEA.
 * User: pospelov
 * Date: 14.05.13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */

public class Control {
    private static ThrLongVolume thrLongVolume = null;

    private static int ctrlWifi = -1;

    public static void checkAutoStartWifi(){
        ctrlWifi = Prefs.getWifi(Main.getAppContext());
        switch (Prefs.getWifi(Main.getAppContext())) {
            case 0: ; break;
            case 1: if (WifiControl.getInstance().isApActive()){
                    ctrlWifi = -1;
                    } else {
                    WifiControl.getInstance().setApEnable();
                    }

                    break;
            case 2: if (WifiControl.getInstance().isWifiActive()){
                    ctrlWifi = -1;
                    } else {
                    WifiControl.getInstance().setWifiEnable();
                    }
                    break;
        }
    }

    public static void checkAutoCloseWifi(){
        switch (ctrlWifi) {
            case 0: ; break;
            case 1: WifiControl.getInstance().setApDisable()  ; break;
            case 2: WifiControl.getInstance().setWifiDisable(); break;
        }
    }

    public static void longVolumeStart(Const.TypeCommand typeCommand){
         if (thrLongVolume ==null) {
                thrLongVolume = new ThrLongVolume(typeCommand);
                thrLongVolume.start();
         }
    }

    public static void longVolumeStop(){
        if (thrLongVolume != null){
            thrLongVolume.setState(ThrLongVolume.STATE_DONE);
            thrLongVolume = null;
        }
    }

    //Thread long scroll
    private static class ThrLongVolume extends Thread {
        private int state;
        private  Const.TypeCommand typeCommand;

        public final static int STATE_DONE = 0 ;
        public final static int STATE_RUNNING = 1 ;

     ThrLongVolume(Const.TypeCommand typeCommand) {
            super();
            this.typeCommand = typeCommand;
        }

        public void setState(int state) {
            this.state = state;
        }

        @Override
        public void run() {
            Looper.prepare();
            state = STATE_RUNNING;
            while (state == STATE_RUNNING) {
                Sender.SendMessage( typeCommand.getValue());
            }

            Looper.loop();
        }
    }
}
