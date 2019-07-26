package by.eparmon.boostnoteutils.model;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Folder {

    private String key;
    private String color;
    private String name;
    private Collection<Note> notes;
}
