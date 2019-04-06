package event.key;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.eclipse.jdt.annotation.Nullable;

public class NativeKeyEventEditorDialog extends JDialog {

    private static final long serialVersionUID = -6753541466089782920L;

    public static void createAndShowDialog(@Nullable Component parent, NativeKeyEventMapping mapping) {
        SwingUtilities.invokeLater(() -> new NativeKeyEventEditorDialog(parent, mapping));
    }

    private final NativeKeyEventMapping modifiedMapping;

    private NativeKeyEventEditorDialog(@Nullable Component parent, NativeKeyEventMapping originalMapping) {
        super(parent == null ? null : SwingUtilities.getWindowAncestor(parent), "Key remapping editor", ModalityType.APPLICATION_MODAL);
        
        modifiedMapping = new NativeKeyEventMapping(originalMapping);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        NativeKeyEventMapping mapping = NativeKeyEventMapping.createDefault();
        createAndShowDialog(null, mapping);
    }

}
