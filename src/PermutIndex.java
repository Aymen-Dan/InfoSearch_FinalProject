import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**TODO: write better comments for methods*/

/**Practice 4 Permutation Index*/
public class PermutIndex {
    HashMap<String, ArrayList<String>> index;
    Index ind;

    public PermutIndex(Index ind){
        this.ind=ind;
        index = new HashMap<>();
        for(String s: ind.matrix.keySet()){
            index.put(s,permute(s));
        }

        this.saveToFile();
    }

    private ArrayList<String> permute(String input) {
        input=input.toLowerCase();
        ArrayList<String> res = new ArrayList<>();
        String word = input+"$";
        res.add(word);
        while(!word.startsWith("$")){
            char ch = word.charAt(0);
            word = word.replaceFirst(String.valueOf(ch),"");
            word = word+ch;
            res.add(word);
        }
        return res;
    }

    public HashMap<String,ArrayList<Integer>> search(String input) throws Exception {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", "");

        if(!input.matches("\\*?[\\w]+\\*?[\\w]+\\*?"))
            throw new Exception("Incorrect format.");

        HashMap<String,ArrayList<Integer>> res=new HashMap<>();

        if(input.contains("*")) {

            input = permuteSearch(input);
            input = input.replaceAll("\\*","");

            for (String str : index.keySet()) {
                for(String str1 : index.get(str)) {
                    if (str1.startsWith(input)) {
                        res.put(str, ind.matrix.get(str));
                        break;
                    }
                }
            }
        }else
        {
            res.put(input,ind.matrix.get(input));
        }

        return res;
    }

    private String permuteSearch(String input){
        input=input.toLowerCase();
        String res = "";
        String word = input+"$";

        while(!word.endsWith("*")){
            char ch = word.charAt(0);
            String s = String.valueOf(ch);
            if(ch=='*') s ="\\*";
            word = word.replaceFirst(s,"");
            word = word+ch;
        }
        res=word;
        return res;
    }


    public void printPermutIndex() {
        System.out.println("Permutation Index:");
        System.out.println("------------------");

        for (String key : index.keySet()) {
            System.out.println("Word: " + key);
            System.out.print("Permutations: ");

            ArrayList<String> values = index.get(key);

            if (values.isEmpty()) {
                System.out.println("No permutations");
            } else {
                System.out.println(values.toString());
            }

            System.out.println();
        }
    }

    /**Save to */
    public void saveToFile() {
        String filePath = "src/results/PermutIndex.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Permutation Index:\n");
            writer.write("------------------\n");

            // Iterate through each key-value pair in the index
            for (Map.Entry<String, ArrayList<String>> entry : index.entrySet()) {
                String key = entry.getKey();
                ArrayList<String> values = entry.getValue();

                // Write the key and its values to the file
                writer.write("Word: " + key + "\n");
                writer.write("Permutations: ");

                if (values.isEmpty()) {
                    writer.write("No permutations");
                } else {
                    writer.write(values.toString());
                }

                writer.write("\n\n");
            }

            System.out.println("Permutation Index saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving PermutIndex to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void openPermutIndexTXT(String filePath) throws IOException {
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

