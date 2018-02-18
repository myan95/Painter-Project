package paint;

import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public abstract class Shape {

    protected int x;
    protected int y;
    protected int w;
    protected int h;
    protected Color StrokeColor;
    protected Color FillColor ;

    public Shape(int x, int y, int w, int h, Color StrokeColor, Color FillColor) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.StrokeColor = StrokeColor;
        this.FillColor = FillColor;
    }
    
    

    public Color getStrokeColor() {
        return StrokeColor;
    }

    public int getPositionX() {
        return x;
    }
    
    public int getPositionY(){
        return y;
    }
    
    public int getWidth(){
        return w;
    }
    
    public int getHeight(){
        return h;
    }

    public Color getFillColor() {
        return FillColor;
    }

    public void setStrokeColor(Color StrokeColor) {
        this.StrokeColor = StrokeColor;
    }

    public void setPositionX(int positionX) {
        this.x = positionX;
    }
    
    public void setPositionY(int positionY) {
        this.y = positionY;
    }
    
    public void setWidth(int width){
        this.w = width;
    }
    
    public void setHeight(int height){
        this.h = height;
    }

    public void setFillColor(Color FillColor) {
        this.FillColor = FillColor;
    }

    public abstract void draw(GraphicsContext gc);
    public abstract boolean contains(Point p);
    public abstract void resize(Point p);

}
