package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import handler.AbstractEventHandler;
import handler.KeyEventHandler;
import ui.ContentManager;
import ui.JTextAreaManager;

/**
 * Entry point for application
 */
public class VisualKeyLogger extends JFrame implements WindowListener {
    
    private static final long serialVersionUID = -3468171593621788434L;
    
    private final List<ContentManager> contentManagers = new ArrayList<>();
    private final List<AbstractEventHandler> eventHandlers = new ArrayList<>();
    
    private final JTextAreaManager textAreaManager;
    private final KeyEventHandler keyEventHandler;
    
    private VisualKeyLogger() {
        setTitle("Visual KeyLogger");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(this);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Serif", Font.BOLD, 20));
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 1000));
        add(scrollPane, BorderLayout.CENTER);
        
        add(createButtonPanel(), BorderLayout.PAGE_END);
        
        // Setup content managers
        textAreaManager = new JTextAreaManager(textArea);
        contentManagers.add(textAreaManager);
        
        // Register event handlers
        keyEventHandler = new KeyEventHandler(textAreaManager);
        eventHandlers.add(keyEventHandler);
        
        pack();
        setVisible(true);
    }
    
    private JComponent createButtonPanel() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton clearButton = new JButton("Clear display");
        clearButton.addActionListener(e -> contentManagers.forEach(ContentManager::clearText));
        toolBar.add(clearButton);
        
        JButton suspendButton = new JButton("Suspend input");
        JButton resumeButton = new JButton("Resume input");
        resumeButton.setVisible(false);
        
        suspendButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                eventHandlers.forEach(AbstractEventHandler::pause);
                suspendButton.setVisible(false);
                resumeButton.setVisible(true);
            }
        });
        toolBar.add(suspendButton);
        resumeButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                eventHandlers.forEach(AbstractEventHandler::resume);
                suspendButton.setVisible(true);
                resumeButton.setVisible(false);
            }
        });
        toolBar.add(resumeButton);
        
        JPanel toolBarPanel = new JPanel();
        // Center-align the button panel by padding it on the left/right
        toolBarPanel.add(new JLabel());
        toolBarPanel.add(toolBar);
        toolBarPanel.add(new JLabel());
        
        return toolBarPanel;
    }
    
    @Override public void windowOpened(WindowEvent e) {
        // Initialize native hook.
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new VisualKeyLogger();
            }
        });
    }
    
}
