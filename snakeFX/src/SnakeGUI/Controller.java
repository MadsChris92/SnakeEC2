package SnakeGUI;


import SnakeLogic.Snake;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.*;

public class Controller {

    public Button btnStart;

    @FXML
    Label labelStatus;
    @FXML
    Canvas canvas;
    String frans = "Frans";
    private boolean gameOver = false;
    private boolean restart = false;
    private double fieldHeight;
    private double fieldWidth;
    private int width = 30;
    private int height = 20;
    private Random random = new Random();
    private int gameLoopDelay = 500;
    private float refreshRate =100;
    private int X;
    private int Y;
    private KeyCode keyPressed = KeyCode.BACK_SPACE;
    GraphicsContext g;
    Snake snake;

    ArrayList<Item> items = new ArrayList<Item>();

    public void btnStartAction(ActionEvent event)
    {
        System.out.println("btn clicked");
        labelStatus.setText("test");
        restartGame();
        drawCanvas();
    }



    /**
     * Executed when JavaFX is initialized. Used to setup the Snake game
     */
    public void initialize()
    {
        snake = new Snake();
        startGame();
        AddItems();
        calculateFields();

        // Start and control game loop
        new AnimationTimer(){

            long lastUpdate;
            public void handle (long now)
            {
                if (now > lastUpdate + refreshRate * 1000000)
                {
                    lastUpdate = now;
                    update(now);

                    if(gameOver){
                        stop();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("GAME OVER");
                        alert.setHeaderText("Do you want to continue?");
                        alert.show();
                    }
                    if(restart){
                        start();
                        restart = false;
                    }
                }
            }
        }.start();
    }

    private void AddItems() {
        items.add(new Item(Color.GREEN, 12, 4));
        items.add(new Item(Color.RED, 12,9));
    }

    public int randomNumber(int max){
        return random.nextInt(max);
    }

    public void keyPressed(KeyCode keyCode)
    {
        this.keyPressed = keyCode;
    }

    /**
     * Game loop - executed continously during the game
     * @param now game time in nano seconds
     */
    private void update(long now)
    {
        g = canvas.getGraphicsContext2D();
        switch (keyPressed)
        {
            case DOWN:
                this.Y++;
                break;
            case LEFT:
                this.X--;
                break;
            case RIGHT:
                this.X++;
                break;
            case UP:
                this.Y--;
                break;
        }

        drawCanvas();
        playerBehaviour();

    }

    /**
     * Calculate height and width of each field
     */
    private void calculateFields() {
        this.fieldHeight = canvas.getHeight() / this.height;
        this.fieldWidth = canvas.getWidth() / this.width;
    }

    /**
     * Draw the canvas - used in the gameloop
     */
    private void drawCanvas() {
        g.setFill(Color.ORANGE);

        g.fillRect(0,0,width*fieldWidth ,height*fieldHeight);

        for (Item item : items)
        {
            g.setFill(item.getColor());
            g.fillRoundRect(item.getX() * fieldWidth, item.getY() * fieldHeight, fieldWidth, fieldHeight, 5,5);
        }
    }

    private void startGame(){
        this.X = random.nextInt(width);
        this.Y = random.nextInt(height);
        snake.setLength(1);
        snake.setPositions(X, Y);
    }

    private void setItems(){
        for (int i = 0; i < items.size(); i++) {
//            items.get(i).setX(random.nextInt(width));
//            items.get(i).setY(random.nextInt(height));
        }
    }

    private void playerBehaviour(){

        if(hasHit()){
            snake.setLength(snake.getLength() + 1);
            System.out.println("Snakelength: [" + snake.getLength() + "]");
            setItems();
        }

        g.setFill(Color.BLUE);
        snake.setPositions(X, Y);

        for (int i = 0; i < snake.getLength(); i++) {
            g.fillRoundRect(snake.getPosX().get(i) * fieldWidth, snake.getPosY().get(i) * fieldHeight, fieldWidth, fieldHeight, 3, 3);
        }

        if(snake.getPosX().get(( snake.getLength()-1)) == width || snake.getPosX().get(snake.getLength()-1) == -1){
            gameOver = true;
        }
        if(snake.getPosY().get(snake.getLength()-1) == height || snake.getPosY().get(snake.getLength()-1) == -1){
            gameOver = true;
        }

        for (int i = 0; i < snake.getLength()-2; i++) {
            if(snake.getPosX().get(snake.getLength()-1) == snake.getPosX().get(i) && snake.getPosY().get(snake.getLength()-1) == snake.getPosY().get(i)){
                gameOver = true;
            }
        }
//        g.fillRoundRect(this.X * fieldWidth, this.Y * fieldHeight, fieldWidth, fieldHeight, 3, 3);
    }

    private boolean hasHit(){
        boolean didHit = false;
        for (Item item : items) {
            if (X == item.getX() && Y == item.getY()) {
                placeItem(item);
                didHit = true;
            }
        }
        return didHit;
    }

    private void placeItem(Item item){
        item.setX(randomNumber(width));
        item.setY(randomNumber(height));

        for (int i = 0; i < snake.getLength() - 1; i++) {
            if(item.getX() == snake.getPosX().get(i) && item.getY() == snake.getPosY().get(i)){
                placeItem(item);
            }
        }
    }

    private void restartGame() {
        X = randomNumber(width);
        Y = randomNumber(height);

        snake.setPositions(X, Y);

        snake.setLength(1);

        for (int i = 0; i < items.size(); i++) {
            items.get(i).setX(randomNumber(width));
            items.get(i).setY(randomNumber(height));
        }

        gameOver = false;
        restart = true;
    }

}
