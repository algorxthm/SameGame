import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.ArrayList;
import javafx.scene.control.ProgressBar;
import java.util.Collections;
import javafx.scene.control.Label;

/**
 * SameGame Class - Represents the game "SameGame".
 * @author Thomas Patton
 */
public class SameGame extends Application {
    /** The array of buttons  */
    private Button[][] buttonArray;
    /** The gridpane that the buttons are oriented on */
    private GridPane boardGridPane;
    /** The progress Bar that indicates how much of the game is completed */
    private static ProgressBar boardCompletion;
    /** The label at the top of the game */
    private Label gameText;
    /** The BorderPane that organizes the distinct elements */
    private BorderPane game;
    /** Static int that stores the board length, defaulted to 12 */
    private static int boardLength = 12;
    /** Static in that stores the board width, defaulted to 12 */
    private static int boardWidth = 12;
    /** Static int that stores the number of colors, defaulted to 3 */
    private static int numColors = 3;
    
    /**
     * Starts the application and initializes several key variables like the panes and the progress bar
     * @param primaryStage is the stage to be modified
     */
    @Override
    public void start(Stage primaryStage) {
        //Initialize the progress bar
        boardCompletion = new ProgressBar();
        //Initialize the borderpane
        game = new BorderPane();
        //Initialize the label
        gameText = new Label("Clicking a button removes all buttons in a cross. Try to have no buttons remaining!");
        //Create SameGame Instance
        SameGame s = new SameGame();                
        //Make the board
        boardGridPane = s.makeBoard();
        //Set the event handlers
        s.setEventHandlers();
        //Make the progress bar visible
        boardCompletion.setProgress(0);
        boardCompletion.setPrefSize(432, 30);
        game.setCenter(boardGridPane);
        game.setTop(gameText);
        game.setBottom(boardCompletion);
        Scene scene = new Scene(game);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SameGame! (Thomas Patton)");
        primaryStage.show();
    }

    /**
     * This method constructs the board as an array of buttons
     * @return GridPane with the formatted buttons
     */
    public GridPane makeBoard(){
        //Gridpane variable to organize the buttons on
        GridPane boardGridPane = new GridPane();
        this.setButtonArray(new Button[boardLength][boardWidth]);
        //Cycles through the buttonArray rows, precondition i < buttonArray.length
        for(int i = 0; i < this.getButtonArray().length; i++){
            //Cycles through the buttonArray columns, precondition i < buttonArray[0].length
            for(int j = 0; j < this.getButtonArray()[i].length; j++){
                this.getButtonArray()[i][j] = new Button("");
                this.getButtonArray()[i][j].setGraphic(this.createCircles());
                boardGridPane.add(this.getButtonArray()[i][j], j, i);
            }
        }
        return boardGridPane;
    }

    /**
     * This method sets the Event Handlers for all the buttons
     */
    public void setEventHandlers() {
        //Cycles through the buttonArray rows, precondition i < buttonArray.length
        for (int i = 0; i < this.getButtonArray().length; i++) {
            for (int j = 0; j < this.getButtonArray()[i].length; j++) {
                //Cycles through the buttonArray columns, precondition i < buttonArray[0].length
                this.getButtonArray()[i][j].setOnAction(new BasicClickHandler());
            }
        }
    }

    /**
     * BasicClickHandler Class deals with handling clicks on the various buttons by giving them to the handle method
     */
    private class BasicClickHandler implements EventHandler<ActionEvent> {
        //The clicked button
        Button clickedButton;
        //Row index of clicked button
        int clickedButtonRow;
        //Column index of clicked Button
        int clickedButtonColumn;
        //Fill of clicked Button
        Paint clickedButtonFill;
        //Stores left counter
        int leftCount;
        //Stores right counter
        int rightCount;
        //Stores top counter
        int topCount;
        //Stores bottom counter
        int bottomCount;
        //Stores the Buttons to be deleted
        ArrayList<Button> crossButtons;
        //Stores the Row coordinates of the buttons to be deleted
        ArrayList<Integer> crossButtonsRow;
        //Stores the column coordinates of the buttons to be deleted
        ArrayList<Integer> crossButtonsColumn;
        
        /**
         * This method handles all of the button clicks
         * @param actionEvent is the actionEvent derived from the button click
         */
        @Override
        public void handle(ActionEvent actionEvent){
            //The clicked button
            clickedButton = (Button) actionEvent.getSource();
            //Row index of clicked button
            clickedButtonRow = boardGridPane.getRowIndex(clickedButton);
            //Column index of clicked Button
            clickedButtonColumn = boardGridPane.getColumnIndex(clickedButton);
            //Fill of clicked Button
            clickedButtonFill = ((Circle) SameGame.this.getButtonArray()[clickedButtonRow][clickedButtonColumn].getGraphic()).getFill();
            crossButtons = new ArrayList<Button>();
            crossButtonsRow = new ArrayList<Integer>();
            crossButtonsColumn = new ArrayList<Integer>();
            leftCount = 0;
            rightCount = 0;
            topCount = 0;
            bottomCount = 0;
            /** Checks the buttons above the clicked one, loop precondition is that the button must be of the same fill */
            for(int j = clickedButtonRow - 1; j > -1 && ((Circle) SameGame.this.getButtonArray()[j][clickedButtonColumn].getGraphic()).getFill() == clickedButtonFill; j--) {
                topCount = topCount + 1;
                crossButtons.add(SameGame.this.getButtonArray()[j][clickedButtonColumn]);
                crossButtonsRow.add(j);
                crossButtonsColumn.add(clickedButtonColumn);
            }
            //Since the top buttons are added from bottom up, this fixes so them so that they're in the right order for top -> down
            Collections.reverse(crossButtonsRow);
            Collections.reverse(crossButtonsColumn);
            /** Checks the buttons to the left of the clicked one, loop precondition is that the button must be of the same fill */
            for(int j = clickedButtonColumn - 1; j > -1 && ((Circle) SameGame.this.getButtonArray()[clickedButtonRow][j].getGraphic()).getFill() == clickedButtonFill; j--) {
                leftCount = leftCount + 1;
                crossButtons.add(SameGame.this.getButtonArray()[clickedButtonRow][j]);
                crossButtonsRow.add(clickedButtonRow);
                crossButtonsColumn.add(j);
            }
            /** Checks the buttons to the right of the clicked one, loop precondition is that the button must be of the same fill */
            for(int j = clickedButtonColumn + 1; j < SameGame.this.getButtonArray()[0].length && ((Circle) SameGame.this.getButtonArray()[clickedButtonRow][j].getGraphic()).getFill() == clickedButtonFill; j++) {
                rightCount = rightCount + 1;
                crossButtons.add(SameGame.this.getButtonArray()[clickedButtonRow][j]);
                crossButtonsRow.add(clickedButtonRow);
                crossButtonsColumn.add(j);
            }
            //Adding the middle (clicked) button so the buttons are added in a top -> down order
            crossButtons.add(SameGame.this.getButtonArray()[clickedButtonRow][clickedButtonColumn]);
            crossButtonsRow.add(clickedButtonRow);
            crossButtonsColumn.add(clickedButtonColumn);
            /** Checks the buttons below the clicked one, loop precondition is that the button must be of the same fill */
            for(int j = clickedButtonRow + 1; j < SameGame.this.getButtonArray().length && ((Circle) SameGame.this.getButtonArray()[j][clickedButtonColumn].getGraphic()).getFill() == clickedButtonFill; j++) {
                bottomCount = bottomCount + 1;
                crossButtons.add(SameGame.this.getButtonArray()[j][clickedButtonColumn]);
                crossButtonsRow.add(j);
                crossButtonsColumn.add(clickedButtonColumn);
            }
            //Delete the buttons
            this.deleteButtons();
            //Delete the gray columns
            SameGame.this.deleteGrayColumns();
            //Update the progressbar
            SameGame.this.updateProgressBar();
        }
        /** 
         * This method deletes the buttons
         * NOTE: This method is part of a nested class because it relies on many variables of the class and thus
         * does not appear on JavaDOC.
         */ 
        public void deleteButtons(){
            /** Delete and shift all checked Buttons */
            int i = 0;
            if(crossButtons.toArray().length > 1) {
                //For each button in the same fill buttons
                for (Button b : crossButtons) {
                    //Shifts each of the buttons down one from top down order, precondition is that z >= 0
                    for (int z = crossButtonsRow.get(i); z >= 0; z--) {
                        if (z != 0)
                            ((Circle) SameGame.this.getButtonArray()[z][crossButtonsColumn.get(i)].getGraphic()).setFill(((Circle) SameGame.this.getButtonArray()[z - 1][crossButtonsColumn.get(i)].getGraphic()).getFill());
                        else {
                            ((Circle) SameGame.this.getButtonArray()[z][crossButtonsColumn.get(i)].getGraphic()).setFill(Color.LIGHTGRAY);
                        }
                    }
                    i = i + 1;
                }
            }
            /** This deletion assists in deletion that only occurs upwards */
            //If there are other clicked buttons
            if(topCount > 0 || bottomCount > 0 || leftCount > 0 || rightCount > 0) {
                //If the original button location still has the same color as the start
                if (((Circle) SameGame.this.getButtonArray()[clickedButtonRow][clickedButtonColumn].getGraphic()).getFill() == clickedButtonFill) {
                    for (int z = clickedButtonRow; z >= 0; z--) {
                        if (z != 0)
                            ((Circle) SameGame.this.getButtonArray()[z][clickedButtonColumn].getGraphic()).setFill(((Circle) SameGame.this.getButtonArray()[z - 1][clickedButtonColumn].getGraphic()).getFill());
                        else {
                            ((Circle) SameGame.this.getButtonArray()[z][clickedButtonColumn].getGraphic()).setFill(Color.LIGHTGRAY);
                        }
                    }
                }
            }
        }
    }

    /**
     * This method creates the colored circles which will be placed on the buttons
     * @return Circle equal to the randomly generated circle produced by the method
     */
    public Circle createCircles(){
        //random circle to be modified
        Circle circle = new Circle(10);
        Color[] colorNames = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE, Color.ORANGE, Color.INDIGO, Color.LAVENDER, Color.AQUAMARINE, Color.MINTCREAM, Color.MEDIUMAQUAMARINE, Color.BISQUE,};
        Color[] colors = new Color[numColors];
        //Gets the new array of colors, user must have entered a number higher than 1
        for(int i = 0; i < numColors; i++){
            colors[i] = colorNames[i];
        }
        Color colorChoice = colors[(int)(Math.random() * colors.length)];
        circle.setFill(colorChoice);
        return circle;
    }
    
    /**
    * This method deletes the empty gray columns
    */ 
    public void deleteGrayColumns(){
        /** If there are any empty columns, clear them out, loop precondition is that the array must have length */
        for(int u = 0; u < this.getButtonArray()[0].length; u++){
            //If there are any empty columns, clear them out. Loop precondition is that the array must have length
            for(int k = 0; k < this.getButtonArray()[0].length; k++){
                //If the color is light gray
                if(((Circle) this.getButtonArray()[this.getButtonArray().length - 1][k].getGraphic()).getFill() == Color.LIGHTGRAY){
                    //run switch algorithm, the length must be greater than 0
                    for(int j = 0; j < this.getButtonArray().length; j++){
                         if(!(k == this.getButtonArray()[0].length - 1)) {
                            ((Circle) this.getButtonArray()[j][k].getGraphic()).setFill(((Circle) this.getButtonArray()[j][k + 1].getGraphic()).getFill());
                            ((Circle) this.getButtonArray()[j][k + 1].getGraphic()).setFill(Color.LIGHTGRAY);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * This method updates the added progress bar
     */
    public void updateProgressBar(){
        /** Update the progress bar */
        int greyNumber = 0;
        int boardSize = this.getButtonArray().length * this.getButtonArray()[0].length;
        //Cycles through the buttonArray rows, precondition l < buttonArray.length
        for(int l = 0; l < this.getButtonArray().length; l++){
            //Cycles through the buttonArray columns, precondition j < buttonArray.length
            for(int j = 0; j < this.getButtonArray()[l].length; j++){
                //If color is lightgray
                if(((Circle) this.getButtonArray()[l][j].getGraphic()).getFill() == Color.LIGHTGRAY){
                    greyNumber = greyNumber + 1;
                }
            }
        }
        boardCompletion.setProgress((double) greyNumber / boardSize);
    }
    
    /**
     * Returns the array of buttons for this instance
     * @return Returns the array of buttons for this instance
     */ 
    public Button[][] getButtonArray(){
        return this.buttonArray;
    }
    
    /**
     * Allows the user to set the button array for this instance
     * @param buttonArray is the buttonArray to be assigned to the field
     */ 
    public void setButtonArray(Button[][] buttonArray){
        this.buttonArray = buttonArray;
    }

    /**
     * Gets the length of the board
     * @return the int length of the board
     */
    public static int getBoardLength() {
        return boardLength;
    }

    /**
     * Lets the user set the length of the board
     * @param boardLength is the length to be set
     */
    public static void setBoardLength(int boardLength) {
        SameGame.boardLength = boardLength;
    }

    /**
     * Lets the user get the board width
     * @return returns the board width
     */
    public static int getBoardWidth(){
        return boardWidth;
    }

    /**
     * Lets the user set the board width
     * @param boardWidth is the board width to be set
     */
    public static void setBoardWidth(int boardWidth) {
        SameGame.boardWidth = boardWidth;
    }

    /**
     * Lets the user get the number of colors
     * @return int value of number of colors
     */
    public static int getNumColors() {
        return numColors;
    }

    /**
     * Lets the user set the number of colors
     * @param numColors is the number of colors to be set
     */
    public static void setNumColors(int numColors) {
        SameGame.numColors = numColors;
    }

    /**
     * Main method - Launches the application
     * @param args are the command line arguments
     */
    public static void main(String[] args) {
        try{
            if(args.length > 3){
                System.out.println("More than 3 arguments provided. Game created with initial 3 arguments");
            }
            if(args.length > 0){
                if(Integer.parseInt(args[0]) < 25 && Integer.parseInt(args[0]) > 0)
                    boardLength = Integer.parseInt(args[0]);
                else
                    System.out.println("That length is a bit large (or 0), length set to 12");
            }
            if(args.length > 0){
                if(Integer.parseInt(args[1]) < 25 && Integer.parseInt(args[1]) > 0)
                    boardWidth = Integer.parseInt(args[1]);
                else
                    System.out.println("That width is a bit large (or 0), width set to 12");
            }
            if(args.length > 0){
                if(Integer.parseInt(args[2]) <= 12 && Integer.parseInt(args[2]) > 0)
                    numColors = Integer.parseInt(args[2]);
                else
                    System.out.println("That number of colors is a bit large (or 0), numColors set to 3");
            }
            Application.launch(args);
        }
        catch(NumberFormatException numberFormatException){
            System.out.println("Invalid command line arguments. Make sure to use 3 numbers");
        }
        catch(ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){
            System.out.println("Not enough or too many arguments provided");
        }
    }
}



