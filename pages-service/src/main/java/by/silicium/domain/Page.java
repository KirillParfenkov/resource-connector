package by.silicium.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiryl Parfiankou on 29.5.16.
 */
public class Page {

    private String id;
    private String name;
    private String content;
    private String parentId;
    private List<String> childrens;

    public Page(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public Page() {
        childrens = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<String> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<String> childrens) {
        this.childrens = childrens;
    }
}