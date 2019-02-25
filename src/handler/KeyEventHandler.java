package handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import ui.JTextAreaManager;

/**
 * Receives key presses from the user and translates them into events that get dispatched to the UI
 */
public class KeyEventHandler extends AbstractEventHandler<JTextAreaManager, NativeKeyEvent> implements NativeKeyListener {
    
    public static Map<Integer, String> parseCSVToRemapping(File file) {
        Map<Integer, String> remapping = new HashMap<>();
        try (Stream<String> stream = Files.lines(file.toPath())) {
            stream.forEach(new Consumer<String>() {
                @Override public void accept(String line) {
                    String[] parts = line.split(",");
                    
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return remapping;
    }
    
    private final Map<Integer, String> remappedKeyText = new HashMap<>();
    private final AtomicBoolean shouldAppend = new AtomicBoolean(true);
    
    public KeyEventHandler(JTextAreaManager manager) {
        super(manager);
    }
    
    public void clearRemapping() {
        remappedKeyText.clear();
    }
    
    @Override public void pause() {
        shouldAppend.getAndSet(false);
    }
    
    @Override public void resume() {
        shouldAppend.getAndSet(true);
    }

    @Override public void nativeKeyTyped(NativeKeyEvent nativeEvent) { }
    @Override public void nativeKeyReleased(NativeKeyEvent nativeEvent) { }

    @Override public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        if (!shouldAppend.get())
            return;
        String text = buildString(nativeEvent);
        manager.addText(text);
    }
    
    private String buildString(NativeKeyEvent nativeEvent) {
        String timestamp = LocalTime.now().toString();
        
        int keyCode = nativeEvent.getKeyCode();
        String remapped = remappedKeyText.get(keyCode);
        String description = remapped != null ? remapped : NativeKeyEvent.getKeyText(keyCode);
        
        return String.format("%s\t%s\n", timestamp, description);
    }

}
