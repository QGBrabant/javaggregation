/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a singleton that contains informations and options about the
 * process that is running. This is also the class to use for saving results.
 *
 * @author qgbrabant
 */
public class Context {

    private final Map<String, String> options;
    private final List<String> argList;
    private ResultPack currentResultPack;
    //public static final String FOLDER = "/home/qgbrabant/MiscProg/NetBeansProjects/DiscreteMerge/data/output/";

    private Context() {
        options = new HashMap<>();
        argList = new ArrayList<>();
    }

    private static class RSHolder {

        private final static Context INSTANCE = new Context();
    }

    private static Context getInstance() {
        return RSHolder.INSTANCE;
    }

    /**
     * Parse the arguments given in args and store them. First arguments can be
     * specified without option name (as may as wanted). After the first option
     * name (starting with a "-") is given, each argument must be preceeded by
     * an option name.
     *
     * @param args
     */
    public static void parseArgs(String[] args) {
        int i = -1;
        while (++i < args.length && args[i].charAt(0) != '-') {
            getInstance().argList.add(args[i]);
        }
        while (++i < args.length) {
            if (args[i].charAt(0) == '-') {
                if (args[i].length() < 2) {
                    throw new IllegalArgumentException("Not a valid argument: " + args[i]);
                } else {
                    if (args.length - 1 == i) {
                        throw new IllegalArgumentException("Expected argument after: " + args[i]);
                    }
                    getInstance().options.put(args[i], args[i + 1]);
                    i++;
                }
            } else {
                throw new IllegalArgumentException("Option name expected instead of: " + args[i]);
            }
        }
    }

    /**
     * Returns the value of an option. Null if the option wasn't specified.
     *
     * @param key the name of the option (including "-").
     * @return the value of the option.
     */
    public static String getOption(String key) {
        return getInstance().options.get(key);
    }

    /**
     * Returns the value of an option. If the option, wasn't specified, the
     * default value is returned.
     *
     * @param key the name of the option (including "-").
     * @param defaultValue
     * @return the value of the option.
     */
    public static String getOption(String key, String defaultValue) {
        return getInstance().options.get(key) == null ? defaultValue : getInstance().options.get(key);
    }

    /**
     * Parses the value of an option and returns it as a list of Doubles. The
     * String is split using "," as separator.
     *
     * @param optionName
     * @param defaultValue
     * @return
     */
    public static List<Double> getDoubleListOption(String optionName, List<Double> defaultValue) {
        String val = getInstance().options.get(optionName);
        if (val == null) {
            return defaultValue;
        } else {
            List<Double> res = new ArrayList<>();
            String[] split = val.split(",");
            for (String s : split) {
                res.add(Double.parseDouble(s));
            }
            return res;
        }
    }

    /**
     * Parses the value of an option and returns it as a list of Integers. The
     * String is split using "," as separator.
     *
     * @param optionName
     * @param defaultValue
     * @return
     */
    public static List<Integer> getIntegerListOption(String optionName, List<Integer> defaultValue) {
        String val = getInstance().options.get(optionName);
        if (val == null) {
            return defaultValue;
        } else {
            List<Integer> res = new ArrayList<>();
            String[] split = val.split(",");
            for (String s : split) {
                res.add(Integer.parseInt(s));
            }
            return res;
        }
    }

    /**
     * Specifies the value of an option.
     *
     * @param key option name
     * @param value
     */
    public static void setOption(String key, String value) {
        getInstance().options.put(key, value);
    }

    /**
     * Returns a reference to the list of arguments (this is not a copy of the
     * original list, so you can add and remove values, if you dare).
     *
     * @return
     */
    public static List<String> getArgList() {
        return getInstance().argList;
    }

    
    public static <R extends Serializable> ResultPack<R> getResultPack(String name, int d) {
        ResultPack<R> pack = (ResultPack<R>) getInstance().currentResultPack;

        if (pack != null && pack.getDimensionality() == d && name.equals(pack.getDimensionality())) {
            return pack;
        }

        if (getOption("-load") == null || !getOption("-load").equals("true")) {
            return new ResultPack<>(name, d);
        }

        File fichier = new File(getOption("-output") + name + "_d" + d + ".result");
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(fichier));
            ResultPack res = (ResultPack) ois.readObject();
            getInstance().currentResultPack = res;
            return res;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ResultPack.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ResultPack.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Cannot open file, ceation of a new result pack.");
            return new ResultPack<>(name, d);
        }
        return null;
    }

    public static <R extends Serializable> void addResult(R res, String expName, Double... parameters) {
        ResultPack pack = getResultPack(expName, parameters.length);
        pack.addResult(new TupleImpl<>(parameters), res);
    }

    /**
     * Clear previous results from the ResultPack
     *
     * @param expName name of the ResultPack
     * @param d number of dimensions of the ResultPack
     */
    public static void clearResults(String expName, int d) {
        getResultPack(expName, d).clear();
    }

    /**
     * Save all results
     */
    public static void save() {
        getInstance().currentResultPack.save();
    }

}
