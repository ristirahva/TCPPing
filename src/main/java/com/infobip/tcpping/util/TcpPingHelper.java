package com.infobip.tcpping.util;

import com.infobip.tcpping.enumeration.Mode;
import com.infobip.tcpping.exception.InvalidModeException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/**
 * Helper class.
 */
public class TcpPingHelper {

    final static Logger logger = Logger.getLogger(TcpPingHelper.class);

    /**
     * Define options by mode (PITCHER or CATCHER).
     *
     * @param mode
     * @return Options
     */
    public static Options getOptions(Mode mode) {
        switch (mode) {
            case PITCHER:
                return getPitcherOptions();
            case CATCHER:
                return getCatcherOptions();
            default:
                throw new InvalidModeException();
        }
    }

    private static Options getPitcherOptions() {
        Options pitcher = new Options();
        pitcher.addOption(Option.builder("p").hasArg(false).required(true).build());
        pitcher.addOption(Option.builder("port").hasArg(true).required(true).build());
        pitcher.addOption(Option.builder("mps").hasArg(true).required(false).build());
        pitcher.addOption(Option.builder("size").hasArg(true).required(false).build());
        pitcher.addOption(Option.builder("hostname").hasArg(true).required(true).build());
        return pitcher;
    }

    public static Options getCatcherOptions() {
        Options catcher = new Options();
        catcher.addOption(Option.builder("c").hasArg(false).required(true).build());
        catcher.addOption(Option.builder("port").hasArg(true).required(true).build());
        catcher.addOption(Option.builder("bind").hasArg(true).required(true).build());
        return catcher;
    }

    /**
     * Define TCP ping mode.
     *
     * @param args
     * @return
     */
    public static Mode getMode(String args[]) {
        if (args.length == 0) {
            return Mode.ERROR;
        }
        if (args[0].equals("-p")) {
            return Mode.PITCHER;
        }
        if (args[0].equals("-c")) {
            return Mode.CATCHER;
        }
        return Mode.ERROR;
    }

    /**
     * Calculate arithmetic mean by previous average, total values number and current value
     *
     * @param total
     * @param previousAverage
     * @param currentValue
     * @return
     */
    public static int calculateAverage(int total, int previousAverage, int currentValue) {
        int average = ((total - 1) * previousAverage + currentValue)/total;
        logger.debug("total = " + total + " prevAvg=" + previousAverage + " currentValue=" + currentValue + " result = " + average);
        return average;
    }
}
