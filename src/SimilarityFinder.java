import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SimilarityFinder {
    private UnionFind uf;
    public SimilarityFinder() {
        uf = new UnionFind();
    }
    public void determineSimilarity(List<Component> list) {
        for (int i = 0; i < list.size(); i ++) {
            uf.add(String.valueOf(i));
        }

        for (int i = 0; i < list.size(); i ++) {
            for (int j = 0; j < list.size(); j ++) {
                if (similiar(list.get(i), list.get(j))) {
                    uf.merge(String.valueOf(i), String.valueOf(j));
                }
            }
        }

        HashMap<String, List<String>> groupingResult = new HashMap<>();
        for (int i = 0; i < list.size(); i ++) {
            String root = uf.find(String.valueOf(i));
            List<String> listCurr = groupingResult.getOrDefault(root, new ArrayList<>());
            listCurr.add(String.valueOf(i));
            groupingResult.put(root, listCurr);
        }
        System.out.println();
        System.out.println("---------------Systematic Changes----------------");
        int idx = 0;
        for (String key : groupingResult.keySet()) {
            System.out.println("Group # " + idx);
            List<String> l = groupingResult.get(key);
            for (String index : l) {
                System.out.println(list.get(Integer.parseInt(index)).getDescription());
            }
            System.out.println();
        }
        System.out.println("---------------Systematic Changes Analyze End----------------");

    }

    private boolean similiar(Component c1, Component c2) {
        HashSet<String> cmpt1IdxSet = new HashSet<>(c1.getRoots());
        HashSet<String> cmpt2IdxSet = new HashSet<>(c2.getRoots());

        int totalNum = Math.min(cmpt1IdxSet.size() + 1, cmpt2IdxSet.size() + 1);
        int matchedNum = 0;
        for (String key : cmpt1IdxSet) {
            if (cmpt2IdxSet.contains(key)) {
                matchedNum ++;
            }
        }

        if (((matchedNum + 1) / totalNum) > 0.8) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        ParseHelper helper = new ParseHelper();
        System.out.print(System.getProperty("user.dir"));
        helper.parseDiffFile("./src/grouping.txt");
        helper.parseLinkFile("./src/Links.txt");

        SimilarityFinder finder = new SimilarityFinder();
        finder.determineSimilarity(helper.getListOfComponent());
    }
}
