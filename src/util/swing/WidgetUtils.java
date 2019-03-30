package util.swing;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class WidgetUtils {

    public static JPanel createCentreAlignedButtons(JButton... buttons) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for (JButton button : buttons) {
            toolBar.add(button);
        }

        JPanel toolBarPanel = new JPanel();
        // Centre-align the button panel by padding it on the left/right
        toolBarPanel.add(new JLabel());
        toolBarPanel.add(toolBar);
        toolBarPanel.add(new JLabel());
        return toolBarPanel;
    }

}
