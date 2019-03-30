package event.key;

import static org.jnativehook.keyboard.NativeKeyEvent.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

import event.key.NativeKeyEventMapping;
import event.key.NativeKeyEventParser;
import util.problem.ProblemStore;

public class NativeKeyEventParserTest {
    
    @Rule public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Test public void testParser() throws Exception {
        tempFolder.create();
        
        File file = tempFolder.newFile();
        try (PrintWriter out = new PrintWriter(file)) {
            out.println("A, ←");
            out.println("S, ↓");
            out.println("D, →");
            out.println("W, ↑");
            out.println("Q, ");
            out.println("E");
        }
        NativeKeyEventParser parser = new NativeKeyEventParser(file);
        ProblemStore problems = parser.parse();
        assertTrue("Fatal errors while parsing input:\n" + problems.toString(), problems.hasNoErrors());
        NativeKeyEventMapping mapping = parser.getRemapping();
        assertEquals("←", mapping.get(VC_A));
        assertEquals(null, mapping.get(VC_Q));
        assertEquals("E", mapping.get(VC_E));
    }
    
}
