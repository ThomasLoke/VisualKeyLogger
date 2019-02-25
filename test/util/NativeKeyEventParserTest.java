package util;

import static org.junit.Assert.*;
import static util.NativeKeyEventParser.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

public class NativeKeyEventParserTest {

    // FIXME: Failing, because VC_MEDIA_STOP and VC_BROWSER_STOP have the same description "Stop"
    @Test public void testUniqueKeyText() {
        List<String> keyValues = new ArrayList<>(VIRTUAL_KEY_CODE_TO_TEXT.values());
        Collections.sort(keyValues);
        assertEquals(keyValues.size(), new HashSet<>(keyValues).size());
    }
    
}
