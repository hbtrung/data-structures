import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeque {
    @Test
    public void testEmpty() {
        ArrayDeque<Integer> tester = new ArrayDeque<>();
        System.out.println(tester.size());
        System.out.println(tester.removeFirst());
        System.out.println(tester.removeLast());
        System.out.println(tester.get(0));
        tester.printDeque();
    }
}
