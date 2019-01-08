public class OffByOne implements CharacterComparator {

    @Override
    public boolean equalChars(char x, char y) {
        if (isAlphabetical(x) && isAlphabetical(y)) {
            return Math.abs(x - y) == 1 || Math.abs(Math.abs(x - y) - 32) == 1;
        }
        return false;
    }

    private boolean isAlphabetical(char a) {
        return (a >= 65 && a <= 90) || (a >= 97 && a <= 122);
    }
}
