package ivan_belyj.commanderchess.model;

import java.util.ArrayList;
import java.util.Random;

public class RandomName {
    private static final String[] _firstNames = new String[] { "Esmok", "Etale", "Liamok", "Esane", "Nfei", "Kaiche" };
    private static final String[] _lastNames = new String[] { "Gvel", "Shalore", "Haasem", "Chafe", "Reich", "Lane", "Eskea" };

    public static String Generate() {
        Random rnd = new Random();
        String rndFirstName = _firstNames[rnd.nextInt(_firstNames.length)];
        String rndLastName = _lastNames[rnd.nextInt(_lastNames.length)];
        return rndFirstName + " " + rndLastName;
    }
}
