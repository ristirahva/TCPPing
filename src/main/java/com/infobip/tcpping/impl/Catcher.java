package com.infobip.tcpping.impl;

import com.infobip.tcpping.Message;
import com.infobip.tcpping.TcpPing;
import com.infobip.tcpping.exception.InvalidArgumentException;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Catcher implementation.
 */
public final class Catcher implements TcpPing {

    final static Logger logger = Logger.getLogger(Catcher.class);

    HashMap<String, String> params;
    int port;
    InetAddress inetAddress;

    /**
     * Constructor
     *
     * @param paramMap parameter map
     */
    public Catcher(Map<String, String> paramMap) {
        params = new HashMap(paramMap);
        port = Integer.parseInt(params.get("port"));
        try {
            inetAddress = InetAddress.getByName(paramMap.get("bind"));
        }
        catch (UnknownHostException uhe) {
            throw new InvalidArgumentException("Wrong hostname value");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getParameters() {
        return params;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws IOException {
        logger.info("Starting catcher with following params: port=" + port + " bind=" + inetAddress.getHostName());

        ServerSocket serverSocket = new ServerSocket(port, 50, inetAddress);
        while (true) {
            logger.debug("waiting for connection");
            Socket connectionSocket = serverSocket.accept();
            System.out.println("accept connection");
            Runnable runnable = () -> processRequest(connectionSocket);
            new Thread(runnable).start();
        }
    }

    /**
     * Process incoming request
     *
     * @param connectionSocket
     */
    private void processRequest(Socket connectionSocket)  {
        try {
            logger.debug("starting processing");
            BufferedReader input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());
            String clientMessage = input.readLine();
            logger.debug("Received: " + clientMessage);
            Message message = Message.createMessageFromString(clientMessage);
            message.setReplyTime(System.currentTimeMillis());
            String reply = message.toString();
            logger.debug("Sent: " + reply);
            output.writeBytes(reply + "\n");
            connectionSocket.close();
        }
        catch (IOException ioe) {
            logger.error("Thread " + Thread.currentThread().getName() + " execution error:" +ioe);
        }
    }
}
