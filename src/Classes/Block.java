package Classes;

import javafx.scene.shape.Rectangle;

public class Block {
    private String color;
    private Rectangle shape;
    private String blockName;
    private int blockValue;

    public Block(String color, Rectangle block, String blockName, int blockValue) {
        this.color = color;
        this.shape = block;
        this.blockName = blockName;
        this.blockValue = blockValue;
    }

    public Block(String color, String blockName, int blockValue) {
        this.color = color;
        this.blockName = blockName;
        this.blockValue = blockValue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Rectangle getShape() {
        return shape;
    }

    public void setShape(Rectangle shape) {
        this.shape = shape;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public int getBlockValue() {
        return blockValue;
    }

    public void setBlockValue(int blockValue) {
        this.blockValue = blockValue;
    }
}
