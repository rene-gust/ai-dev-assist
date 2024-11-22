package com.oxidEsales.aIDevAssist;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.oxidEsales.aIDevAssist.File.ModuleFolderSearcher;
import com.oxidEsales.aIDevAssist.File.TestClassCreator;
import com.oxidEsales.aIDevAssist.Model.ModuleFolderSearchResult;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;


public class AIUnitTestAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        VirtualFile virtualFileFromContextMenu = anActionEvent.getData(CommonDataKeys.VIRTUAL_FILE);

        ModuleFolderSearcher moduleFolderSearcher = new ModuleFolderSearcher();
        ModuleFolderSearchResult moduleFolderSearchResult = moduleFolderSearcher.search(virtualFileFromContextMenu);

        String phpFileContent = "";
        try {
            assert virtualFileFromContextMenu != null;
            phpFileContent = Files.readString(Path.of(virtualFileFromContextMenu.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        ChatGPTClient chatGPTClient = new ChatGPTClient();
        try {
            response = chatGPTClient.query(
                "You are an experienced PHP developer specializing in unit testing. Please write comprehensive PHPUnit tests for the following class: \n\n"
                + phpFileContent
                + "Please include:\n"
                + "- Tests for all public methods\n"
                + "- Edge cases and boundary conditions\n"
                + "- Both valid and invalid inputs"
            );
            response = response.replaceFirst("```php\\s*", "").replaceFirst("\\s*```$", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String testClassContent = response;

        TestClassCreator testClassCreator = new TestClassCreator();
        testClassCreator.create(moduleFolderSearchResult, virtualFileFromContextMenu, testClassContent);
    }
}