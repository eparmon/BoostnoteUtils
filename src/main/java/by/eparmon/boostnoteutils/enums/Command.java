package by.eparmon.boostnoteutils.enums;

import by.eparmon.boostnoteutils.exception.BoostException;
import by.eparmon.boostnoteutils.service.NotesService;

public enum Command {

    SCAN("scan");

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

    public void run() {
        switch (this) {
            case SCAN:
                NotesService.scanNotes();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
