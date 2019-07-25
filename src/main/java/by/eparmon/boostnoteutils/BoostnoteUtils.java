package by.eparmon.boostnoteutils;

import by.eparmon.boostnoteutils.args.Flag;
import by.eparmon.boostnoteutils.exception.BoostException;
import by.eparmon.boostnoteutils.args.Command;
import by.eparmon.boostnoteutils.service.HelpService;

import java.util.ArrayList;
import java.util.List;

public class BoostnoteUtils {

    private static Command command;
    private static List<Flag> flags = new ArrayList<>();
    private static List<String> arguments = new ArrayList<>();

    public static void main(String... args) {
        try {
            readArgs(args);
            runCommand();
        } catch (BoostException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void readArgs(String[] args) throws BoostException {
        if (args.length == 0)
            throw new BoostException("missing command.\nTry 'boost --help' for more information.\n");
        for (String arg : args) {
            if (arg.startsWith("--")) {
                flags.add(Flag.get(arg));
            } else if (command == null) {
                command = Command.get(arg);
            } else {
                arguments.add(arg);
            }
        }
    }

    private static void runCommand() {
        if (flags.contains(Flag.HELP))
            HelpService.showHelpMessage(command);
    }
}
