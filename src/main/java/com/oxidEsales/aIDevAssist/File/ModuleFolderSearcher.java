package com.oxidEsales.aIDevAssist.File;

import com.intellij.openapi.vfs.VirtualFile;
import com.oxidEsales.aIDevAssist.ComposerJson.ComposerJsonParser;
import com.oxidEsales.aIDevAssist.ComposerJson.ComposerJsonSearcher;
import com.oxidEsales.aIDevAssist.Model.ComposerPSR4Entry;
import com.oxidEsales.aIDevAssist.Model.ModuleFolderSearchResult;
import com.oxidEsales.aIDevAssist.PhpParser.PhpNamespaceParser;

import java.io.File;

public class ModuleFolderSearcher {
    public ModuleFolderSearchResult search(VirtualFile virtualFile) {
        String phpNameSpaceOfPhpFile = this.getPhpNamespace(virtualFile);

        String composerFilePath = this.searchComposerFile(virtualFile);

        if (composerFilePath != null && phpNameSpaceOfPhpFile != null) {
            ComposerJsonParser parser = new ComposerJsonParser();
            ComposerPSR4Entry composerPSR4Entry = parser.findNameSpaceFolder(composerFilePath, phpNameSpaceOfPhpFile);
            String relativeModuleFolder = composerPSR4Entry.relativeFolder();
            relativeModuleFolder = relativeModuleFolder.replace("." + File.separator, "");
            String composerFolderPath = composerFilePath.replace("composer.json", "");
            String absoluteModuleFolder = composerFolderPath + relativeModuleFolder;

            return new ModuleFolderSearchResult(
                    composerPSR4Entry,
                    phpNameSpaceOfPhpFile,
                    absoluteModuleFolder
            );
        }

        return null;
    }

    public String searchComposerFile(VirtualFile virtualFile) {
        ComposerJsonSearcher fileSearcher = new ComposerJsonSearcher();
        return fileSearcher.search(virtualFile);
    }

    public String getPhpNamespace(VirtualFile virtualFile) {
        PhpNamespaceParser phpNamespaceParser = new PhpNamespaceParser();
        return phpNamespaceParser.getNamespace(virtualFile);
    }
}
