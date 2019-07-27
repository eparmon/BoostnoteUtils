package by.eparmon.boostnoteutils.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class FileHelper {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createFileIfNotExists(String path) {
        File file = new File(path);
        try {
            int separator = path.lastIndexOf('/');
            if (separator != -1)
                new File(path.substring(0, separator)).mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public static List<String> getLines(File file) {
        try {
            return Files.lines(file.toPath()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
