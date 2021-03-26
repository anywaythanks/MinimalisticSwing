package minimalistic.plaf;


import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import static swingTools.Factory.factoryGraphics2D;
import static swingTools.FontSizeCalculator.*;
import static stringTools.Calculator.multiply;

/**
 * A minimalistic user interface for a standard button in Java Swing.
 *
 * @author anywaythanks
 * @version 0.1
 */
public class MinimalisticButtonUI extends BasicButtonUI {
    private final static MinimalisticButtonUI MINIMALISTIC_BUTTON_UI = new MinimalisticButtonUI();
    private double arcWeight = 0, arcHeight = 0;
    private float moveAlongYWhenPressed = 1, multiplierFont = 0.7F;
    private String textForFontSize = "";

    public static ComponentUI createUI(JComponent c) {
        return MINIMALISTIC_BUTTON_UI;
    }

    @Override
    public void installUI(JComponent component) {
        super.installUI(component);
    }

    @Override
    public void uninstallUI(JComponent component) {
        super.uninstallUI(component);
    }

    private void paintBackground(Graphics2D g2d, AbstractButton button) {
        //TODO: Изменить метод brighter()
        g2d.setColor(button.getModel().isRollover() ? button.getBackground().brighter() : button.getBackground());
        g2d.fill(new RoundRectangle2D.Double(0, 0, button.getWidth(), button.getHeight(), arcWeight, arcHeight));
    }

    private void paintText(Graphics2D g2d, AbstractButton button, float moveY) {
        g2d.setColor(button.getForeground());
        setMaxSizeFontContainer(g2d, button.getWidth(), button.getHeight(), textForFontSize.length() > 0 ? textForFontSize : multiply("x", 10));
        reMultiplierFont(g2d, multiplierFont);
        g2d.drawString(button.getText(),
                calculateStringMiddleXInContainer(0, g2d, button.getText(), button.getWidth()),
                calculateStringMiddleYInContainer(0, g2d, button.getHeight()) + moveY);
    }

    public double getArcWeight() {
        return arcWeight;
    }

    public void setArcWeight(double arcWeight) {
        this.arcWeight = arcWeight;
    }

    public double getArcHeight() {
        return arcHeight;
    }

    public void setArcHeight(double arcHeight) {
        this.arcHeight = arcHeight;
    }

    public void setTextForFontSize(String textForFontSize) {
        this.textForFontSize = textForFontSize;
    }

    public void setMoveAlongYWhenPressed(float moveAlongYWhenPressed) {
        this.moveAlongYWhenPressed = moveAlongYWhenPressed;
    }

    public void setMultiplierFont(float multiplierFont) {
        this.multiplierFont = multiplierFont;
    }

    @Override
    public void paint(Graphics g, JComponent component) {
        Graphics2D g2d = factoryGraphics2D(g);
        AbstractButton button = (AbstractButton) component;

        paintBackground(g2d, button);
        paintText(g2d, button, button.getModel().isPressed() ? moveAlongYWhenPressed : 0);
    }
}
