package com.infobip.tcpping;

import com.infobip.tcpping.enumeration.Constant;
import com.infobip.tcpping.enumeration.Mode;
import com.infobip.tcpping.exception.InvalidArgumentException;
import com.infobip.tcpping.impl.Catcher;
import com.infobip.tcpping.impl.Pitcher;
import com.infobip.tcpping.util.TcpPingHelper;
import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for both Pitcher and Catcher.
 */
public class TcpPingFactory {

    /**
     * Create Pitcher or Catcher object.
     *
     * @param mode PITCHER or CATCHER
     * @param args command line arguments
     * @return TcpPing implementation.
     * @throws InvalidArgumentException
     */
    public static TcpPing createObject(Mode mode, String args[]) throws InvalidArgumentException {
        TcpPing object = null;
        Options options = TcpPingHelper.getOptions(mode);
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args, false);
        }
        catch (ParseException pe) {
            throw new InvalidArgumentException(pe.getMessage());
        }

        if (mode == Mode.PITCHER) {
            object = new Pitcher(prepareParamsForPitcher(commandLine));
        }
        else if(mode == Mode.CATCHER) {
            object = new Catcher(prepareParamsForCatcher(commandLine));
        }
        return object;
    }

    /**
     * Pitcher parameters preparation.
     *
     * @param commandLine
     * @return
     */
    private static Map<String, String> prepareParamsForPitcher(CommandLine commandLine) {
        String param;
        Map paramMap = new HashMap<String, String>();
        param = commandLine.getOptionValue("port");
        if (param.matches("^\\d+")) {
            if (Integer.parseInt(param) <= 0 ) {
                throw new InvalidArgumentException("port number must be > 0");
            }
            paramMap.put("port", commandLine.getOptionValue("port"));
        }
        else {
            throw new InvalidArgumentException("port number must be numeric");
        }
        param = commandLine.getOptionValue("mps", Constant.MPS_DEFAULT.toString());

        if (param.matches("^\\d+")) {
            if (Integer.parseInt(param) <= 0) {
                throw new InvalidArgumentException("mps value must be > 0");
            }
            paramMap.put("mps", param);
        }
        else {
            throw new InvalidArgumentException("mps value must be numeric");
        }
        param = commandLine.getOptionValue("size", Constant.SIZE_DEFAULT.toString());
        if (param.matches("^\\d+")) {
            if (Integer.parseInt(param) < Constant.SIZE_MIN.getValue()
                    || Integer.parseInt(param) > Constant.SIZE_MAX.getValue()) {
                throw new InvalidArgumentException("size value must be between " + Constant.SIZE_MIN.getValue()
                + " and " + Constant.SIZE_MAX.getValue());

            }
            paramMap.put("size", param);
        }
        else {
            throw new InvalidArgumentException("mps value must be numeric");
        }
        paramMap.put("hostname", commandLine.getOptionValue("hostname"));
        return paramMap;
    }

    /**
     * Catcher parameters preparation.
     *
     * @param commandLine
     * @return
     */
    private static Map<String, String> prepareParamsForCatcher(CommandLine commandLine) {
        String param;
        Map paramMap = new HashMap<String, String>();
        param = commandLine.getOptionValue("port");
        if (commandLine.getOptionValue("port").matches("^\\d+")) {
            if (Integer.parseInt(param) <= 0 ) {
               throw new InvalidArgumentException("port number must be > 0");
            }
            paramMap.put("port", commandLine.getOptionValue("port"));
        }
        else {
            throw new InvalidArgumentException("port number must be numeric");
        }

        paramMap.put("bind", commandLine.getOptionValue("bind"));
        return paramMap;
    }
}
