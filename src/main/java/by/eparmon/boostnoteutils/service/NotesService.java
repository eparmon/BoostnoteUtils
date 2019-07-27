package by.eparmon.boostnoteutils.service;

import by.eparmon.boostnoteutils.cson.CsonParser;
import by.eparmon.boostnoteutils.enums.Colour;
import by.eparmon.boostnoteutils.enums.Configuration;
import by.eparmon.boostnoteutils.file.FileHelper;
import by.eparmon.boostnoteutils.model.Folder;
import by.eparmon.boostnoteutils.model.Note;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static by.eparmon.boostnoteutils.BoostnoteUtils.OBJECT_MAPPER;
import static by.eparmon.boostnoteutils.BoostnoteUtils.SCANNER;

public class NotesService {

    private static final String DEFAULT_BOOSTNOTE_HOME_PATH = System.getProperty("user.home") + "/Boostnote";
    private static final String BOOSTNOTE_JSON_FILE_NAME = "boostnote.json";
    private static final String BOOSTNOTE_NOTES_DIRECTORY_NAME = "notes";

    private static File boostnoteJson;
    private static File boostnoteNotesDirectory;

    private static Map<String, Folder> folders = new HashMap<>();

    static Folder createFolder(String title, Colour colour) {
        Folder folder = Folder.builder()
                .key(generateKey())
                .title(title)
                .notes(Collections.emptySet())
                .build();
        determineBoostnoteHomePath();
        JsonNode boostnoteJson = parseBoostnoteJson();
        ArrayNode folders = (ArrayNode) boostnoteJson.get("folders");
        folders.add(OBJECT_MAPPER.createObjectNode()
                .put("key", folder.getKey())
                .put("name", folder.getTitle())
                .put("color", colour.getCode()));
        saveBoostnoteJson(boostnoteJson);
        return folder;
    }

    private static String generateKey() {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 20; i++)
            key.append(Integer.toHexString((int) (Math.random() * 16)));
        return key.toString();
    }

    static Note createNote(String title,
                           Folder folder,
                           boolean isStarred,
                           boolean isPinned,
                           Collection<String> tags) {
        Note note = Note.builder()
                .title(title)
                .folder(folder)
                .isStarred(isStarred)
                .isPinned(isPinned)
                .tags(tags)
                .content("# " + title)
                .build();
        determineBoostnoteHomePath();
        File file = FileHelper.createFileIfNotExists(boostnoteNotesDirectory + "/" + UUID.randomUUID() + ".cson");
        String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("createdAt: \"" + now + "\"");
            writer.println("updatedAt: \"" + now + "\"");
            writer.println("type: \"MARKDOWN_NOTE\"");
            writer.println("folder: \"" + folder.getKey() + "\"");
            writer.println("title: \"" + note.getTitle() + "\"");
            writer.println("tags: [");
            tags.forEach(tag -> writer.println("  \"" + tag + "\""));
            writer.println("]");
            writer.println("content: '''");
            writer.println("  # " + note.getTitle());
            writer.println("---");
            writer.println("[//]: # (Curriculum start)");
            writer.println("[//]: # (Curriculum end)");
            writer.println("'''");
            writer.println("linesHighlighted: []");
            writer.println("isStarred: true");
            writer.println("isPinned: true");
            writer.println("isTrashed: false");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return note;
    }

    public static void scanNotes() {
        determineBoostnoteHomePath();
        folders.clear();
        extractFolders();
        extractNotes();
        // store notes as necessary
    }

    private static void determineBoostnoteHomePath() {
        String assumedBoostnoteHomePath = getAssumedBoostnoteHomePath();
        setBoostnoteHomePath(assumedBoostnoteHomePath);
        while (boostnoteHomePathIsInvalid()) {
            System.out.print("Could not find the Boostnote home directory. Please, enter the path to it: ");
            setBoostnoteHomePath(SCANNER.nextLine());
        }
    }

    private static String getAssumedBoostnoteHomePath() {
        String boostnoteHomePath = ConfigurationService.getConfiguration(Configuration.BOOSTNOTE_HOME_PATH);
        if (boostnoteHomePath == null) {
            boostnoteHomePath = DEFAULT_BOOSTNOTE_HOME_PATH;
            ConfigurationService.setConfiguration(Configuration.BOOSTNOTE_HOME_PATH, DEFAULT_BOOSTNOTE_HOME_PATH);
        }
        return boostnoteHomePath;
    }

    private static void setBoostnoteHomePath(String path) {
        boostnoteJson = new File(path + "/" + BOOSTNOTE_JSON_FILE_NAME);
        boostnoteNotesDirectory = new File(path + "/" + BOOSTNOTE_NOTES_DIRECTORY_NAME);
    }

    private static boolean boostnoteHomePathIsInvalid() {
        return !boostnoteJson.isFile() || !boostnoteNotesDirectory.isDirectory();
    }

    private static void extractFolders() {
        for (JsonNode folderNode : parseFolders()) {
            String key = folderNode.get("key").asText();
            folders.put(key, Folder.builder()
                    .key(key)
                    .title(folderNode.get("name").asText())
                    .notes(new HashSet<>())
                    .build());
        }
    }

    private static Iterable<? extends JsonNode> parseFolders() {
        return parseBoostnoteJson().get("folders");
    }

    private static JsonNode parseBoostnoteJson() {
        try {
            return OBJECT_MAPPER.readTree(boostnoteJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveBoostnoteJson(JsonNode newJson) {
        try (PrintWriter writer = new PrintWriter(boostnoteJson)) {
            Object json = OBJECT_MAPPER.readValue(newJson.toString(), Object.class);
            writer.println(OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extractNotes() {
        for (File noteFile : Objects.requireNonNull(boostnoteNotesDirectory.listFiles())) {
            Map<String, String> noteInfo = CsonParser.parseCsonFile(noteFile);
            Folder folder = folders.get(noteInfo.get("folder"));
            Note note = Note.builder()
                    .title(noteInfo.get("title"))
                    .folder(folder)
                    .isStarred(Boolean.parseBoolean(noteInfo.get("isStarred")))
                    .isPinned(Boolean.parseBoolean(noteInfo.get("isPinned")))
                    .tags(parseArray(noteInfo.get("tags")))
                    .content(noteInfo.get("content"))
                    .build();
            folder.getNotes().add(note);
        }
    }

    private static Collection<String> parseArray(String array) {
        return Arrays.stream(StringUtils.split(array, '\n'))
                .filter(line -> line.contains("\""))
                .map(line -> line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")))
                .collect(Collectors.toSet());
    }
}
