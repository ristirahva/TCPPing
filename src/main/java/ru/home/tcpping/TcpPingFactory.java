package ru.home.tcpping;

import ru.home.tcpping.enumeration.Constant;
import ru.home.tcpping.enumeration.Mode;
import ru.home.tcpping.exception.InvalidArgumentException;
import ru.home.tcpping.impl.Catcher;
import ru.home.tcpping.impl.Pitcher;
import ru.home.tcpping.util.TcpPingHelper;
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
    public static TcpPing create(Mode mode, String args[]) throws InvalidArgumentException {
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
