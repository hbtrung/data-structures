import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        String str1 = "cat";
        String str2 = "";
        String str3 = null;
        String str4 = "abccba";
        String str5 = "defgfed";
        String str6 = "1";

        assertFalse(palindrome.isPalindrome(str1));
        assertTrue(palindrome.isPalindrome(str2));
        assertFalse(palindrome.isPalindrome(str3));
        assertTrue(palindrome.isPalindrome(str4));
        assertTrue(palindrome.isPalindrome(str5));
        assertTrue(palindrome.isPalindrome(str6));
    }

    @Test
    public void testIsPalindromeCC() {
        String str1 = "cat";
        String str2 = "";
        String str3 = null;
        String str4 = "dtrque";
        String str5 = "qwerdxp";
        String str6 = "1";
        OffByOne obo = new OffByOne();

        assertFalse(palindrome.isPalindrome(str1, obo));
        assertTrue(palindrome.isPalindrome(str2, obo));
        assertFalse(palindrome.isPalindrome(str3, obo));
        assertTrue(palindrome.isPalindrome(str4, obo));
        assertTrue(palindrome.isPalindrome(str5, obo));
        assertTrue(palindrome.isPalindrome(str6, obo));
    }
}
