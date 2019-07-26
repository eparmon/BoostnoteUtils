package by.eparmon.boostnoteutils.service;

import by.eparmon.boostnoteutils.enums.Configuration;
import by.eparmon.boostnoteutils.file.FileHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

class ConfigurationService {

    private static final String CONFIG_PATH = "~/.boostnoteutils/config";

    static {
        FileHelper.createFileIfNotExists(CONFIG_PATH);
    }

    static String getConfiguration(Configuration config) {
        return getConfiguration().get(config.name());
    }

    static void setConfiguration(Configuration config, String value) {
        Map<String, String> configuration = getConfiguration();
        configuration.put(config.name(), value);
        try (PrintWriter writer = new PrintWriter(CONFIG_PATH)) {
            configuration.forEach((k, v) -> writer.println(k + ":" + v));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> getConfiguration() {
        try {
            return Files.lines(Paths.get(CONFIG_PATH))
                    .collect(Collectors.toMap(ConfigurationService::getConfigName, ConfigurationService::getConfigValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getConfigName(String line) {
        return line.substring(0, line.indexOf(":"));
    }

    private static String getConfigValue(String line) {
        return line.substring(line.indexOf(":") + 1);
    }
}
