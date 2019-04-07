package event.key;

import static event.key.NativeKeyEventMapping.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.eclipse.jdt.annotation.Nullable;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import util.swing.WidgetUtils;

public class NativeKeyEventEditorDialog extends JDialog {

    private static final long serialVersionUID = -6753541466089782920L;

    public static void createAndShowDialog(@Nullable Component parent, NativeKeyEventMapping mapping) {
        SwingUtilities.invokeLater(() -> new NativeKeyEventEditorDialog(parent, mapping));
    }

    private final Map<Integer, JTextField> keyToTextFields = new HashMap<>();

    private NativeKeyEventEditorDialog(@Nullable Component parent, NativeKeyEventMapping originalMapping) {
        super(parent == null ? null : SwingUtilities.getWindowAncestor(parent), "Key remapping editor", ModalityType.APPLICATION_MODAL);
        
        DefaultFormBuilder content = new DefaultFormBuilder(new FormLayout("f:p, 50dlu, f:p:g, 10dlu"));
        content.append(WidgetUtils.createBoldedLabel("KEY"));
        content.append(WidgetUtils.createBoldedLabel("DISPLAYED TEXT"));

        originalMapping.forEach((key, value) -> {
            content.append(WidgetUtils.createBoldedLabel(getDefaultText(key)));

            JTextField textField = new JTextField(value);
            keyToTextFields.put(key, textField);
            content.append(textField);
        });

        // Wrap content in a scroll pane
        JScrollPane contentScrollPane = new JScrollPane(content.getPanel());
        add(contentScrollPane, BorderLayout.CENTER);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            keyToTextFields.forEach((key, value) -> originalMapping.put(key, value.getText()));
            dispose();
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(WidgetUtils.createCentreAlignedButtons(applyButton, cancelButton), BorderLayout.PAGE_END);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        NativeKeyEventMapping mapping = createDefault();
        createAndShowDialog(null, mapping);
    }

}
