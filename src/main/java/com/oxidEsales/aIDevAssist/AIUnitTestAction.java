package com.oxidEsales.aIDevAssist;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.oxidEsales.aIDevAssist.File.ModuleFolderSearcher;
import com.oxidEsales.aIDevAssist.File.TestClassCreator;
import com.oxidEsales.aIDevAssist.Model.ModuleFolderSearchResult;
import org.jetbrains.annotations.NotNull;

public class AIUnitTestAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        VirtualFile virtualFileFromContextMenu = anActionEvent.getData(CommonDataKeys.VIRTUAL_FILE);

        ModuleFolderSearcher moduleFolderSearcher = new ModuleFolderSearcher();
        ModuleFolderSearchResult moduleFolderSearchResult = moduleFolderSearcher.search(virtualFileFromContextMenu);

        TestClassCreator testFolderCreator = new TestClassCreator();
        testFolderCreator.create(moduleFolderSearchResult);
    }
}