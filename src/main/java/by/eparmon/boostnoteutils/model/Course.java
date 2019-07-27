package by.eparmon.boostnoteutils.model;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    private String title;
    private String url;
    private Collection<String> topics;
}
