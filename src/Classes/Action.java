package Classes;

import javafx.scene.layout.VBox;

public class Action {
    private Block blockSwapped;
    private Tower source;
    private Tower target;
    private int[] preExecutionGameState;

    public Action(Block blockSwapped, Tower source, Tower target) {
        this.blockSwapped = blockSwapped;
        this.source = source;
        this.target = target;
    }

    public Action(Tower source, Tower target) {
        this.source = source;
        this.target = target;
    }

    public Block getBlockSwapped() {
        return blockSwapped;
    }

    public void setBlockSwapped(Block blockSwapped) {
        this.blockSwapped = blockSwapped;
    }

    public Tower getSource() {
        return source;
    }

    public void setSource(Tower source) {
        this.source = source;
    }

    public Tower getTarget() {
        return target;
    }

    public void setTarget(Tower target) {
        this.target = target;
    }

    public int[] getPreExecutionGameState() {
        return preExecutionGameState;
    }

    public void setPreExecutionGameState(int[] preExecutionGameState) {
        this.preExecutionGameState = preExecutionGameState;
    }

    //custom methods
    public void execute(){
        this.blockSwapped = this.source.removeLastBlock();
        this.target.addBlock(this.blockSwapped);
    }

    public void undo(){
        this.blockSwapped = this.target.removeLastBlock();
        this.source.addBlock(this.blockSwapped);
    }
}
