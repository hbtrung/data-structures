import static org.junit.Assert.*;
import org.junit.Test;

public class TestInteger {
    @Test
    public void testEqual() {
        int i = 127;
        int j = 127;
        Integer x = (Integer)i;
        Integer y = (Integer)j;
        assertTrue(x == y);

        int a = 128;
        int b = 128;
        Integer a1 = (Integer)a;
        Integer b1 = (Integer)b;
//        System.out.println(a1.hashCode());
//        System.out.println(b1.hashCode());
        assertTrue(a1 == b1);
    }
}
