package util.problem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import util.problem.ProblemStore.Problem;
import util.swing.WidgetUtils;

@NonNullByDefault
public class ProblemStoreDialog extends JDialog {

    private static final long serialVersionUID = 7102450160109521195L;

    public static void createAndShowDialogIfRequired(ProblemStore problems, String title, @Nullable Component parent) {
        if (problems.isEmpty())
            return; // Don't have to show the dialog if we've nothing to report
        SwingUtilities.invokeLater(() -> new ProblemStoreDialog(problems, title, parent));
    }

    private ProblemStoreDialog(ProblemStore problems, String title, @Nullable Component parent) {
        super(parent == null ? null : SwingUtilities.getWindowAncestor(parent), title);

        JLabel header = new JLabel("There were errors, warnings and/or information during execution");
        add(header, BorderLayout.PAGE_START);

        DefaultFormBuilder content = new DefaultFormBuilder(new FormLayout("f:m, 3dlu, f:p:g"));
        List<Problem> problemList = problems.getProblems();
        for (int i = 0; i < problemList.size(); i++) {
            appendProblemToPanel(problemList.get(i), content);
        }

        // Wrap content in a scroll pane
        JScrollPane contentScrollPane = new JScrollPane(content.getPanel());
        add(contentScrollPane, BorderLayout.CENTER);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> dispose());
        add(WidgetUtils.createCentreAlignedButtons(okButton), BorderLayout.PAGE_END);

        pack();
        setVisible(true);
    }

    private void appendProblemToPanel(Problem problem, DefaultFormBuilder builder) {
        builder.append(new JLabel(problem.level.getIcon()));
        builder.append(new JLabel(problem.description));
    }

    public static void main(String[] args) {
        ProblemStore problems = new ProblemStore();
        problems.addError("An example error", null);
        problems.addWarning("An example warning");
        problems.addInfo("Some informative info");
        problems.addWarning("Yet another warning");
        createAndShowDialogIfRequired(problems, "All the problems", null);
    }

}
