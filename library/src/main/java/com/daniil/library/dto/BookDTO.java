package com.daniil.library.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookDTO {

    @Nullable
    private Integer id;
    private String title;
    private String category;
    private String author;
    private int publicationYear;
    private boolean availability;

}
