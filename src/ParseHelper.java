import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ParseHelper {

    private  boolean flag;
    private List<Component> listOfComponent;
    private  Component currComponent;
    private UnionFind uf;

    public ParseHelper() {
        flag = false;
        listOfComponent = new ArrayList<>();
        uf = new UnionFind();
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
}
