import java.util.List;

/**
 * Represents a player in the game with their nickname and warehouses.
 */
public class Player {
    private String nickname;
    private List<Warehouse> warehouses;

    public Player() {
    }

    public Player(String nickname, List<Warehouse> warehouses) {
        this.nickname = nickname;
        this.warehouses = warehouses;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }
}
