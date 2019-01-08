public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        if (word == null) {
            return null;
        }
        Deque<Character> result = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            result.addLast(word.charAt(i));
        }
        return result;
    }

    private boolean isPalindromeHelper(Deque<Character> p, int cnt) {
        if (cnt == 0) {
            return true;
        }
        return p.removeFirst() == p.removeLast() && isPalindromeHelper(p, cnt - 1);
    }

    private boolean isPalindromeCCHelper(Deque<Character> p, int cnt, CharacterComparator cc) {
        if (cnt == 0) {
            return true;
        }
        return cc.equalChars(p.removeFirst(), p.removeLast()) &&
                isPalindromeCCHelper(p, cnt - 1, cc);
    }

    public boolean isPalindrome(String word) {
//        if (word == null) {
//            return false;
//        }
//        for (int i = 0; i < word.length()/2; i++) {
//            if (word.charAt(i) != word.charAt(word.length() - 1 - i)) {
//                return false;
//            }
//        }
//        return true;

        Deque<Character> p = wordToDeque(word);
        if (p == null) {
            return false;
        }
        return isPalindromeHelper(p, p.size() / 2);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> p = wordToDeque(word);
        if (p == null) {
            return false;
        }
        return isPalindromeCCHelper(p, p.size() / 2, cc);
    }
}
