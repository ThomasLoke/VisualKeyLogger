package util.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static util.parser.NativeKeyEventParser.VIRTUAL_KEY_CODE_TO_TEXT;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

import util.problem.ProblemStore;

public class NativeKeyEventParserTest {
    
    @Rule public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test public void testUniqueKeyText() throws Exception {
        List<String> keyValues = new ArrayList<>(VIRTUAL_KEY_CODE_TO_TEXT.values());
        Collections.sort(keyValues);
        // Check that there's a 1-1 mapping between the key and values
        assertEquals(keyValues.size(), new HashSet<>(keyValues).size());
    }
    
    @Test public void testParser() throws Exception {
        tempFolder.create();
        
        File file = tempFolder.newFile();
        try (PrintWriter out = new PrintWriter(file)) {
            out.println("A, ←");
            out.println("S, ↓");
            out.println("D, →");
            out.println("F, ↑");
        }
        NativeKeyEventParser parser = new NativeKeyEventParser(file);
        ProblemStore problems = parser.parse();
        assertTrue("Fatal errors while parsing input:\n" + problems.toString(), problems.hasNoErrors());
    }
    
}
