/**
 * @author timday
 * created on 29.7.2017
 * this syllable counter is primarily for a flesch readability calculator
 * so err on the side of too many syllables rather than too few
 * ('ed' on end issue - 'manned' counts as 2. ed on end is sometimes syllable, someties not
 */

public class SyllableCount {

    /**
     * -- isVowel --
     * Returns true if a character is a vowel, false if it is anything else
     * @language English
     * @param letter
     * @return The number of syllables (int)
     */
	public static boolean isVowel(char letter) {
		 boolean isAVowel = false;
		   
		   switch (letter) {
			case 'a':
			case 'e':
			case 'i':
			case 'o':
			case 'u':
			case 'A':
			case 'E':
			case 'I':
			case 'O':
			case 'U':
			case 'y':
			case 'Y':
				isAVowel = true;
				break;
			default:
//				isAVowel = false;
				break;
		   }
		   return isAVowel;
	}

	
	/**
	 * -- syllableCount --
	 * Returns the number of syllables in a word (int)
	 * By counting the vowels, not counting vowels next to other vowels
	 * and not counting an ending e (that isn't preceded by a vowel)
	 * 
	 * @language English
	 * @param word
	 * @return The number of syllables (int)
	 */
    public static int syllableCount(String word) {
        word = word.toLowerCase();

        int syllCount = 0;
	    char currentChar;
	    char secondLast;
	   
	    boolean isCurrentCharVowel = false;
	    boolean isPreviousCharVowel = false; //store the vowel-status of previous char so we can check for double vowels.
	    boolean isVowelBeforeLastE = false;
	    
	    for (int i=0; i<word.length(); i++) { //cycle through each letter to look for vowels
               
		    currentChar = word.charAt(i);
		    isCurrentCharVowel = isVowel(currentChar); //returns true/false, using helper method

				
		    if (isCurrentCharVowel && !isPreviousCharVowel) { //if this is a vowel, and the one before it is not
			   syllCount++;
		    }


            isPreviousCharVowel = isCurrentCharVowel; //remember this for the next iteration
            secondLast = word.charAt(word.length()-2);
            isVowelBeforeLastE = isVowel(secondLast);


		    //"io" dipthong check
		    if (i > 1 && word.charAt(i-1)=='i' && word.charAt(i) == 'o') {
                syllCount++;
                isPreviousCharVowel = false; //if the next is a vowel, we just counted it already
            }

            //"ia" dipthong check
            if (i > 1 && word.charAt(i-1)=='i' && word.charAt(i) == 'a') {
                syllCount++;
                isPreviousCharVowel = false; //if the next is a vowel, we just counted it already
            }


            // end 'io' dipthong check

	    } //end for loop

	    //if word ends with e and that e is not preceded by a vowel
	    if (word.endsWith("e") && !isVowelBeforeLastE) {
		    syllCount--; // it is likely a silent e. dont count as syllable
	    } 

	    if (syllCount == 0) {
	        syllCount = 1; //all words have at least 1
        }

        // 2 char diphong check fixes
        if (word.contains("tion")){ //fix for th "io" count, for "tion". just one syll
            syllCount--;
        }
        if (word.contains("tial") ){ //fix for th "io" count, for "tion". just one syll
            syllCount--;
        }
        if (word.contains("cial")){ //fix for th "io" count, for "tion". just one syll
            syllCount--;
        }


        if (word.length() > 3) {
            if (word.contains("oi")){
                syllCount++;
            }
        }

        // edge cases (hard coded)
        if (word.equals("invisible")){
            return 4;
        }


	    return syllCount;
    }


    public static void main(String[] args){

		System.out.println(syllableCount("blx") + " : 1 blx");
        System.out.println(syllableCount("great") + " : 1 great");
        System.out.println(syllableCount("ava") + " : 2 ava");
        System.out.println(syllableCount("eye") + " : 1 eye");
        System.out.println(syllableCount("hyphenation") + " : 4 hyphenation");
		System.out.println(syllableCount("canoe") + " : 2 canoe");
		System.out.println(syllableCount("fairy") + " : 2 fairy");
		System.out.println(syllableCount("zygote") + " : 2 zygote");
        System.out.println(syllableCount("rio") + " : 2 Rio");   //2 char dipthongchecks
        System.out.println(syllableCount("ratio") + " : 3 ratio");
        System.out.println(syllableCount("ration") + " : 2 ration");
        System.out.println(syllableCount("toil") + " : 2 toil"); //2 char dipthongchecks
        System.out.println(syllableCount("koi") + " : 1 koi");   //2 char dipthongchecks
        System.out.println();
		System.out.println(syllableCount("invisible") + " : 4 invisible (hard code)");
        System.out.println(syllableCount("radioelectrocardiograph") + " : 10 radioelectrocardiograph");
        System.out.println();

        System.out.println(syllableCount("interpenetratingly") + " : 7 interpenetratingly");

        System.out.println(syllableCount("unconventionality") + " : 7 unconventionality");
        System.out.println(syllableCount("jurisprudentially") + " : 6/7 jurisprudentially");
        System.out.println(syllableCount("histomorphological") + " : 7 histomorphological");
        System.out.println(syllableCount("hylozoistically") + " : 7 hylozoistically");
        System.out.println(syllableCount("nanocuboctahedron") + " : 7 nanocuboctahedron");
        System.out.println(syllableCount("unsatisfactorily") + " : 7 unsatisfactorily");

        System.out.println(syllableCount("abbreviator") + " : 5 abbreviator");
        System.out.println(syllableCount("bacterial") + " : 4 bacterial");
        System.out.println(syllableCount("glacial") + " : 2 glacial");
        System.out.println(syllableCount("spacial") + " : 2 spacial");
        System.out.println(syllableCount("spatially") + " : 3/4 spatially");
        System.out.println(syllableCount("accessorial") + " : 5 accessorial");

        // ia is 2 unless cial or tial
        System.out.println();
        System.out.println(syllableCount("straights") + " : 1 straights");
        System.out.println(syllableCount("screeched") + " : 1 screeched");
        System.out.println(syllableCount("schlepped") + " : 1 schlepped");
        System.out.println(syllableCount("manned") + " : 1 manned");
        System.out.println(syllableCount("meyer") + " : 2 meyer");

        System.out.println(syllableCount("abandoned") + " : 3 abandoned");
        System.out.println(syllableCount("abducted") + " : 3 abducted");
        System.out.println(syllableCount("abolished") + " : 3 abolished");
        System.out.println(syllableCount("absorbed") + " : 2 absorbed");
	}
}

