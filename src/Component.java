import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Component {
    private List<String> changeItems;
    private List<String> changeIndex;
    private List<String> roots;
    private String description;
    private UnionFind uf;

    public Component(UnionFind uf) {
        changeItems = new ArrayList<>();
        changeIndex = new ArrayList<>();
        description = "";
        roots = new ArrayList<>();
        this.uf = uf;
    }

    public void calculateRoots() {
        for (String s : changeIndex) {
            roots.add(uf.find(s));
        }
    }

    public List<String> getRoots() {
        return roots;
    }

    public List<String> getChangeIndex() {
        return changeIndex;
    }

    public String getDescription() {
        return description;
    }

    public void addDescription(List<String> words) {
        description = String.join(" ", words);
    }

    public void addChangeItems(List<String> words) {
        String result = String.join(" ", words);
        changeItems.add(result);
        changeIndex.add(words.get(0));
    }



}
