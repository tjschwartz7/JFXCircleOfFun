
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javafx.animation.*;
import javafx.util.Duration;

import java.util.Random;

public class JPLMain extends Application
{
	
	Circle circle;
	double cVel;
	double msTime;
	int mode;
	int angle;
	
	boolean start;
	
	//Start gets called immediately after the UI is launched
	//It is only called one time, and is typically used to setup your UI 
	//with objects like buttons and shapes.
	//Primary stage is passed as an argument:
	//When you add stuff to it, it'll be displayed on the UI
	@Override
	public void start(Stage primaryStage) 
	{
		mode = 0;
		angle = 0;
		start = false;
		final StackPane root = new StackPane();
		int numMillisPerUpdate = 10;
		msTime = 0;
		Scene scene = new Scene(root, 300, 250);
	    root.setStyle("-fx-background-color: GREY;");
	    
	    Rectangle bounds = new Rectangle(-1000, -1000, 1000, 1000);
	    bounds.setFill(Color.BLACK);
	    root.getChildren().add(bounds);
	    
	    circle = new Circle();
	    circle.setTranslateX(0);
    	circle.setTranslateY(0);
    	circle.setRadius(15);
    	circle.setFill(Color.BLUE);
	    root.getChildren().add(circle);
	   
	    
	    
	    Button toggle_play = new Button();
	    toggle_play.toFront();
	    toggle_play.setTranslateY(-100);
	    toggle_play.setText("Start");
	    root.getChildren().add(toggle_play);
	    
	    
	    toggle_play.setOnAction(new EventHandler<ActionEvent>() 
	    {
	        @Override
	        public void handle(ActionEvent event) 
	        {
	        	if(!start)
	        	{
	        		start = true;
	        		toggle_play.setText("Pause");
	        	}
	        	else
	        	{
	        		start = false;	
	        		toggle_play.setText("Resume");
	        	}
	        }
	    });
	    
	    
	    
	    Button toggle_mode = new Button();
	    toggle_mode.toFront();
	    toggle_mode.setTranslateX(100);
	    toggle_mode.setTranslateY(-100);
	    toggle_mode.setText("Physics");
	    root.getChildren().add(toggle_mode);
	    
	    toggle_mode.setOnAction(new EventHandler<ActionEvent>() 
	    {
	        @Override
	        public void handle(ActionEvent event) 
	        {
	        	//Allow modes 0-1
	        	mode = (mode + 1) % 2;
	        	switch(mode)
	        	{
	        	//Physics case
	        	case 0:
	        		toggle_mode.setText("Physics");
	        		msTime = 0;
	        		break;
	        	case 1:
	        		toggle_mode.setText("Pong");
	        		break;
	        	case 2:
	        		toggle_mode.setText("NA");
	        		break;
	        	}
	        	
	        }
	    });
	    
	    Timeline update = new Timeline(
                new KeyFrame(Duration.millis(numMillisPerUpdate), 
                new EventHandler<ActionEvent>() {
		
		   @Override
		   public void handle(ActionEvent event) 
		   {
			 
			   if(start)
			   {
				   if(mode == 0)
				   {
					   //x = 1/2at^2+vt+x
					   msTime += numMillisPerUpdate;
					   double sTime = msTime / 1000;
					   double yi = circle.getTranslateY();
					   double yf = yi + cVel * sTime + .5*9.81*sTime*sTime;
					   cVel = cVel*sTime+9.81*sTime;
					   if(Math.abs(yf)+circle.getRadius() < 500)
					   {
						   circle.setTranslateY(yf);
					   }   
				   }
				   else if(mode == 1)
				   {
					   //Enter polar mode
					   double xi = circle.getTranslateX();
					   double yi = circle.getTranslateY();
					   
					   double xf = 5*Math.cos(angle) + xi;
					   double yf = 5*Math.sin(angle) + yi;
					   
					   if(Math.abs(xf)+circle.getRadius() < 500 && Math.abs(yf)+circle.getRadius() < 500)
					   {
						   circle.setTranslateX(xf);
						   circle.setTranslateY(yf);
					   }
					   //Try again 'from a new angle'
					   else angle += 45;
					   
				   }
			   }
		       
		   }
		}));
	    
	    update.setCycleCount(Timeline.INDEFINITE);
	    update.play();

	    primaryStage.setTitle("JPL");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	public static void main(String[] args) 
	{
		//Launch the application
			//Call start first
			//Start will initialize the stage:
			//Layout (inputs and appearance)
			//Start will also set 'lambdas' 
				//These are threads that run after start finishes; they handle
				//logic like 'do this if a button is pressed'
			//After start exits, the main process will essentially be a watchdog
			//process that watches all the threads you created in 'start'.
	    Application.launch(args);
	}
}


