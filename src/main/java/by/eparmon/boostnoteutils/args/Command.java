package by.eparmon.boostnoteutils.args;

import by.eparmon.boostnoteutils.exception.BoostException;

public enum Command {

    INIT("init");

    private String name;

    public String getName() {
        return name;
    }

    Command(String name) {
        this.name = name;
    }

    public static Command get(String name) throws BoostException {
        for (Command command : values())
            if (command.name.equals(name))
                return command;
        throw new BoostException("unknown command: '" + name + "'.\nTry 'boost --help' for the list of valid commands.\n");
    }
}
