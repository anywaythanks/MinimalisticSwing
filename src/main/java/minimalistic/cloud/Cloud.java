package minimalistic.cloud;

import swingTools.interfaces.ForbidOpacity;

import javax.swing.*;

public class Cloud extends JComponent implements ForbidOpacity {
    private CloudPanel panel;
    private CloudFrame frame;
    private double opacity;

    public Cloud(CloudPanel panel, CloudFrame frame) {
        setPanel(panel);
        setFrame(frame);
        setLayout(null);
        setOpaque(false);
    }

    public void setPanel(CloudPanel panel) {
        if (this.panel != null) {
            panel.setBounds(this.panel.getBounds());
        }

        Object clonePanel = panel.clone();
        if (clonePanel instanceof CloudPanel) {
            this.panel = (CloudPanel) clonePanel;
        }
        add(this.panel);
    }

    public void setFrame(CloudFrame frame) {
        if (this.frame != null) {
            frame.setBounds(this.frame.getBounds());
        }

        Object cloneFrame = frame.clone();
        if (cloneFrame instanceof CloudFrame) {
            this.frame = (CloudFrame) cloneFrame;
        }
        add(frame);
    }

    public void setDisable(boolean b) {
        panel.setDisable(b);
    }

    @Override
    public boolean isDisable() {
        return false;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        frame.setBounds(0, 0, width, height);
        panel.setBounds(frame.getPanelZone());
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
        panel.setOpacity(opacity);
        frame.setOpacity(opacity);
    }

    public double getOpacity() {
        return opacity;
    }
}
