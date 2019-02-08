package by.dima.resourceserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Document {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;
    private String name;
    private String title;
    private String description;

    @JsonProperty
    private String creator;

    @ManyToMany(targetEntity = User.class)
    @JsonProperty
    private List<User> signList;

    @ManyToMany(targetEntity = User.class)
    @JsonProperty
    private List<User> needSignList;

    @JsonProperty
    private Date acceptDate;

    @JsonProperty
    private Date createDate;

    @JsonProperty
    private Date validTo;
    private boolean archived;

    @ManyToMany(targetEntity = DocumentAccess.class, fetch = FetchType.EAGER)
    private List<DocumentAccess> accesses;

    public Document() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<User> getSignList() {
        return signList;
    }

    public void setSignList(List<User> signList) {
        this.signList = signList;
    }

    public List<User> getNeedSignList() {
        return needSignList;
    }

    public void setNeedSignList(List<User> needSignList) {
        this.needSignList = needSignList;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
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
        Document document = (Document) o;
        return archived == document.archived &&
                Objects.equals(id, document.id) &&
                Objects.equals(name, document.name) &&
                Objects.equals(title, document.title) &&
                Objects.equals(description, document.description) &&
                Objects.equals(creator, document.creator) &&
                Objects.equals(signList, document.signList) &&
                Objects.equals(needSignList, document.needSignList) &&
                Objects.equals(acceptDate, document.acceptDate) &&
                Objects.equals(createDate, document.createDate) &&
                Objects.equals(validTo, document.validTo) &&
                Objects.equals(accesses, document.accesses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
