package ru.home.tcpping.impl;

import ru.home.tcpping.Message;
import ru.home.tcpping.TcpPing;
import ru.home.tcpping.exception.PitcherExecutionException;
import ru.home.tcpping.util.TcpPingHelper;
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

    private final static Logger logger = Logger.getLogger(Pitcher.class);

    private AtomicBoolean reportTicker = new AtomicBoolean(false);

    private final HashMap<String, String> params;
    private int port;
    private int mps;
    private int size;
    private String hostname;

    /**
     * Constructor
     *
     * @param params parameter map
     */
    public Pitcher(Map<String, String> params) {
        this.params = new HashMap<>(params);
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
        int sent = 0;
        int speed = 0;

        int abAverage = 0, baAverage = 0, abaAverage = 0;
        int maxAbaTime = 0;

        Socket clientSocket;
        BufferedReader input;
        DataOutputStream output;
        Message outputMessage, replyMessage;
        String reply;
        int abTime, baTime, abaTime;

        logger.info("Execute Pitcher");

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
            replyMessage = new Message(reply);
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
                        + " Max A-B-A time: " + maxAbaTime + " ms"
                        + " Average A-B time: " + abAverage + " ms"
                        + " Average B-A time: " + baAverage + " ms"
                        + " Average A-B-A time: " + abaAverage + " ms";
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
