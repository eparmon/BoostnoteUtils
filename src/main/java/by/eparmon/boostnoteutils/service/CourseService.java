package by.eparmon.boostnoteutils.service;

import by.eparmon.boostnoteutils.enums.Colour;
import by.eparmon.boostnoteutils.model.Folder;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static by.eparmon.boostnoteutils.BoostnoteUtils.SCANNER;

public class CourseService {

    public static void startCourse() {
        System.out.print("Enter the course name: ");
        String title = SCANNER.nextLine();
        while (title.isEmpty()) {
            System.out.print("The name cannot be empty. Please, enter the name: ");
            title = SCANNER.nextLine();
        }
        startCourse(title);
    }

    public static void startCourse(String title) {
        System.out.println("Colour options: ");
        for (Colour colour : Colour.values())
            System.out.println(colour.ordinal() + ": " + colour.getName());
        System.out.print("Choose one: ");
        Colour colour = Colour.get(SCANNER.nextLine());
        while (colour == null) {
            System.out.println("Please, enter either number or name of the colour");
            colour = Colour.get(SCANNER.nextLine());
        }
        Folder folder = NotesService.createFolder(title, colour);
        System.out.print("Please, enter the tags, separated by comma: ");
        NotesService.createNote(folder.getTitle(), folder, true, true,
                Arrays.asList(StringUtils.split(SCANNER.nextLine(), ",")));
    }
}
