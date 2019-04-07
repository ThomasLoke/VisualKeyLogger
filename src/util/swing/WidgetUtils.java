package util.swing;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class WidgetUtils {

    private static class FormattedLabelFactory {

        private static String wrapWithTags(String text, String... tags) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tags.length; i++) {
                sb.append(String.format("<%s>", tags[i]));
            }
            sb.append(text);
            for (int i = tags.length - 1; i >= 0; i--) {
                sb.append(String.format("</%s>", tags[i]));
            }
            return sb.toString();
        }

        private final StringBuilder builder = new StringBuilder();

        private FormattedLabelFactory appendBoldedText(String text) {
            return appendText(text, "b");
        }

        private FormattedLabelFactory appendText(String text, String... tags) {
            builder.append(wrapWithTags(text, tags));
            return this;
        }

        private JLabel build() {
            return new JLabel(wrapWithTags(builder.toString(), "html"));
        }
    }

    public static JLabel createBoldedLabel(String text) {
        return new FormattedLabelFactory().appendBoldedText(text).build();
    }

    public static JPanel createCentreAlignedButtons(JButton... buttons) {
        return createCentreAlignedButtons(true, buttons);
    }

    public static JPanel createCentreAlignedButtons(boolean addSeparator, JButton... buttons) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for (int i = 0; i < buttons.length; i++) {
            toolBar.add(buttons[i]);
            if (addSeparator && i < buttons.length - 1) {
                toolBar.addSeparator();
            }
        }

        JPanel toolBarPanel = new JPanel();
        // Centre-align the button panel by padding it on the left/right
        toolBarPanel.add(new JLabel());
        toolBarPanel.add(toolBar);
        toolBarPanel.add(new JLabel());
        return toolBarPanel;
    }

}
