import java.io.*;
import java.util.ArrayList;



/**TODO: write better comments for methods*/

/**Practice 1 Dictionary*/
public class Dictionary implements Serializable {

    public String[] vocab;
    private int num;
    private int numTotal;
    final double vocabSize;
    private final ArrayList<Double> fileSizes;
    private final ArrayList<String> fileNames;
    private final String vocabPathTXT;


    //constructor
    public Dictionary(String folder){
        File dir = new File(folder);
        File[] files = dir.listFiles();

        assert files != null;
        int init_capacity = files.length;
        fileSizes = new ArrayList<>();
        fileNames = new ArrayList<>();
        vocab = new String[init_capacity];

        for (File file : files) {

            if(file.isFile()) {
                fileSizes.add(file.length()/1024.0);
                fileNames.add(file.getName()); // Store the file name
                BufferedReader br = null;
                String line;
                try {

                    br = new BufferedReader(new FileReader(file));
                    while ((line = br.readLine()) != null) {
                        addWords(line);
                    }

                } catch(IOException e) {
                    System.out.println(e);
                }
                finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("vocabulary.txt"));
            bw.write(toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        vocabSize = new File("vocabulary.txt").length()/1024.0;
        vocabPathTXT = new File("vocabulary.txt").getAbsolutePath();
    }

    //checks words in a line and adds them to an array
    private void addWords(String line){
        if(line.equals("")) return;
        String[] temp = line.split("[\\W]+");
        for (String s : temp) {
            if (s.matches("[a-zA-Z0-9_]+")) {
                numTotal++;
                addWord(s.toLowerCase());
            }
        }
    }

    public int length(){
        return vocab.length;
    }

    public int number(){
        return num;
    }



    //make room for a word in an array
    private void shift(int idx){
        if(num >= length() - 1) {
            String[] res = new String[length() * 2];
            if (idx >= 0) {
                System.arraycopy(vocab, 0, res, 0, idx);
            }
            if (num + 1 - idx >= 0){
                System.arraycopy(vocab, idx, res, idx + 1, num + 1 - idx);
            }
            vocab = res;
        }
        else{
            if (num + 1 - idx >= 0){
                System.arraycopy(vocab, idx, vocab, idx + 1, num + 1 - idx);
            }
        }

    }

    private int index(String word) {
        return binarySearch(0, num -1, word);
    }

    //adds words to the vocab
    private void addWord(String word){
        int idx = index(word);
        if(idx < length() && word.equals(vocab[idx])) return;
        shift(idx);
        vocab[idx]=word;
        num++;
    }


    //search for a place to put a word in
    private int binarySearch(int first, int last, String word) {
        int res;
        if (first > last) res = first;
        else {
            int mid = first + (last - first) / 2;
            String midWord = vocab[mid];
            if (word.equals(midWord)) res = mid;
            else if (word.compareTo(midWord) < 0){
                res = binarySearch(first, mid - 1, word);}
            else res = binarySearch(mid + 1, last, word);
        }
        return res;
    }

    //returns statistics
    public String statsTXT(){

        return "Stats for vocabulary.txt: " + "\nNumber of words in vocabulary: " + num +
                "\nDictionary size: " + vocabSize + " kb";
    }

    public String listOfFiles() {
        StringBuilder s = new StringBuilder("\nList of files: ");
        double size = 0;
        s.append("\nNumber of files: ").append(fileNames.size());

        for(int i=0; i<fileSizes.size(); i++) {
            size += fileSizes.get(i);
            s.append("\nFile ").append(i + 1).append(": ").append(fileNames.get(i)).append(" - ").append(fileSizes.get(i)).append(" kb");
        }
        s.append("\n\nTotal number of words in all files: ").append(numTotal);
        s.append("\nTotal : ").append(size).append(" kb");
        return s.toString();
    }

    public String statsSer() {

        File serFile = new File("vocabulary.ser");

        if (!serFile.exists()) {
            return "\nThe file vocabulary.ser does not exist.";
        }

        StringBuilder s = new StringBuilder("\nStats for vocab.ser: ");
        double fileSize = serFile.length() / 1024.0; // Size in kilobytes
        s.append("\nSize of serialized vocabulary.ser file: ").append(fileSize).append(" kb");
        Dictionary deserializedDictionary = Dictionary.deserialize("vocabulary.ser");
        assert deserializedDictionary != null;
        s.append("\nNumber of words in the deserialized vocabulary: ").append(deserializedDictionary.number());
        s.append("\nDictionary size of the deserialized vocabulary: ").append(deserializedDictionary.vocabSize).append(" kb\n");

        return s.toString();
    }


    //returns elements of a created vocab
    public void print(){
        OutputStream out = new BufferedOutputStream( System.out );
        for(int i=0;i<num;i++){
            try {
                out.write((vocab[i]+"\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //returns the path to the vocabulary.txt file on disc
    public String getVocabularyFilePath() {
        return vocabPathTXT;
    }

    //returns path to .ser TODO: MAKE A METHOD THAT DOES EVERY TYPE OF FILE W/O NAME
    public String getFilePath() {

        return new File ("vocabulary.ser").getAbsolutePath();
    }


    public String toString(){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < num; i++){
            res.append(vocab[i]).append("\n");
        }
        return res.toString();
    }



    //SERIALIZATION METHODS

    // Serialize the vocabulary object to a file
    public void serialize(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            // System.out.println("Dictionary serialized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialize the vocabulary object from a file
    public static Dictionary deserialize(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof Dictionary) {
                // System.out.println("Dictionary deserialized successfully!");
                return (Dictionary) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
