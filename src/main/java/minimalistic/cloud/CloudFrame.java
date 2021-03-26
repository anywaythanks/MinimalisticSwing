package minimalistic.cloud;

import swingTools.interfaces.Opacity;

import javax.swing.*;
import java.awt.*;

public abstract class CloudFrame extends JComponent implements Cloneable, Opacity {
    public abstract Rectangle getPanelZone();

    public abstract Object clone();
}
