package by.dima.resourceserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class DocumentAccess {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;

    private String post;
    private String department;

    public DocumentAccess() {
    }

    public DocumentAccess(String department, String post) {
        this.department = department;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentAccess access = (DocumentAccess) o;
        return id.equals(access.id) &&
                Objects.equals(post, access.post) &&
                Objects.equals(department, access.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
