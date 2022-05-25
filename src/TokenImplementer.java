//import java.util.ArrayList;
//import java.util.List;
//import java.util.StringTokenizer;
//
//public class TokenImplementer {
//    private UnionFind uf;
//    public TokenImplementer() {
//        uf = new UnionFind();
//    }
//
//    // Tokenize each statement
//    public List<String> parseStatement(String stm) {
//        List<String> result = new ArrayList<>();
//
//        System.out.println(" ------------------------------------------------------------------------------ ");
//        StringTokenizer st = new StringTokenizer(stm, ". ", true);
//        System.out.println("testReturnDelimiters : Count-Tokens " + st.countTokens());
//        while (st.hasMoreTokens()) {
//            String tk = st.nextToken();
//            if (!tk.equals(" ")) {
//                result.add(tk);
//                System.out.println("testReturnDelimiters : Next-Token = " + tk);
//            }
//        }
//
//        return result;
//    }
//
//    public static void main(String[] args) {
//        ParseHelper helper = new ParseHelper();
//        helper.parseDiffFile("/Users/junyu/Desktop/2022 spring/cs230/Project/project/src/grouping.txt");
//    }
//}
