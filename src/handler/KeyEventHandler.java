package handler;

import java.time.LocalTime;
import java.util.Optional;
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
    
    private final @NonNull AtomicBoolean shouldAppend = new AtomicBoolean(true);
    
    private final AtomicReference<NativeKeyEventMapping> mappingRef;

    public KeyEventHandler(JTextAreaManager manager, AtomicReference<NativeKeyEventMapping> mappingRef) {
        super(manager);
        this.mappingRef = mappingRef;
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
        Optional<String> text = buildString(nativeEvent);
        text.ifPresent(s -> manager.addText(s));
    }
    
    private Optional<String> buildString(NativeKeyEvent nativeEvent) {
        // Use the default mapping if none present
        NativeKeyEventMapping mapping = mappingRef.get();
        mapping = mapping != null ? mapping : NativeKeyEventMapping.DEFAULT;

        int keyCode = nativeEvent.getKeyCode();
        String remapped = mapping.get(keyCode);
        // Don't print anything if remapped to null
        if (remapped == null)
            return Optional.empty();
        
        String timestamp = LocalTime.now().toString();
        return Optional.of(String.format("%s\t%s\n", timestamp, remapped));
    }

}
