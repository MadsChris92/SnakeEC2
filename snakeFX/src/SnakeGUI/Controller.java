package SnakeGUI;


import SnakeLogic.Item;
import SnakeLogic.Snake;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
    private long lastUpdate;
    private boolean gameOver = false;
    private boolean restart = false;
    private double fieldHeight;
    private double fieldWidth;
    private int width = 30;
    private int height = 20;
    private int amountOFIems = 40;
    private Random random = new Random();
    private int gameLoopDelay = 500;
    private float refreshRate =100;
    private int X;
    private int Y;
    private KeyCode keyPressed = KeyCode.BACK_SPACE;
    private int direction = 5;
    private GraphicsContext g;
    private Snake snake;

    ArrayList<Item> items = new ArrayList<Item>();

    public void btnStartAction(ActionEvent event)
    {
        System.out.println("btn clicked");
        labelStatus.setText("test");
        restartGame();
        drawCanvas();
        playerBehaviour();
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
        btnStart.setText("Restart");

        // Start and control game loop
        newAnimation();
    }

    public void newAnimation(){
        new AnimationTimer(){


            public void handle (long now)
            {
                if (now > lastUpdate + refreshRate * 1000000)
                {
                    lastUpdate = now;

                    if(gameOver){
                        labelStatus.setText("GAME OVER");
                    }else{
                        labelStatus.setText("Snake Length: " + snake.getLength());
                        update(now);
                    }
                }
            }
        }.start();

    }

    private void AddItems() {
        for (int i = 0; i < amountOFIems; i++) {
            items.add(new Item(Color.RED, randomNumber(width), randomNumber(height)));
            }
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
        /*
        0 = Y--;
        1 = Y++;
        2 = X++;
        3 = X--;
         */
        g = canvas.getGraphicsContext2D();
        switch (keyPressed)
        {
            case DOWN:
                if(direction == 0){
                    Y--;
                }else{
                    Y++;
                    direction = 1;
                }
                break;
            case LEFT:
                if(direction == 2){
                    X++;
                }else{
                    this.X--;
                    direction = 3;
                }
                break;
            case RIGHT:
                if(direction == 3){
                    X--;
                }else{
                    X++;
                    direction = 2;
                }
                break;
            case UP:
                if(direction == 1){
                    Y++;
                }else{
                    Y--;
                    direction = 0;
                }
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

        System.out.println("X: "+ fieldHeight + "Y: " + fieldHeight);
    }

    private void drawCanvas() {
        g.setFill(Color.AZURE);

//        g.fillRect(0,0,width*fieldWidth ,height*fieldHeight);
        g.fillRoundRect(0,0,width*fieldWidth ,height*fieldHeight,10,10);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                g.setFill(Color.ORANGE);
                g.fillRoundRect(x*fieldWidth, y*fieldHeight, fieldWidth, fieldHeight, 2,2);
            }

        }

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

    private void playerBehaviour(){

        if(hasHit()){
            snake.setLength(snake.getLength() + 1);
            System.out.println("Snakelength: [" + snake.getLength() + "]");

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
        snake.getPosX().clear();
        snake.getPosY().clear();
        X = randomNumber(width);
        Y = randomNumber(height);
        snake.setLength(1);
        snake.setPositions(X, Y);
        keyPressed(KeyCode.BACK_SPACE);
        lastUpdate = 0;

        for (int i = 0; i < items.size(); i++) {
            items.get(i).setX(randomNumber(width));
            items.get(i).setY(randomNumber(height));
        }
        gameOver = false;
    }

}
