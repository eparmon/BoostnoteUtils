package by.eparmon.boostnoteutils.enums;

import by.eparmon.boostnoteutils.exception.BoostException;
import by.eparmon.boostnoteutils.service.CourseService;
import by.eparmon.boostnoteutils.service.NotesService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {

    SCAN("scan"), START_COURSE("start-course");

    private String name;

    public static Command get(String name) throws BoostException {
        for (Command command : values())
            if (command.name.equals(name))
                return command;
        throw new BoostException("unknown command: '" + name + "'.\nTry 'boost --help' for the list of valid commands.\n");
    }

    public void run(String... args) {
        switch (this) {
            case SCAN:
                NotesService.scanNotes();
                break;
            case START_COURSE:
                if (args.length == 0)
                    CourseService.startCourse();
                else
                    CourseService.startCourse(args[0]);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
