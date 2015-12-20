package se.datadosen.component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * A ControlPanel is a JPanel which has "RiverLayout" as default layout manager.
 * This is the preferred panel for making user interfaces in a simple way.
 * 
 * @author David Ekholm
 * @version 1.0
 * @see RiverLayout
 */
public class ControlPanel extends JPanel implements
        se.datadosen.util.JComponentHolder
{
    
    /**
     * Create a plain ControlPanel
     */
    public ControlPanel()
    {
        super(new RiverLayout());
    }
    
    /**
     * Create a control panel framed with a titled border
     * 
     * @param title is the frame's title
     */
    public ControlPanel(String title)
    {
        this();
        setTitle(title);
    }
    
    /**
     * Sets a title surrounded by an etched border.
     * 
     * @param title is the text to display for the title
     */
    public void setTitle(String title)
    {
        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), title));
    }
}
