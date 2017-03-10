package student;
/*
 * This class is meant to contain your algorithm.
 * You should implement the static method:
 *   formatParagraph - which formats one paragraph
 */
import java.util.*;


public class TextFormatting {
    /*
    simple greedy paragraph formatting that just packs each line as full as possible
    this will usually not give an optimal result

    input:
      words: an array of the words in the paragraph
      width: the desired formatting width
      result: an empty ArrayList into which you should add the resulting paragraph

    returns:
      the formatted paragraph as an ArrayList of Strings, 1 string for each
        formatted line of the paragraph
      the return value is the total calculated badness value for the paragraph
    */
    public static int formatParagraph(String[] words, int width, ArrayList<String> result) {
        int[][] table = buildTable(words, width);
        ArrayList<Integer> optimal_breaks = new ArrayList<>();
        int badness = optimalBadness(table, optimal_breaks);
        int previous_break = 0;
        for(int optimal_break : optimal_breaks) {
            result.add(String.join(" ", Arrays.copyOfRange(words, previous_break, optimal_break)));
            previous_break = optimal_break;
        }
        result.add(String.join(" ", Arrays.copyOfRange(words, previous_break, words.length)));

        return badness;
    }

    // extra credit paragraph formatting that has no late penalty for the last line of the paragraph
    public static int xc_formatParagraph(String[] words, int width, ArrayList<String> result) {
        return -1;        // not implemented
    }

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

    private static int calculateBadness(int string_length, int max_width) {
        int remaining_space = max_width - string_length;

        return (remaining_space >= 0) ? (int) Math.pow(remaining_space, 3) : Integer.MAX_VALUE;
    }

    private static int optimalBadness(int[][] table, List<Integer> optimal_breaks) {
        int num_words = table[0].length;
        ArrayList<Integer> breaks = new ArrayList<>();
        int badness[] = new int[num_words + 1];
        badness[0] = 0;
        for (int i = 1; i <= num_words; i++) {
            int min_badness = Integer.MAX_VALUE;
            for (int j = 0; j < i; j++) {
                int curr_badness;
                int table_value = table[j][i - 1];
                if (table_value == Integer.MAX_VALUE) {
                    curr_badness = Integer.MAX_VALUE;
                } else {
                    curr_badness = badness[j] + table_value;
                }

                if (curr_badness < min_badness) {
                    min_badness = curr_badness;

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
        optimal_breaks.addAll(optimalBreaks(breaks));

        return badness[num_words];
    }

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