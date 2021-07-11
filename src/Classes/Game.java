package Classes;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import sample.Controller;

import java.util.*;
import java.util.stream.Stream;

public class Game {
    public Player activePlayer;
    BooleanProperty hasGameEndProperty;
    IntegerProperty numberOfSwaps;
    Block[] blocks;
    Tower[] towers;
    Tower source;
    Tower target;
    LinkedList<Action> actions = new LinkedList<>();

    Tower startTower, endTower;

    //Constructors
    public Game() { }

    public Game(Tower[] towers, Rectangle... blocks) {
        this.blocks = new Block[blocks.length];
        this.towers = towers;

        for(Tower tower : this.towers){
            if(tower.isStartTower) this.startTower = tower;
            if (tower.isEndTower) this.endTower = tower;
        }

        String[] colors = {"#f00","#0f0","#00f"};
        double[] sizes = {80,105,128};

        for (int i = 0; i < blocks.length; i++) {
            this.blocks[i] = new Block(colors[i],blocks[i],"block" + i,i+ 1);
            this.blocks[i].getShape().setWidth(sizes[i]);
            this.blocks[i].getShape().setStyle(String.format("-fx-fill:%s;",colors[i]));
        }
        this.numberOfSwaps = new SimpleIntegerProperty();
        this.numberOfSwaps.set(0);

        this.hasGameEndProperty = new SimpleBooleanProperty();
        hasGameEndProperty.set(false);
    }
    //------------------------------------------------------------------------------------------

    //Custom methods
    public void swapAction(Node towerTriggered,Tower towerTriggeredFastForward){
        //Doesn't perform the swap if both source and target are the same
        Tower triggeredTower;
        //when fast forwarding
        if(towerTriggeredFastForward != null) triggeredTower = towerTriggeredFastForward;

        //when a real player is playing
        else triggeredTower = getTowerBySource(towerTriggered);

        if (this.source == null){
            this.source = triggeredTower;
            System.out.println("Source Set: " + this.source.getTowerId());
            return;
        }else if(this.target == null){
            this.target = triggeredTower;
            System.out.println("Target Set: " + this.target.getTowerId());
        }
        if(this.source == this.target){
            this.source = null;
            this.target = null;
            System.out.println("Source and Target has been reset");
            return;
        };

        if(isActionValid()){
            this.numberOfSwaps.set(numberOfSwaps.get() + 1);
            Action currentAction = new Action(source, target);
            actions.add(currentAction);
            this.source = null;
            this.target = null;

            //get the tower value - for the fast forward feature
            int[] preExecutionGameState = new int[towers.length];
            for (int i = 0; i < towers.length; i++) {
                preExecutionGameState[i] = towers[0].getTowerValue();
            }
            currentAction.setPreExecutionGameState(preExecutionGameState);

            //action execution
            currentAction.execute();
            Controller.undoStaticButton.setDisable(false);

            //changes the bound property to true if the game has ended
            hasGameEndProperty.set(hasGameEnded());

        }else{
            System.out.println("Invalid Action!");
            this.source = null;
            this.target = null;
            System.out.println("Post Invalid:Source and Target has been reset");
        }
    }

    public boolean isActionValid(){
        if(target.getBlocksInTower().isEmpty()) return true;
        return this.source.peekLastBlock().getBlockValue() < this.target.peekLastBlock().getBlockValue();
    }

    public void startGame(){
        for(int i = blocks.length -1; i >= 0; i--){
            startTower.addBlock(blocks[i]);
            System.out.println(blocks[i].getBlockName() + " " + blocks[i].getBlockValue());
        }
        for (Tower tower : towers) {
            tower.getTowerNode().setOnMouseClicked(event -> {
                if(event.getSource() instanceof VBox)
                    swapAction((Node) event.getSource(),null);
            });
        }
    }

    public boolean hasGameEnded(){
        return endTower.getBlocksInTower().size() == blocks.length;
    }

    public Tower getTowerBySource(Node sourceNode){
        for (Tower tower: this.towers) {
            if(tower.getTowerNode() == sourceNode)
                return tower;
        }
        return null;
    }

    public Tower getTowerBySource(VBox sourceNode){
        for (Tower tower: this.towers) {
            if(tower.getTowerNode() == sourceNode)
                return tower;
        }
        return null;
    }

    public void undoLastMove(){
        actions.removeLast().undo();
        numberOfSwaps.set(numberOfSwaps.get() - 1);
    }

//    public void fastForward(){
//        BooleanProperty booleanProperty = new SimpleBooleanProperty();
//
//        booleanProperty.addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//
//            }
//        });
//        int test = 1;
//        ArrayList<int[]> winMoves = new ArrayList<>();
//        winMoves.add(new int[]{0,0,2});
//        winMoves.add(new int[]{0,0,1});
//        winMoves.add(new int[]{2,2,1});
//        winMoves.add(new int[]{0,0,2});
//        winMoves.add(new int[]{1,2,0});
//        winMoves.add(new int[]{1,1,2});
//        winMoves.add(new int[]{0,0,2});
//        new Thread(){
//
//        };
//    }
//    public void actionFastForwardExecution(int[] actionInt){
//        new Action(this.towers[actionInt[0]].getBlocksInTower().peek(),this.towers[actionInt[1]],this.towers[actionInt[2]]).execute();
//    }
    //------------------------------------------------------------------------------------------

    public LinkedList<Action> getActions() {
        return actions;
    }

    public BooleanProperty isHasGameEndProperty() {
        return hasGameEndProperty;
    }

    public BooleanProperty hasGameEndPropertyProperty() {
        return hasGameEndProperty;
    }

    public IntegerProperty getNumberOfSwaps() {
        return numberOfSwaps;
    }

    public Tower[] getTowers() {
        return towers;
    }

    public void setTowers(Tower[] towers) {
        this.towers = towers;
    }
}
