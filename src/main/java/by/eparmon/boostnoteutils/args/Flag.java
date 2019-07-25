package by.eparmon.boostnoteutils.args;

import by.eparmon.boostnoteutils.exception.BoostException;

public enum Flag {
    HELP("help");

    private String name;

    Flag(String name) {
        this.name = name;
    }

    public static Flag get(String name) throws BoostException {
        for (Flag flag : values())
            if (flag.name.equals(name.substring(2)))
                return flag;
        throw new BoostException("unknown flag: '" + name + "'.\nTry 'boost --help' for the list of valid flags.\n");
    }
}
