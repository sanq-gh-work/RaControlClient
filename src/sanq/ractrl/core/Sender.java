package sanq.ractrl.core;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import sanq.ractrl.launcher.Main;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 04.05.13
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class Sender {
    private static  ExecutorService executor = Executors.newSingleThreadExecutor() ;
    private static Socket socket;
    private static Future<Boolean> future ;

    public static synchronized boolean SendMessage(int mes) {
        return SendMessage( mes, false);
    }

    public static synchronized boolean SendMessage(int mes, boolean waitForResult) {
        Boolean res = false;

       if ((future == null) || (future.isDone())) {
            future = executor.submit(new CallSender(new HandlerPrintError(), String.valueOf(mes)));
            if (waitForResult) {
                while (!future.isDone()) {
                    //подождать пока задача не выполнится
                }
                try {
                    res = future.get();
                } catch (InterruptedException ie) {
                    ie.printStackTrace(System.err);
                } catch (ExecutionException ee) {
                    ee.printStackTrace(System.err);
                }
            }
        }
        return res;
    }


    // inner Callable
    private static class CallSender implements Callable<Boolean> {
       private static  String mes;
       private static Handler hwd;
       private CallSender(Handler hwd , String mes) {
           this.mes = mes;
           this.hwd =  hwd;
       }

       @Override
       public Boolean call() throws Exception {
           return  SendMessage(mes);

       }

       private static void sendOsMessage(Handler hwd , Const.TypeOsMessage typeOsMessage , String mes){
           Message msg = hwd.obtainMessage();
           Bundle b  = new Bundle();
           b.putString(typeOsMessage.toString(), mes) ;
           msg.setData(b);
           hwd.sendMessage(msg);
       }

       private static boolean SendMessage(String mes) {
           boolean res = false;
           try {
               Socket socket = new Socket();
               socket.connect(new InetSocketAddress(Prefs.getIp(Main.getAppContext()), Prefs.getPort(Main.getAppContext())), Const.TIMEOUT_SENDER);
               mes += Const.EOC;
               PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
               out.println(mes);
               res = true;
               socket.close();
           } catch (Exception ex) {
               Log.d(Const.TAG, ex.getMessage());
               sendOsMessage(hwd, Const.TypeOsMessage.ERROR,  ex.getMessage() );
               try {
                   if (socket != null & socket.isConnected()) {
                       socket.close();
                   }
                   socket = null;
               } catch (Exception ignore) { /*NOP*/ }
           }
           return res;
       }

      }

}
