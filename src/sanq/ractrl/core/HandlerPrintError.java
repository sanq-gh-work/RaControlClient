package sanq.ractrl.core;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import sanq.ractrl.launcher.Main;

/**
 * Created with IntelliJ IDEA.
 * User: pospelov
 * Date: 16.05.13
 * Time: 11:59
 * To change this template use File | Settings | File Templates.
 */
public class HandlerPrintError extends Handler {
    public void handleMessage(Message msg) {
        String errMes = msg.getData().getString(Const.TypeOsMessage.ERROR.toString());
        Toast.makeText(Main.getAppContext(), errMes, Toast.LENGTH_LONG).show();
    }
}
