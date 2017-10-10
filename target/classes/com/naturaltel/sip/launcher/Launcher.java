package com.naturaltel.sip.launcher;

import org.apache.log4j.Logger;

import com.naturaltel.cache.Config;
import com.naturaltel.config.ConfigLoader;
import com.naturaltel.sip.gw.GenericStanzaListener;
import com.naturaltel.sip.gw.SipManager;
import com.naturaltel.udp.UdpServer;

public class Launcher {

    private static final Logger mLogger = Logger.getLogger("debug");

    private Launcher() {
        try {
//          String currentDir = System.getProperty("user.dir");
//          System.out.println(currentDir);



            ConfigLoader.loadConfiguration();

            mLogger.info("Init Sip Server...");
            mLogger.info("LRF configuration is loaded ... ");

            SipManager sipManager = SipManager.getInstance();
            sipManager.addGenericListener(new GenericStanzaListener());
            sipManager.start();

            mLogger.info("Init UDP Server...");
            UdpServer udpServer = UdpServer.getInstance();
            udpServer.start();
            mLogger.info("LRF started...");
            mLogger.info("");

        } catch (Exception e) {
            mLogger.error("", e);
            //todo alarm
            mLogger.error("LRF is failed to start...");

            System.exit(1);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String xmlConfigPath = null;
        if (args.length > 0) {
            xmlConfigPath = args[0].trim();
        }
        Config.setXmlConfigPath(xmlConfigPath);
        new Launcher();
    }

}
