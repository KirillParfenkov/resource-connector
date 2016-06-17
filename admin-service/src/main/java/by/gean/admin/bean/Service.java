package by.gean.admin.bean;

/**
 * Created by Kiryl_Parfiankou on 6/17/2016.
 */
public class Service {

    private String name;
    private String url;

    public Service(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}