import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;




/**TODO: write better comments for methods*/

/**Practice 4 Inverted Tree*/
public class InvertedTree {
    TrieNode root;

    public InvertedTree(String sourceFilePath) {
        root = new TrieNode();
        String resultFilePath = "src/results/InvertedTree.txt";
        insertFromFiles(sourceFilePath);
        saveToFile(resultFilePath);
    }

    public ArrayList<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>(); //Prefix not found
            }
            node = node.children.get(c);
        }
        return node.terms;
    }

    // Helper method for printing the tree
    public void printInvTree() {
        printInvTree(root, "");
    }

    // Recursive method to traverse and print the tree
    private void printInvTree(TrieNode node, String prefix) {
        for (char c : node.children.keySet()) {
            TrieNode childNode = node.children.get(c);
            String currentTerm = prefix + c;

            // If the node has terms, print them
            if (!childNode.terms.isEmpty()) {
                System.out.println(currentTerm + ": " + childNode.terms);
            }
            // Recursively traverse child nodes
            printInvTree(childNode, currentTerm);
        }
    }


    /** Method to insert terms from .txt files in the /res folder*/
    public void insertFromFiles(String folderPath) {

        // Iterate through files in the folder and insert each term into the tree
        try {
            // Assuming all files have .txt extension in the folder
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".txt"))
                    .forEach(file -> {
                        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                // Assuming each line in the file is a term
                                insert(line.trim());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void insert(String term) {
        TrieNode node = root;
        for (char c : term.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.terms.add(term);
    }

    // Save InvertedTree to a text file
    public void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("Inverted Tree:\n");
            writer.write("------------------\n");

            // Use a StringBuilder to build the content
            StringBuilder content = new StringBuilder();

            // Call a helper method to recursively traverse and build the content
            buildFileContent(root, "", content);

            // Write the content to the file
            writer.write(content.toString());

            System.out.println("InvertedTree saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving InvertedTree to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to recursively traverse and build the content for the file
    private void buildFileContent(TrieNode node, String prefix, StringBuilder content) {
        for (char c : node.children.keySet()) {
            TrieNode childNode = node.children.get(c);
            String currentTerm = prefix + c;

            // If the node has terms, append them to the content
            if (!childNode.terms.isEmpty()) {
                content.append(currentTerm).append(": ").append(childNode.terms).append("\n");
            }

            // Recursively traverse child nodes
            buildFileContent(childNode, currentTerm, content);
        }
    }

    public void openInvertedTreeTXT(String filePath) throws IOException {
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

