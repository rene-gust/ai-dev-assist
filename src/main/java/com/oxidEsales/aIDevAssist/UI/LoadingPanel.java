package com.oxidEsales.aIDevAssist.UI;

import com.oxidEsales.aIDevAssist.MyToolWindowFactory;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoadingPanel extends JPanel {

    public LoadingPanel() {
        super(new FlowLayout(FlowLayout.CENTER));
        this.addLabel();
        this.addLoadingIcon();
    }

    private void addLabel() {
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(loadingLabel);
    }

    private void addLoadingIcon() {
        URL url = MyToolWindowFactory.class.getResource(
                "/loading-animation.gif"
        );
        JLabel spinner;
        if (url != null) {
            spinner = new JLabel(new ImageIcon(url));
            spinner.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(spinner);
        }
    }
}
