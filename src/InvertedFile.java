import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**TODO: write better comments for methods*/

/**Practice 6 Inverted File*/
public class InvertedFile {

    private final String dictionaryFilePath;
    public final List<String> documents;
    private final Map<String, List<Integer>> invertedFile;

    // Constructor
    public InvertedFile(String dictionaryFilePath, String documentsFolderPath) {
        this.dictionaryFilePath = dictionaryFilePath;
        this.documents = loadDocuments(documentsFolderPath);
        this.invertedFile = buildInvertedFile();

        saveInvertedFileToFile();
    }

    // Load documents from a folder
    private List<String> loadDocuments(String folderPath) {
        List<String> documentContents = new ArrayList<>();

        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            documentContents.add(Files.readString(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return documentContents;
    }

    // Build inverted file from the provided dictionary and documents
    private Map<String, List<Integer>> buildInvertedFile() {
        Map<String, List<Integer>> invertedFileMap = new HashMap<>();

        // Load the dictionary from file
        String[] dictionary = readDictionaryFromFile(dictionaryFilePath);

        for (int docId = 0; docId < documents.size(); docId++) {
            String document = documents.get(docId);
            List<String> terms = tokenize(document);

            for (String term : terms) {
                invertedFileMap.computeIfAbsent(term, k -> new ArrayList<>()).add(docId);
            }
        }

        return invertedFileMap;
    }

    // Tokenize a document into terms
    private List<String> tokenize(String document) {
        List<String> terms = new ArrayList<>();

        // Custom tokenization logic: split on non-alphanumeric characters and convert to lowercase
        String[] words = document.split("[^a-zA-Z0-9]+");

        for (String word : words) {
            if (!word.isEmpty()) {
                terms.add(word.toLowerCase());
            }
        }

        return terms;
    }

    // Save inverted file to a file
    public void saveInvertedFileToFile(String filePath) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(invertedFile);
            System.out.println("Inverted file saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read inverted file from a file
    public Map<String, List<Integer>> readInvertedFileFromFile(String filePath) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, List<Integer>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Read dictionary from file
    private String[] readDictionaryFromFile(String filePath) {
        List<String> dictionaryList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionaryList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dictionaryList.toArray(new String[0]);
    }

    // Save inverted file to a file
    public void saveInvertedFileToFile() {
        String filePath = "src/results/InvertedFile.txt";  // Update the path
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(invertedFile);
            System.out.println("Inverted file saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

