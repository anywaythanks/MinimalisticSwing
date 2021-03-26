package minimalistic.cloud.frames;

import containers.BinaryContainer;
import minimalistic.cloud.CloudFrame;
import swingTools.ColorTools;


import java.awt.*;
import java.awt.geom.*;

import static swingTools.Factory.factoryGraphics2D;
import static swingTools.ColorTools.setOpacityColor;

public class CloudRoundRectangleFrame extends CloudFrame {
    public enum Side {
        LEFT, UP, RIGHT, DOWN, NULL
    }

    private static class Triangle {
        double length, downOrRight, upOrLeft;
        Point2D coordinate;

        public Triangle() {
            this(0, 0, 0, new Point(0, 0));
        }

        public Triangle(double length, double downOrRight, double upOrLeft, Point2D coordinate) {
            this.length = length;
            this.downOrRight = downOrRight;
            this.upOrLeft = upOrLeft;
            this.coordinate = coordinate;
        }

        @Override
        public String toString() {
            return "Triangle{" +
                    "length=" + length +
                    ", downOrRight=" + downOrRight +
                    ", upOrLeft=" + upOrLeft +
                    ", coordinate=" + coordinate +
                    '}';
        }
    }

    private final BinaryContainer<Dimension2D> arc = new BinaryContainer<>(new Dimension(), new Dimension());
    private final BinaryContainer<Dimension2D> separator = new BinaryContainer<>(new Dimension(), new Dimension());
    private final BinaryContainer<Triangle> triangle = new BinaryContainer<>(new Triangle(), new Triangle());
    private final BinaryContainer<Color> colorFrame;
    private Color userBackground = new Color(0, 0, 0, 0);
    private double opacity = 1;
    private float strokeLine = 1;
    private Side side = Side.LEFT;

    public CloudRoundRectangleFrame() {
        colorFrame = new BinaryContainer<>(Color.BLACK, Color.BLACK);
        setOpaque(false);
    }

    public void setSide(Side side) {
        if (side != null) {
            this.side = side;
        }
        recalculateAll();
        repaint();
    }

    public void setTriangle(Triangle triangle) {
        this.triangle.getUserT().length = triangle.length;
        this.triangle.getUserT().downOrRight = triangle.downOrRight;
        this.triangle.getUserT().upOrLeft = triangle.upOrLeft;
        this.triangle.getUserT().coordinate = triangle.coordinate;
        recalculateTriangle();
    }

    public void setTriangleLength(double length) {
        triangle.getUserT().length = length;
        recalculateTriangle();
    }

    public void setTriangleDownOrRight(double downOrRight) {
        triangle.getUserT().downOrRight = downOrRight;
        recalculateTriangle();
    }

    public void setTriangleUpOrLeft(double upOrLeft) {
        triangle.getUserT().upOrLeft = upOrLeft;
        recalculateTriangle();
    }

    public void setTriangleCoordinate(Point2D coordinate) {
        triangle.getUserT().coordinate = coordinate;
        recalculateTriangle();
    }

    public void setSeparator(double width, double height) {
        separator.getUserT().setSize(width, height);
        recalculateSeparator();
        repaint();
    }

    public void setStrokeLine(float strokeLine) {
        this.strokeLine = strokeLine;
        recalculateAll();
        repaint();
    }

    public void setColorFrame(Color colorFrame) {
        this.colorFrame.setUserT(colorFrame);
        this.colorFrame.setAbsoluteT(ColorTools.setOpacityColor(colorFrame, getOpacity()));
    }

    public void setSeparator(Dimension2D separator) {
        setSeparator(separator.getWidth(), separator.getHeight());
    }

    public void setArc(double arcWidth, double arcHeight) {
        arc.getUserT().setSize(arcWidth, arcHeight);
        recalculateArc();
        repaint();
    }

    public void setArc(Dimension2D arc) {
        setArc(arc.getWidth(), arc.getHeight());
    }

    @Override
    public void setOpacity(double opacity) {
        this.opacity = opacity;
        setBackground(userBackground);
        setColorFrame(colorFrame.getUserT());
    }

    @Override
    public void setBackground(Color color) {
        userBackground = color;
        super.setBackground(setOpacityColor(color, getOpacity()));
    }

    @Override
    public double getOpacity() {
        return opacity;
    }

    @Override
    public void setBounds(int i, int i1, int i2, int i3) {
        super.setBounds(i, i1, i2, i3);
        recalculateAll();
    }

    public void setBounds(int x, int y, int width, int height, double arcWidth, double arcHeight) {
        setArc(arcWidth, arcHeight);
        setBounds(x, y, width, height);

    }

    public Rectangle getPanelZone() {
        switch (side) {
            case LEFT:
                return new Rectangle((int) (triangle.getAbsoluteT().length + separator.getAbsoluteT().getWidth()),
                        (int) (separator.getAbsoluteT().getHeight()),
                        (int) (getWidth() - (triangle.getAbsoluteT().length + separator.getAbsoluteT().getWidth() * 2)),
                        (int) (getHeight() - separator.getAbsoluteT().getHeight() * 2));
            case RIGHT:
                return new Rectangle((int) (separator.getAbsoluteT().getWidth()),
                        (int) (separator.getAbsoluteT().getHeight()),
                        (int) (getWidth() - (triangle.getAbsoluteT().length + separator.getAbsoluteT().getWidth() * 2)),
                        (int) (getHeight() - separator.getAbsoluteT().getHeight() * 2));
            case UP:
                return new Rectangle((int) (separator.getAbsoluteT().getWidth()),
                        (int) (triangle.getAbsoluteT().length + separator.getAbsoluteT().getHeight()),
                        (int) (getWidth() - (separator.getAbsoluteT().getWidth() * 2)),
                        (int) (getHeight() - triangle.getAbsoluteT().length - separator.getAbsoluteT().getHeight() * 2));
            case DOWN:
                return new Rectangle((int) (separator.getAbsoluteT().getWidth()),
                        (int) (separator.getAbsoluteT().getHeight()),
                        (int) (getWidth() - (separator.getAbsoluteT().getWidth() * 2)),
                        (int) (getHeight() - triangle.getAbsoluteT().length - separator.getAbsoluteT().getHeight() * 2));
            default:
                return new Rectangle((int) (separator.getAbsoluteT().getWidth()),
                        (int) (separator.getAbsoluteT().getHeight()),
                        (int) (getWidth() - (separator.getAbsoluteT().getWidth() * 2)),
                        (int) (getHeight() - separator.getAbsoluteT().getHeight() * 2));
        }
    }


    private void recalculateArc() {
        arc.getAbsoluteT().setSize(
                Math.min(getWidth() - strokeLine * 2, Math.max(arc.getUserT().getWidth(), strokeLine)),
                Math.min(getHeight() - strokeLine * 2, Math.max(arc.getUserT().getHeight(), strokeLine))
        );
    }

    private void recalculateSeparator() {
        separator.getAbsoluteT().setSize(
                Math.min(getWidth() / 2.0, Math.max(separator.getUserT().getWidth(), strokeLine)),
                Math.min(getHeight() / 2.0, Math.max(separator.getUserT().getHeight(), strokeLine))
        );
    }

    private void recalculateTriangle() {
        switch (side) {
            case DOWN:
            case UP:
                triangle.getAbsoluteT().coordinate.setLocation(
                        Math.max(arc.getAbsoluteT().getWidth() / 2 + strokeLine / 2, Math.min(getWidth() - arc.getAbsoluteT().getWidth() / 2 - strokeLine / 1.5, triangle.getUserT().coordinate.getX())),
                        triangle.getUserT().coordinate.getY()
                );
                triangle.getAbsoluteT().upOrLeft = Math.max(0, Math.min(triangle.getAbsoluteT().coordinate.getX() - arc.getAbsoluteT().getWidth() / 2 - strokeLine / 2, triangle.getUserT().upOrLeft));
                triangle.getAbsoluteT().downOrRight = Math.max(0, Math.min(getWidth() - arc.getAbsoluteT().getWidth() / 2 - strokeLine / 2 - triangle.getAbsoluteT().coordinate.getX(), triangle.getUserT().downOrRight));
                triangle.getAbsoluteT().length = Math.max(0, Math.min(getHeight() - arc.getAbsoluteT().getHeight(), triangle.getUserT().length));
                break;
            case RIGHT:
            case LEFT:
                triangle.getAbsoluteT().coordinate.setLocation(
                        triangle.getUserT().coordinate.getX(),
                        Math.max(arc.getAbsoluteT().getHeight() / 2 + strokeLine / 2, Math.min(getHeight() - arc.getAbsoluteT().getHeight() / 2 - strokeLine / 2, triangle.getUserT().coordinate.getY()))
                );
                triangle.getAbsoluteT().upOrLeft = Math.max(0, Math.min(triangle.getAbsoluteT().coordinate.getY() - arc.getAbsoluteT().getHeight() / 2 - strokeLine / 2, triangle.getUserT().upOrLeft));
                triangle.getAbsoluteT().downOrRight = Math.max(0, Math.min(getHeight() - arc.getAbsoluteT().getHeight() / 2 - strokeLine / 2 - triangle.getAbsoluteT().coordinate.getY(), triangle.getUserT().downOrRight));
                triangle.getAbsoluteT().length = Math.max(0, Math.min(getWidth() - arc.getAbsoluteT().getWidth(), triangle.getUserT().length));
                break;
        }
    }

    private void recalculateAll() {
        recalculateArc();
        recalculateSeparator();
        recalculateTriangle();
    }

    private void drawBackground(Graphics2D g2D) {
        g2D.setColor(getBackground());
        g2D.fill(new RoundRectangle2D.Double(
                strokeLine + (side == Side.LEFT ? triangle.getAbsoluteT().length : 0),
                strokeLine + (side == Side.UP ? triangle.getAbsoluteT().length : 0),
                getWidth() - strokeLine * 2 - (side == Side.LEFT || side == Side.RIGHT ? triangle.getAbsoluteT().length : 0),
                getHeight() - strokeLine * 2 - (side == Side.UP || side == Side.DOWN ? triangle.getAbsoluteT().length : 0),
                Math.max(0, arc.getAbsoluteT().getWidth() - strokeLine * 2),
                Math.max(0, arc.getAbsoluteT().getHeight() - strokeLine * 2)));

        Path2D path2D = new Path2D.Double();
        switch (side) {
            case LEFT:
                path2D.moveTo(strokeLine, triangle.getAbsoluteT().coordinate.getY());
                path2D.lineTo(triangle.getAbsoluteT().length + strokeLine, triangle.getAbsoluteT().coordinate.getY() - triangle.getAbsoluteT().upOrLeft);
                path2D.lineTo(triangle.getAbsoluteT().length + strokeLine, triangle.getAbsoluteT().coordinate.getY() + triangle.getAbsoluteT().downOrRight);
                g2D.fill(path2D);
                break;
            case DOWN:
                path2D.moveTo(triangle.getAbsoluteT().coordinate.getX() - triangle.getAbsoluteT().upOrLeft, getHeight() - triangle.getAbsoluteT().length - strokeLine);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX(), getHeight() - strokeLine);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX() + triangle.getAbsoluteT().downOrRight, getHeight() - triangle.getAbsoluteT().length - strokeLine);
                g2D.fill(path2D);
                break;
            case RIGHT:
                path2D.moveTo(getWidth() - triangle.getAbsoluteT().length - strokeLine, triangle.getAbsoluteT().coordinate.getY() - triangle.getAbsoluteT().upOrLeft);
                path2D.lineTo(getWidth() - strokeLine, triangle.getAbsoluteT().coordinate.getY());
                path2D.lineTo(getWidth() - triangle.getAbsoluteT().length - strokeLine, triangle.getAbsoluteT().coordinate.getY() + triangle.getAbsoluteT().downOrRight);
                g2D.fill(path2D);
                break;
            case UP:
                path2D.moveTo(triangle.getAbsoluteT().coordinate.getX() - triangle.getAbsoluteT().upOrLeft, triangle.getAbsoluteT().length + strokeLine);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX(), strokeLine);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX() + triangle.getAbsoluteT().downOrRight, triangle.getAbsoluteT().length + strokeLine);
                g2D.fill(path2D);
                break;
        }

    }

    private void drawArcFrame(Graphics2D g2D) {
        g2D.draw(new Arc2D.Double(
                getWidth() - arc.getAbsoluteT().getWidth() + strokeLine / 2 - (side == Side.RIGHT ? triangle.getAbsoluteT().length : 0),
                strokeLine / 2 + (side == Side.UP ? triangle.getAbsoluteT().length : 0),
                arc.getAbsoluteT().getWidth() - strokeLine, arc.getAbsoluteT().getHeight() - strokeLine, 0, 90, Arc2D.OPEN));
        g2D.draw(new Arc2D.Double(
                strokeLine / 2 + (side == Side.LEFT ? triangle.getAbsoluteT().length : 0),
                strokeLine / 2 + (side == Side.UP ? triangle.getAbsoluteT().length : 0),
                arc.getAbsoluteT().getWidth() - strokeLine, arc.getAbsoluteT().getHeight() - strokeLine, 90, 90, Arc2D.OPEN));
        g2D.draw(new Arc2D.Double(
                strokeLine / 2 + (side == Side.LEFT ? triangle.getAbsoluteT().length : 0),
                getHeight() - arc.getAbsoluteT().getHeight() + strokeLine / 2 - (side == Side.DOWN ? triangle.getAbsoluteT().length : 0),
                arc.getAbsoluteT().getWidth() - strokeLine, arc.getAbsoluteT().getHeight() - strokeLine, 180, 90, Arc2D.OPEN));
        g2D.draw(new Arc2D.Double(
                getWidth() - arc.getAbsoluteT().getWidth() + strokeLine / 2 - (side == Side.RIGHT ? triangle.getAbsoluteT().length : 0),
                getHeight() - arc.getAbsoluteT().getHeight() + strokeLine / 2 - (side == Side.DOWN ? triangle.getAbsoluteT().length : 0),
                arc.getAbsoluteT().getWidth() - strokeLine, arc.getAbsoluteT().getHeight() - strokeLine, 270, 90, Arc2D.OPEN));
    }

    private void drawLeftWall(Graphics2D g2D) {
        double y1 = 0, y2 = 0;
        switch (side) {
            case UP:
                y1 = triangle.getAbsoluteT().length;
                break;
            case DOWN:
                y2 = -triangle.getAbsoluteT().length;
        }
        g2D.draw(new Line2D.Double(strokeLine / 2, arc.getAbsoluteT().getHeight() / 2 + y1, strokeLine / 2, getHeight() - arc.getAbsoluteT().getHeight() / 2 + y2));
    }

    private void drawRightWall(Graphics2D g2D) {
        double y1 = 0, y2 = 0;
        switch (side) {
            case UP:
                y1 = triangle.getAbsoluteT().length;
                break;
            case DOWN:
                y2 = -triangle.getAbsoluteT().length;
        }
        g2D.draw(new Line2D.Double(getWidth() - strokeLine / 2, arc.getAbsoluteT().getHeight() / 2 + y1, getWidth() - strokeLine / 2, getHeight() - arc.getAbsoluteT().getHeight() / 2 + y2));
    }

    private void drawUpWall(Graphics2D g2D) {
        double x1 = 0, x2 = 0;
        switch (side) {
            case LEFT:
                x1 = triangle.getAbsoluteT().length;
                break;
            case RIGHT:
                x2 = -triangle.getAbsoluteT().length;
        }
        g2D.draw(new Line2D.Double(arc.getAbsoluteT().getWidth() / 2 + x1, strokeLine / 2, getWidth() - arc.getAbsoluteT().getWidth() / 2 + x2, strokeLine / 2));
    }

    private void drawDownWall(Graphics2D g2D) {
        double x1 = 0, x2 = 0;
        switch (side) {
            case LEFT:
                x1 = triangle.getAbsoluteT().length;
                break;
            case RIGHT:
                x2 = -triangle.getAbsoluteT().length;
        }
        g2D.draw(new Line2D.Double(arc.getAbsoluteT().getWidth() / 2 + x1, getHeight() - strokeLine / 2, getWidth() - arc.getAbsoluteT().getWidth() / 2 + x2, getHeight() - strokeLine / 2));
    }

    private void drawWallFrame(Graphics2D g2D) {
        Path2D path2D = new Path2D.Double();
        switch (side) {
            case LEFT:
                drawUpWall(g2D);
                drawRightWall(g2D);
                drawDownWall(g2D);

                path2D.moveTo(triangle.getAbsoluteT().length + strokeLine / 2, arc.getAbsoluteT().getHeight() / 2);
                path2D.lineTo(triangle.getAbsoluteT().length + strokeLine / 2, triangle.getAbsoluteT().coordinate.getY() - triangle.getAbsoluteT().upOrLeft);
                path2D.lineTo(strokeLine / 2, triangle.getAbsoluteT().coordinate.getY());
                path2D.lineTo(triangle.getAbsoluteT().length + strokeLine / 2, triangle.getAbsoluteT().coordinate.getY() + triangle.getAbsoluteT().downOrRight);
                path2D.lineTo(triangle.getAbsoluteT().length + strokeLine / 2, getHeight() - arc.getAbsoluteT().getHeight() / 2);
                g2D.draw(path2D);
                break;
            case DOWN:
                drawLeftWall(g2D);
                drawUpWall(g2D);
                drawRightWall(g2D);

                path2D.moveTo(arc.getAbsoluteT().getWidth() / 2, getHeight() - triangle.getAbsoluteT().length - strokeLine / 2);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX() - triangle.getAbsoluteT().upOrLeft, getHeight() - triangle.getAbsoluteT().length - strokeLine / 2);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX(), getHeight() - strokeLine / 2);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX() + triangle.getAbsoluteT().downOrRight, getHeight() - triangle.getAbsoluteT().length - strokeLine / 2);
                path2D.lineTo(getWidth() - arc.getAbsoluteT().getWidth() / 2, getHeight() - triangle.getAbsoluteT().length - strokeLine / 2);
                g2D.draw(path2D);
                break;
            case RIGHT:
                drawDownWall(g2D);
                drawLeftWall(g2D);
                drawUpWall(g2D);

                path2D.moveTo(getWidth() - triangle.getAbsoluteT().length - strokeLine / 2, arc.getAbsoluteT().getHeight() / 2);
                path2D.lineTo(getWidth() - triangle.getAbsoluteT().length - strokeLine / 2, triangle.getAbsoluteT().coordinate.getY() - triangle.getAbsoluteT().upOrLeft);
                path2D.lineTo(getWidth() - strokeLine / 2, triangle.getAbsoluteT().coordinate.getY());
                path2D.lineTo(getWidth() - triangle.getAbsoluteT().length - strokeLine / 2, triangle.getAbsoluteT().coordinate.getY() + triangle.getAbsoluteT().downOrRight);
                path2D.lineTo(getWidth() - triangle.getAbsoluteT().length - strokeLine / 2, getHeight() - arc.getAbsoluteT().getHeight() / 2);
                g2D.draw(path2D);
                break;
            case UP:
                drawRightWall(g2D);
                drawDownWall(g2D);
                drawLeftWall(g2D);

                path2D.moveTo(arc.getAbsoluteT().getWidth() / 2, triangle.getAbsoluteT().length + strokeLine / 2);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX() - triangle.getAbsoluteT().upOrLeft, triangle.getAbsoluteT().length + strokeLine / 2);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX(), strokeLine / 2);
                path2D.lineTo(triangle.getAbsoluteT().coordinate.getX() + triangle.getAbsoluteT().downOrRight, triangle.getAbsoluteT().length + strokeLine / 2);
                path2D.lineTo(getWidth() - arc.getAbsoluteT().getWidth() / 2, triangle.getAbsoluteT().length + strokeLine / 2);
                g2D.draw(path2D);
                break;
            case NULL:
                drawRightWall(g2D);
                drawDownWall(g2D);
                drawLeftWall(g2D);
                drawUpWall(g2D);
                break;
        }
    }

    private void drawFrame(Graphics2D g2D) {
        g2D.setColor(colorFrame.getAbsoluteT());
        g2D.setStroke(new BasicStroke(strokeLine, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        drawArcFrame(g2D);
        drawWallFrame(g2D);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = factoryGraphics2D(g);
        super.paintComponent(g);
        drawBackground(g2D);
        drawFrame(g2D);
    }

    @Override
    public Object clone() {
        CloudRoundRectangleFrame cloneFrame = new CloudRoundRectangleFrame();
        cloneFrame.setArc(arc.getUserT());
        cloneFrame.setSeparator(separator.getUserT());
        cloneFrame.setTriangle(triangle.getUserT());
        cloneFrame.setColorFrame(colorFrame.getUserT());
        cloneFrame.setBackground(userBackground);
        cloneFrame.setOpacity(opacity);
        cloneFrame.setStrokeLine(strokeLine);
        cloneFrame.setSide(side);

        return cloneFrame;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CloudRoundRectangleFrame{");
        sb.append("arc=").append(arc);
        sb.append(", separator=").append(separator);
        sb.append(", triangle=").append(triangle);
        sb.append(", colorFrame=").append(colorFrame);
        sb.append(", userBackground=").append(userBackground);
        sb.append(", opacity=").append(opacity);
        sb.append(", strokeLine=").append(strokeLine);
        sb.append(", side=").append(side);
        sb.append('}');
        return sb.toString();
    }
}
