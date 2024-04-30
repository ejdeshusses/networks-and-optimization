package ejd;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class CopyGraphs {
    private File sourceDirectory;
    private File targetDirectory;

    public CopyGraphs(File sourceDirectory, File targetDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
    }

    public void clearDirectory() throws IOException {
        if (targetDirectory.exists()) {
            for (File file : targetDirectory.listFiles()) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
    }

    private void deleteDirectory(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
        directory.delete();
    }

    public void copyTxtFiles() throws IOException {
        if (!sourceDirectory.exists()) {
            throw new IllegalArgumentException("Source directory does not exist: " + sourceDirectory);
        }

        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        copyFilesInDirectory(sourceDirectory, targetDirectory);
    }

    private void copyFilesInDirectory(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!new File(target, source.getName()).exists()) {
                new File(target, source.getName()).mkdirs();
            }

            String[] children = source.list();
            for (int i = 0; i < children.length; i++) {
                copyFilesInDirectory(new File(source, children[i]), new File(target, source.getName()));
            }
        } else if (source.getName().endsWith(".txt")) {
            Files.copy(source.toPath(), new File(target, source.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void main(String[] args) throws IOException {
        // File sourceDirectory = new File("source_directory_path");
        // File targetDirectory = new File("target_directory_path");
        
        File sourceDirectory = new File("cs595_python/sample_graphs");
        File targetDirectory = new File("cs595folder/src/main/java/ejd/data_graphs");


        CopyGraphs fileCopier = new CopyGraphs(sourceDirectory, targetDirectory);
        fileCopier.clearDirectory();
        fileCopier.copyTxtFiles();
    }
}

