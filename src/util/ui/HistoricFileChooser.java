package util.ui;

import java.awt.Component;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class HistoricFileChooser {
    
    public static final FileFilter CSV_FILTER = new FileFilter() {
        @Override public boolean accept(File file) {
            // Don't filter out directories from view
            return file.isDirectory() || file.getName().endsWith(".csv");
        }

        @Override public String getDescription() {
            return "CSV files";
        }
    };
    
    public static final Settings DEFAULT = new Settings();
    public static final Settings CSV = new Settings(Optional.empty(), Optional.of(CSV_FILTER));
    
    public static class Settings {
        private final Optional<File> initialDirectory;
        private final Optional<FileFilter> fileFilter;
        
        public Settings() {
            this(Optional.empty(), Optional.empty());
        }
        
        public Settings(Optional<File> initialDirectory, Optional<FileFilter> fileFilter) {
            this.initialDirectory = initialDirectory;
            this.fileFilter = fileFilter;
        }
        
        public Settings setInitialDirectory(@Nullable File initialDirectory) {
            return new Settings(Optional.ofNullable(initialDirectory), fileFilter);
        }
        
        public Settings setFileFilter(@Nullable FileFilter fileFilter) {
            return new Settings(initialDirectory, Optional.ofNullable(fileFilter));
        }
    }
    
    private static final Map<UUID, HistoricFileChooser> ID_TO_FILE_CHOOSER = new ConcurrentHashMap<>();
    
    public static HistoricFileChooser getOrCreateFileChooser(UUID uuid, Settings settings) {
        return ID_TO_FILE_CHOOSER.computeIfAbsent(uuid, id -> new HistoricFileChooser(settings));
    }

    private final JFileChooser fileChooser;
    
    private HistoricFileChooser(Settings settings) {
        fileChooser = new JFileChooser();
        if (settings.initialDirectory.isPresent()) {
            fileChooser.setCurrentDirectory(settings.initialDirectory.get());
        }
        if (settings.fileFilter.isPresent()) {
            fileChooser.setFileFilter(settings.fileFilter.get());
        }
    }
    
    public synchronized Optional<File> showAndSelectFile(Component parent) {
        int returnVal = fileChooser.showOpenDialog(parent);
        return returnVal != JFileChooser.APPROVE_OPTION ? Optional.empty() : Optional.ofNullable(fileChooser.getSelectedFile());
    }
    
}
