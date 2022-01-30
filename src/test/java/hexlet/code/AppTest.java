package hexlet.code;

import org.junit.jupiter.api.Test;

import static hexlet.code.Test.hello;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author andreiserov
 */
class AppTest {

    @Test void emptyTest() {
        assertEquals("Hello", hello());
    }

}
