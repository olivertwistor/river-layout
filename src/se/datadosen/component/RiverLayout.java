package se.datadosen.component;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * <p>
 * RiverLayout makes it very simple to construct user interfaces as components
 * are laid out similar to how text is added to a word processor (Components
 * flow like a "river". RiverLayout is however much more powerful than
 * FlowLayout: Components added with the add() method generally gets laid out
 * horizontally, but one may add a string before the component being added to
 * specify "constraints" like this: add("br hfill", new
 * JTextField("Your name here"); The code above forces a "line break" and
 * extends the added component horizontally. Without the "hfill" constraint, the
 * component would take on its preferred size.
 * </p>
 * <p>
 * List of constraints:
 * <ul>
 * <li>br - Add a line break</li>
 * <li>p - Add a paragraph break</li>
 * <li>tab - Add a tab stop (handy for constructing forms with labels followed
 * by fields)</li>
 * <li>hfill - Extend component horizontally</li>
 * <li>vfill - Extent component vertically (currently only one allowed)</li>
 * <li>left - Align following components to the left (default)</li>
 * <li>center - Align following components horizontally centered</li>
 * <li>right - Align following components to the right</li>
 * <li>vtop - Align following components vertically top aligned</li>
 * <li>vcenter - Align following components vertically centered (default)</li>
 * </ul>
 * </p>
 * RiverLayout is LGPL licenced - use it freely in free and commercial programs
 *
 * @author David Ekholm
 * @version 1.1 (2005-05-23) -Bugfix: JScrollPanes were oversized (sized to
 *          their containing component) if the container containing the
 *          JScrollPane was resized.
 */
public class RiverLayout extends FlowLayout
{

    /**
     * Line break.
     */
    public static final String     LINE_BREAK      = "br";

    /**
     * Paragraph break.
     */
    public static final String     PARAGRAPH_BREAK = "p";

    /**
     * Tab stop.
     */
    public static final String     TAB_STOP        = "tab";

    /**
     * Horizontal fill.
     */
    public static final String     HFILL           = "hfill";

    /**
     * Vertical fill.
     */
    public static final String     VFILL           = "vfill";

    /**
     * Horizontal left alignment.
     */
    public static final String     LEFT            = "left";

    /**
     * Horizontal right alignment.
     */
    public static final String     RIGHT           = "right";

    /**
     * Horizontal center alignment.
     */
    public static final String     CENTER          = "center";

    /**
     * Vertical top alignment.
     */
    public static final String     VTOP            = "vtop";

    /**
     * Vertical center alignment.
     */
    public static final String     VCENTER         = "vcenter";

    private Map<Component, String> constraints;
    private String                 valign;
    private int                    hgap;
    private int                    vgap;
    private Insets                 extraInsets;
    private Insets                 totalInsets;

    /**
     * Creates a river layout object with horizontal gap between objects of 10
     * px and vertical gap of 5 px.
     */
    public RiverLayout()
    {
        this(10, 5);
    }

    /**
     * Creates a river layout object with a horizontal and a vertical gap
     * between objects.
     *
     * @param h_gap is the horizontal gap (in px) to use
     * @param v_gap is the vertical gap (in px) to use
     */
    public RiverLayout(int h_gap, int v_gap)
    {
        this.constraints = new HashMap<>();
        this.valign = VCENTER;
        this.hgap = h_gap;
        this.vgap = v_gap;
        this.setExtraInsets(new Insets(0, h_gap, h_gap, h_gap));
        this.totalInsets = new Insets(0, 0, 0, 0); // Dummy values; set by
        // getInsets()
    }

    /**
     * Gets the horizontal gap between components.
     */
    @Override
    public int getHgap()
    {
        return this.hgap;
    }

    /**
     * Sets the horizontal gap between components.
     */
    @Override
    public void setHgap(int h_gap)
    {
        this.hgap = h_gap;
    }

    /**
     * Gets the vertical gap between components.
     */
    @Override
    public int getVgap()
    {
        return this.vgap;
    }

    /**
     * Gets the extra insets.
     *
     * @return The extra insets.
     */
    public Insets getExtraInsets()
    {
        return this.extraInsets;
    }

    /**
     * Sets the extra insets.
     *
     * @param newExtraInsets is the new extra insets
     */
    public void setExtraInsets(Insets newExtraInsets)
    {
        this.extraInsets = newExtraInsets;
    }

    /**
     * Gets the insets for a given container.
     *
     * @param target is the container for which the insets are retrieved
     * @return The insets for the given container.
     */
    protected Insets getInsets(Container target)
    {
        Insets insets = target.getInsets();
        this.totalInsets.top = insets.top + this.extraInsets.top;
        this.totalInsets.left = insets.left + this.extraInsets.left;
        this.totalInsets.bottom = insets.bottom + this.extraInsets.bottom;
        this.totalInsets.right = insets.right + this.extraInsets.right;
        return this.totalInsets;
    }

    /**
     * Sets the vertical gap between components.
     */
    @Override
    public void setVgap(int vgap)
    {
        this.vgap = vgap;
    }

    /**
     * @param name the name of the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp)
    {
        this.constraints.put(comp, name);
    }

    /**
     * Removes the specified component from the layout. Not used by this class.
     *
     * @param comp the component to remove
     * @see java.awt.Container#removeAll
     */
    @Override
    public void removeLayoutComponent(Component comp)
    {
        this.constraints.remove(comp);
    }

    /**
     * Determines whether the given component is first in row, i.e. has any of
     * the following constraints: a line break or a paragraph break.
     *
     * @param comp is the component
     * @return True if the given component is first in row; false otherwise.
     */
    boolean isFirstInRow(Component comp)
    {
        String cons = this.constraints.get(comp);
        return cons != null
                && (cons.indexOf(RiverLayout.LINE_BREAK) != -1 || cons
                .indexOf(RiverLayout.PARAGRAPH_BREAK) != -1);
    }

    /**
     * Determines whether the given component has horizontal fill.
     *
     * @param comp is the component to check
     * @return True if the given component has horizontal fill; false otherwise.
     */
    boolean hasHfill(Component comp)
    {
        return this.hasConstraint(comp, RiverLayout.HFILL);
    }

    /**
     * Determines whether the given component has vertical fill.
     *
     * @param comp is the component to check
     * @return True if the given component has vertical fill; false otherwise.
     */
    boolean hasVfill(Component comp)
    {
        return this.hasConstraint(comp, RiverLayout.VFILL);
    }

    /**
     * Determines whether the given component has a given constraint.
     *
     * @param comp is the component
     * @param test is the constraint
     * @return True if the given component has a given constraint; false
     *         otherwise.
     */
    boolean hasConstraint(Component comp, String test)
    {
        String cons = this.constraints.get(comp);
        if (cons == null)
        {
            return false;
        }
        StringTokenizer tokens = new StringTokenizer(cons);
        while (tokens.hasMoreTokens())
        {
            if (tokens.nextToken().equals(test))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Figure out tab stop x-positions
     *
     * @param target is the container inside which the tab stops exist
     * @return A ruler object containing all tab stops that exist inside the
     *         given container.
     */
    protected Ruler calcTabs(Container target)
    {
        Ruler ruler = new Ruler();
        int nmembers = target.getComponentCount();

        int x = 0;
        int tabIndex = 0; // First tab stop
        for (int i = 0; i < nmembers; i++)
        {
            Component m = target.getComponent(i);
            // if (m.isVisible()) {
            if (this.isFirstInRow(m) || i == 0)
            {
                x = 0;
                tabIndex = 0;
            }
            else
            {
                x += this.hgap;
            }
            if (this.hasConstraint(m, TAB_STOP))
            {
                ruler.setTab(tabIndex, x); // Will only increase
                x = ruler.getTab(tabIndex++); // Jump forward if neccesary
            }
            Dimension d = m.getPreferredSize();
            x += d.width;
        }
        // }
        return ruler;
    }

    /**
     * Returns the preferred dimensions for this layout given the <i>visible</i>
     * components in the specified target container.
     *
     * @param target the component which needs to be laid out
     * @return the preferred dimensions to lay out the subcomponents of the
     *         specified container
     * @see Container
     * @see #minimumLayoutSize
     * @see java.awt.Container#getPreferredSize
     */
    @Override
    public Dimension preferredLayoutSize(Container target)
    {
        synchronized (target.getTreeLock())
        {
            Dimension dim = new Dimension(0, 0);
            Dimension rowDim = new Dimension(0, 0);
            int nmembers = target.getComponentCount();
            boolean firstVisibleComponent = true;
            int tabIndex = 0;
            Ruler ruler = this.calcTabs(target);

            for (int i = 0; i < nmembers; i++)
            {
                Component m = target.getComponent(i);
                // if (m.isVisible()) {
                if (this.isFirstInRow(m))
                {
                    tabIndex = 0;
                    dim.width = Math.max(dim.width, rowDim.width);
                    dim.height += rowDim.height + this.vgap;
                    if (this.hasConstraint(m, PARAGRAPH_BREAK))
                    {
                        dim.height += 2 * this.vgap;
                    }
                    rowDim = new Dimension(0, 0);
                }
                if (this.hasConstraint(m, TAB_STOP))
                {
                    rowDim.width = ruler.getTab(tabIndex++);
                }
                Dimension d = m.getPreferredSize();
                rowDim.height = Math.max(rowDim.height, d.height);
                if (firstVisibleComponent)
                {
                    firstVisibleComponent = false;
                }
                else
                {
                    rowDim.width += this.hgap;
                }
                rowDim.width += d.width;
                // }
            }
            dim.width = Math.max(dim.width, rowDim.width);
            dim.height += rowDim.height;

            Insets insets = this.getInsets(target);
            dim.width += insets.left + insets.right;// + hgap * 2;
            dim.height += insets.top + insets.bottom;// + vgap * 2;
            return dim;
        }
    }

    /**
     * Returns the minimum dimensions needed to layout the <i>visible</i>
     * components contained in the specified target container.
     *
     * @param target the component which needs to be laid out
     * @return the minimum dimensions to lay out the subcomponents of the
     *         specified container
     * @see #preferredLayoutSize
     * @see java.awt.Container
     * @see java.awt.Container#doLayout
     */
    @Override
    public Dimension minimumLayoutSize(Container target)
    {
        synchronized (target.getTreeLock())
        {
            Dimension dim = new Dimension(0, 0);
            Dimension rowDim = new Dimension(0, 0);
            int nmembers = target.getComponentCount();
            boolean firstVisibleComponent = true;
            int tabIndex = 0;
            Ruler ruler = this.calcTabs(target);

            for (int i = 0; i < nmembers; i++)
            {
                Component m = target.getComponent(i);
                // if (m.isVisible()) {
                if (this.isFirstInRow(m))
                {
                    tabIndex = 0;
                    dim.width = Math.max(dim.width, rowDim.width);
                    dim.height += rowDim.height + this.vgap;
                    if (this.hasConstraint(m, PARAGRAPH_BREAK))
                    {
                        dim.height += 2 * this.vgap;
                    }
                    rowDim = new Dimension(0, 0);
                }
                if (this.hasConstraint(m, TAB_STOP))
                {
                    rowDim.width = ruler.getTab(tabIndex++);
                }
                Dimension d = m.getMinimumSize();
                rowDim.height = Math.max(rowDim.height, d.height);
                if (firstVisibleComponent)
                {
                    firstVisibleComponent = false;
                }
                else
                {
                    rowDim.width += this.hgap;
                }
                rowDim.width += d.width;
                // }
            }
            dim.width = Math.max(dim.width, rowDim.width);
            dim.height += rowDim.height;

            Insets insets = this.getInsets(target);
            dim.width += insets.left + insets.right;// + hgap * 2;
            dim.height += insets.top + insets.bottom;// + vgap * 2;
            return dim;
        }
    }

    /**
     * Centers the elements in the specified row, if there is any slack.
     *
     * @param target the component which needs to be moved
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width dimensions
     * @param height the height dimensions
     * @param rowStart the beginning of the row
     * @param rowEnd the the ending of the row
     * @param ltr toggles whether the components has "left-to-right" reading
     *            direction (true) or "right-to-left" reading direction (false)
     * @param ruler is the ruler object that contains the tab stops to use when
     *            moving the components
     */
    protected void moveComponents(Container target, int x, int y, int width,
            int height, int rowStart, int rowEnd, boolean ltr, Ruler ruler)
    {
        synchronized (target.getTreeLock())
        {
            int _x = x;
            int _y = y;

            switch (this.getAlignment())
            {
                case FlowLayout.LEFT:
                    _x += ltr ? 0 : width;
                    break;
                case FlowLayout.CENTER:
                    _x += width / 2;
                    break;
                case FlowLayout.RIGHT:
                    _x += ltr ? width : 0;
                    break;
                case LEADING:
                    break;
                case TRAILING:
                    _x += width;
                    break;
                default:
                    break;
            }
            int tabIndex = 0;
            for (int i = rowStart; i < rowEnd; i++)
            {
                Component m = target.getComponent(i);
                // if (m.isVisible()) {
                if (this.hasConstraint(m, TAB_STOP))
                {
                    _x = this.getInsets(target).left + ruler.getTab(tabIndex++);
                }
                int dy = this.valign == VTOP ? 0 : (height - m.getHeight()) / 2;
                if (ltr)
                {
                    m.setLocation(_x, _y + dy);
                }
                else
                {
                    m.setLocation(target.getWidth() - _x - m.getWidth(), _y
                            + dy);
                }
                _x += m.getWidth() + this.hgap;
                // }
            }
        }
    }

    /**
     * Move a number of components inside a given container to a new location.
     *
     * @param target is the container in which the components lies
     * @param dx is the relative location change on the horizontal axis (x)
     * @param dy is the relative location change on the vertical axis (y)
     * @param rowStart is the start index of the components to move
     * @param rowEnd is the end index of the components to move
     */
    protected static void relMove(Container target, int dx, int dy,
            int rowStart, int rowEnd)
    {
        synchronized (target.getTreeLock())
        {
            for (int i = rowStart; i < rowEnd; i++)
            {
                Component m = target.getComponent(i);
                // if (m.isVisible()) {
                m.setLocation(m.getX() + dx, m.getY() + dy);
                // }
            }

        }
    }

    /**
     * Adjusts the alignment of the given component.
     *
     * @param m is the component to adjust
     */
    protected void adjustAlignment(Component m)
    {
        if (this.hasConstraint(m, RiverLayout.LEFT))
        {
            this.setAlignment(FlowLayout.LEFT);
        }
        else if (this.hasConstraint(m, RiverLayout.RIGHT))
        {
            this.setAlignment(FlowLayout.RIGHT);
        }
        else if (this.hasConstraint(m, RiverLayout.CENTER))
        {
            this.setAlignment(FlowLayout.CENTER);
        }
        if (this.hasConstraint(m, RiverLayout.VTOP))
        {
            this.valign = VTOP;
        }
        else if (this.hasConstraint(m, RiverLayout.VCENTER))
        {
            this.valign = VCENTER;
        }

    }

    /**
     * Lays out the container. This method lets each component take its
     * preferred size by reshaping the components in the target container in
     * order to satisfy the constraints of this <code>FlowLayout</code> object.
     *
     * @param target the specified component being laid out
     * @see Container
     * @see java.awt.Container#doLayout
     */
    @Override
    public void layoutContainer(Container target)
    {
        this.setAlignment(FlowLayout.LEFT);
        synchronized (target.getTreeLock())
        {
            Insets insets = this.getInsets(target);
            int maxwidth = target.getWidth() - (insets.left + insets.right);
            int maxheight = target.getHeight() - (insets.top + insets.bottom);

            int nmembers = target.getComponentCount();
            int x = 0, y = insets.top + this.vgap;
            int rowh = 0, start = 0, moveDownStart = 0;

            boolean ltr = target.getComponentOrientation().isLeftToRight();
            Component toHfill = null;
            Component toVfill = null;
            Ruler ruler = this.calcTabs(target);
            int tabIndex = 0;

            for (int i = 0; i < nmembers; i++)
            {
                Component m = target.getComponent(i);
                // if (m.isVisible()) {
                Dimension d = m.getPreferredSize();
                m.setSize(d.width, d.height);

                if (this.isFirstInRow(m))
                {
                    tabIndex = 0;
                }
                if (this.hasConstraint(m, TAB_STOP))
                {
                    x = ruler.getTab(tabIndex++);
                }
                if (!this.isFirstInRow(m))
                {
                    if (i > 0 && !this.hasConstraint(m, TAB_STOP))
                    {
                        x += this.hgap;
                    }
                    x += d.width;
                    rowh = Math.max(rowh, d.height);
                }
                else
                {
                    if (toVfill != null && moveDownStart == 0)
                    {
                        moveDownStart = i;
                    }
                    if (toHfill != null)
                    {
                        toHfill.setSize(toHfill.getWidth() + maxwidth - x,
                                toHfill.getHeight());
                        x = maxwidth;
                    }
                    this.moveComponents(target, insets.left, y, maxwidth - x,
                            rowh, start, i, ltr, ruler);
                    x = d.width;
                    y += this.vgap + rowh;
                    if (this.hasConstraint(m, PARAGRAPH_BREAK))
                    {
                        y += 2 * this.vgap;
                    }
                    rowh = d.height;
                    start = i;
                    toHfill = null;
                }
                // }
                if (this.hasHfill(m))
                {
                    toHfill = m;
                }
                if (this.hasVfill(m))
                {
                    toVfill = m;
                }
                this.adjustAlignment(m);
            }

            if (toVfill != null && moveDownStart == 0)
            {
                moveDownStart = nmembers;
            }
            if (toHfill != null)
            { // last component
                toHfill.setSize(toHfill.getWidth() + maxwidth - x,
                        toHfill.getHeight());
                x = maxwidth;
            }
            this.moveComponents(target, insets.left, y, maxwidth - x, rowh,
                    start, nmembers, ltr, ruler);
            int yslack = maxheight - (y + rowh);
            if (yslack != 0 && toVfill != null)
            {
                toVfill.setSize(toVfill.getWidth(),
                        yslack + toVfill.getHeight());
                relMove(target, 0, yslack, moveDownStart, nmembers);
            }
        }
    }

}

/**
 * This class describes tab stops.
 *
 * @author David Ekholm
 */
class Ruler
{
    private Vector<Integer> tabs = new Vector<>();

    /**
     * Adds a new tab stop to the ruler object.
     *
     * @param num is the position index in the ruler the tab stop will get
     * @param xpos is the horizontal position of the tab stop
     */
    public void setTab(int num, int xpos)
    {
        if (num >= this.tabs.size())
        {
            this.tabs.add(num, new Integer(xpos));
        }
        else
        {
            // Transpose all tabs from this tab stop and onwards
            int delta = xpos - this.getTab(num);
            if (delta > 0)
            {
                for (int i = num; i < this.tabs.size(); i++)
                {
                    this.tabs.set(i, new Integer(this.getTab(i) + delta));
                }
            }
        }
    }

    /**
     * Gets a tab stop with a specified index.
     *
     * @param num is the index of the tab stop to get
     * @return The horizontal position of the tab stop with the specified index
     */
    public int getTab(int num)
    {
        return this.tabs.get(num).intValue();
    }

    @Override
    public String toString()
    {
        StringBuffer ret = new StringBuffer(this.getClass().getName() + " {");
        for (int i = 0; i < this.tabs.size(); i++)
        {
            ret.append(this.tabs.get(i));
            if (i < this.tabs.size() - 1)
            {
                ret.append(',');
            }
        }
        ret.append('}');
        return ret.toString();
    }

    /**
     * Creates a ruler object and sets a couple of tab stops. Also, the ruler is
     * printed out to the standard output.
     *
     * @param args is not used
     */
    public static void main(String[] args)
    {
        Ruler r = new Ruler();
        r.setTab(0, 10);
        r.setTab(1, 20);
        r.setTab(2, 30);
        System.out.println(r);
        r.setTab(1, 25);
        System.out.println(r);
        System.out.println(r.getTab(0));
    }
}
