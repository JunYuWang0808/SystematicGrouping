import java.util.ArrayList;
import java.util.List;

public class DeleteAction {
    public ChangeType t;
    private String index;
    public List<String> deletedSmt;

    public DeleteAction() {
        t = ChangeType.DELETE;
        deletedSmt = new ArrayList<>();
        index = "";
    }

    public String getIndex() {
        return index;
    }
    public void addDeleteSmt(String s) {
        deletedSmt.add(s);
    }

    public void setIndex(String idx) {
        index = idx;
    }

    public void setType(String s) {
        if (s.equals("deleteVariableDeclaration")) {
            t = ChangeType.DELETE;
        } else if (s.equals("updateExpressionStatement")) {
            t = ChangeType.UPDATE;
        } else {
            t = ChangeType.UPDATE;
            System.out.println("UNKNOWN TYPE in deleteAction java");
        }
    }
}
