package sanq.ractrl.core;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import sanq.ractrl.launcher.Main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: pospelov
 * Date: 14.05.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class WifiControl {
    private WifiManager wifiManager;
    private static WifiControl ourInstance = new WifiControl();

    public static WifiControl getInstance() {
        return ourInstance;
    }

    private WifiControl() {
        wifiManager =  (WifiManager)Main.getAppContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void setApDisable(){
        ApEnable(false);
    }

    public void setApEnable(){
        ApEnable(true);
    }
    public void setWifiDisable(){
        WifiEnable(false);
    }

    public void setWifiEnable(){
        WifiEnable(true);
    }

    public boolean isWifiActive() {
        return wifiManager.isWifiEnabled();
    }

    public boolean isApActive() {
        boolean res = false;
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();
        boolean methodFound = false;
                try {
                    for (Method isWifiApEnabledmethod : wmMethods) {
                        if (isWifiApEnabledmethod.getName().equals("isWifiApEnabled")) {
                            res = (Boolean) isWifiApEnabledmethod.invoke(wifiManager);
//                            for (Method method1 : wmMethods) {
//                                if (method1.getName().equals("getWifiApState")) {
//                                    int apstate = (Integer) method1.invoke(wifiManager);
//                                }
//                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
     return res;
    }


    private void WifiEnable(boolean enabled) {
        wifiManager.setWifiEnabled(enabled);
    }


    private void ApEnable(boolean enabled){
        if(wifiManager.isWifiEnabled()){
           wifiManager.setWifiEnabled(false);
        }
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();
        boolean methodFound=false;
        for(Method method: wmMethods){
            if(method.getName().equals("setWifiApEnabled")){
                methodFound=true;
                try {
                    boolean apstatus=(Boolean) method.invoke(wifiManager,null, enabled);
                    for (Method isWifiApEnabledmethod: wmMethods)
                    {
                        if(isWifiApEnabledmethod.getName().equals("isWifiApEnabled")){
                            Long startTime = Calendar.getInstance().getTimeInMillis();
                            while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){
                                if ( (Calendar.getInstance().getTimeInMillis() - startTime) > Const.TIMEOUT_WIFI){
                                    break;
                                }
                            }
                            //todo return resul of actions (true/false)
                            for(Method method1: wmMethods){
                                if(method1.getName().equals("getWifiApState")){
                                    int apstate=(Integer)method1.invoke(wifiManager);
                                }
                            }
                        }
                    }
                    if(apstatus)
                    {
                        System.out.println("SUCCESS");
                    }else
                    {
                        System.out.println("FAILED");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!methodFound){
            //statusView.setText("Your phone's API does not contain setWifiApEnabled method to configure an access point");
        }

    }




    private void createWifiAccessPoint() {
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();   //Get all declared methods in WifiManager class
        boolean methodFound=false;
        for(Method method: wmMethods){
            if(method.getName().equals("setWifiApEnabled")){
                methodFound=true;
                WifiConfiguration netConfig = new WifiConfiguration();
                netConfig.SSID = "\"testAP\"";
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                //netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                //netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                //netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                //netConfig.preSharedKey=password;
                //netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                //netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                //netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                //netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                try {
                    boolean apstatus=(Boolean) method.invoke(wifiManager, netConfig,true);
                    //statusView.setText("Creating a Wi-Fi Network \""+netConfig.SSID+"\"");
                    for (Method isWifiApEnabledmethod: wmMethods)
                    {
                        if(isWifiApEnabledmethod.getName().equals("isWifiApEnabled")){
                            while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){
                            };
                            for(Method method1: wmMethods){
                                if(method1.getName().equals("getWifiApState")){
                                    int apstate;
                                    apstate=(Integer)method1.invoke(wifiManager);
                                    //                    netConfig=(WifiConfiguration)method1.invoke(wifi);
                                    //statusView.append("\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n");
                                }
                            }
                        }
                    }
                    if(apstatus)
                    {
                        System.out.println("SUCCESSdddd");
                        //statusView.append("\nAccess Point Created!");
                        //finish();
                        //Intent searchSensorsIntent = new Intent(this,SearchSensors.class);
                        //startActivity(searchSensorsIntent);

                    }else
                    {
                        System.out.println("FAILED");
                        //statusView.append("\nAccess Point Creation failed!");
                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!methodFound){
            //statusView.setText("Your phone's API does not contain setWifiApEnabled method to configure an access point");
        }
    }

}
