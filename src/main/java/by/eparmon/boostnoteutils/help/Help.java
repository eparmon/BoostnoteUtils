package by.eparmon.boostnoteutils.help;

import by.eparmon.boostnoteutils.args.Command;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class Help {

    private static final String RESOURCE_PATH_BASE = "/help/";
    private static final String GENERAL_HELP_FILE_PATH = "/help/general.txt";

    public static void showHelpMessage(Command command) {
        String fileName = command != null ? RESOURCE_PATH_BASE + command.getName() + ".txt" : GENERAL_HELP_FILE_PATH;
        try {
            System.out.println(IOUtils.toString(Help.class.getResourceAsStream(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
