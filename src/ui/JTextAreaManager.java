package ui;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JTextAreaManager implements ContentManager {
    
    /** ALL operations on this must be wrapped with {@link handleEvent} */
    private final JTextArea textArea;
    
    public JTextAreaManager(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override public void addText(String str) {
        handleEvent(() -> textArea.append(str));
    }

    @Override public void clearText() {
        handleEvent(() -> textArea.setText(""));
    }

    /** Executes the event on the EDT */
    private void handleEvent(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

}
