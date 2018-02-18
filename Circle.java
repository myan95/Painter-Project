package paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;




public class Circle extends Ellipse {

    public Circle(int centerX , int centerY, int radius, Color StrokeColor, Color FillColor) {
        super(centerX , centerY , radius, radius, StrokeColor, FillColor);
    }
     public void draw(GraphicsContext gc) {
        gc.setFill(FillColor);
        gc.setStroke(StrokeColor);
        gc.setLineWidth(5);
        gc.fillOval(x, y, w, w);
        gc.strokeOval(x, y, w, w);
    }  
}
