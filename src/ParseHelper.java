import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import java.nio.file.Files;

public class ParseHelper {

    private  boolean flag;
    private List<Component> listOfComponent;
    private  Component currComponent;
    private HashMap<String, DeleteAction> deleteActionMap;
    private UnionFind uf;

    public ParseHelper() {
        flag = false;
        listOfComponent = new ArrayList<>();
        uf = new UnionFind();
        deleteActionMap = new HashMap<>();
    }

    public List<Component> getListOfComponent() {
        return listOfComponent;
    }
    public void parseLinkFile(String filename) {

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> list = List.of(data.split(" "));
                if (list.size() > 7 && list.get(6).equals("Systematic-Change")) {
                    String firstChangeIdx= list.get(0);
                    String secondChangeIdx = list.get(3);
                    uf.add(firstChangeIdx);
                    uf.add(secondChangeIdx);
                    uf.merge(firstChangeIdx, secondChangeIdx);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void parseDiffFile(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> list = List.of(data.split(" "));
                if (list.size() == 1) {
                    // finish reading a component
                    flag = false;
                    listOfComponent.add(currComponent);
                } else if (list.get(0).equals("CLDIFFCmd.")) {
                    // start a new component
                    flag = true;
                    currComponent = new Component(uf);
                    currComponent.addDescription(list);
                    continue;
                }

                if (flag) {
                    currComponent.addChangeItems(list);
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public HashMap<String, DeleteAction> parseConciseDiffFile(String filename, String sourceFile) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            DeleteAction action = null;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.strip();
                List<String> list = List.of(data.split(" "));
                if (list.get(0).charAt(0) >= '0' && list.get(0).charAt(0) <= '9' ) {
                    // start a new component
                    if (action != null) {
                        deleteActionMap.put(action.getIndex(), action);
                    }
                    action = new DeleteAction();
                    action.setType(list.get(1));
                    action.setIndex(list.get(0));

                } else {
                   if (action.t == ChangeType.DELETE) {
                       List<String> tempList = List.of(list.get(0).split("[(),]"));
                       int indexFile = Integer.parseInt(tempList.get(1));
                       String line = Files.readAllLines(Paths.get(sourceFile)).get(indexFile - 1);
                       action.addDeleteSmt(line);
                   } else if (action.t == ChangeType.UPDATE) {
                       if (list.get(0).contains("delete")) {
                           action.addDeleteSmt(list.get(1));
                       }
                   }
               }


            }
            deleteActionMap.put(action.getIndex(), action);
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return deleteActionMap;
    }
}
