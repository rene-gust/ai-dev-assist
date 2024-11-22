package com.oxidEsales.aIDevAssist.ComposerJson;
import com.oxidEsales.aIDevAssist.Model.ComposerPSR4Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class ComposerJsonParser {
    public ComposerPSR4Entry findNameSpaceFolder(String composerJsonFilePath, String phpNameSpaceOfPhpFile) {
        try (
                InputStream fis = new FileInputStream(composerJsonFilePath);
                JsonReader reader = Json.createReader(fis)
        ) {

            JsonObject composerJson = reader.readObject();

            JsonObject autoload = composerJson.getJsonObject("autoload");
            if (autoload != null) {
                JsonObject psr4 = autoload.getJsonObject("psr-4");
                if (psr4 != null) {
                    for (Map.Entry<String, ?> entry : psr4.entrySet()) {
                        String namespace = entry.getKey();
                        String path = psr4.getString(namespace);

                        if (phpNameSpaceOfPhpFile.contains(namespace)) {
                            return new ComposerPSR4Entry(
                                    namespace,
                                    path
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        return null;
    }
}
