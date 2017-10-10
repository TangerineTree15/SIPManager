package com.naturaltel.sip.gw;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sip.ClientTransaction;
import javax.sip.address.AddressFactory;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

import com.naturaltel.cache.Config;
import com.naturaltel.config.ConfigLoader;

import gov.nist.javax.sip.address.SipUri;

public class SipServerTest {

    private static String headerName = null;

    public static void main(String[] args) {

        try {
            ConfigLoader.loadConfiguration();
            SipManager sipManager = SipManager.getInstance();
            MessageFactory messageFactory = sipManager.getMessageFactory();
            HeaderFactory headerFactory = sipManager.getHeaderFactory();
            AddressFactory addressFactory = sipManager.getAddressFactory();

            String arg = null;
            if (args.length == 0) {
                arg = "4";
            } else {
                arg = args[0];
            }
            System.out.println("arg: " + arg);

            if ("1".equals(arg)) {
                headerName = Config.getCellNetworkInfo().getHeaderName1();
            } else if ("2".equals(arg)) {
                headerName = Config.getCellNetworkInfo().getHeaderName2();
            } else if ("3".equals(arg)) {
                headerName = Config.getCellNetworkInfo().getHeaderName3();
            } else if ("4".equals(arg)) {
                headerName = Config.getCellNetworkInfo().getHeaderName4();
            }
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                int counter = 0;

                @Override
                public void run() {
                    try {
                        if (counter == 1) {
                            timer.cancel();
                            timer.purge();
                        }
                        
//                        String fromHost = "172.20.140.208";
//                        int fromPort = 5060;
//                        String toHost = "10.77.20.18";
//                        int toPort = 5060;
                        String fromHost = "192.168.10.28";
                        int fromPort = 5060;
                        String toHost = "192.168.10.243";
                        int toPort = 5060;

//                        TelUri toUri = new TelUri("128101234");
//                        toUri.setHost("10.77.20.18");
//                        toUri.setPort(5060);
                        
                        
                           SosUri toUri = new SosUri();
                                                toUri.setHost(toHost);
                                                toUri.setPort(toPort);
                                                toUri.setUser("Jacky002");


                        SipUri fromUri = new SipUri();
                        fromUri.setUser("lala001");
                        fromUri.setHost(fromHost);
                        fromUri.setPort(fromPort);

                        String method = Request.INVITE;
                        CallIdHeader callIdHeader = sipManager.getNewCallIdHeader();
                        long cseq = 10;
                        CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(cseq, method);
                        FromHeader fromHeader = headerFactory.createFromHeader(addressFactory.createAddress(fromUri), "tag");
                        // The "To" header.
                        ToHeader toHeader = headerFactory.createToHeader(addressFactory.createAddress(toUri), null);
                        List<ViaHeader> viaHeaders = new ArrayList<>();
                        ViaHeader viaHeader = headerFactory.createViaHeader("192.168.10.60", 5060, "udp", null);
                        viaHeaders.add(viaHeader);
                        ViaHeader viaHeader2 = headerFactory.createViaHeader("192.168.10.61", 5060, "tcp", null);
                        viaHeaders.add(viaHeader2);
                        MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
                        ContactHeader contactHeader = headerFactory.createContactHeader();

                        Request req = messageFactory.createRequest(toUri, method, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwardsHeader);

                        req.addHeader(contactHeader);
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=46601246149DF;network-provided");
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=46601246149DF;network-provided");
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=46201246149DF;network-provided");

 //start -- 3G 
                        //fet Taipei lac=9122 cellid=49494 contact=+88612810 20 03 49494
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=4660323a2xxc156;network-provided");

                        // fet Taoyuan lac=9221 cellid=49494  contact=+88612810 30 0349494
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=466032405xxc156;network-provided");

                        // fet HsinTzu county lac=9211 cellid=49494 contact=+88612810 36 0349494
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=4660323fbxxc156;network-provided");

                                               // fet HsinTzu city lac=9242 cellid=49494   contact=+88612810 35 03 49494
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=46603241axxc156;network-provided");

                        //abroad  contact=+88612810200323088
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=4620323a2xxc156;network-provided");

                        
                        
                        //start -- urn:service:sos  
                        //fet Taipei lac=9122 cellid=49494 contact=+88612810 302 49494
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=4660323a2xxc156;network-provided");

                        // fet Taoyuan lac=9221 cellid=49494  contact=+88612810 303 49494
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=466032405xxc156;network-provided");

                        // fet HsinTzu county  lac=9211 cellid=49494   contact=+88612810 333 49494
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=4660323fbxxc156;network-provided");

                                               // fet HsinTzu city lac=9242 cellid=49494     contact=+88612810 334 49494
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=46603241axxc156;network-provided");

                        //abroad    contact=+88612810 302 23088
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;cgi-3gpp=460323a2xxc156;network-provided");

                        //end -- urn:service:sos
//end -- 3G 
                        
                        
                       
 //start -- 4G 
                        //fet Taipei contact=+886128102001409999
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=4660126418FDF;network-provided");

                        // fet Taoyuan cellid=213333  contact=+886128103001213333
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=46601234155DF;network-provided");

                        // fet HsinTzu county cellid=220053 contact=+886128103601220053
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=46601235B95DF;network-provided");

                                               // fet HsinTzu city cellid=220038  contact=+886128103501220038
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=46601235B86DF;network-provided");

                        //abroad  contact=+886128102001100531
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=46201246149DF;network-provided");

                        
                        
                        //start -- urn:service:sos  
                        //fet Taipei contact=+88612810302409999
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=4660126418FDF;network-provided");

                        // fet Taoyuan cellid=213333  contact=+88612810 303 213333
                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=46601234155DF;network-provided");

                        // fet HsinTzu county cellid=220053 contact=+88612810333220053
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=46601235B95DF;network-provided");

                                               // fet HsinTzu city cellid=220038  contact=+88612810334220038
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=46601235B86DF;network-provided");

                        //abroad  contact=+88612810543100531
//                        Header pCellularNetworkInfoHeader = headerFactory.createHeader(headerName, "3GP-GERAN;utran-cell-id-3gpp=46201246149DF;network-provided");

                        //end -- urn:service:sos
//end -- 4G 
                        req.addHeader(pCellularNetworkInfoHeader);

                        ClientTransaction ct = sipManager.getNewClientTransaction(req);
//                        ct.sendRequest();
                        sipManager.sendRequestToServer(req);
                        System.out.println(req.toString());
//                        req = ct.createCancel();
//                        ct = sipManager.getNewClientTransaction(req);
//                        ct.sendRequest();

                        req.setMethod(Request.CANCEL);
                        Thread.sleep(2000);
                        sipManager.sendRequestToServer(req);
//                        System.out.println(req.toString());
                        counter++;
                        System.out.println("counter123: " + counter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, 200, 20);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
