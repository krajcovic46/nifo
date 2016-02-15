package IFO;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class IfocolTest {

    Ifocol ifocol = new Ifocol("");

    @Test
    public void controlTests() {
        assertTrue(ifocol.add(1));
        assertFalse(ifocol.add(1));

        assertTrue(ifocol.remove(1));
        assertFalse(ifocol.remove(100));

        assertEquals(new HashSet<>(), ifocol.getFilesInside());
    }
}