package com.naturaltel.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.naturaltel.cache.Config;
import com.naturaltel.config.ConfigLoader;
import com.naturaltel.config.entity.UdpCmds;

import org.apache.log4j.Logger;

public class UdpServer implements Runnable {

    private final String PING;
    private final String ACK;
    private final String RELOAD_CITY_CELL_INFO;
    private final String RELOAD_CONFIG;

    private static UdpServer mServer;
    private DatagramSocket mUdpSocket;
    private boolean mRun = false;
    private Thread mThread;
    private int mErrorCount;

    private Logger logger = Logger.getLogger(getClass());

    private UdpServer() {
        UdpCmds udpCmds = Config.getUdpServerInfo().getUdpCmds();
        ACK = udpCmds.getAckCmd();
        PING = udpCmds.getPingCmd();
        RELOAD_CITY_CELL_INFO = udpCmds.getReloadCityCellCmd();
        RELOAD_CONFIG = udpCmds.getReloadConfigCmd();
    }

    public static UdpServer getInstance() {
        if (mServer == null) {
            mServer = new UdpServer();
        }
        return mServer;
    }

    public boolean isRun() {
        return mThread == null ? false : mRun;
    }

    public void stop() {
        mRun = false;
        if (mUdpSocket != null) {
            mUdpSocket.close();
            mUdpSocket = null;
        }
    }

    public void start() throws SocketException {
        mRun = true;
        mUdpSocket = new DatagramSocket(new InetSocketAddress(Config.getUdpServerInfo().getAddress(), Config.getUdpServerInfo().getPort()));
        mErrorCount = 0;
        mThread = new Thread(this);
        mThread.start();
        logger.info("UDP server is started");

    }

    private void sendAck(InetAddress srcAddress, int srcPort) throws IOException, Exception {
        byte[] sendData = (ACK + "\r\n").getBytes("UTF-8");
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, srcAddress, srcPort);
        mUdpSocket.send(sendPacket);
    }

    public void run() {
        while (mRun) {
            try {
                byte[] recData = new byte[16];
                DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
                mUdpSocket.receive(recPacket);
                InetAddress srcAddress = recPacket.getAddress();
                int srcPort = recPacket.getPort();
                String str = new String(recPacket.getData(), "UTF-8").trim();
                String[] tokens = str.split("\r\n");
                for (int i = 0; i < tokens.length; i++) {
                    logger.info("Got event:" + tokens[i]);
                    if (tokens[i].equals(PING)) {
                        sendAck(srcAddress, srcPort);
                        break;
                    } else if (tokens[i].equals(RELOAD_CITY_CELL_INFO)) {
                        logger.info("reloading cell info .... ");
                        ConfigLoader.reloadCityCellInfo();
                        sendAck(srcAddress, srcPort);
                        logger.info("reloaded cell info .... ");
                        break;

                        //RELOAD_CONFIG
                    } else if (tokens[i].equals(RELOAD_CONFIG)) {
                        logger.info("reloading all configuration .... ");
                        ConfigLoader.reloadAllConfiguration();
                        sendAck(srcAddress, srcPort);
                        logger.info("reloaded all configuration .... ");
                        break;
                    }
                }
            } catch (IOException e) {
                //todo alarm
                logger.error("", e);

            } catch (Exception e) {
                //todo alarm
                logger.error("", e);

            }
        }
    }
}
