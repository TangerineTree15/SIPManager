package com.naturaltel.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;


public class UdpServerTest {
    
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            
            int count = 0;
            @Override
            public void run() {
                if(count == 5) {
                    timer.cancel();
                    timer.purge();
                }
                count++;
                DatagramSocket datagramSocket = null;
                try {
                    InetSocketAddress bindaddr = new InetSocketAddress("localhost", 8617);
                    datagramSocket = new DatagramSocket();
                    byte[] sendData = ("reloadCityCell" + "\r\n").getBytes("UTF-8");
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, bindaddr);
                    datagramSocket.send(sendPacket);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(datagramSocket != null) {
                        datagramSocket.close();
                    }
                }
                
            }
        }, 10000, 30000);

    }
    
}
