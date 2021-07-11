package Classes;

import javafx.scene.layout.VBox;

import java.util.Stack;

public class Tower {
    VBox towerNode;
    Stack<Block> blocksInTower;
    String towerId;
    boolean isStartTower;
    boolean isEndTower;

    public Stack<Block> getBlocksInTower() {
        return blocksInTower;
    }

    public void setBlocksInTower(Stack<Block> blocksInTower) {
        this.blocksInTower = blocksInTower;
    }

    public String getTowerId() {
        return towerId;
    }

    public void setTowerId(String towerId) {
        this.towerId = towerId;
    }

    public VBox getTowerNode() {
        return towerNode;
    }

    public void setTowerNode(VBox towerNode) {
        this.towerNode = towerNode;
    }

    public Tower(VBox towerNode, String towerId,boolean isStartTower, boolean isEndTower) {
        this.towerNode = towerNode;
        this.blocksInTower = new Stack<>();
        this.towerId = towerId;
        this.isStartTower = isStartTower;
        this.isEndTower = isEndTower;
    }



    //custom methods
    public Block peekLastBlock(){
        return blocksInTower.peek();
    }

    public void addBlock(Block blockToAdd){
        this.blocksInTower.push(blockToAdd);
        this.getTowerNode().getChildren().add(0,blockToAdd.getShape());
    }

    public Block removeLastBlock(){
        Block removedBlock = this.blocksInTower.pop();
        this.getTowerNode().getChildren().remove(removedBlock.getShape());
        return removedBlock;
    }

    public int getTowerValue(){
        int retVal = 0;
        for(Block block: this.blocksInTower){
            retVal++;
        }
        return retVal;
    }
}
