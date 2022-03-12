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
        String sampleLocation = null; //change this to your txt test file :)

        runSpellChecker(initializeSpellChecker(sampleLocation));
    }

    /**
     * winds up the spell checker (with option to use custom txt)
     * 
     * @param sampleLocation txt you're trying to spell check
     * @return an instance of SpellChecker
     */
    public static SpellChecker initializeSpellChecker(String sampleLocation) {
        if (sampleLocation == null) {
            return initializeSpellChecker();
        } else {
            return new SpellChecker(sampleLocation);
        }
    }// initializeSpellChecker method

    /**
     * winds up the spell checker 
     * @return an instance of SpellChecker
     */
    public static SpellChecker initializeSpellChecker() {
        return new SpellChecker();
    }// initializeSpellChecker method

    /**
     * calls spellchecker to do tests and print results to terminal.
     * 
     * @param myChecker instance of spellchecker
     */
    public static void runSpellChecker(SpellChecker myChecker) {
        myChecker.performAllChecks();
        myChecker.printOp(); //could have made this one call, but I like output separate if I can.
    }// runSpellChecker method
}//A01 class