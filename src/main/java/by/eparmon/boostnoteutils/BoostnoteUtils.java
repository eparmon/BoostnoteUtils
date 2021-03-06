package by.eparmon.boostnoteutils;

import by.eparmon.boostnoteutils.enums.Command;
import by.eparmon.boostnoteutils.enums.Flag;
import by.eparmon.boostnoteutils.exception.BoostException;
import by.eparmon.boostnoteutils.service.HelpService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoostnoteUtils {

    public static final Scanner SCANNER = new Scanner(System.in);
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        else
            command.run();
    }
}
