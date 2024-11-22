package com.oxidEsales.aIDevAssist.PhpParser;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.util.regex.*;

public class PhpNamespaceParser {
    public String getNamespace(VirtualFile virtualFile) {
        try {
            File file = new File(virtualFile.getPath());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String namespace = null;

            // Regex to find the namespace
            Pattern pattern = Pattern.compile("^namespace\\s+([a-zA-Z0-9_\\\\]+);");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    namespace = matcher.group(1);
                    break;
                }
            }

            reader.close();

            return namespace;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

