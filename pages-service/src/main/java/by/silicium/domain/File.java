package by.silicium.domain;

/**
 * Created by Kiryl_Parfiankou on 5/23/2016.
 */
public class File {

    private String id;
    private String name;

    public File(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public File() {
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
}
