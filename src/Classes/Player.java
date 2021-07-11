package Classes;

public class Player {
    private String name;
    private int numberOfGames;
    private int minSwaps;
    private String username;
    private String password;
    private int score;

    public Player(String name, int numberOfGames, int minSwaps, String username, String password) {
        this.name = name;
        this.numberOfGames = numberOfGames;
        this.minSwaps = minSwaps;
        this.username = username;
        this.password = password;
    }
    public Player(String[] playerData) {
        if (playerData.length < 6) {
            System.out.println("Invalid Player data! All fields set to e");
            playerData = new String[]{"e","0","0","e","e","0"};
        }
        this.name = playerData[0];
        this.numberOfGames = playerData[1] != null ?Integer.parseInt(playerData[1]): 0;
        this.minSwaps = playerData[2] != null ?Integer.parseInt(playerData[2]): 0;
        this.username = playerData[3];
        this.password = playerData[4];
        this.score = playerData[5] != null ?Integer.parseInt(playerData[5]): 0;
    }

    public Player(String username, String password){
        this.name = username;
        this.numberOfGames = 0;
        this.minSwaps = 0;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinSwaps() {
        return minSwaps;
    }

    public void setMinSwaps(int minSwaps) {
        this.minSwaps = minSwaps;
    }

    public void setScore(int score){
        this.score += score;
    }

    public int getScore(){
        return this.score;
    }

    public String getPlayerSaveString(){
        return this.name + "," +
                this.numberOfGames + "," +
                this.minSwaps + "," +
                this.username + "," +
                this.password + "\n";
    }

    @Override
    public String toString(){
        return new StringBuilder("Player Data: {")
                .append("name: ").append(this.name)
                .append(" numberOfGames: ").append(this.numberOfGames)
                .append(" minSwaps: ").append(this.minSwaps)
                .append(" username: ").append(this.username)
                .append(" password: ").append(this.password)
                .append(" }").toString();
    }
}
