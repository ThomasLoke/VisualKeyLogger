package handler;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jdt.annotation.NonNull;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import ui.JTextAreaManager;
import util.parser.NativeKeyEventMapping;

/**
 * Receives key-presses from the user and translates them into events that get dispatched to the UI
 */
public class KeyEventHandler extends AbstractEventHandler<JTextAreaManager, NativeKeyEvent> implements NativeKeyListener {
    
    private final AtomicReference<NativeKeyEventMapping> mappingRef = new AtomicReference<>();
    private final @NonNull AtomicBoolean shouldAppend = new AtomicBoolean(true);
    
    public KeyEventHandler(JTextAreaManager manager) {
        super(manager);
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
        NativeKeyEventMapping mapping = mappingRef.get();
        String remapped = mapping == null ? null : mapping.get(keyCode);
        String description = remapped != null ? remapped : NativeKeyEvent.getKeyText(keyCode);
        
        return String.format("%s\t%s\n", timestamp, description);
    }

}