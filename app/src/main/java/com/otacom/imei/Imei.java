package com.otacom.imei;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Classe reponsavel por agregar identificação ao dispositivo móvel
 * Data da versão 2018-11-12
 *
 * @author Joao Ignacio -
 * @apiNote última atualização na versão 28 Oreo
 */
public class Imei {

    private String imeiNumber;
    private String macAddress;
    private String serialNumber;
    private String subscriberId;
    private String nai;
    //private int simCarrierId;
    private String meid;


    public Imei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager)
                    context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            WifiManager wifiManager = (WifiManager)
                    context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            // Testa se foi permitido acesar ao hardware Telefone
            assert telephonyManager != null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imeiNumber = telephonyManager.getImei().toString(); // get IMEI API 26+
                //simCarrierId = telephonyManager.getSimCarrierId();
                meid = telephonyManager.getMeid().toString();
            } else {
                imeiNumber = telephonyManager.getDeviceId(); // get IMEI Compat API
            }

            serialNumber = telephonyManager.getSimSerialNumber();
            subscriberId = telephonyManager.getSubscriberId();
            nai = telephonyManager.getNai();


            // Testa se foi permitido acessar ao hardware de Wifi
            assert wifiManager != null;
            macAddress = wifiManager.getConnectionInfo().getMacAddress();

        } catch (Exception e) {
            Log.e("IMEI CLASS", "Exceção = " + e.toString(), e);
        }
    }

    public String getIdentificador() {
        if (imeiNumber != null) return imeiNumber;
        if (macAddress != null) return macAddress;
        if (serialNumber != null) return serialNumber;
        if (subscriberId != null) return subscriberId;
        if (nai != null) return nai;
        //if (simCarrierId != 0) return String.valueOf(simCarrierId);
        if (meid != null) return meid;
        return null;
    }

    @Override
    public String toString() {
        String imei = (imeiNumber != null) ? imeiNumber : "null";
        String mac = (macAddress != null) ? macAddress : "null";
        String serial = (serialNumber != null) ? serialNumber : "null";
        String sub = (subscriberId != null) ? subscriberId : "null";
        String nai = (this.nai != null) ? this.nai : "null";
        //String sim = (simCarrierId != 0) ? String.valueOf(simCarrierId) : "0";
        String meid = (this.meid != null) ? this.meid : "null";

        return ("IMEI = " + imei +
                "\nMacAdress = " + mac +
                "\nSerial Number= " + serial +
                "\nSubscriber ID = " + meid +
                "\nNetwork Access Identifier = " + nai +
                //"\nSim Carrier ID = " + sim +
                "\nMEID = " + meid
        );

    }
}
