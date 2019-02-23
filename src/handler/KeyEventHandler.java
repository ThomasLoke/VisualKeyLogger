package handler;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import ui.ContentManager;

/**
 * Receives key presses from the user and translates them into events that get dispatched to the UI
 */
public class KeyEventHandler extends AbstractEventHandler<NativeKeyEvent> implements NativeKeyListener {
    
    private final AtomicBoolean shouldAppend = new AtomicBoolean(true);
    
    public KeyEventHandler(ContentManager manager) {
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
        String description = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
        return String.format("%s\t%s\n", timestamp, description);
    }

}
