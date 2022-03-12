/* 
Description: Assignment 1
Author: alex rodriguez
Date: 3.12.22
Bugs: None that I know of
Reflection: you're setting the bar pretty high for assignments! 
this one was a blast.
really put this one off because it seemed really tough but had a great 
time actually putting the code down. 
difficulty : thinking about it for a week : 8/10
             sitting down and pounding it out : 5/10
*/
package a01;

public class A01 {
    public static void main(String[] args) throws Exception {
        String sampleLocation = null; // change this to your txt test file :)
        String dictionaryLocation = null;
        runSpellChecker(initializeSpellChecker(sampleLocation, dictionaryLocation));
    }

    /**
     * winds up the spell checker (with option to use custom txt/dictionary)
     * 
     * @param sampleLocation txt you're trying to spell check
     * @return an instance of SpellChecker
     */
    public static SpellChecker initializeSpellChecker(String sampleLocation, String dictionaryLocation) {
        if (sampleLocation == null) {
<<<<<<< HEAD
            if (dictionaryLocation == null) {
                return new SpellChecker();
            } else {
                return new SpellChecker(null, dictionaryLocation);
            }
=======
            return new SpellChecker();
>>>>>>> eb02034f26e9194616a9724095ae8d12bfe3ae13
        } else {
            if (dictionaryLocation == null) {
                return new SpellChecker(sampleLocation);
            } else {
                return new SpellChecker(sampleLocation, dictionaryLocation);
            }
        }
    }

<<<<<<< HEAD
    // initializeSpellChecker method

=======
>>>>>>> eb02034f26e9194616a9724095ae8d12bfe3ae13
    /**
     * calls spellchecker to do tests and print results to terminal.
     * 
     * @param myChecker instance of spellchecker
     */
    public static void runSpellChecker(SpellChecker myChecker) {
        myChecker.performAllChecks();
        myChecker.printOp(); // could have made this one call, but I like output separate if I can.
    }// runSpellChecker method
<<<<<<< HEAD
}// A01 class
=======
}//A01 class
>>>>>>> eb02034f26e9194616a9724095ae8d12bfe3ae13
