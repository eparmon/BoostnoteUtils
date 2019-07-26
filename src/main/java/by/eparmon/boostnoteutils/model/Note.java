package by.eparmon.boostnoteutils.model;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    private String title;
    private Folder folder;
    private boolean isStarred;
    private boolean isPinned;
    private Collection<String> tags;
    private String content;
}
