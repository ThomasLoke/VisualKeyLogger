package util.parser;

import static util.parser.NativeKeyEventMapping.DEFAULT_INVERSE;
import static util.parser.NativeKeyEventMapping.createDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import util.problem.ProblemStore;

/**
 * Parses a given file and returns a Map<Integer, String> that maps the virtual key code to its String representation
 * Required format for file: Two-column CSV of (1=String, 2=String) where 1 is the default string representation of 
 * the key-press and 2 is the new string representation.
 */
public class NativeKeyEventParser {
    
    private final @NonNull File file;
    private final @NonNull NativeKeyEventMapping keyCodeToTextRemapping = createDefault();
    
    public NativeKeyEventParser(@NonNull File file) {
        this.file = file;
    }
    
    public ProblemStore parse() {AtomicInteger inputLineCount = new AtomicInteger();
        AtomicInteger parsedLineCount = new AtomicInteger();
        ProblemStore problems = new ProblemStore();
        try (Stream<String> stream = Files.lines(file.toPath())) {
            stream.forEach(new Consumer<String>() {
                @Override public void accept(String line) {
                    inputLineCount.getAndIncrement();
                    
                    List<String> parts = Arrays.asList(line.split(","));
                    parts.forEach(String::trim);
                    if (parts.size() != 2) {
                        problems.addWarning(String.format("Unable to parse the line \'%s\': Does not conform to the expected format, so ignoring line", line));
                        return;
                    }
                    String keyText = parts.get(0);
                    Integer keyCode = DEFAULT_INVERSE.get(keyText);
                    if (keyCode == null) {
                        problems.addWarning(String.format("Unable to parse the line \'%s\': No matching key text found for \'%s\', so ignoring line", line, keyText));
                        return;
                    }
                    String keyDescription = parts.get(1);
                    // If its an empty string, then interpret that as a null value, i.e. ignore key-presses from that key
                    keyCodeToTextRemapping.put(keyCode, keyDescription.isEmpty() ? null : keyDescription);
                    
                    parsedLineCount.getAndIncrement();
                }
            });
        } catch (IOException e) {
            problems.addError("Unable to read file", e);
        }
        problems.addInfo(String.format("Successfully parsed %d/%d lines", parsedLineCount.get(), inputLineCount.get()));
        return problems;
    }
    
    public @NonNull NativeKeyEventMapping getRemapping() {
        return keyCodeToTextRemapping;
    }
    
}
