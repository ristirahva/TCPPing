package com.infobip.tcpping.impl;

import com.infobip.tcpping.Message;
import com.infobip.tcpping.TcpPing;
import com.infobip.tcpping.exception.PitcherExecutionException;
import com.infobip.tcpping.util.TcpPingHelper;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Pitcher implementation.
 */
public final class Pitcher implements TcpPing {

    final static Logger logger = Logger.getLogger(Pitcher.class);

    AtomicBoolean reportTicker = new AtomicBoolean(false);
    int sent;
    int speed;
    int abAverage, baAverage, abaAverage;
    int maxAbaTime;
    int average;

    HashMap<String, String> params;
    int port;
    int mps;
    int size;
    String hostname;

    /**
     * Constructor
     *
     * @param params parameter map
     */
    public Pitcher(Map<String, String> params) {
        this.params = new HashMap(params);
        port = Integer.parseInt(params.get("port"));
        mps = Integer.parseInt(params.get("mps"));
        size = Integer.parseInt(params.get("size"));
        hostname = params.get("hostname");
        logger.info("Starting pitcher with following params: port=" + port + " MPS=" + mps + " size=" + size + " hostname=" + hostname);
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
        Socket clientSocket;
        BufferedReader input;
        DataOutputStream output;
        Message outputMessage, replyMessage;
        String reply;
        int abTime, baTime, abaTime;

        logger.info("Execute Pitcher");

        maxAbaTime = 0;
        startStatistics();

        while(true) {
            clientSocket = new Socket(hostname, port);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new DataOutputStream(clientSocket.getOutputStream());
            outputMessage = createMessage(sent, size);
            logger.debug("TO SERVER: " + outputMessage.toString());
            output.writeBytes(outputMessage.toString() + '\n');

            ++sent;
            ++speed;

            reply = input.readLine();
            logger.debug("Reply from server: " + reply);
            replyMessage = Message.createMessageFromString(reply);
            long currentTime = System.currentTimeMillis();
            abTime = (int)(replyMessage.getReplyTime() - replyMessage.getCreationTime());
            baTime = (int)(currentTime - replyMessage.getReplyTime());
            abaTime = abTime + baTime;
            logger.debug("A-B time: " + abTime + " ms");
            logger.debug("B-A time: " + baTime + " ms");
            logger.debug("A-B-A time: " + abaTime + " ms");
            maxAbaTime = Math.max(maxAbaTime, abaTime);

            abAverage = TcpPingHelper.calculateAverage(speed, abAverage, abTime);
            baAverage = TcpPingHelper.calculateAverage(speed, baAverage, baTime);
            abaAverage = TcpPingHelper.calculateAverage(speed, abaAverage, abaTime);
            logger.debug("A-B average: " + abAverage);
            if (reportTicker.get()) {
                reportTicker.compareAndSet(true, false);
                String statistics = "Current time: " + new Date() + " total messages sent: " + sent + " speed:" + speed +"\n"
                        + " Max A-B-A time: " + maxAbaTime
                        + " Average A-B time: " + abAverage
                        + " Average B-A time: " + baAverage
                        + " Average A-B-A time: " + abaAverage;
                logger.info(statistics);
                System.out.println(statistics);
                speed = 0;
                maxAbaTime = 0;
            }
            try {
                Thread.sleep(1000 / mps);
            }
            catch (InterruptedException ie) {
                throw new PitcherExecutionException("Pitcher execution error: " + ie);
            }
            clientSocket.close();
        }

    }

    /**
     * Statistics starter (once a 1 second).
     */
    private void startStatistics() {
        Timer timer = new Timer();
        TimerTask statistics = new TimerTask() {
            @Override
            public void run() {
                reportTicker.compareAndSet(false, true);
                logger.debug("Time to output the report");
            }
        };
        timer.scheduleAtFixedRate(statistics, 1000, 1000);
    }
}
