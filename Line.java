package paint;

import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;



public class Line extends Shape {
    
    public Line(int startpointX , int startPointY, int endpointX , int endPointY, Color StrokeColor, Color FillColor) {
        super(startpointX , startPointY ,endpointX , endPointY , StrokeColor, FillColor);
    }


    @Override
    public void resize(Point newPos) {
        w = newPos.x;
        h = newPos.y;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(5);
        gc.setStroke(StrokeColor);
        gc.strokeLine(x, y, w, h);
        gc.lineTo(x, y);
    }

    @Override
    public boolean contains(Point p) {
        if(p.x > x && p.x < w)
            if(p.y > y && p.y < h)
                return true;
        return false;
    }

  
}
