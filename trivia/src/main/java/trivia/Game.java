package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    //replaced string literals with constants
    private final String ROCK = "Rock";
    private final String POP = "Pop";
    private final String SCIENCE = "Science";
    private final String SPORTS = "Sports";

    private Logger logger;

    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses = new int[6];
    boolean[] inPenaltyBox = new boolean[6];

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
        logger = Logger.getLogger(Game.class.getName());
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast(createQuestion(POP, i));
            scienceQuestions.addLast(createQuestion(SCIENCE, i));
            sportsQuestions.addLast(createQuestion(SPORTS, i));
            rockQuestions.addLast(createQuestion(ROCK, i));
        }
    }

    public String createQuestion(String type, int index) {
        return type + " Question " + index;
    }

    public boolean isPlayable() {
        return (howManyPlayers() >= 2);
    }

    public boolean add(String playerName) {


        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;

        logger.log(Level.WARNING,playerName + " was added");
        logger.log(Level.ALL,"They are player number " + players.size());
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        logger.log(Level.ALL,players.get(currentPlayer) + " is the current player");
        logger.log(Level.ALL,"They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
                places[currentPlayer] = places[currentPlayer] + roll;
                if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

                logger.log(Level.ALL,players.get(currentPlayer)
                        + "'s new location is "
                        + places[currentPlayer]);
                logger.log(Level.ALL,"The category is " + currentCategory());
                askQuestion();
            } else {
                logger.log(Level.ALL,players.get(currentPlayer) + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            places[currentPlayer] = places[currentPlayer] + roll;
            if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

            logger.log(Level.ALL,players.get(currentPlayer)
                    + "'s new location is "
                    + places[currentPlayer]);
            logger.log(Level.ALL,"The category is " + currentCategory());
            askQuestion();
        }

    }

    private void askQuestion() {
        if (currentCategory() == POP) {
            System.out.println(popQuestions.removeFirst());
        }
        if (currentCategory() == SCIENCE) {
            System.out.println(scienceQuestions.removeFirst());
        }
        if (currentCategory() == SPORTS) {
            System.out.println(sportsQuestions.removeFirst());
        }
        if (currentCategory() == ROCK) {
            System.out.println(rockQuestions.removeFirst());
        }
    }


    //remainder operator covering all categories
    private String currentCategory() {
        if (places[currentPlayer] % 4 == 0) return POP;
        if (places[currentPlayer] % 4 == 1) return SCIENCE;
        if (places[currentPlayer] % 4 == 2) return SPORTS;
        return ROCK;
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                logger.log(Level.ALL,"Answer was correct!!!!");
                purses[currentPlayer]++;
                logger.log(Level.ALL,players.get(currentPlayer)
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
                return true;
            }


        } else {

            logger.log(Level.ALL,"Answer was corrent!!!!");
            purses[currentPlayer]++;
            logger.log(Level.ALL,players.get(currentPlayer)
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        logger.log(Level.ALL,"Question was incorrectly answered");
        logger.log(Level.ALL,players.get(currentPlayer) + " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }
}
