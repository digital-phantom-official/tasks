package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    //replaced string literals with constants
    private static final String ROCK = "Rock";
    private static final String POP = "Pop";
    private static final String SCIENCE = "Science";
    private static final String SPORTS = "Sports";

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

        logger.log(Level.ALL, "{0} was added", playerName);
        logger.log(Level.ALL, "They are player number {0}", players.size());
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        logger.log(Level.ALL, "{0} is the current player", players.get(currentPlayer));
        logger.log(Level.ALL, "They have rolled a {0}", roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                logger.log(Level.ALL, "{0} is getting out of the penalty box", players.get(currentPlayer));
                places[currentPlayer] = places[currentPlayer] + roll;
                if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

                logger.log(Level.ALL, "{0}\'s new location is {1}"
                        , new Object[]{players.get(currentPlayer), places[currentPlayer]});
                logger.log(Level.ALL, "The category is {0}", currentCategory());
                askQuestion();
            } else {
                logger.log(Level.ALL, "{0} is not getting out of the penalty box", players.get(currentPlayer));
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            places[currentPlayer] = places[currentPlayer] + roll;
            if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

            logger.log(Level.ALL, "{0}\'s new location is {1}"
                    , new Object[]{players.get(currentPlayer), places[currentPlayer]});
            logger.log(Level.ALL, "The category is {0}", currentCategory());
            askQuestion();
        }

    }

    private Object askQuestion() {
        Object question = null;
        if (currentCategory().equals(POP)) {
            question = popQuestions.removeFirst();
        }
        if (currentCategory().equals(SCIENCE)) {
            question = scienceQuestions.removeFirst();
        }
        if (currentCategory().equals(SPORTS)) {
            question = sportsQuestions.removeFirst();
        }
        if (currentCategory().equals(ROCK)) {
            question = rockQuestions.removeFirst();
        }
        if (question != null) {
            logger.log(Level.ALL, question.toString());
            return question;
        } else {
            throw new IllegalStateException(question.toString());
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
                logger.log(Level.ALL, "Answer was correct!!!!");
                purses[currentPlayer]++;
                logger.log(Level.ALL, "{0} now has {1} Gold Coins.",
                        new Object[]{players.get(currentPlayer), purses[currentPlayer]});

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

            logger.log(Level.ALL, "Answer was correct!!!!");
            purses[currentPlayer]++;
            logger.log(Level.ALL, "{0} now has {1} Gold Coins.",
                    new Object[]{players.get(currentPlayer), purses[currentPlayer]});

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        logger.log(Level.ALL, "Question was incorrectly answered");
        logger.log(Level.ALL, "{0} was sent to the penalty box", players.get(currentPlayer));
        inPenaltyBox[currentPlayer] = true;

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return purses[currentPlayer] != 6;
    }
}
