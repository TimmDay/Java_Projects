/**
 * Created by timday on 7/28/17.
 *
 * libraries:
 * - opennlp-tools-1.7.2
 * - opennlp-uima-1.7.2
 *
 * models
 * the opennlp require several machine learning model files to be stored in the working directory. These include:
 * - en-sent.bin
 */

/** TODO LIST
 * import file from working directory. will make testing easier
 * Q: sendect and tokenizer ME. what is ME. better/worse than alternative?
 * - language detection. because I should learn it. can we detect if foreign languages are present in part?
 *
 *
 * SENTIMENT ANAL - separate class
 * - document categorization opennlp
 * - mahout naive bayes
 *
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;



public class TextStatistics {

    // INSTANCE VARS
    private String[] sentences;
    private HashMap<Integer, String[]> sentenceWithTokens;        // key: sentence index, val: list of tokens in that sentence
    private HashMap<Integer, String[]> sentenceWithTags;          // key: sentence index, val: list of tags in that sentence
    private HashMap<Integer, String[]> sentenceWithLemmas;        // key: sentence index, val: list of lemmas in that sentence
    private HashMap<Integer, ArrayList<String>> sentenceWithWordWS;
    private HashMap<Integer, int[]> sentenceWithSyllableCount;    // key: sentence index, val: count syllables in each word

    private int totalSentences = 0;
    private int totalTokens = 0;
    private int totalSyllables = 0;
    private int totalWordsByWS = 0;
    private int totalAlphaCharacters = 0;
    private int totalCharacters = 0;

    private double valFleschReadingEase = 0;
    private double valFleschKincaidGradeLevel = 0;

    // CONSTRUCTOR

    public TextStatistics(String text) throws IOException {

        // method to read string if file


        // sentence segmenter
        InputStream senStream = new FileInputStream("journalPro/en-sent.bin");
        SentenceModel sentenceModel = new SentenceModel(senStream);
        SentenceDetectorME detectorME = new SentenceDetectorME(sentenceModel);
        sentences = detectorME.sentDetect(text);
        totalSentences = sentences.length;

        //language detection todo
//        InputStream langStream = new FileInputStream("model");
//        LanguageDetectorModel m = new LanguageDetectorModel(is);

        // tokenizer
        InputStream tokStream = new FileInputStream("journalPro/en-token.bin");
        TokenizerModel model = new TokenizerModel(tokStream);
        Tokenizer tokenizer = new TokenizerME(model);

        // part of speech tagger
        InputStream tagStream = new FileInputStream("journalPro/en-pos-maxent.bin");
        POSModel posModel = new POSModel(tagStream);//
        POSTaggerME tagger = new POSTaggerME(posModel);

        // lemmatizer
        InputStream lemStream = new FileInputStream("journalPro/en-lemmatizer.dict");
        DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(lemStream);


        //instantiate instance variables
        sentenceWithTokens = new HashMap<>();
        sentenceWithTags = new HashMap<>();
        sentenceWithLemmas = new HashMap<>();



        // FOR EACH SENTENCE - Populate data for each token in each sentence. like a i,j coordinate system
        for (int i=0; i<sentences.length; i++) {

            // use the opennlp tools
            String[] tokens = tokenizer.tokenize(sentences[i]);
            String[] tags = tagger.tag(tokens);
            String[] lemmas = lemmatizer.lemmatize(tokens, tags);
            String[] words = sentences[i].split("\\s+");

            // populate the instance variables for this sentence (sentence index is stored as key)
            sentenceWithTokens.put(i, tokens);
            sentenceWithTags.put(i, tags);
            sentenceWithLemmas.put(i, lemmas);
            totalTokens += tokens.length;
            totalWordsByWS += words.length;

            // FOR EACH WORD by whitespace, get syllable count
            for (String word : words) {
                totalSyllables += SyllableCount.syllableCount(word);

                // for each character
                for (int j=0; j<word.length(); j++) {
                    if (Character.isLetter(word.charAt(j))) { //strips all punctuation and numbers
                        totalAlphaCharacters++;
                    }
                }
                totalCharacters += word.length();
            } // end for each word


            // FOR EACH TOKEN in this sentence
            for (String tok : tokens) {


            } // end for each token




        } // end for each sentence



        valFleschReadingEase = Readability.fleschReadingEase(233, 142, 6);
        valFleschKincaidGradeLevel = Readability.fleschKincaidGradeLevel(233, 142, 6);
        // totalSyllables, totalWordsByWS, totalSentences
        // 233, 142, 6
    }


    /*
     * HELPER method that read the text from a file
     *
     * @param fileName the file to be read
     * @return the text from the file as a single string
     * @throws IOException
     */
    private String readFromFile(String fileName) throws IOException {
        //create the stream for reading the file
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String currentLine;
        String text = "";
        while ((currentLine = br.readLine()) != null) {
            text += currentLine;
        }
        br.close(); // close stream
        return text;
    }






    public void printStuff() {
        for (String sen : sentences){
            System.out.println(sen);
        }

        System.out.println();
        System.out.println("total characters: " + totalCharacters);
        System.out.println("total alpha characters: " + totalAlphaCharacters);
        System.out.println("total syllables: " + totalSyllables);
        System.out.println("total tokens: " + totalTokens);
        System.out.println("total words(ws): " + totalWordsByWS);
        System.out.println("total sentences: " + totalSentences);

        System.out.printf("\nFlesch Reading Ease: %.2f", valFleschReadingEase);
        System.out.printf("\nFlesch-Kincaid grade level: %.2f", valFleschKincaidGradeLevel);
    }






    // DEMO
    public static void main(String[] args){

        String text = "Stargate is a military science fiction and media franchise based on the film written by Dean Devlin " +
                "and Roland Emmerich. The franchise is based on the idea of an alien Einstein–Rosen bridge device " +
                "(the Stargate) that enables nearly instantaneous travel across the cosmos. The franchise began with the " +
                "film Stargate, released on October 28, 1994, by Metro-Goldwyn-Mayer and Carolco, which grossed US$197 " +
                "million worldwide. In 1997, Brad Wright and Jonathan Glassner created a television series titled " +
                "Stargate SG-1 as a sequel to the film. This show was joined by Stargate Atlantis in 2004, Stargate Universe " +
                "in 2009, and a prequel web series, Stargate: Origins, in 2017. Also consistent with the same story are a variety" +
                " of books, video games and comic books, as well as the direct-to-DVD movies Stargate: The Ark of Truth and " +
                "Continuum, which concluded the first television show after 10 seasons.";
//         human:
        // io:    697 233 142 6  52.3 11.8
        // jdp:   697 238 144 6  42.7 13.3
        // rf:        -   -   -  47.6 12.8
        // jdp with io: " " " 44(-8.3)  13 (+1.2)


//        String text = "The very wily Mr Fox loved to eat rabbits. Big rabbits. Small rabbits";
        // human. 19 13 3
        // io:    18 13 3  85.3 2.4
        // jdp:   18 13 3  85.3 2.4
        // 72.3, 4.3

//        String text = "file in working directory";

        try {
            TextStatistics test = new TextStatistics(text);
            test.printStuff();


        } catch (Exception e){
            System.out.println("exceptional");
            e.printStackTrace();
        }



    }
}
