package ivan_belyj.commanderchess.model;

import javafx.scene.paint.Color;

import java.util.Random;

public class GameManager {
    public PartyGame newGame(String player1Name, Color player1Color, String player2Name, Color player2Color) {
        Player p1 = new Player(player1Name, player1Color);
        Player p2 = new Player(player2Name, player2Color);

        PartyGame partyGame = new PartyGame(p1, p2);
        return partyGame;
    }
}
