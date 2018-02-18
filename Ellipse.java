package paint;

import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;



public class Ellipse extends Shape {
    
    
    public Ellipse(int centerX , int centerY, int radiusX, int radiusY, Color StrokeColor, Color FillColor) {
        super(centerX, centerY, radiusX, radiusY ,StrokeColor, FillColor);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(FillColor);
        gc.setStroke(StrokeColor);
        gc.setLineWidth(5);
        gc.fillOval(x, y, w, h);
        gc.strokeOval(x, y, w, h);
    }
    
    @Override
    public boolean contains(Point p)
    {
        if((p.x >= x) && ((p.x - x) <= w))
            if((p.y >= y) && ((p.y - y) <= h))
                return true;
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