package by.gean.admin.bean;

import java.util.List;

/**
 * Created by Kiryl_Parfiankou on 6/17/2016.
 */
public class MenuItem {

    private String name;
    private String link;
    private List<MenuItem> children;

    public MenuItem() {
    }

    public MenuItem(String name, String link, List<MenuItem> childrens) {
        this.name = name;
        this.link = link;
        this.children = childrens;
    }

    public MenuItem(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItem> children) {
        this.children = children;
    }
}