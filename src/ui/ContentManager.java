package ui;

import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public abstract class ContentManager<J extends JComponent> {
    
    /** ALL operations on this must be wrapped with {@link handleEvent} */
    private final J content;
    
    protected ContentManager(J content) {
        this.content = content;
    }
    
    public abstract void clear();
    
    /** Executes the event on the EDT */
    protected void handleEvent(Consumer<J> event) {
        SwingUtilities.invokeLater(() -> event.accept(content));
    }
    
}
