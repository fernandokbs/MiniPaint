 
package minipaint;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.shape.*;
import javafx.scene.layout.Pane;
import javafx.scene.input.*;


import java.util.Stack;
import javafx.scene.control.MenuItem;

import com.Archivo;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;

/**
 *
 * @author Luis Fernando
 */
public class FXMLDocumentController implements Initializable {
    
    
    private Button boton;
    
    @FXML
    private ColorPicker color;
    
    @FXML
    Pane board;
    
    
    private String Color;
    private Shape shape;
    private Stack<Shape> Redo =  new Stack<>();
    private Stack<Shape> Undo = new Stack<>();
    private Archivo archivo;
    
    boolean newShape = false;
    
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    
    @FXML
    private void create(ActionEvent e)
    {           
        String value = ((Button)e.getSource()).getText();
        
        if(value.equals("circle"))
        {    
            shape = new Circle(100.0f,200.0f,20.0f,color.getValue());
            envents(shape);
            click(shape);
            
            Redo.push(shape);
                  
        }
        else if(value.equals("rectangle"))
        {
            shape = new Rectangle(100,300,200,40);
            shape.setFill(color.getValue());
            envents(shape);
            click(shape);
            
            Redo.push(shape);
              
        }
        else if(value.equals("eclipse"))
        {
             shape = new Ellipse(100,200,80,50);
             shape.setFill(color.getValue());
             envents(shape);
             click(shape);
             Redo.push(shape);
             
        }
        else if(value.equals("pacman"))
        { 
            
            Arc arc = new Arc(50.0f,50.0f,25.0f,25.0f,45.0f,270.0f);   
            arc.setType(ArcType.ROUND);         
            shape  = arc;
            shape.setFill(color.getValue());
            envents(shape);
            click(shape);
            Redo.push(shape);
        }
        
        board.getChildren().add(shape);
         
    }
      
    @FXML
    private void R(ActionEvent e)
    {
         String value = ((MenuItem)e.getSource()).getText();
         
         if (value.equals("Undo")) {
             
             if (!Redo.isEmpty()) {
                 Shape shape = Redo.pop();
                 shape.setVisible(false);
                 Undo.push(shape);
             }
             else
             {
                  System.out.println("pila vacia");
             }
                      
        }
         else if(value.equals("Redo"))
         {
              if (!Undo.isEmpty()) {
                  Shape shape  = Undo.pop();
                  shape.setVisible(true);
                  Redo.push(shape);
                 
             }
         }
         
    }
    
    public void file(ActionEvent e)
    {
          String value = ((MenuItem)e.getSource()).getText();
          archivo = new Archivo(board);
        
          if (value.equals("Save")) {
              archivo.save();
            
          }
          else if (value.equals("Open"))
          {
              archivo.open();
          }
          
          else if (value.equals("New")) {
              
              board.getChildren().clear();
              board.setBackground(Background.EMPTY);
            
        }
    
    }
   
     
    
    public void envents(Shape shape)
    {       
          shape.setCursor(Cursor.MOVE);
          shape.setOnMousePressed(t->{
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();
                 orgTranslateX = ((Shape)(t.getSource())).getTranslateX();
                 orgTranslateY = ((Shape)(t.getSource())).getTranslateY();
            });
            
            shape.setOnMouseDragged(t->{
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    ((Shape)(t.getSource())).setTranslateX(newTranslateX);
                    ((Shape)(t.getSource())).setTranslateY(newTranslateY);
                          
            });
    }
    
    public void click(Shape shape)
    {
        shape.setOnMouseClicked(e->{
             if(e.getButton().equals(MouseButton.PRIMARY)){
                if(e.getClickCount() == 2){
                    shape.setCursor(Cursor.NE_RESIZE);
                }
                else if (e.getClickCount()==1) {
                     shape.setCursor(Cursor.MOVE);
                 }
              }
             else if (e.getButton().equals(MouseButton.SECONDARY)) {
            
                  shape.setFill(color.getValue());
            }
            
            
        });      
          shape.setOnScroll(e->{
            
            double zoomFactor = 1.05;
                double deltaY = e.getDeltaY();
                if (deltaY < 0){
                  zoomFactor = 2.0 - zoomFactor;
                }
                shape.setScaleX(shape.getScaleX() * zoomFactor);
                shape.setScaleY(shape.getScaleY() * zoomFactor);
               
        });     
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            
    }    
    
}
