package com.oxidEsales.aIDevAssist;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.oxidEsales.aIDevAssist.UI.LoadingPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyToolWindowFactory implements ToolWindowFactory, ProjectManagerListener {

    private static MyToolWindowFactory instance;
    private JTextArea inputTextArea;
    private JTextPane outputTextPane;
    private JBScrollPane outputScrollPane;

    private ChatGPTClient chatClient;

    public ToolWindow AItoolWindow;

    private LoadingPanel loadingPanel = null;


    //  private MyToolWindowFactory() {}

    public static MyToolWindowFactory getInstance() {
        if (instance == null) {
            instance = new MyToolWindowFactory();
        }
        return instance;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, ToolWindow toolWindow) {
        // Create your tool window content here
        JPanel contentPanel = new JPanel(new BorderLayout());

        this.loadingPanel = new LoadingPanel();
        //this.loadingPanel.setVisible(false);
        contentPanel.add(loadingPanel, BorderLayout.NORTH);

        // Create a text area for user input
        inputTextArea = new JTextArea();
        inputTextArea.setEditable(true);

        // Create a text pane for output
        outputTextPane = new JTextPane();
        outputTextPane.setEditable(false);

        // Wrap the output text pane in a JBScrollPane
        outputScrollPane = new JBScrollPane(outputTextPane);

        this.chatClient = new ChatGPTClient();

        // Add a listener for user input
        inputTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Get the user input
                    String input = inputTextArea.getText().trim();

                    // Clear the input area
                    inputTextArea.setText("");

                    // Show the user input in the output area
                    appendToOutput(">>> " + input, JBColor.BLUE);

                    // Do something with the user input
                    processInput(input);
                }
            }
        });

        // Create a panel to hold the input text area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputTextArea, BorderLayout.CENTER);

        // Add the input and output panels to the content panel
        contentPanel.add(outputScrollPane, BorderLayout.CENTER);
        contentPanel.add(inputPanel, BorderLayout.SOUTH);

        Content toolWindowContent = toolWindow.getContentManager().getFactory().createContent(
                contentPanel,
                "",
                false
        );
        toolWindow.getContentManager().addContent(toolWindowContent);

        AItoolWindow = toolWindow;
        instance = this;
    }

    public void showToolWindow(Project project) {
        if (AItoolWindow != null) {
            AItoolWindow.show(null);
        } else {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

            ToolWindow toolWindow = toolWindowManager.getToolWindow("AIDevAssist");
            if (toolWindow != null) {
                this.createToolWindowContent(project, toolWindow);
                toolWindow.show();
            } else {
                System.err.println("Tool window AIDevAssist not exists");
            }
        }
    }

    public void processInput(String input) {

        String response = "";
        try {
            response = this.chatClient.query(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        appendToOutput("AI RESPONSE: " + response, JBColor.BLACK);
    }

    public void appendToOutput(String text, Color color) {
        // Append text to the output area
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        if(this.outputTextPane == null)
        {
            return;
        }
        int length = outputTextPane.getDocument().getLength();
        this.outputTextPane.setCaretPosition(length);
        this.outputTextPane.setCharacterAttributes(attributeSet, false);
  //      outputTextPane.replaceSelection(text + "\n");

        StyledDocument doc = outputTextPane.getStyledDocument();


        try
        {
            doc.insertString(doc.getLength(), "\n"+text+ "\n", null );
        }
        catch(Exception e) { e.printStackTrace(); }



        // Scroll to the bottom of the output area
        JScrollBar verticalScrollBar = outputScrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    /**
     * @TODO this panel is not shown again id once set invisible
     */
    public void setLoadingVisible(boolean visible) {
        if (this.loadingPanel != null) {
            this.loadingPanel.setVisible(visible);
        }
    }
}