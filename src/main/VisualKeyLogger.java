package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

import org.eclipse.jdt.annotation.NonNull;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import handler.AbstractEventHandler;
import handler.KeyEventHandler;
import ui.ContentManager;
import ui.JTextAreaManager;
import util.parser.NativeKeyEventMapping;
import util.ui.HistoricFileChooser;

/**
 * Entry point for application
 */
public class VisualKeyLogger extends JFrame implements WindowListener {
    
    private static final long serialVersionUID = -3468171593621788434L;
    
    private final List<ContentManager> contentManagers = new ArrayList<>();
    private final List<AbstractEventHandler> eventHandlers = new ArrayList<>();
    
    private final @NonNull NativeKeyEventMapping keyMapping;
    private final @NonNull JTextAreaManager textAreaManager;
    private final @NonNull KeyEventHandler keyEventHandler;
    
    private VisualKeyLogger() {
        setTitle("Visual KeyLogger");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(this);
        
        setJMenuBar(createMenuBar());
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Serif", Font.BOLD, 20));
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 1000));
        add(scrollPane, BorderLayout.CENTER);
        
        add(createButtonPanel(), BorderLayout.PAGE_END);
        
        keyMapping = NativeKeyEventMapping.createDefault();

        // Setup content managers
        textAreaManager = new JTextAreaManager(textArea);
        contentManagers.add(textAreaManager);
        
        // Register event handlers
        keyEventHandler = new KeyEventHandler(textAreaManager, keyMapping);
        eventHandlers.add(keyEventHandler);
        
        pack();
        setVisible(true);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createRemappingMenu());
        return menuBar;
    }
    
    private JMenu createRemappingMenu() {
        JMenu menu = new JMenu("Remapping settings");
        menu.getAccessibleContext().setAccessibleDescription("Remapping settings");
        
        JMenuItem importItem = new JMenuItem("Import");
        importItem.getAccessibleContext().setAccessibleDescription("Import remapping settings from file");
        importItem.addActionListener(new ActionListener() {
            private final UUID uuid = UUID.randomUUID();
            @Override public void actionPerformed(ActionEvent e) {
                Optional<File> csvFileOption = HistoricFileChooser.getOrCreateFileChooser(uuid, HistoricFileChooser.CSV).showAndSelectFile(VisualKeyLogger.this);
                if (!csvFileOption.isPresent())
                    return;
                File csvFile = csvFileOption.get();
                if (!csvFile.exists()) {
                    JOptionPane.showMessageDialog(VisualKeyLogger.this,
                            String.format("The selected file %s does not exist", csvFile.getAbsolutePath()),
                            "Import error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // TODO: Parse file and update model
            }
        });
        menu.add(importItem);
        
        JMenuItem exportItem = new JMenuItem("Export");
        exportItem.getAccessibleContext().setAccessibleDescription("Export current remapping settings to file");
        exportItem.addActionListener(new ActionListener() {
            private final UUID uuid = UUID.randomUUID();
            @Override public void actionPerformed(ActionEvent e) {
                Optional<File> csvFileOption = HistoricFileChooser.getOrCreateFileChooser(uuid, HistoricFileChooser.CSV).showAndSelectFile(VisualKeyLogger.this);
                if (!csvFileOption.isPresent())
                    return;
                File csvFile = csvFileOption.get();
                if (csvFile.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(VisualKeyLogger.this, 
                            String.format("The file %s already exists--do you want to overwrite it?", csvFile.getAbsolutePath()), 
                            "Confirm overwrite",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (confirm != JOptionPane.OK_OPTION)
                        return;
                }
                try (BufferedWriter writer = Files.newBufferedWriter(csvFile.toPath())) {
                    writer.write(keyMapping.getDefaultToMappedText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(VisualKeyLogger.this,
                            String.format("Failed to write to the selected file %s: %s",
                                    csvFile.getAbsolutePath(), ex.getMessage()),
                            "Import error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        menu.add(exportItem);
        
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.getAccessibleContext().setAccessibleDescription("Display and edit remapping settings");
        editItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                // TODO: Display + edit dialog
            }
        });
        menu.add(editItem);
        
        return menu;
    }
    
    private JComponent createButtonPanel() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton clearButton = new JButton("Clear display");
        clearButton.setFocusable(false);
        clearButton.addActionListener(e -> contentManagers.forEach(ContentManager::clear));
        toolBar.add(clearButton);
        
        JButton suspendResumeButton = new JButton("Suspend input");
        suspendResumeButton.setFocusable(false);
        suspendResumeButton.addActionListener(new ActionListener() {
            private boolean paused = false;
            @Override public void actionPerformed(ActionEvent e) {
                if (paused) {
                    eventHandlers.forEach(AbstractEventHandler::resume);
                    suspendResumeButton.setText("Suspend input");
                } else {
                    eventHandlers.forEach(AbstractEventHandler::pause);
                    suspendResumeButton.setText("Resume input");
                }
                paused = !paused;
            }
        });
        toolBar.add(suspendResumeButton);
        
        JPanel toolBarPanel = new JPanel();
        // Centre-align the button panel by padding it on the left/right
        toolBarPanel.add(new JLabel());
        toolBarPanel.add(toolBar);
        toolBarPanel.add(new JLabel());
        
        return toolBarPanel;
    }
    
    @Override public void windowOpened(WindowEvent e) {
        // Initialise native hook.
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(keyEventHandler);
    }
    
    @Override public void windowClosed(WindowEvent e) {
        // Clean up the native hook.
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem unregistering the native hook: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    @Override public void windowClosing(WindowEvent e) { }
    @Override public void windowIconified(WindowEvent e) { }
    @Override public void windowDeiconified(WindowEvent e) { }
    @Override public void windowActivated(WindowEvent e) { }
    @Override public void windowDeactivated(WindowEvent e) { }
    
    static {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override public void uncaughtException(Thread t, Throwable e) {
                // Catch any unhandled exceptions and report them
                StringJoiner sj = new StringJoiner(System.lineSeparator());
                sj.add("An unexpected error caused this application to terminate; please report the follow error:");
                sj.add("");
                sj.add("Thread: " + t.getName());
                sj.add(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
                for (StackTraceElement elem : e.getStackTrace()) {
                    sj.add("    " + elem.toString());
                }
                String text = sj.toString();
                // JTextArea to facilitate copy-paste
                JTextArea textArea = new JTextArea();
                textArea.setText(text);
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, textArea, "Uh-oh!", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new VisualKeyLogger();
            }
        });
    }
    
}
