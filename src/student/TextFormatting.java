package student;
/*
 * This class is meant to contain your algorithm.
 * You should implement the static method:
 *   formatParagraph - which formats one paragraph
 */
import java.util.ArrayList;

public class TextFormatting {
    // simple greedy paragraph formating that just packs each line as full as possible
    // this will usually not give an optimal result
    //
    // input:
    //   words: an array of the words in the paragraph
    //   width: the desired formatting width
    //   result: an empty ArrayList into which you should add the resulting paragraph
    //
    // returns:
    //   the formatted paragraph as an ArrayList of Strings, 1 string for each
    //     formatted line of the paragraph
    //   the return value is the total calculated badness value for the paragraph
    public static int formatParagraph(String[] words, int width, ArrayList<String> result)  {
        int i = 0;
        int badness = 0;
        while (i < words.length) {       // this loop adds another line to result
            StringBuilder buf = new StringBuilder();
            buf.append(words[i++]);
            // add more words until full
            while (i < words.length && buf.length() + 1 + words[i].length() <= width)
                buf.append(" "+words[i++]);      // space between words

            badness += Math.pow(width-buf.length(), 3);
            result.add(buf.toString());   // add this line to the paragraph
        }
        return badness;
    }

    // extra credit paragraph formatting that has no late penalty for the last line of the paragraph
    public static int xc_formatParagraph(String[] words, int width, ArrayList<String> result) {
        return -1;		// not implemented
    }

}

