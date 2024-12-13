package vttp.batch5.ssf.noticeboard.models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Notice {
    
    @NotEmpty(message = "Title is mandatory.")
    @Size(min = 3, max = 128, message = "Title must be between 3 and 128 characters.")
    private String title;

    @NotEmpty(message = "Notice poster's email is mandatory.")
    @Email(message = "Must be a valid email.")
    private String poster;

    @NotNull(message = "Post date is mandatory.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Post date must be in the future.")
    private Date postDate;

    @NotEmpty(message = "Categories is mandatory, must include at least 1 category.")
    private List<String> categories;

    @NotEmpty(message = "Contents of the notice is mandatory")
    private String text;


    public Notice() {
    }


    public Notice(
            @NotEmpty(message = "Title is mandatory.") @Size(min = 3, max = 128, message = "Title must be between 3 and 128 characters.") String title,
            @NotEmpty(message = "Notice poster's email is mandatory.") @Email(message = "Must be a valid email.") String poster,
            @NotNull(message = "Post date is mandatory.") @Future(message = "Post date must be in the future.") Date postDate,
            @NotEmpty(message = "Categories is mandatory, must include at least 1 category.") List<String> categories,
            @NotEmpty(message = "Contents of the notice is mandatory") String text) {
        this.title = title;
        this.poster = poster;
        this.postDate = postDate;
        this.categories = categories;
        this.text = text;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getPoster() {
        return poster;
    }


    public void setPoster(String poster) {
        this.poster = poster;
    }


    public Date getPostDate() {
        return postDate;
    }


    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }


    public List<String> getCategories() {
        return categories;
    }


    public void setCategories(List<String> categories) {
        this.categories = categories;
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }

    
    
}
