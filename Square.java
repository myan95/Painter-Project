package paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;



public class Square extends Rectangle {

    public Square(int x, int y, int side, Color StrokeColor, Color FillColor) {
        super(x, y, side, side, StrokeColor, FillColor);
    }
      public void draw(GraphicsContext gc) {
        gc.setFill(FillColor);
        gc.setStroke(StrokeColor);
        gc.setLineWidth(5);
       // gc.stroke();
        gc.strokeRect(x, y, w, w);
        gc.fillRect(x, y, w, w);
    }
}
