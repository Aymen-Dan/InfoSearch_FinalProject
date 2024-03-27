package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**Practice 6 Compression Utils*/
public class CompressionUtils {

    /**Front-end packing for dictionary compression*/
    public static String[] compressDictionary(String[] dictionary) {
        String[] compressedDictionary = new String[dictionary.length];
        int currentCode = 0;

        for (int i = 0; i < dictionary.length; i++) {
            compressedDictionary[i] = currentCode + ":" + dictionary[i];
            currentCode++;
        }

        return compressedDictionary;
    }

    /**Save the compressed vocabulary to a file*/
    public static void saveCompressedVocabularyToFile(String[] compressedVocab) {
        String filePath = "src/results/Compressed_vocabulary.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String entry : compressedVocab) {
                writer.println(entry);
            }
            System.out.println("Compressed vocabulary saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**Read the compressed vocabulary from the file in the src/results folder*/
    public static String[] readCompressedVocabularyFromFile() {
        List<String> compressedVocabList = new ArrayList<>();

        String filePath = "src/results/Compressed_vocabulary.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                compressedVocabList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressedVocabList.toArray(new String[0]);
    }

    /**Decompression method for front-end packing of an array of strings*/
    public static String[] decompressDictionaryArray(String[] compressedDictionary) {
        String[] decompressedDictionary = new String[compressedDictionary.length];

        for (int i = 0; i < compressedDictionary.length; i++) {
            String entry = compressedDictionary[i];
            String[] parts = entry.split(":");
            decompressedDictionary[i] = parts[1];
        }

        return decompressedDictionary;
    }


    /**Compression of the inverted file using Variable Byte Coding (VBC)*/
    public static byte[] compressInvertedFile(List<Integer> documentIds) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        for (int docId : documentIds) {
            while (docId >= 128) {
                byteArrayOutputStream.write((byte) (docId % 128 + 128));
                docId /= 128;
            }
            byteArrayOutputStream.write((byte) docId);
        }

        return byteArrayOutputStream.toByteArray();
    }

    /**Decompression of the inverted file using Variable Byte Coding (VBC)*/
    public static List<Integer> decompressInvertedFile(byte[] compressedInvertedFile) {
        List<Integer> decompressedInvertedFile = new ArrayList<>();
        int docId = 0;

        for (byte b : compressedInvertedFile) {
            docId = docId * 128 + (b % 128);
            if (b < 0) {
                decompressedInvertedFile.add(docId);
                docId = 0;
            }
        }

        return decompressedInvertedFile;
    }

    /**Save the compressed inverted file to a file*/
    public static void saveCompressedInvertedFileToFile(byte[] compressedInvertedFile, String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(compressedInvertedFile);
            System.out.println("Compressed inverted file saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Read the compressed inverted file from a file*/
    public static byte[] readCompressedInvertedFileFromFile(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int bytesRead;
            byte[] buffer = new byte[1024];

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
