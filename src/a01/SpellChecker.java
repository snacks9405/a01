package a01;

import java.util.Arrays;
import java.util.Iterator;

import net.datastructures.ArrayList;
import net.datastructures.LinkedPositionalList;

public class SpellChecker {
    private LinkedPositionalList<String> dictionary; 
    private LinkedPositionalList<String> sample; //initial sample with correct words removed
    private int initialSize; //size of initial sample before words were removed
    private ArrayList<char[]> misspelledWordsList; //char[] to easily manipulate the words
    private ArrayList<LinkedPositionalList<String>> recommendedSubs; //contains all words suitable to replace misspelled words
    private ArrayList<String[]> readyForPrint; //organized and alphabetized set of recommended words split by word being replaced
    private int numSwaps;
    private int numInsertions;
    private int numDeletions;
    private int numReplacements;

    private static final String DEFAULT_SAMPLE_LOCATION = "example.txt"; //my test txt based on lab doc

    /**
     * SpellChecker constructor
     */
    public SpellChecker() {
        this(DEFAULT_SAMPLE_LOCATION);
    }// SpellChecker constructor

    /**
     * SpellChecker constructor overloaded to allow custom sample execution
     * 
     * @param sampleLocation    location of desired txt to spell check
     */
    public SpellChecker(String sampleLocation) {
        this(sampleLocation, null);
    }// SpellChecker constructor

    /**
     * SpellChecker constructor overloaded to allow custom dictionary/sample execution
     * 
     * @param sampleLocation location of desired txt to spell check
     * @param dictionaryLocation location of desired dictionary to reference
     */
    public SpellChecker(String sampleLocation, String dictionaryLocation) {
        if (dictionaryLocation == null) {
            setDictionary();
        } else {
            setDictionary(dictionaryLocation);
        }
        setSample(sampleLocation);
        initialSize = sample.size();
        removeCorrectWords();
    }

    private void setDictionary(){
        dictionary = FileIO.getDictionary();
    }

    /**
     * fills local dictionary with *your choice* of wordy words
     * @param dictionaryLocation
     */
    private void setDictionary(String dictionaryLocation) {
        dictionary = FileIO.getDictionary(dictionaryLocation);
    }// setDictionary location

    /**
     * fills local sample with wordy words
     * @param sampleLocation where we're getting the words
     */
    public void setSample(String sampleLocation) {
        sample = FileIO.getSample(sampleLocation);
    }// setSample method

    /**
     * takes the correctly spelled words out of the sample set. we don't care about those.
     * asks loadWordsToArrayList to put them in a nice arraylist for us.
     * initializes recommendedSubs to correct number of LinkedPositionalLists to avoid null pointers later
     */
    private void removeCorrectWords() {
        recommendedSubs = new ArrayList<>();
        Iterator<String> iterator = sample.iterator();
        while (iterator.hasNext()) {
            if (existsInDictionary(iterator.next())) {
                iterator.remove();
            }
        }
        loadWordsToArrayList(sample);
        for (int i = 0; i < misspelledWordsList.size(); i++) {
            recommendedSubs.add(i, new LinkedPositionalList<String>());
        }
    }// removeCorrectWords method

    /**
     * helper for removeCorrectWords. just checks if the word is spelled right really.
     * @param currentWord 
     * @return true if it finds the word in the dictionary
     */
    private Boolean existsInDictionary(String currentWord) {
        for (String x : dictionary) {
            if (currentWord.equals(x)) {
                return true;
            }
        }
        return false;
    }// existsInDictionary method

    /**
     * helper for removeCorrectWords. turns the wrong ones into char[]'s and 
     * loads them into an ArrayList
     * 
     * @param sample words to process
     */
    private void loadWordsToArrayList(LinkedPositionalList<String> sample) {
        ArrayList<char[]> wordList = new ArrayList<>();

        for (String s : sample) {
            wordList.add(wordList.size(), s.toLowerCase().toCharArray());
        }

        misspelledWordsList = wordList;
    }// loadWordsToArrayList method

    /**
     * runs the wrong words through each of the tests. aggregates recommends.
     * verifies no duplicates and stores in recommendedSubs list.
     */
    public void performAllChecks() {
        int recommendedSubsIndex = 0;

        for (char[] c : misspelledWordsList) {
            LinkedPositionalList<LinkedPositionalList<String>> toLoad = new LinkedPositionalList<>();
            LinkedPositionalList<String> foundReplacements = recommendedSubs.get(recommendedSubsIndex);
            toLoad.addLast(swapFinder(c));
            toLoad.addLast(insertionFinder(c));
            toLoad.addLast(deletionFinder(c));
            toLoad.addLast(replacementFinder(c));
            for (LinkedPositionalList<String> x : toLoad) {
                for (String s : x) {
                    if (!duplicateCheck(s, foundReplacements)) {
                        foundReplacements.addLast(s);
                    }
                }
            }
            recommendedSubs.set(recommendedSubsIndex++, foundReplacements);
        }
    }// performAllChecks method

    /**
     * iterates the dictionary looking for words that meet swap criteria
     * 
     * @param currentWord word being checked for swaps
     * @return list containing all verified swaps. 
     */
    private LinkedPositionalList<String> swapFinder(char[] currentWord) {
        LinkedPositionalList<String> foundSwaps = new LinkedPositionalList<>();
        int first = 0; //index of first letter being swapped.
        int second = 1; //index of adjacent letter to swap with first.
        while (second < currentWord.length) { //swap until you drop (or almost oob)
            String swappedWord = swapHelper(currentWord, first++, second++); //check each possible swap against dictionary
            for (String s : dictionary) {
                if (s.equals(swappedWord) && !s.equals(String.valueOf(currentWord))) {
                    if (!duplicateCheck(s, foundSwaps)) { //check for duplicates. 
                        foundSwaps.addLast(s);
                        numSwaps++; //increment swaps for each non-duplicate find. 
                    }
                }
            }

        }
        return foundSwaps;
    }// swapFinder method

    /**
     * helps out the swapFinder by doing all the letter swapping
     * 
     * @param word word to manipulate
     * @param first index of first letter to be swapped
     * @param second index of letter to swap with first
     * @return manipulated word
     */
    private String swapHelper(char[] word, int first, int second) {
        char[] swapped = new char[word.length];
        for (int i = 0; i < word.length; i++) {
            if (i == first) {
                swapped[i] = word[second];
            } else if (i == second) {
                swapped[i] = word[first];
            } else {
                swapped[i] = word[i];
            }
        }
        return String.valueOf(swapped); //return string to check against dictionary word
    }// swapHelper method

    /**
     * iterates the dictionary looking for words that meet insertion criteria
     * 
     * @param currentWord word being checked for insertions
     * @return list containing all verified insertions. 
     */
    private LinkedPositionalList<String> insertionFinder(char[] currentWord) {
        int removeChar = 0; //index of character to remove from currentWord
        LinkedPositionalList<String> foundInserts = new LinkedPositionalList<>();
        while (removeChar < currentWord.length) { //remove one char at a time until out of chars
            String wordMinusLetter = insertionHelper(currentWord, removeChar++);
            for (String s : dictionary) {
                if (s.equals(wordMinusLetter)) {
                    if (!duplicateCheck(s, foundInserts)) {
                        foundInserts.addLast(s);
                        numInsertions++;
                    }
                }
            }
        }
        return foundInserts;
    }// insertionFinder method

    /**
     * helps out the insertionFinder by removing the letter at index remove
     * 
     * @param word word to manipulate
     * @param remove index of letter to remove
     * @return manipulated word
     */
    private String insertionHelper(char[] word, int remove) {
        char[] wordMinusLetter = new char[word.length - 1]; //initialize a char[] with 1 less spot
        int shortWordIndex = 0;
        for (int i = 0; i < word.length; i++) {
            if (i == remove) {
                continue; //skip that letter!
            } else {
                wordMinusLetter[shortWordIndex++] = word[i];
            }
        }
        return String.valueOf(wordMinusLetter);
    }// insertionHelper method

    /**
     * iterates the dictionary looking for words that meet deletion criteria
     * 
     * @param currentWord word being checked for deletions
     * @return list containing all verified deletions. 
     */
    private LinkedPositionalList<String> deletionFinder(char[] currentWord) {
        LinkedPositionalList<String> foundDeletions = new LinkedPositionalList<>();
        for (String s : dictionary) {
            if (s.length() == currentWord.length + 1) { //dictionary word must be 1 char longer
                if (deletionHelper(currentWord, s.toCharArray())) {
                    if (!duplicateCheck(s, foundDeletions)) {
                        foundDeletions.addLast(s);
                        numDeletions++;
                    }
                }
            }
        }
        return foundDeletions;
    }// deletionFinder method

    /**
     * helps out the deletionFinder by stepping through each word looking for one extra letter
     * 
     * @param misspelledWord 
     * @param dictionaryWord
     * @return true if match. false if no match
     */
    private Boolean deletionHelper(char[] misspelledWord, char[] dictionaryWord) {
        int shortWordIndex = 0; //misspelled word index. short highlights why it gets its own variable.
        int lettersDifferent = 0;
        for (int i = 0; i < dictionaryWord.length; i++) {
            if (lettersDifferent == 2) {
                break; //stop searching if there's more than one letter different.
            } else if (shortWordIndex == misspelledWord.length && (lettersDifferent == 1 || lettersDifferent == 0)) {
                break; //stop searching if we reach the end of the misspelled word within acceptable differences. 
            } else if (misspelledWord[shortWordIndex] == dictionaryWord[i]) {
                shortWordIndex++;
            } else {
                lettersDifferent++;
            }
        }

        return (lettersDifferent < 2);
    }// deletionHelper method

    /**
     * iterates the dictionary looking for words that meet replacement criteria
     * 
     * @param currentWord word being checked for replacements
     * @return list containing all verified replacements. 
     */
    private LinkedPositionalList<String> replacementFinder(char[] currentWord) {
        LinkedPositionalList<String> foundReplacements = new LinkedPositionalList<>();
        for (String s : dictionary) {
            if (s.length() == currentWord.length && !s.equals(String.valueOf(currentWord))) { //length will be the same but word can't be
                if (replacementHelper(currentWord, s.toCharArray())) {
                    if (!duplicateCheck(s, foundReplacements)) {
                        foundReplacements.addLast(s);
                        numReplacements++;
                    }
                }
            }
        }
        return foundReplacements;
    }// replacementFinder method

    /**
     * helps out the replacementFinder by stepping through each word looking for one letter difference
     * @param misspelledWord
     * @param dictionaryWord
     * @return true if match. false if no match
     */
    private Boolean replacementHelper(char[] misspelledWord, char[] dictionaryWord) {
        int lettersDifferent = 0;
        for (int i = 0; i < misspelledWord.length; i++) {
            if (lettersDifferent == 2) {
                break;
            } else if (misspelledWord[i] == dictionaryWord[i]) {
                continue;
            } else {
                lettersDifferent++; //count up each different letter
            }
        }
        return (lettersDifferent == 1); //looking for just one difference
    }// replacementHelper method

    /**
     * duplicate check to make sure we only include one replacement. only really necessary for
     * swap but doesn't hurt to include it everywhere. 
     * @param word word we're confirming is original
     * @param listToCheck list to check against
     * @return true if it's already in the list. false if it's not
     */
    private Boolean duplicateCheck(String word, LinkedPositionalList<String> listToCheck) {
        Boolean duplicateCheck = false;
        for (String s : listToCheck) {
            if (s.equals(word)) {
                duplicateCheck = true;
                break;
            }
        }
        return duplicateCheck;
    }// duplicateCheck method

    /**
     * fills an ArrayList<String[]> of all the recommended substitutions sorted by respective
     * misspelled word then alphabetized. really didn't want to sort each set myself so this 
     * takes advantage of arrays.sort
     */
    private void listAlphebetizer() {
        readyForPrint = new ArrayList<>(); //sorted and clean data ready for the printer
        int bigIndex = 0;
        for (LinkedPositionalList<String> x : recommendedSubs) {
            String[] toLoad = new String[x.size()]; //fills this String[] with all found words...
            int index = 0;
            for (String s : x) {
                toLoad[index++] = s;
            }
            Arrays.sort(toLoad); //then sorts it...
            readyForPrint.add(bigIndex++, toLoad); //then puts it into arraylist
        }
    }// listAlphabetizer method

    /**
     * formats each list of recommended words to include , or no suggestion tag.
     * 
     * @param listToFormat set of words to format
     * @return StringBuilder in proper output format.
     */
    private StringBuilder printFormatter(String[] listToFormat) {
        StringBuilder formattedString = new StringBuilder(); 

        if (listToFormat.length == 0) {
            return formattedString.append("No Suggestions"); //if we didn't find a substitution
        }
        for (int i = 0; i < listToFormat.length; i++) {
            if (i == listToFormat.length - 1) {
                formattedString.append(listToFormat[i]); //last word gets no comma
            } else {
                formattedString.append(listToFormat[i] + ", ");
            }
        }
        return formattedString;
    }// printFormatter method

    /**
     * calculates average suggestions per word
     * 
     * @return average suggestions per word
     */
    private double averageSuggestionsPerWordCalculator() {
        double[] totals = new double[readyForPrint.size()]; //put each words total into an array...
        int index = 0;
        double average = 0.0;
        for (String[] x : readyForPrint) {
            totals[index++] = x.length;
        }
        for (double x : totals) {
            average += x; //add them all up...
        }
        return average / totals.length; //and divide by the total number of misspelled words.
    }// averageSuggestionsPerWordCalculator

    /**
     * launches alphabetizer to generate clean data, then prints in lab doc
     * formatting requirements. 
     */
    public void printOp() {
        listAlphebetizer();
        int index = 0;
        for (String s : sample) {
            System.out.printf("%s - %s\n", s, printFormatter(readyForPrint.get(index++)));

        }
        System.out.printf("# of words spellchecked: %d\n", initialSize);
        System.out.printf("%% of words misspelled: %.1f\n", sample.size() * 100.0 / initialSize);
        System.out.printf("Average # of suggestions / misspelled word: %.1f\n", averageSuggestionsPerWordCalculator()); 
        System.out.printf("Swaps: %d\n", numSwaps);
        System.out.printf("Insertions: %d\n", numInsertions);
        System.out.printf("Deletions: %d\n", numDeletions);
        System.out.printf("Replacements: %d\n", numReplacements);
    }// printOp method
}// SpellChecker class