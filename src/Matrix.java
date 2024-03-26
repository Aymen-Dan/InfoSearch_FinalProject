import java.awt.*;
import java.io.*;
import java.util.*;



/**TODO: write better comments for methods*/

/**Practice 2 Matrix*/
public class Matrix {

    int init_capacity;
    String[] doc_names;
    HashMap<String, byte[]> matrix;

    //constructor
    public Matrix(String folder) {
        matrix = new HashMap<>();

        File dir = new File(folder);

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Invalid folder path: " + folder);
            return;
        }

        File[] files = dir.listFiles();
        assert files != null;
        init_capacity = files.length;

        int doc = -1;
        for (File file : files) {
            doc++;

            if (file.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        addWords(doc, line);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }

        doc_names = new String[doc + 1];
        for (int i = 0; i < doc + 1; i++) {
            doc_names[i] = files[i].getName();
        }

        // Save to file
        saveToFile();
    }
    //checking and adding words
    public void addWords(int doc, String line){
        if(line.equals("")) return;
        String[] temp = line.split("[^a-zA-Z0-9_]+");
        for (String s : temp) {
            if (s.matches("[a-zA-Z0-9_]+")) {
                addWord(s.toLowerCase(), doc);
            }
        }
    }

    //adding words to hashmap
    public void addWord(String word,int doc){
        if(!matrix.containsKey(word)){
            byte [] arr = new byte [init_capacity];
            arr[doc] = 1;
            matrix.put(word,arr);
        }
        else{
            if(matrix.get(word).length<=doc){
                resize(matrix.get(word));
            }
            else{
                matrix.get(word)[doc] = 1;
            }

        }
    }

    //to string
    public String matrixStats() {
        StringBuilder result = new StringBuilder("\nMatrix:\n");

        ArrayList<String> res_list = new ArrayList(matrix.keySet());
        Collections.sort(res_list);
        for(int i = 0; i < doc_names.length; i++){
            result.append(i).append(" ");
        }
        result.append("\n");
        for(String s: res_list){
            for(int i = 0; i < doc_names.length; i++){
                result.append(String.valueOf(matrix.get(s)[i])).append(" ");
            }
            result.append(s).append("\n");
        }

        return result.toString();
    }

    //make size x2 bigger
    private void resize(byte[] arr) {
        byte[] temp = new byte[arr.length*2];

        System.arraycopy(arr, 0, temp, 0, arr.length);
        arr = temp;
    }

    //boolean search
    public byte[] search(String input) throws Exception {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+","");

        char and = '&', or ='∨', not ='!';
        if(!input.matches("(("+not+")?[\\w]+((("+and+")|("+or+"))("+not+")?[\\w]+)*)"))
            throw new Exception("Incorrect format. ");

        String [] words = input.split("(("+and+")|("+or+"))");
        String[] operators = input.split("[^&∨]");
        operators = check(operators);

        byte [] nots = new byte [words.length];
        for(int i=0;i<nots.length;i++){
            if(words[i].charAt(0)==not){
                words[i] = words[i].replaceAll("!","");
                nots[i]=1;
            }
        }

        byte [] res = copy(matrix.get(words[0]));
        if(res==null)res=new byte[doc_names.length];
        if(nots[0]==1)res = swap(res);


        for(int i =1;i<words.length;i++){
            if(res==null)res=new byte[doc_names.length];

            byte[] temp = copy(matrix.get(words[i]));
            if(temp==null)temp=new byte[doc_names.length];

            if(nots[i]==1) temp = swap(temp);

            for(int j =0;j< doc_names.length;j++) {
                if((operators[i-1].equals("&"))){
                    if(res[j]==1 && temp[j]==1){
                        res[j]=1;
                    }
                    else res[j]=0;
                } else if(operators[i-1].equals("∨")){
                    if(res[j]==1 || temp[j]==1){
                        res[j]=1;
                    }
                    else res[j]=0;

                }else res[j]=0;

            }
        }
        return res;
    }

    //checking if operators are ok
    private String [] check(String [] arr){
        ArrayList<String> temp = new ArrayList();
        int j = 0;
        for (String s : arr) {
            if (s.equals("&") || s.equals("∨")) {
                temp.add(j, s);
                j++;
            }
        }
        String [] res = new String [j];
        res = temp.toArray(res);
        return res;
    }

    //not array
    private byte[] swap(byte[] arr){
        byte [] res = new byte[arr.length];
        for(int i=0;i<arr.length;i++){
            res[i] = (byte) (1-arr[i]);
        }
        return res;
    }

    private byte[] copy(byte[] arr){
        byte [] res = new byte[arr.length];
        System.arraycopy(arr, 0, res, 0, arr.length);
        return res;
    }


    /**SAVE INTO MATRIX.TXT FILE*/
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/results/matrix.txt"))) {
            ArrayList<String> res_list = new ArrayList<>(matrix.keySet());
            Collections.sort(res_list);

            for (int i = 1; i < doc_names.length + 1; i++) {
                writer.write("Doc" + i + " | ");//header of the file
            }
            writer.write("word");
            writer.newLine();
            writer.write("-------------------------------");
            writer.newLine();

            for (String s : res_list) {
                for (int i = 0; i < doc_names.length; i++) {
                    writer.write(matrix.get(s)[i] + "    | ");//y or no
                }
                writer.write(s + " ");//word
                writer.newLine();
            }


            // Close the writer so the file appears immediately
            writer.close();

            System.out.println("Matrix saved to src/results/matrix.txt");
        } catch (IOException e) {
            System.out.println("Error saving matrix to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**OPEN A MATRIX.TXT FILE*/
    public void openMatrixTXT(String filePath) throws IOException {
        File file = new File(filePath);

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            System.out.println("\nPulling up the file...");

            if (file.exists()) {
                desktop.open(file);
            } else {
                System.out.println("File not found: " + filePath + "; Please restart the program.");
            }
        } else {
            System.out.println("Desktop is not supported.");
        }
    }


    public static void print(byte[] res) {
        for (byte re : res) System.out.print(re + " ");
        System.out.println();
    }
}

