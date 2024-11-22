package com.oxidEsales.aIDevAssist.ComposerJson;

import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class ComposerJsonSearcher {
    public String search(VirtualFile virtualFile) {
        if (virtualFile != null) {
            VirtualFile parent = virtualFile.getParent();
            while (parent != null) {
                VirtualFile composerJson = parent.findChild("composer.json");
                if (composerJson != null) {
                    String composerJsonPath = null;
                    try {
                        URI uri = new URI(composerJson.toString());
                        File file = new File(uri);
                        composerJsonPath = file.getAbsolutePath();
                    } catch (URISyntaxException e) {}

                    return composerJsonPath;
                }
                parent = parent.getParent();
            }
        }

        return null;
    }
}
