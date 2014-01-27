package org.chii2.util;

import org.apache.commons.lang.StringUtils;
import regex2.Pattern;
import regex2.PatternSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

/**
 * Configuration Utility
 */
public class ConfigUtils {
    // Configuration Split Pattern
    private static final Pattern ConfigSplitPattern = Pattern.compile("\\|", Pattern.CASE_INSENSITIVE);
    private static final Pattern RegexSplitPattern = Pattern.compile(":", Pattern.CASE_INSENSITIVE);

    /**
     * Try ro load a config from properties, then parse into a array of configuration string
     *
     * @param props Configuration collection
     * @param key   Configuration key
     * @return Configuration String
     */
    public static String loadConfiguration(Dictionary props, String key) {
        // This should be checked, before passed to this method
        if (props != null && !props.isEmpty()) {
            // Read config string from property
            Object config = props.get(key);
            // It config string is not null or empty
            if (config != null && StringUtils.isNotBlank(String.valueOf(config))) {
                return String.valueOf(config);
            }
        }

        // Return null when parse failed
        return null;
    }

    /**
     * Try ro load a config from properties, then parse into a array of configuration string
     *
     * @param props Configuration collection
     * @param key   Configuration key
     * @return Configuration Array
     */
    public static List<String> loadConfigurations(Dictionary props, String key) {
        // This should be checked, before passed to this method
        if (props != null && !props.isEmpty()) {
            // Read config string from property
            Object configs = props.get(key);
            // It config string is not null or empty
            if (configs != null && StringUtils.isNotBlank(String.valueOf(configs))) {
                // Split into several configurations
                return Arrays.asList(ConfigSplitPattern.split(StringUtils.trim(String.valueOf(configs))));
            }
        }

        // Return null when parse failed
        return null;
    }

    /**
     * Try ro load a config from properties, then compile into a regexp pattern
     *
     * @param props Configuration collection
     * @param key   Configuration key
     * @return Regexp Pattern
     */
    public static Pattern loadPattern(Dictionary props, String key) {
        // This should be checked, before passed to this method
        if (props != null && !props.isEmpty()) {
            // Read config string from property
            Object config = props.get(key);
            // It config string is not null or empty
            if (config != null && StringUtils.isNotBlank(String.valueOf(config))) {
                try {
                    return Pattern.compile(StringUtils.trim(String.valueOf(config)), Pattern.CASE_INSENSITIVE);
                } catch (PatternSyntaxException e) {
                    return null;
                }
            }
        }

        // Return null when parse failed
        return null;
    }

    /**
     * Try ro load a config from properties, then compile into a list of regexp pattern
     *
     * @param props Configuration collection
     * @param key   Configuration key
     * @return Regexp Pattern List
     */
    public static List<Pattern> loadPatterns(Dictionary props, String key) {
        // This should be checked, before passed to this method
        if (props != null && !props.isEmpty()) {
            // Read config string from property
            Object configs = props.get(key);
            // It config string is not null or empty
            if (configs != null && StringUtils.isNotBlank(String.valueOf(configs))) {
                List<Pattern> patterns = new ArrayList<>();
                for (String config : RegexSplitPattern.split(StringUtils.trim(String.valueOf(configs)))) {
                    if (StringUtils.isNotBlank(config)) {
                        try {
                            patterns.add(Pattern.compile(StringUtils.trim(config), Pattern.CASE_INSENSITIVE));
                        } catch (PatternSyntaxException e) {
                            // Skip Error Pattern
                        }
                    }
                }
                return patterns;
            }
        }

        // Return null when parse failed
        return null;
    }
}
