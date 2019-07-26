package by.eparmon.boostnoteutils.service;

import by.eparmon.boostnoteutils.BoostnoteUtils;
import by.eparmon.boostnoteutils.cson.CsonParser;
import by.eparmon.boostnoteutils.enums.Configuration;
import by.eparmon.boostnoteutils.model.Folder;
import by.eparmon.boostnoteutils.model.Note;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NotesService {

    private static final String DEFAULT_BOOSTNOTE_HOME_PATH = System.getProperty("user.home") + "/Boostnote";
    private static final String BOOSTNOTE_JSON_FILE_NAME = "boostnote.json";
    private static final String BOOSTNOTE_NOTES_DIRECTORY_NAME = "notes";

    private static File boostnoteJson;
    private static File boostnoteNotesDirectory;

    private static Map<String, Folder> folders = new HashMap<>();

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
            setBoostnoteHomePath(new Scanner(System.in).nextLine());
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
                    .color(folderNode.get("color").asText())
                    .name(folderNode.get("name").asText())
                    .notes(new HashSet<>())
                    .build());
        }
    }

    private static Iterable<? extends JsonNode> parseFolders() {
        try {
            return BoostnoteUtils.OBJECT_MAPPER.readTree(boostnoteJson).get("folders");
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
