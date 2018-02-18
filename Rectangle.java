package paint;

import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Shape {

    public Rectangle(int x, int y, int width, int length, Color StrokeColor, Color FillColor) {
        super(x, y, width, length, StrokeColor, FillColor);
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(FillColor);
        gc.setStroke(StrokeColor);
        gc.setLineWidth(5);
       // gc.stroke();
        gc.strokeRect(x, y, w, h);
        gc.fillRect(x, y, w, h);
    }

    @Override
    public boolean contains(Point p) {
        if ((p.x >= x) && ((p.x - x) <= w)) {
            if ((p.y >= y) && ((p.y - y) <= h)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void resize(Point newPosition) {
        h = (int) Math.abs(newPosition.y - y);
        w = (int) Math.abs(newPosition.x - x);
        if ((newPosition.x - x) < 0) {
                x = x - w;
        }
        if ((newPosition.y - y) < 0) {
            y = y - h;
        }
    }

}
