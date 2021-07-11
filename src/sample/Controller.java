package sample;

import Classes.Action;
import Classes.Game;
import Classes.Player;
import Classes.Tower;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import javax.xml.soap.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;


public class Controller implements Initializable {
    @FXML
    Rectangle block1,block2,block3;

    @FXML
    VBox startTowerNode,midTowerNode,endTowerNode;

    @FXML
    AnchorPane welcomePane,endPane,basePane,blockContainerPane,loginPane, registerPane;

    @FXML
    Label swapCount,swapScore;

    @FXML
    TextField registerUsernameText,registerPasswordText,loginUsernameText,loginPasswordText;

    //leaderboard stuff
    @FXML
    TableView<Player> leaderboardTableView;
    @FXML
    TableColumn<Player,String> nameColumn;
    @FXML
    TableColumn<Player,String> minSwapColumn;

    @FXML
    Button undoButton,newGameButtonEndPane;
    public static Button undoStaticButton;

    public static AnchorPane mainPane;
    public static Label swapCountStaticLabel;

    public Tower[] towers;

    Game game;
    Player loggedUser;
    BooleanProperty isLoggedIn = new SimpleBooleanProperty();

    ObservableList<Player> leaderboardData;

    public void newGame(){
        welcomePane.setVisible(false);
        resetTowers();
        undoStaticButton.setDisable(true);
        blockContainerPane.getChildren().removeAll();
        Tower startTower = new Tower(startTowerNode,"111",true,false);
        Tower midTower = new Tower(midTowerNode,"222",false,false);
        Tower endTower = new Tower(endTowerNode,"333",false,true);
        this.towers = new Tower[]{startTower,midTower,endTower};
        game = new Game(towers,
                block1,
                block2,
                block3
        );

        game.activePlayer = this.loggedUser; //------------------------------ settings the logged user the active user
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
                game.swapAction((Node) event.getSource(),null);
        });
        midTower.getTowerNode().setOnMouseClicked(event -> {
            if(event.getSource() instanceof VBox)
                game.swapAction((Node) event.getSource(),null);
        });
        endTower.getTowerNode().setOnMouseClicked(event -> {
            if(event.getSource() instanceof VBox) {
                game.swapAction((Node) event.getSource(),null);
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

    public void fastForward(){
        ArrayList<int[]> winMoves = new ArrayList<>();
        winMoves.add(new int[]{0,2});
        winMoves.add(new int[]{0,1});
        winMoves.add(new int[]{2,1});
        winMoves.add(new int[]{0,2});
        winMoves.add(new int[]{1,0});
        winMoves.add(new int[]{1,2});
        winMoves.add(new int[]{0,2});

        Iterator<int[]> winMoveIterator = compareActions().iterator();

        BooleanProperty fastForwardNextStep = new SimpleBooleanProperty();
        fastForwardNextStep.addListener(observable -> {
            if (fastForwardNextStep.get()) {
                fastForwardExecution(winMoveIterator,fastForwardNextStep);
            }
        });

        //starting the iteration so that the process continues after that
        fastForwardExecution(winMoveIterator,fastForwardNextStep);

    }

    public void fastForwardExecution(Iterator<int[]> winMoveIterator,BooleanProperty fastForwardNextStep){
        if(!winMoveIterator.hasNext()) return;
        int[] winMoveIndices = winMoveIterator.next();
        System.out.println(Arrays.toString(winMoveIndices));
        fastForwardSwapAction(winMoveIndices[0],winMoveIndices[1]);
        actionSleep(fastForwardNextStep);
    }

    public void fastForwardSwapAction(int sourceIndex,int targetIndex){
        game.swapAction(null,towers[sourceIndex]);
        game.swapAction(null,towers[targetIndex]);
    }

    public void actionSleep(BooleanProperty fastForwardNextStep){
        Thread thread = null;

        Thread finalThread = thread;
        Task<Boolean> testBoolean = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                finalThread.sleep(1000);
                updateValue(false);
                updateValue(true);
                return true;
            }
        };
        thread = new Thread(testBoolean);
        thread.setDaemon(true);
        thread.start();
        fastForwardNextStep.bind(testBoolean.valueProperty());
    }

    public List<int[]> compareActions(){
        LinkedList<Action> gameActions = game.getActions();
        ArrayList<int[]> winMoves = new ArrayList<>();
        winMoves.add(new int[]{0,2});
        winMoves.add(new int[]{0,1});
        winMoves.add(new int[]{2,1});
        winMoves.add(new int[]{0,2});
        winMoves.add(new int[]{1,0});
        winMoves.add(new int[]{1,2});
        winMoves.add(new int[]{0,2});
        ArrayList<int[]> winStates = new ArrayList<>();
        winStates.add(new int[]{6,0,0});
        winStates.add(new int[]{5,0,1});
        winStates.add(new int[]{3,2,1});
        winStates.add(new int[]{3,3,0});
        winStates.add(new int[]{0,3,3});
        winStates.add(new int[]{1,2,3});
        winStates.add(new int[]{1,0,5});

        int fromIndex = 0;
        for (int i = gameActions.size() -1; i >= 0; i--){
            if(winStates.contains(gameActions.get(i).getPreExecutionGameState())){
                gameActions.get(i).undo();
                fromIndex = winStates.indexOf(gameActions.get(i).getPreExecutionGameState());
                System.out.println("Valid State found at iteration: " + i);
                break;
            }else{
                gameActions.get(i).undo();
            }
        }
        return winMoves.subList(fromIndex,winMoves.size());
    }

    public void loadLeaderboardData() throws FileNotFoundException {
        try (Scanner fileReader = new Scanner(new File("src/sample/test.txt"))) {
            this.leaderboardData = FXCollections.observableArrayList();
            while (fileReader.hasNextLine()) {
                String playerString = fileReader.nextLine();
                String[] playerData = playerString.split(",");
                Player plyr = new Player(playerData);
                this.leaderboardData.add(plyr);
                System.out.println(plyr);
                System.out.println(playerString);
            }
        }
    }

    public void saveLeaderboardData(){
        try (FileWriter fileWriter = new FileWriter("test.txt");){
            fileWriter.write("");
            for (Player player: leaderboardData){
                fileWriter.append(player.getPlayerSaveString());
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginPane(){
        loginPane.setDisable(false);
        loginPane.setVisible(true);
        registerPane.setDisable(true);
        registerPane.setVisible(false);
    }

    public void showRegisterPane(){
        registerPane.setDisable(false);
        registerPane.setVisible(true);
        loginPane.setVisible(false);
        loginUsernameText.setDisable(true);
    }

    //registering User
    public void registerUser(){
        String usernameInput = registerUsernameText.getText();
        String passwordInput = registerPasswordText.getText();

        //input validations------------------------------------------------------
        if (inputValidationEmpty(usernameInput,passwordInput)){
            System.out.println("Username and password fields are required");
            return;
        }
        if(inputValidationLength(passwordInput, 8)){
            System.out.println("Password should be at least 8 characters");
        }
        if(inputValidationLength(usernameInput,3)){
            System.out.println("Username should be at least 3 characters long");
        }
        //------------------------------------------------------------------------

        if(isUsernameTaken(usernameInput)) {//-------------------------------checks if the username is already taken
            System.out.println("Sorry, the username is already taken");
            return;
        }
        //------------------------------------------------------------------------


        Player newPlayer = new Player(usernameInput,passwordInput);//--------create player instance
        //------------------------------------------
        this.leaderboardData.add(newPlayer);//-------------------------------adds player to the player list
        //------------------------------------------

        System.out.println("Player successfully registered");
        System.out.println(newPlayer);
        //------------------------------------------
        showLoginPane();//---------------------------------------------------displays login page

        registerUsernameText.setText("");//----------------------------------resets username field
        registerPasswordText.setText("");//----------------------------------resets password field
    }

    public void loginUser(){
        String usernameInput = loginUsernameText.getText();
        String passwordInput = loginPasswordText.getText();

        //input validations------------------------------------------------------
        if (inputValidationEmpty(usernameInput,passwordInput)){
            System.out.println("Username and password fields are required");
            return;
        }
        if(inputValidationLength(passwordInput, 8)){
            System.out.println("Invalid Password");
            return;
        }
        if(inputValidationLength(usernameInput,3)){
            System.out.println("Invalid Username");
            return;
        }
        //------------------------------------------------------------------------

        Player playerSearched = this.findUserByUsername(usernameInput); //---finds player by the username

        if(playerSearched == null || !playerSearched.getPassword().equals(passwordInput)){
            //---------------------------------------------------------------checks if the username and password are correct
            System.out.println("Invalid login Credentials. Username, password pair doesn't match a user");
            return;
        }else{
            this.loggedUser = playerSearched;
            System.out.println("Successfully logged in as: " + playerSearched.getUsername());
        }
        System.out.println("loginUser  method finished");
        loginUsernameText.setText("");//-------------------------------------resets username field
        loginPasswordText.setText("");//-------------------------------------resets password field
    }

    //checks if input fields are not empty
    public boolean inputValidationEmpty(String...fieldsStrings){
        for (String field: fieldsStrings){
            if(field.isEmpty()) return true;
        }
        return false;
    }
    public boolean inputValidationLength(String field, int length){
        if(field.length() < length) return true;
        return false;
    }

    public boolean isUsernameTaken(String username){
        for(Player player: this.leaderboardData){
            if(player.getUsername().equals(username)) return true;
        }
        return false;
    }

    public Player findUserByUsername(String username){
        for(Player player: this.leaderboardData){
            if(player.getUsername().equals(username)) return player;
        }
        return null;
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

        //load leaderboard data
        try {
            loadLeaderboardData();
        }catch (FileNotFoundException e){}
        //sorting the players by rank

        //leaderboard initialization
        nameColumn.setCellValueFactory(new PropertyValueFactory<Player,String>("name"));
        minSwapColumn.setCellValueFactory(new PropertyValueFactory<Player,String>("minSwaps"));

        leaderboardTableView.setItems(this.leaderboardData);

//        isLoggedIn.bind();
    }

    public void displayedLeaderboard(){

    }
}
