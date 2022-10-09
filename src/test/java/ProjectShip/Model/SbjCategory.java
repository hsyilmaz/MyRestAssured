package ProjectShip.Model;

public class SbjCategory {

    String id;
    String name;
    String code;
    boolean active;


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return active;
    }

    public SbjCategory(String name, String code) {
        setName(name);
        setCode(code);
        setActive(true);
    }
}
