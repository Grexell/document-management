package by.dima.resourceserver.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
    @Id
    @Column(length = 35)
    private String username;

    private String name;

    @ManyToMany(targetEntity = DocumentAccess.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<DocumentAccess> accesses;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DocumentAccess> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<DocumentAccess> accesses) {
        this.accesses = accesses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) &&
                name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name);
    }
}
