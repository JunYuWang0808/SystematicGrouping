import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SimilarityFinder {
    private UnionFind uf;
    public SimilarityFinder() {
        uf = new UnionFind();
    }
    public HashMap<String, List<String>> determineSimilarity(List<Component> list) {
        // 1. init union find
        for (int i = 0; i < list.size(); i ++) {
            uf.add(String.valueOf(i));
            list.get(i).calculateRoots();
        }
        // 2. calculate union find of component
        for (int i = 0; i < list.size(); i ++) {
            for (int j = 0; j < list.size(); j ++) {
                if (similiar(list.get(i), list.get(j))) {
                    uf.merge(String.valueOf(i), String.valueOf(j));
                }
            }
        }
        // 3. build hashmap, key: root, value: components whose roots are <root>
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
        return groupingResult;

//        HashMap<Integer, List<String>> systematicChangeMap = systematicChangeGenerator(groupingResult, list);
//        System.out.println();
    }



//    public  HashMap<Integer, List<String>> systematicChangeGenerator(HashMap<String, List<String>> groupingResult,List<Component> list ) {
//        // key is component index, value is the list of changes that it should keep to generate regex
//        HashMap<Integer, List<String>> result = new HashMap<>();
//        for (String key : groupingResult.keySet()) {
//            // for loop every component in this group
//            List<String> compIndexList = groupingResult.get(key);
//            HashMap<String, Integer> freq = new HashMap<>();
//            // for each component, add freq to the map
//            for (String idx : compIndexList) {
//                Component currComp = list.get(Integer.parseInt(idx));
//                List<String> changeIndex = currComp.getChangeIndex();
//                for (String indexItem : changeIndex) {
//                    String rootIndex = currComp.uf.find(indexItem);
//                    freq.put(rootIndex, freq.getOrDefault(rootIndex, 0) + 1);
//                }
//            }
//            List<String> keptChangeList = new ArrayList<>();
//            Component keyComponent = list.get(Integer.parseInt(key));
//            for (String idx : keyComponent.getChangeIndex()) {
//                String tempRootIdx = keyComponent.uf.find(idx);
//                if (freq.get(tempRootIdx) >= groupingResult.get(key).size()) {
//                    keptChangeList.add(idx);
//                }
//            }
//
//            result.put(Integer.parseInt(key), keptChangeList);
//        }
//
//
//        return result;
//    }


    // Given two components, calculate similarity based on changes' root
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

        if (((matchedNum + 1) / totalNum) >= 0.99) {
            return true;
        }

        return false;
    }

    public static List<String> generateToken(HashMap<String, List<String>> systematicMap, List<Component> list,
                                             UnionFind uf, HashMap<String, DeleteAction> deleteActionHashMap) {
        List<String> result = new ArrayList<>();
        for (String key : systematicMap.keySet()) {
            Component keyComp = list.get(Integer.parseInt(key));
            List<String> roots = keyComp.getRoots();
            StringBuilder sb = new StringBuilder();
            sb.append(".*");
            for (String r : roots) {
                DeleteAction da = deleteActionHashMap.get(r);
                for (int i = 0; i < da.deletedSmt.size(); i ++) {
                    sb.append(da.deletedSmt.get(i));
                    sb.append(".*");
                }
            }
            result.add(sb.toString());
        }

        return result;
    }

    public static void main(String[] args) {
        ParseHelper helper = new ParseHelper();
        System.out.print(System.getProperty("user.dir"));
        // step1. generate component
        helper.parseDiffFile("./src/CLIDIFFOUTPUT/grouping.txt");
        // step2. generate change link unionfind
        helper.parseLinkFile("./src/CLIDIFFOUTPUT/Links.txt");
        // step3. group component based on unionfind
        SimilarityFinder finder = new SimilarityFinder();
        HashMap<String, List<String>> systematicMap = finder.determineSimilarity(helper.getListOfComponent());
        UnionFind uf = helper.getListOfComponent().get(0).uf; // unionfind of change links
        // step4. calculate common sequence
        HashMap<String, DeleteAction> deleteActionHashMap = helper.parseConciseDiffFile("./src/CLIDIFFOUTPUT/concise_diff.txt", "./src/source/CLDIFFCmd.txt");
        List<String> regex = generateToken(systematicMap,helper.getListOfComponent(), uf, deleteActionHashMap );


    }
}
