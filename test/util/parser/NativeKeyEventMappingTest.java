package util.parser;

import static org.jnativehook.keyboard.NativeKeyEvent.*;
import static org.junit.Assert.*;
import static util.parser.NativeKeyEventMapping.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

public class NativeKeyEventMappingTest {

    @Test public void testUniqueKeyText() throws Exception {
        List<String> keyValues = new ArrayList<>(DEFAULT.values());
        Collections.sort(keyValues);
        // Check that there's a 1-1 mapping between the key and values
        assertEquals(keyValues.size(), new HashSet<>(keyValues).size());
    }

    @Test public void testDefaultToMappedText() throws Exception {
        NativeKeyEventMapping mapping = createDefault();
        mapping.put(VC_A, "←");
        mapping.put(VC_S, "↓");
        mapping.put(VC_D, "→");
        mapping.put(VC_W, "↑");
        mapping.put(VC_Q, null);

        String text = mapping.getDefaultToMappedText();
        assertTrue(text.contains("A, ←"));
        assertTrue(text.contains("S, ↓"));
        assertTrue(text.contains("D, →"));
        assertTrue(text.contains("W, ↑"));
        assertTrue(text.contains("Q, "));
    }

}
