package sanq.ractrl.core;

import sanq.ractrl.launcher.Main;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 04.05.13
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 *
 * test
 *
 */
public interface Const {
    public final static String TAG = "myApp";

//  public final static String SERVER_IP = "192.168.43.193";
//  public final static int PORT = 1935;


    public final static int TIMEOUT_SENDER = 3000;
    public final static String EOC = "END_COMMAND";


    public final static int TIMEOUT_WIFI = 5000;



    public static enum Direct{
        LEFT, RIGHT;
    }


    public enum TypeOsMessage{
        ERROR
    }


    public static enum TypeCommand {

        SERVER_SHUTDOWN(0),
        SYSTEM_SHUTDOWN(1),
        SYSTEM_REBOOT(2),
        SYSTEM_CANCEL(3),
        VOLUME_UP(4),
        VOLUME_DOWN(5),
        VOLUME_MIN(6),
        VOLUME_PREV(7),
        VOLUME_MAX(8);

        public int getValue(){
            return cmd;
        }

        public static TypeCommand fromInteger(int x) {
            switch(x) {
                case 0: return SERVER_SHUTDOWN;
                case 1: return SYSTEM_SHUTDOWN;
                case 2: return SYSTEM_REBOOT;
                case 3: return SYSTEM_CANCEL;
                case 4: return VOLUME_UP;
                case 5: return VOLUME_DOWN;
                case 6: return VOLUME_MIN;
                case 7: return VOLUME_PREV;
                case 8: return VOLUME_MAX;
            }
            return null;
        }

        private final int cmd;
        private TypeCommand(int cmd) {
            this.cmd = cmd;
        }
    }

}
