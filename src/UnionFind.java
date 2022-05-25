import java.util.HashMap;

class UnionFind {
    private HashMap<String, String> father;
    private HashMap<String, Integer> sizeOfSet;
    private int numOfSet;

    public UnionFind() {
        father = new HashMap<>();
        sizeOfSet = new HashMap<>();
        numOfSet = 0;
    }

    public void add (String x) {
        if (father.containsKey(x)) {
            return;
        }

        father.put(x, null);
        sizeOfSet.put(x, 1);
        numOfSet += 1;
    }

    public String find(String x) {
        String root = x;
        while (father.get(root) != null) {
            root = father.get(root);
        }

        while (!x.equals(root)) {
            String originalFather = father.get(x);
            father.put(x, root);
            x = originalFather;
        }

        return root;
    }

    public void merge(String a, String b) {
        String rootX = find(a);
        String rootY = find(b);

        if (rootX.equals(rootY)) {
            return;
        }

        numOfSet -= 1;
        father.put(rootY, rootX);
        sizeOfSet.put(rootX, sizeOfSet.get(rootY) + sizeOfSet.get(rootX));
    }

    public int getNumOfSet() {
        return numOfSet;
    }

}