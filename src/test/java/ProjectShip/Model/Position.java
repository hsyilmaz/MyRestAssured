package ProjectShip.Model;

public class Position {

    private String id;
    private String name;
    private String shortName;
    private String tenantId;

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    public void setId(String id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getId() {
        return id;
    }

    public Position(String name, String shortName){
        setName(name);
        setShortName(shortName);
        setTenantId("5fe0786230cc4d59295712cf");

    }



}
