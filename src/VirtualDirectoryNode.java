import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class VirtualDirectoryNode extends VirtualNode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Map<String, VirtualNode> children = new LinkedHashMap<>();

    public VirtualDirectoryNode(String name, String path) {
        super(name, path);
    }

    public void addChild(VirtualNode node) {
        children.put(node.getName(), node);
    }

    public void removeChild(String name) {
        children.remove(name);
    }

    public VirtualNode getChild(String name) {
        return children.get(name);
    }

    public Collection<VirtualNode> getChildren() {
        return children.values();
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }
}