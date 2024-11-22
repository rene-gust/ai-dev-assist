package com.oxidEsales.aIDevAssist.File;

import com.oxidEsales.aIDevAssist.Model.ModuleFolderSearchResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestClassCreator {
    public boolean create(ModuleFolderSearchResult moduleFolderSearchResult) {

        String moduleNamespace = moduleFolderSearchResult.composerPSR4Entry().namespace();
        String phpFileFullNamespace = moduleFolderSearchResult.namespaceOfPhpFile();
        String phpFileSubNamespace = moduleFolderSearchResult.namespaceOfPhpFile().replace(moduleNamespace, "");
        String testsFolderPath = moduleFolderSearchResult.absoluteModuleFolder() + File.separator + "tests" + File.separator + "Unit";
        String testClassFolderPath = testsFolderPath + File.separator + phpFileSubNamespace.replace("\\", File.separator);
        Path path = Paths.get(testClassFolderPath);

        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            System.err.println("Failed to create directories: " + e.getMessage());
            return false;
        }

        return true;
    }
}
