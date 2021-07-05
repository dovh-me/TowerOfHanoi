package sample;

import Classes.Game;
import Classes.Tower;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML
    Rectangle block1,block2,block3;

    @FXML
    VBox startTowerNode,midTowerNode,endTowerNode;

    @FXML
    AnchorPane endPane,basePane,blockContainerPane;

    @FXML
    Label swapCount,swapScore;

    @FXML
    Button undoButton,newGameButtonEndPane;
    public static Button undoStaticButton;

    public static AnchorPane mainPane;
    public static Label swapCountStaticLabel;

    Game game;

    public void newGame(){
        resetTowers();
        undoStaticButton.setDisable(true);
        blockContainerPane.getChildren().removeAll();
        Tower startTower = new Tower(startTowerNode,"111",true,false);
        Tower midTower = new Tower(midTowerNode,"222",false,false);
        Tower endTower = new Tower(endTowerNode,"333",false,true);
        game = new Game(new Tower[]{startTower,midTower,endTower},
                block1,
                block2,
                block3
        );


        //binding the swapCount Label with the swap count variable in the game class
        IntegerBinding swapBinding = new IntegerBinding() {

            {
                super.bind(game.getNumberOfSwaps());
            }

            @Override
            protected int computeValue() {
                return game.getNumberOfSwaps().get();
            }
        };
        swapCount.textProperty().bind(Bindings.format("Swap Count: %d",swapBinding));
        swapScore.textProperty().bind(Bindings.format("%d",swapBinding));

        BooleanBinding showEndPaneBinding = new BooleanBinding() {
            {
                super.bind(game.isHasGameEndProperty());
            }
            @Override
            protected boolean computeValue() {
                return game.isHasGameEndProperty().get();
            }
        };
        endPane.visibleProperty().bind(showEndPaneBinding);

        startTower.getTowerNode().setOnMouseClicked(event -> {
            if(event.getSource() instanceof VBox)
                game.swapAction((Node) event.getSource());
        });
        midTower.getTowerNode().setOnMouseClicked(event -> {
            if(event.getSource() instanceof VBox)
                game.swapAction((Node) event.getSource());
        });
        endTower.getTowerNode().setOnMouseClicked(event -> {
            if(event.getSource() instanceof VBox) {
                game.swapAction((Node) event.getSource());
                if(game.hasGameEnded()) basePane.setDisable(true);
            }
        });
        game.startGame();
    }


    public void endGame(){
        basePane.setDisable(true);
    }

    public void resetTowers(){
        startTowerNode.getChildren().remove(0,startTowerNode.getChildren().size());
        midTowerNode.getChildren().remove(0,midTowerNode.getChildren().size());
        endTowerNode.getChildren().remove(0,endTowerNode.getChildren().size());
    }

    public void undoLastMove(){
        game.undoLastMove();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTowerNode.setAlignment(Pos.BOTTOM_CENTER);
        midTowerNode.setAlignment(Pos.BOTTOM_CENTER);
        endTowerNode.setAlignment(Pos.BOTTOM_CENTER);

        mainPane = basePane;
        swapCountStaticLabel = swapCount;
        undoStaticButton = undoButton;
        undoStaticButton.setDisable(true);
    }

    public void startNewGameEndPane(){

    }
}
