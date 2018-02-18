/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import javafx.scene.canvas.Canvas;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Point;
import java.awt.image.RenderedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author helana ayman
 */
public class DrawingAreaController implements Initializable {

    private Path path;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colorPicker.setValue(Color.GREEN);
        colorPicker1.setValue(Color.RED);

    }
    @FXML
    private Button btCircle;
    @FXML
    private Button btEllipse;
    @FXML
    private Button btRectangle;
    @FXML
    private Button btSquare;
    @FXML
    private Button btLine;
    @FXML
    private HBox hbox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ColorPicker colorPicker1;
    @FXML
    private Canvas canvas;

    private String type = "";
    private ArrayList<Shape> shapes = new ArrayList<Shape>();
    private int x = 0;
    private int y = 0;
    private Shape shapeSelected = null;
    private Stack<Shape> oldS = new Stack<Shape>();
    private Stack<Shape> newS = new Stack<Shape>();

    private Stage stage;

    public void inti(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void setType(ActionEvent event) {
        type = event.getSource().toString();
    }

    @FXML
    private void start(MouseEvent event) {
        x = (int) event.getX();
        y = (int) event.getY();
    }

    @FXML
    private void end(MouseEvent event) {
        Point newPosition = new Point((int) event.getX(), (int) event.getY());
        Color fillColor = colorPicker.getValue();
        Color StrokeColor = colorPicker1.getValue();

        if (type.contains("Rectangle")) {
            int length = (int) Math.abs(newPosition.y - y);
            int width = (int) Math.abs(newPosition.x - x);
            if ((newPosition.x - x) < 0) {
                x -= width;
            }
            if ((newPosition.y - y) < 0) {
                y -= length;
            }
            Rectangle r = new Rectangle(x, y, width, length, StrokeColor, fillColor);
            shapes.add(r);
            oldS.push(r);
        } else if (type.contains("Square")) {
            int differenceX = Math.abs(newPosition.x - x);
            int differenceY = Math.abs(newPosition.y - y);
            double diagonal = Math.sqrt(Math.pow(differenceX, 2) + Math.pow(differenceY, 2));
            double side = diagonal / Math.sqrt(2);
            if ((newPosition.x - x) < 0) {
                x -= side;
            }
            if ((newPosition.y - y) < 0) {
                y -= side;
            }
            Square s = new Square(x, y, (int) side, StrokeColor, fillColor);
            shapes.add(s);
            oldS.push(s);
        } else if (type.contains("Line")) {
            Line l = new Line(x, y, newPosition.x, newPosition.y, StrokeColor, fillColor);
            shapes.add(l);
            oldS.add(l);

        } else if (type.contains("Circle")) {
            int differenceX = Math.abs(newPosition.x - x);
            int differenceY = Math.abs(newPosition.y - y);
            double radius = Math.sqrt(Math.pow(differenceX, 2) + Math.pow(differenceY, 2));
            if ((newPosition.x - x) < 0) {
                x -= radius;
            }
            if ((newPosition.y - y) < 0) {
                y -= radius;
            }
            Circle c = new Circle(x, y, (int) radius, StrokeColor, fillColor);
            shapes.add(c);
            oldS.push(c);
        } else if (type.contains("Ellipse")) {
            int length = (int) Math.abs(newPosition.y - y);
            int width = (int) Math.abs(newPosition.x - x);
            if ((newPosition.x - x) < 0) {
                x -= width;
            }
            if ((newPosition.y - y) < 0) {
                y -= length;
            }
            Ellipse e = new Ellipse(x, y, width, length, StrokeColor, fillColor);
            shapes.add(e);
            oldS.push(e);
        } else if (type.contains("Select")) {
            for (Shape s : shapes) {

                if (s.contains(new Point(x, y))) {
                    shapeSelected = s;
                    final GraphicsContext g = canvas.getGraphicsContext2D();
                     g.setStroke(Color.BLACK);
                    g.strokeRoundRect((s.x)-11,(s.y)-11, (s.getWidth())+20,( s.getHeight())+20, 5, 5);
                   
                }
            }
        } else if (type.contains("Move")) {
            if (shapeSelected != null) {
                if (!((x - shapeSelected.getPositionX() < 0) || ((y - shapeSelected.getPositionY()) < 0))) {
                    int distX = Math.abs(x - shapeSelected.getPositionX());
                    int distY = Math.abs(y - shapeSelected.getPositionY());
                    shapeSelected.setPositionX(newPosition.x - distX);
                    shapeSelected.setPositionY(newPosition.y - distY);
                }

            }
        } else if (type.contains("Resize")) {
            shapeSelected.resize(newPosition);
        } else if (type.contains("Free Hand")) {
            FreeHand();
        }
        if (!type.contains("Free Hand")&!type.contains("Select")) {
            reDraw();
        }
        //reDraw();
    }

    @FXML
    private void FreeHand() {
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.setStroke(colorPicker1.getValue());
                        graphicsContext.stroke();
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.setStroke(colorPicker1.getValue());
                        graphicsContext.stroke();
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {

                    }

                });

    }

    private void initDraw(GraphicsContext gc) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        //gc.fill();
        gc.strokeRect(
                0, //x of the upper left corner
                0, //y of the upper left corner
                canvasWidth, //width of the rectangle
                canvasHeight);  //height of the rectangle
        gc.setFill(colorPicker.getValue());
        gc.setStroke(colorPicker1.getValue());
        gc.setLineWidth(1);

    }

    @FXML
    private void Clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void reDraw() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Shape s : shapes) {
            s.draw(canvas.getGraphicsContext2D());
        }
    }

    public GraphicsContext getGC() {
        return canvas.getGraphicsContext2D();
    }

    @FXML
    private void delete(ActionEvent event) {
        if (shapeSelected != null) {
            shapes.remove(shapeSelected);
            shapeSelected = null;
            reDraw();
        }
    }

    @FXML
    private void changeColor(ActionEvent event) {
        if (shapeSelected != null) {
            shapeSelected.setFillColor(colorPicker.getValue());
            shapeSelected.setStrokeColor(colorPicker1.getValue());
            reDraw();
        }
    }

    @FXML
    private void Undo(ActionEvent event) {
        if (!oldS.isEmpty()) {
            Shape s = oldS.pop();
            newS.push(s);
            shapes.remove(s);
            reDraw();
        }
    }

    @FXML
    private void Redo(ActionEvent event) {
        if (!newS.isEmpty()) {
            Shape s = newS.pop();
            oldS.push(s);
            shapes.add(s);
            reDraw();
        }

    }

    @FXML
    public void openfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        try {
            if (selectedFile != null) {
                shapes.clear();
                FileInputStream fis = new FileInputStream(selectedFile);
                XMLDecoder xdec = new XMLDecoder(fis);
                SimpleShape s = (SimpleShape) xdec.readObject();
                if (s.getType().equals("Rectangle")) {
                    Rectangle rec = new Rectangle(s.getX(), s.getY(), s.getW(), s.getH(), s.getStrokeColor(), s.getFillColor());
                    shapes.add(rec);
                } else if (s.getType().equals("Square")) {
                    Square sq = new Square(s.getX(), s.getY(), s.getW(), s.getStrokeColor(), s.getFillColor());
                    shapes.add(sq);
                } else if (s.getType().equals("Ellipse")) {
                    Ellipse el = new Ellipse(s.getX(), s.getY(), s.getW(), s.getH(), s.getStrokeColor(), s.getFillColor());
                    shapes.add(el);
                } else if (s.getType().equals("Circle")) {
                    Circle cir = new Circle(s.getX(), s.getY(), s.getW(), s.getStrokeColor(), s.getFillColor());
                    shapes.add(cir);
                }
                reDraw();
            }
        } catch (Exception e) {

        }
    }

    @FXML
    public void SaveAsFileXml() throws IOException {
        FileOutputStream fOutput = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file As XML");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".xml", "*.xml"));
        fileChooser.setInitialFileName("New Project");
        File savedFile = fileChooser.showSaveDialog(null);
        if (savedFile != null) {
            try {
                fOutput = new FileOutputStream(savedFile);
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }

        XMLEncoder xMLEncoder = new XMLEncoder(new BufferedOutputStream(fOutput));

        for (int i = 0; i < shapes.size(); i++) {
            SimpleShape s = new SimpleShape();
            s.setX(shapes.get(i).getPositionX());
            s.setY(shapes.get(i).getPositionY());
            s.setW(shapes.get(i).getWidth());
            s.setH(shapes.get(i).getHeight());
            s.setFillColor(shapes.get(i).getFillColor());
            s.setStrokeColor(shapes.get(i).getStrokeColor());
            if (shapes.get(i) instanceof Rectangle) {
                s.setType("Rectangle");
            } else if (shapes.get(i) instanceof Square) {
                s.setType("Square");
            } else if (shapes.get(i) instanceof Ellipse) {
                s.setType("Ellipse");
            } else if (shapes.get(i) instanceof Circle) {
                s.setType("Circle");
            }
            xMLEncoder.writeObject(s);
        }
        xMLEncoder.close();
    }

    public void SaveAsFilePng() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file As PNG");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".png", "*.png"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);

            } catch (IOException ex) {
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Object getPreferredSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
