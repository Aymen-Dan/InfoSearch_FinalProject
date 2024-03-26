import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**TODO: write better comments for methods*/

/**Practice 4 3-Gram Index*/
public class ThreeGIndex {

    private HashMap<String, ArrayList<String>> index;
    private Index ind;

    public ThreeGIndex(Index ind) {
        this.ind = ind;
        index = new HashMap<>();
        for (String s : ind.matrix.keySet()) {
            index.put(s, permute(s));
        }

        this.saveToFile();
    }

    private ArrayList<String> permute(String input) {
        input = input.toLowerCase();
        ArrayList<String> res = new ArrayList<>();
        String word = "$" + input + "$";
        String temp = "";
        int counter = 0;

        while (!temp.endsWith("$")) {
            temp = word.substring(counter, counter + 3);
            res.add(temp);
            counter++;
        }
        return res;
    }

    public HashMap<String, ArrayList<Integer>> search(String input) throws Exception {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", "");

        if (!input.matches("\\*?[\\w]+\\*?[\\w]+\\*?"))
            throw new Exception("Incorrect format.");

        HashMap<String, ArrayList<Integer>> res = new HashMap<>();

        if (!input.contains("*")) {
            HashMap<String, ArrayList<Integer>> ret = new HashMap<>();
            ret.put(input, ind.matrix.get(input));
            return ret;
        }
        input = "$" + input + "$";
        input = input.replaceAll("\\*", "&");

        ArrayList<String> temp = permuteSearch(input);

        // Formatting and printing the search result
        System.out.println("Search result for '" + input + "':");
        for (String str : index.keySet()) {
            int n1 = 0;
            for (String str2 : temp) {
                int n = 0;
                for (String str1 : index.get(str)) {
                    if (str1.equals(str2)) {
                        n++;
                    }
                }
                if (n > 0) n1++;
            }
            if (n1 >= temp.size()) {
                System.out.println("Word: " + str);
                System.out.println("Document IDs: " + ind.matrix.get(str));
                System.out.println();
                res.put(str, ind.matrix.get(str));
            }
        }

        return res;
    }

    /**
     * Helper method for search
     */
    private ArrayList<String> permuteSearch(String input) {
        input = input.toLowerCase();
        ArrayList<String> res = new ArrayList<>();
        String word = input;
        String temp = "";
        int counter = 0;

        while (!temp.endsWith("$")) {
            temp = word.substring(counter, counter + 3);
            if (!temp.contains("&")) {
                res.add(temp);
            }
            counter++;
        }
        return res;
    }

    /**
     * Print Three-Gram Index to console
     */
    public void printThGndex() {
        System.out.println("Three-Gram Index:");

        for (String key : index.keySet()) {
            System.out.print(key + ": ");
            ArrayList<String> values = index.get(key);

            for (int i = 0; i < values.size(); i++) {
                System.out.print(values.get(i));
                if (i < values.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Save Three-Gram Index to ThreeGIndex.txt
     */
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/results/ThreeGIndex.txt"))) {
            writer.write("3-gram Index:\n");
            writer.write("------------------\n");
            // Iterate through each key-value pair in the index
            for (Map.Entry<String, ArrayList<String>> entry : index.entrySet()) {
                String key = entry.getKey();
                ArrayList<String> values = entry.getValue();

                // Write the key and its values to the file
                writer.write("Word: " + key + "\n");
                writer.write("3-grams: " + String.join(", ", values) + "\n\n");

            }

            System.out.println("Three-Gram Index saved to src/results/ThreeGIndex.txt");
        } catch (IOException e) {
            System.out.println("Error saving ThreeGIndex to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void openThreeGIndexTXT(String filePath) throws IOException {
        File file = new File(filePath);

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            System.out.println("\nOpening the file...");

            if (file.exists()) {
                desktop.open(file);
            } else {
                System.out.println("File not found: " + filePath + "; Please restart the program.");
            }
        } else {
            System.out.println("Desktop is not supported.");
        }
    }
}