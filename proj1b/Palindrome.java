public class Palindrome {
    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> palindeque = new ArrayDequeSolution<Character>();
        for (int i = 0; i < word.length(); i++) {
            char a = word.charAt(i);
            palindeque.addLast(a);
        }
        return palindeque;
    }

    public static boolean isPalindrome(String word) {
        for (int j = 0; j < word.length() / 2; j++) {
            if (word.charAt(j) != word.charAt(word.length() - j - 1)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
        word = word.toLowerCase();
        Deque<Character> palindeque = wordToDeque(word);
        return isPalindrome(palindeque, cc);
    }

    private static boolean isPalindrome(Deque<Character> palindeque, CharacterComparator cc) {
        if (palindeque.size() == 0 || palindeque.size() == 1) {
            return true;
        }
        char f = palindeque.removeFirst();
        char l = palindeque.removeLast(); 
        if (cc.equalChars(f, l)) {
            return (isPalindrome(palindeque, cc) && cc.equalChars(f, l));
        } else {
            return false;
        }
    }
}
