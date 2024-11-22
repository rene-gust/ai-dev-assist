package com.oxidEsales.aIDevAssist.File;

import com.intellij.openapi.vfs.VirtualFile;
import com.oxidEsales.aIDevAssist.Model.ModuleFolderSearchResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClassCreator {
    public void create(
            ModuleFolderSearchResult moduleFolderSearchResult,
            VirtualFile virtualFile,
            String unitTestClassContent
    ) {

        String moduleNamespace = moduleFolderSearchResult.composerPSR4Entry().namespace();
        String phpFileSubNamespace = moduleFolderSearchResult.namespaceOfPhpFile().replace(moduleNamespace, "");
        String testsFolderPath = moduleFolderSearchResult.absoluteModuleFolder() + File.separator + "tests" + File.separator + "Unit";
        String testClassFolderPath = testsFolderPath + File.separator + phpFileSubNamespace.replace("\\", File.separator);
        Path path = Paths.get(testClassFolderPath);

        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            System.err.println("Failed to create directories: " + e.getMessage());
        }

        String phpFilePath = virtualFile.getPath();
        Pattern pattern = Pattern.compile(File.separator + "([^" + File.separator + "]+).php$");
        Matcher matcher = pattern.matcher(phpFilePath);
        String phpFileName = "";
        if (matcher.find()) {
            phpFileName = matcher.group(1);
        }

        if (!phpFileName.isEmpty()) {
            try (
                    FileWriter writer = new FileWriter(
                        testClassFolderPath + File.separator + phpFileName + "Test.php"
                    )
            ) {
                writer.write(unitTestClassContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
