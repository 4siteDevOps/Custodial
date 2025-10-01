package com.newgen.iforms.CustodialSocket;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import com.newgen.iform.CustodialConstants.CustodialConstants;
import com.newgen.iforms.logging.WriteToLog;

public class ServiceSocketConnector {
    private static Logger writeToLog = null;
    private String utilityIP;
    private String utilityPort;
    private Map<String, String> propDetails = null;
    
    public ServiceSocketConnector() {
    	writeToLog = WriteToLog.getLoggerInstance();
    	utilityIP = null;
    	utilityPort = null;
    	propDetails = new HashMap<>();
	}
    
    public String socketConnector(String callName, String workitemName, String message, String requesttype) {
        readPropertyFile();
        
        utilityIP = propDetails.get(CustodialConstants.T24_IPKEY);
        utilityPort = propDetails.get(CustodialConstants.T24_PORTKEY);
        
        try (Socket skt = new Socket(utilityIP, Integer.parseInt(utilityPort))) {
            // Log client connection
            writeToLog.info("[Client Connected]");

            skt.setSoTimeout(200000);
            String response = null;

            try (DataOutputStream dout = new DataOutputStream(skt.getOutputStream())) {
                String socketRequestMessage;
                if(callName.equalsIgnoreCase("AMLCheck"))
                	socketRequestMessage = message;
                else 
                	socketRequestMessage = createServiceRequest(callName, workitemName ,message, requesttype);
                
                dout.write(socketRequestMessage.getBytes(StandardCharsets.UTF_16LE));
                dout.flush();
                // Log sent request
                writeToLog.info("[Sending Request]: " + socketRequestMessage);

                // Wait for the server response
                try (DataInputStream din = new DataInputStream(skt.getInputStream())) {
                    StringBuilder responseBuilder = new StringBuilder();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = din.read(buffer)) != -1) {
                        responseBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_16LE));
                    }
                    response = responseBuilder.toString();
                    // Log received response
                    writeToLog.info("[Reading Response]: " + response);
                }
            }

            return response;
        } catch (UnknownHostException e) {
            // Log unknown host error
            writeToLog.error("Unknown host: " + e.getMessage());
        } catch (IOException e) {
            // Log IO error
            writeToLog.error("IO error: " + e.getMessage());
        }
        return null;
    }

    private void readPropertyFile() {
        writeToLog.info("[Reading Property File]");
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(CustodialConstants.T24_CONFIGFILELOCATION)) {
            prop.load(input);
            String ip = prop.getProperty("HostName");
            String port = prop.getProperty("PortNumber");
            writeToLog.info("IP Address: " + ip);
            writeToLog.info("Port: " + port);
            propDetails.put(CustodialConstants.T24_IPKEY, ip);
            propDetails.put(CustodialConstants.T24_PORTKEY, port);
        } catch (IOException ex) {
            writeToLog.error("[Reading Property File Exception]: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String createServiceRequest(String callType, String wiName, String inMsg, String uniqueStr) {
        StringBuilder s = new StringBuilder();
        s.append("<OPTION>").append(callType).append("</OPTION>");
        s.append("<WIName>").append(wiName).append("</WIName>");
        s.append("<MESSAGE>").append(inMsg).append("</MESSAGE>");
        s.append("<UNIQUESTRING>").append(uniqueStr).append("</UNIQUESTRING>");
        
        return s.toString();
    }

}
