package com.example.errorfreetext.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "text must not be blank")
    @Size(min = 3, message = "text must be at least 3 characters")
    @Pattern(regexp = "(?s).*\\p{L}.*", message = "text must contain letters")
    private String text;

    @Pattern(regexp = "(?i)ru|en", message = "language must be ru or en")
    private String language;
}
