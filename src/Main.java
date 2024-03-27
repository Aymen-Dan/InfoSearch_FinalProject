import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args){



        Scanner in = new Scanner(System.in);

        File textsFolder = new File("src/resources");

        if (textsFolder.isDirectory()) {
            File[] files = textsFolder.listFiles();

            if (files != null && files.length > 0) {
                System.out.println("\nGreat! Files present. Let's begin.\n\n");
            } else {
                System.out.println("\nThere are no files in the folder 'src/resources'. Please add some files.\n\n");
            }
        } else {
            System.out.println("\nThe folder 'src/resources' is not the directory. Please create the folder and add some files to it.\n\n");
        }



        //TXT SAVE
        long startTimeTXT = System.nanoTime();//start time
        Dictionary_Pract6_Compression vTXT = new Dictionary_Pract6_Compression("src/resources"); // CREATE VOCAB
        long endTimeTXT = System.nanoTime();
        long elapsedTimeTXT = endTimeTXT - startTimeTXT; //final time it took

        //SER SAVE
       /* long startTimeSER = System.nanoTime();//start time
        Dictionary_Pract6_Compression vSER = new Dictionary_Pract6_Compression("src/resources"); // CREATE VOCAB
        vSER.serialize("src/results/dictionary.ser");
        long endTimeSER = System.nanoTime();
        long elapsedTimeSER = endTimeSER - startTimeSER; //final time it took*/


        //TODO: CREATE BETTER OPTION MENU
        System.out.println("""

                0 - Print dictionary.txt;
                1 - Print list of files;
                2 - Show stats;
                3 - Show time needed to form dictionary
                4 - Open dictionary.txt file;
                -1 - Exit
                """);
        System.out.println("Your input here: ");
        int i = in.nextInt();



        while(i != -1) {
            switch (i) {
                case 0:
                    vTXT.print();
                    break;
                case 1:
                    System.out.println(vTXT.listOfFiles());
                    break;
                case 2:
                    System.out.println(vTXT.statsTXT());
                   // System.out.println(vSER.statsSer());
                    break;
                case 3:
                    System.out.println("Time for dictionary.txt: " + elapsedTimeTXT + " ns, or " + elapsedTimeTXT / 1_000_000.0 + " ms, or " + elapsedTimeTXT / 1_000_000_000.0 + " s");
                   // System.out.println("Time for dictionary.ser: " + elapsedTimeSER + " ns, or " + elapsedTimeSER / 1_000_000.0 + " ms, or " + elapsedTimeSER / 1_000_000_000.0 + " s");
                    break;
                case 4:
                    try {
                        File file = new File(vTXT.getDictionaryFilePath());
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Wrong format");
            }
            System.out.println("""

                    0 - Print dictionary.txt;
                    1 - Print list of files;
                    2 - Show stats;
                    3 - Show time needed to form dictionary;
                    4 - Open dictionary.txt file;
                    -1 - Exit
                    """);
            System.out.println("Your input here: ");
            i = in.nextInt();
        }
    }




}