package com.oxidEsales.aIDevAssist;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

public class AIReFormat120MaxAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        assert editor != null;
        Document document = editor.getDocument();
        MyToolWindowFactory myToolWindowFactory = MyToolWindowFactory.getInstance();
        // Work off of the primary caret to get the selection info
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();

        String selectedText = editor.getSelectionModel().getSelectedText();

        ChatGPTClient client = new ChatGPTClient();

        String query = "Reformat this code, so its max 120 chars long each line. Provide only code: \n\n" + selectedText;
        String response = "";

        myToolWindowFactory.showToolWindow(project);
        myToolWindowFactory.setLoadingVisible(true);

        try {
            response = client.query(query);
            response = response.replaceFirst("```php\\s*", "").replaceFirst("\\s*```$", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String finalResponse = response;

        myToolWindowFactory.showToolWindow(project);

        myToolWindowFactory.appendToOutput(">>> " + query, JBColor.BLUE);
        myToolWindowFactory.appendToOutput("Processed input: " + response, JBColor.BLACK);

        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, finalResponse)
        );

        primaryCaret.removeSelection();

        myToolWindowFactory.setLoadingVisible(false);
    }
}
