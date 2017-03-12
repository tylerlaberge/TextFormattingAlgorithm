package student;
import java.util.*;

/*
 * A class used for neatly formatting text.
 */
public class TextFormatting {
    /*
     * A dynamic paragraph formatting algorithm to neatly format text evenly.
     *
     * input:
     *   words: an array of the words in the paragraph
     *   width: the desired formatting width
     *   result: an empty ArrayList into which the resulting paragraph is added.
     *
     * returns:
     *   the formatted paragraph as an ArrayList of Strings, 1 string for each
     *   formatted line of the paragraph
     *   the return value is the total calculated badness value for the paragraph
     */
    public static int formatParagraph(String[] words, int width, ArrayList<String> result) {
        int[][] table = buildTable(words, width);
        ArrayList<Integer> optimal_breaks = new ArrayList<>(); // list of when to have line breaks
        int badness = optimalBadness(table, optimal_breaks);
        int previous_break = 0;
        for(int optimal_break : optimal_breaks) { // format the paragraph
            // Join the words up to the line break and separate them each by a space.
            result.add(String.join(" ", (CharSequence[]) Arrays.copyOfRange(words, previous_break, optimal_break)));
            previous_break = optimal_break;
        }
        result.add(String.join(" ", (CharSequence[]) Arrays.copyOfRange(words, previous_break, words.length)));

        return badness;
    }

    // extra credit paragraph formatting that has no late penalty for the last line of the paragraph
    public static int xc_formatParagraph(String[] words, int width, ArrayList<String> result) {
        return -1;        // not implemented
    }

    /*
     * Build a table of badness values.
     *
     * Each row of the table represents a new line, and each column represents a word of the text.
     * Each individual cell represents the badness value of a line constructed by the words up to the cells column.
     *
     * input:
     *   words: An array of words to construct the table with.
     *   width: The maximum width of a line.
     *
     * returns:
     *   A 2d array of integers representing the badness values.
     */
    private static int[][] buildTable(String[] words, int width) {
        int[][] table = new int[words.length][words.length];

        for (int i = 0; i < words.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < words.length; j++) {
                if (j != i) {
                    sb.append(' ');
                }
                sb.append(words[j]);

                table[i][j] = calculateBadness(sb.length(), width);
            }
        }
        return table;
    }

    /*
     * Calculate the badness value for a given string length and maximum width.
     *
     * The badness is defined as the difference between the maximum width and string length to the 3rd power.
     *
     * input:
     *   string_length: The length of the string to compute the badness with.
     *   maximum_width: The maximum line width.
     *
     * returns:
     *   The calculated badness value.
     */
    private static int calculateBadness(int string_length, int max_width) {
        int remaining_space = max_width - string_length;

        return (remaining_space >= 0) ? (int) Math.pow(remaining_space, 3) : Integer.MAX_VALUE;
    }

    /*
     * Compute the optimal badness from a table of badness values.
     *
     * input:
     *   table: The constructed table of badness values.
     *   optimal_breaks: An empty list to store optimal line breaks into.
     *
     * returns:
     *   The optimal line breaks into the optimal breaks list.
     *   The return value is the computed optimal badness.
     */
    private static int optimalBadness(int[][] table, List<Integer> optimal_breaks) {
        int num_words = table[0].length;
        ArrayList<Integer> breaks = new ArrayList<>(); //List to store line breaks into as they occur
        int badness[] = new int[num_words + 1];
        badness[0] = 0;
        for (int i = 1; i <= num_words; i++) { //Find the optimal badness for i-words
            int min_badness = Integer.MAX_VALUE;
            for (int j = 0; j < i; j++) { // Find the minimum badness value for all configurations of the i-words.
                int curr_badness;
                int table_value = table[j][i - 1];
                if (table_value == Integer.MAX_VALUE) {
                    curr_badness = Integer.MAX_VALUE;
                } else {
                    curr_badness = badness[j] + table_value;
                }

                if (curr_badness < min_badness) {
                    min_badness = curr_badness;

                    // Found a new optimal badness so update the line break for the ith-word
                    try {
                        breaks.set(i - 1, j);
                    } catch (IndexOutOfBoundsException e) {
                        breaks.add(j);
                    }
                }
            }
            badness[i] = min_badness;
        }

        optimal_breaks.clear();
        optimal_breaks.addAll(optimalBreaks(breaks)); // Remove non optimal line breaks and add them all to the optimal breaks list.

        return badness[num_words];
    }

    /*
     * Construct a list of the optimal line breaks to get to the optimal badness value.
     *
     * inputs:
     *   all_breaks: A list of all the line breaks that occurred during computing of the optimal badness value.
     *
     * returns:
     *   A list of the optimal line breaks to get to the optimal badness value.
     */
    private static List<Integer> optimalBreaks(List<Integer> all_breaks) {
        int curr_break = all_breaks.get(all_breaks.size() - 1);
        if (curr_break == 0) {
            return new ArrayList<>();
        }
        List<Integer> optimal_breaks = optimalBreaks(all_breaks.subList(0, curr_break));
        optimal_breaks.add(curr_break);

        return optimal_breaks;
    }
}