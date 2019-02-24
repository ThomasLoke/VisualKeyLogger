package ui;

import javax.swing.JTextArea;

public class JTextAreaManager extends ContentManager<JTextArea> {
    
    public JTextAreaManager(JTextArea textArea) {
        super(textArea);
    }

    public void addText(String str) {
        handleEvent(content -> content.append(str));
    }

    public void clear() {
        handleEvent(content -> content.setText(""));
    }

}
