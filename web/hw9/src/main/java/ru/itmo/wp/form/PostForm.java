
package ru.itmo.wp.form;

import ru.itmo.wp.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostForm {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    private String title;

    @NotNull
    @NotBlank
    @Size(min = 0, max = 65000)
    private String text;

    @NotNull
    @NotBlank
    String tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
