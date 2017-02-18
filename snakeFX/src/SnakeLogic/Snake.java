package SnakeLogic;

import java.util.ArrayList;

/**
 * Created by Mads on 17-02-2017.
 */
public class Snake implements GameObject {

    int length = 1;

    ArrayList<Integer> posX = new ArrayList<>();

    ArrayList<Integer> posY = new ArrayList<>();

    @Override
    public void grow() {

    }

    @Override
    public void die() {

    }

    @Override
    public void update() {

    }

    public void setPositions(int x, int y){

        posY.add(y);
        posX.add(x);

        if(length < posY.size()){
            posY.remove(0);
            posX.remove(0);
        }
//        System.out.println(posX.toString());
    }

    //Getters and Setters

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<Integer> getPosX() {
        return posX;
    }

    public ArrayList<Integer> getPosY() {
        return posY;
    }

}
