package minimalistic.cloud;

import swingTools.interfaces.Disable;
import swingTools.interfaces.Opacity;

import javax.swing.*;

public abstract class CloudPanel extends JComponent implements Cloneable, Disable, Opacity {
    public abstract Object clone();
}
