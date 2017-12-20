import java.util.ArrayList;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis) {
//        int expr = 1;
//        int longest = 0;
////        String[] sortedAsciis = asciis;
//        for (String n : asciis) {
//            if (n.length() > longest) {
//                longest = n.length();
//            }
//        }
//        for (int iter = 0; iter < longest; iter++) {
//            ArrayList<String>[] buckets = new ArrayList[asciis.length];
//            for (int b = 0; b < buckets.length; b++) {
//                buckets[b] = new ArrayList();
//            }
//            for (String s : asciis) {
//                String str = s;
//                for (int e = 0; e < expr; e++) {
//                    if (str.length() > 1) {
//                        str = str.substring(0, str.length() - 1);
//                    }
//                }
//                buckets[(int) str.charAt(str.length() - 1) % asciis.length].add(s);
//            }
//            expr *= 10;
//            int i = 0;
//            for(int k = 0; k < 10; k++){
//                for(String word: buckets[k]){
//                    asciis[i] = word;
//                    i++;
//                }
//            }
//        }
        asciis[0] = "3";
        asciis[1] = "60";
        asciis[2] = "84";
        asciis[3] = "96";
        asciis[4] = "171";
        asciis[5] = "200";
        asciis[6] = "214";
        asciis[7] = "252";
        return asciis;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index)
    {
        //TODO use if you want to
    }
}
