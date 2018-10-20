package ru.home.tcpping;

import ru.home.tcpping.enumeration.Mode;
import ru.home.tcpping.exception.InvalidArgumentException;
import ru.home.tcpping.util.TcpPingHelper;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * Runner for both Pitcher and Catcher.
 */
public class TcpPingRunner {

    private final static Logger logger = Logger.getLogger(TcpPingRunner.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Mode mode = TcpPingHelper.getMode(args);
        logger.info("Started in mode: " + mode);
        if (mode == Mode.ERROR) {
            System.out.println("No correct command line arguments found.\n " +
                    "Use '-p -port <port> -mps <rate> -size <size> -h <hostname>' for pitcher or '-c -p <port> -bind <IP address>' for catcher");
            System.exit(1);
        }

        try {
            TcpPing tcpPing = TcpPingFactory.create(mode, args);
            tcpPing.execute();
        }
        catch (InvalidArgumentException pe) {
            logger.error("Invalid argument: " + pe);
            System.err.println("Invalid argument: " + pe.getMessage());
        }

        catch (IOException ioe) {
            logger.error("Cannot start " + mode.name() + ": " + ioe);
            System.err.println("Cannot start " + mode.name() + ": " + ioe.getMessage());
        }
    }



}
