import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static utils.CompressionUtils.compressDictionary;



/**TODO: write better comments for methods*/

/**TODO: COMPARE TO DICTIONARY 1 AND MAY THE BEST SURVIVE*/

/**Practice 6 Dictionary*/
public class Dictionary_Pract6_Compression {

    public String[] vocab;
    private int num;
    private int numTotal;
    final double vocabSize;
    private final ArrayList<Double> fileSizes;
    private final ArrayList<String> fileNames;

    private final String vocabPathTXT;

    /**Constructor
     * @param folder path to folder with documents
     * */
    public Dictionary_Pract6_Compression(String folder) {
        File dir = new File(folder);
        File[] files = dir.listFiles();

        assert files != null;
        int init_capacity = files.length;
        fileSizes = new ArrayList<>();
        fileNames = new ArrayList<>();
        vocab = new String[init_capacity];

        for (File file : files) {

            if (file.isFile()) {
                fileSizes.add(file.length() / 1024.0);
                fileNames.add(file.getName()); // Store the file name
                BufferedReader br = null;
                String line;
                try {

                    br = new BufferedReader(new FileReader(file));
                    while ((line = br.readLine()) != null) {
                        addWords(line);
                    }

                } catch (IOException e) {
                    System.out.println(e);
                } finally {
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

        // Save the dictionary to a file
        saveDictionaryToFile();
        vocabSize = new File("src/results/dictionary.txt").length() / 1024.0;
        vocabPathTXT = new File("dictionary.txt").getAbsolutePath();

        // Compress the dictionary using front-end packing
        String[] compressedDictionary = compressDictionary(this.vocab);
    }


    public int length() {
        return vocab.length;
    }

    public int number() {
        return num;
    }

    public String[] getVocab(){
        return vocab;
    }

    /**Gets words from a line and adds them to an array
     * @param line line of text file*/
    private void addWords(String line) {
        if (line.equals("")) return;
        String[] temp = line.split("[\\W]+");
        for (String s : temp) {
            if (s.matches("[a-zA-Z0-9_]+")) {
                numTotal++;
                addWord(s.toLowerCase());
            }
        }
    }

    /**Make room for a word in an array
     * @param idx index of a word in the dictionary*/
    private void shift(int idx) {
        if (num >= length() - 1) {
            String[] res = new String[length() * 2];
            if (idx >= 0) {
                System.arraycopy(vocab, 0, res, 0, idx);
            }
            if (num + 1 - idx >= 0) {
                System.arraycopy(vocab, idx, res, idx + 1, num + 1 - idx);
            }
            vocab = res;
        } else {
            if (num + 1 - idx >= 0) {
                System.arraycopy(vocab, idx, vocab, idx + 1, num + 1 - idx);
            }
        }

    }

    /**Word index getter*/
    private int getIndex(String word) {
        return binarySearch(0, num - 1, word);
    }


    /**Helper method to add words to the dictionary
     * @param word a given word (from file line)*/
    private void addWord(String word) {
        int idx = getIndex(word);
        if (idx < length() && word.equals(vocab[idx])) return;
        shift(idx);
        vocab[idx] = word;
        num++;
    }


    /**Search for a place to put a word in
     * @param first index of first element in arr segment
     * @param last index of last element in arr segment
     * @param word index of current element if arr segment*/
    private int binarySearch(int first, int last, String word) {
        int res;
        if (first > last) res = first;
        else {
            int mid = first + (last - first) / 2;
            String midWord = vocab[mid];
            if (word.equals(midWord)) res = mid;
            else if (word.compareTo(midWord) < 0) {
                res = binarySearch(first, mid - 1, word);
            } else res = binarySearch(mid + 1, last, word);
        }
        return res;
    }

    //SERIALIZATION METHODS

    /**Serialize the dictionary object to a file*/
    public void serialize(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            // System.out.println("Dictionary serialized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Deserialize the dictionary object from a file
     * @param  filename name of file to serialize*/
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



    //ALL THE TECHNICAL METHODS
    /**Return statistics*/
    public String statsTXT() {

        return "Stats for dictionary.txt: " + "\nNumber of words in dictionary: " + num + "\ndictionary size: " + vocabSize + " kb \n" + listOfFiles();
    }

    public String statsSer() {

        File serFile = new File("dictionary.ser");

        if (!serFile.exists()) {
            return "\nThe file dictionary.ser does not exist.";
        }

        StringBuilder s = new StringBuilder("\nStats for vocab.ser: ");
        double fileSize = serFile.length() / 1024.0; // Size in kilobytes
        s.append("\nSize of serialized dictionary.ser file: ").append(fileSize).append(" kb");
        Dictionary deserializedDictionary = Dictionary.deserialize("dictionary.ser");
        assert deserializedDictionary != null;
        s.append("\nNumber of words in the deserialized dictionary: ").append(deserializedDictionary.number());
        s.append("\nDictionary size of the deserialized dictionary: ").append(deserializedDictionary.vocabSize).append(" kb\n");

        return s.toString();
    }

    /**List files in collection*/
    public String listOfFiles() {
        StringBuilder s = new StringBuilder("\nList of files: ");
        double size = 0;
        s.append("\nNumber of files: ").append(fileNames.size());

        for (int i = 0; i < fileSizes.size(); i++) {
            size += fileSizes.get(i);
            s.append("\nFile ").append(i + 1).append(": ").append(fileNames.get(i)).append(" - ").append(fileSizes.get(i)).append(" kb");
        }
        s.append("\n\nTotal number of words in all files: ").append(numTotal);
        s.append("\nTotal : ").append(size).append(" kb");
        return s.toString();
    }

    /**Return the path to the dictionary.txt file on disc*/
    public String getDictionaryFilePath() {
        return vocabPathTXT;
    }

    /**Print dictionary in readable format*/
    public void print() {
        OutputStream out = new BufferedOutputStream(System.out);
        for (int i = 0; i < num; i++) {
            try {
                out.write((vocab[i] + "\n").getBytes());
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

    /**Save the compressed dictionaru to a file*/
    private void saveDictionaryToFile() {
        String filePath = "src/results/dictionary.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String word : vocab) {
                writer.println(word);
            }
            System.out.println("Dictionary saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Open a file specified by its path
     * @param filePath path to file to be opened*/
    public static void openFile(String filePath) {
        try {
            System.out.println("\nPulling up the file...");
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
