package by.eparmon.boostnoteutils.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class FileHelper {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void createFileIfNotExists(String path) {
        try {
            new File(path.substring(0, path.lastIndexOf('/'))).mkdirs();
            new File(path).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getLines(File file) {
        try {
            return Files.lines(file.toPath()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
