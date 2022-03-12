/**
 * performs file i/o operations.
 */
package a01;

import net.datastructures.LinkedPositionalList;
import java.io.*;
import java.util.Scanner;

public class FileIO {
    final static String DICTIONARY_FILE_NAME = "jlawler-wordlist.txt";
    final static String DELIMETER = "[^a-zA-Z]";
    /**
     * parse jlawler-wordlist.txt into LinkedPositionalList<String>
     * 
     * @return LinkedPositionalList<String> containing dictionary
     */
    public static LinkedPositionalList<String> getDictionary() {
        LinkedPositionalList<String> dictionary = buildLinkedList(openText(DICTIONARY_FILE_NAME), null);
        return dictionary;
    }// getDictionary method

    /**
     * overloaded to allow dictionary selection
     * 
     * @param dictionaryLocation
     * @return 
     */
    public static LinkedPositionalList<String> getDictionary(String dictionaryLocation) {
        LinkedPositionalList<String> dictionary = buildLinkedList(openText(dictionaryLocation), null);
        return dictionary;
    }// getDictionary method

    /**
     * parse sample txt into LinkedPositionalList<String>
     * 
     * @param fileName location of txt sample
     * @return LinkedPositionalList<String> containing sample
     */
    public static LinkedPositionalList<String> getSample(String fileName) {
        LinkedPositionalList<String> sample = buildLinkedList(openText(fileName), DELIMETER);

        return sample;
    }// getSample method

    /**
     * launches a BufferedReader to parse txt
     * 
     * @param filename location of file
     * @return BufferedReader containing file
     */
    private static BufferedReader openText(String filename) {
        BufferedReader fileText = null;

        try {
            fileText = new BufferedReader(new FileReader(filename));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return fileText;
    }// openText method

    /**
     * parse BufferedReader into LinkedPositionalList<String>
     * 
     * @param input     BufferedReader containing desired txt
     * @param delimeter optional delimeter (legacy used for tests)
     * @return LinkedPositionalList<String> parsed through BufferedReader
     */
    public static LinkedPositionalList<String> buildLinkedList(BufferedReader input, String delimeter) {
        LinkedPositionalList<String> wordList = new LinkedPositionalList<>();
        if (delimeter == null) {
            try {
                while (input.ready()) {
                    wordList.addLast(input.readLine());
                }
                input.close();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            Scanner scan = new Scanner(input).useDelimiter(delimeter);
            while (scan.hasNext()) {
                wordList.addLast(scan.next());
            }
            scan.close();
        }

        return wordList;
    }// buildLinkedList method
}// FileIO class
