package com.example.avinash.screencast2;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by AVINASH on 9/11/2016.
 */
public class Utils {
    static public InetAddress getBroadcastAddress(Context context) throws IOException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null) {
            return null;
        }

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        return InetAddress.getByAddress(quads);
    }

    static public boolean sendBroadcastMessage(Context context, DatagramSocket socket, int port, String message) {

        try {
            InetAddress broadcastAddr = getBroadcastAddress(context);
            if (broadcastAddr == null) {
                return false;
            }
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(),
                    broadcastAddr, port);
            socket.send(packet);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
