package com.moshuk.aistorm;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;

public class AIOptimizeAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        Document document = editor.getDocument();
        // Work off of the primary caret to get the selection info
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();

        String selectedText = editor.getSelectionModel().getSelectedText();

        ChatGPTClient client = new ChatGPTClient();

        String query = "Fix this code. Provide only code: \n\n" + selectedText;
        String response = "";
        try {
            response = client.query(query);
            response = response.replaceFirst("```php\\s*", "").replaceFirst("\\s*```$", "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        final String finalResponse = response;
        MyToolWindowFactory myToolWindowFactory = MyToolWindowFactory.getInstance();

        if(myToolWindowFactory.AItoolWindow == null)
        {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

            ToolWindow myToolWindow = toolWindowManager.getToolWindow("AiStorm");
            myToolWindowFactory.createToolWindowContent(project, myToolWindow);
            myToolWindowFactory.showToolWindow();

        }

        myToolWindowFactory.appendToOutput(">>> " + query, Color.BLUE);
        myToolWindowFactory.appendToOutput("Processed input: " + response, Color.BLACK);

        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, finalResponse)
        );

        primaryCaret.removeSelection();
    }
}