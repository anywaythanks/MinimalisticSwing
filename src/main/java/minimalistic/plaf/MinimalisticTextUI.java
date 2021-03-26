package minimalistic.plaf;

import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static swingTools.Factory.factoryGraphics2D;

public class MinimalisticTextUI extends BasicTextUI implements FocusListener {
    private final PromptTextComponent promptTextComponent;
    private PropertyPrefix propertyPrefix;

    public enum PropertyPrefix {
        TextField, TextArea
    }

    public void setPropertyPrefix(PropertyPrefix propertyPrefix) {
        this.propertyPrefix = propertyPrefix;
    }

    @Override
    protected String getPropertyPrefix() {
        return propertyPrefix.toString();
    }

    public MinimalisticTextUI(PropertyPrefix propertyPrefix) {
        promptTextComponent = new PromptTextComponent("");
        promptTextComponent.addPromptPaint(this::repaint);
        setPropertyPrefix(propertyPrefix);
    }

    public MinimalisticTextUI(PropertyPrefix propertyPrefix, String prompt) {
        promptTextComponent = new PromptTextComponent(prompt);
        promptTextComponent.addPromptPaint(this::repaint);
        setPropertyPrefix(propertyPrefix);
    }

    public MinimalisticTextUI(PropertyPrefix propertyPrefix, String prompt, boolean hideOnFocus) {
        promptTextComponent = new PromptTextComponent(prompt, hideOnFocus);
        promptTextComponent.addPromptPaint(this::repaint);
        setPropertyPrefix(propertyPrefix);
    }

    public MinimalisticTextUI(PropertyPrefix propertyPrefix, String prompt, boolean hideOnFocus, Color colorPrompt) {
        promptTextComponent = new PromptTextComponent(prompt, hideOnFocus, colorPrompt);
        promptTextComponent.addPromptPaint(this::repaint);
        setPropertyPrefix(propertyPrefix);
    }

    public PromptTextComponent getPromptTextComponent() {
        return promptTextComponent;
    }

    private void repaint() {
        if (getComponent() != null) {
            getComponent().repaint();
        }
    }

    @Override
    protected void paintSafely(Graphics g) {
        Graphics2D g2d = factoryGraphics2D(g);
        JTextComponent component = getComponent();
        g2d.setFont(component.getFont());
        super.paintSafely(g2d);
        promptTextComponent.paint(g2d, component);
    }


    @Override
    protected void installListeners() {
        super.installListeners();
        getComponent().addFocusListener(this);
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        getComponent().removeFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        promptTextComponent.focusGained(focusEvent);
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        promptTextComponent.focusLost(focusEvent);
    }
}