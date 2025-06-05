import java.io.Serializable;

public abstract class VirtualNode implements Serializable {
    protected String name;
    protected String path;

    public VirtualNode(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}