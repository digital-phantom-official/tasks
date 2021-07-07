import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trivia.Game;

public class CharacterizationTests {


  private static final Random SEEDER = new Random();
  private static final int TEST_COUNT = 12000;
  private Random rand;


  @Test
  @DisplayName("The modified program should behave like the original.")
  public void caracterizationTest() {
    for (int i = 1; i < TEST_COUNT; i++) {
      int seed = SEEDER.nextInt();
      resetRandom(seed);
      String actualOutput = extractRefactorOutput();
      resetRandom(seed);
      String expectedOutput = extractOriginalOutput();
      assertEquals(expectedOutput, actualOutput, "");
    }
  }

  private void resetRandom(int seed) {
    this.rand = new Random(seed);
  }

  private int dice() {
    return rand.nextInt(5) + 1;
  }

  private boolean answeredCorrectly() {
    return rand.nextInt() % 7 != 0;
  }

  private String extractOriginalOutput() {
    master.Game aGame = new master.Game();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream inmemory = new PrintStream(baos)) {
      System.setOut(inmemory);

      aGame.add("Chad");
      aGame.add("Karen");
      aGame.add("Kevin");

      boolean notAWinner = true;
      while (notAWinner) {
        aGame.roll(dice());
        notAWinner = answeredCorrectly() ? aGame.wasCorrectlyAnswered() : aGame.wrongAnswer();
      }

    }
    String output = new String(baos.toByteArray());
    return output;
  }

  private String extractRefactorOutput() {
    Game aGame = new Game();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream inmemory = new PrintStream(baos)) {
      System.setOut(inmemory);

      aGame.add("Chad");
      aGame.add("Karen");
      aGame.add("Kevin");

      boolean notAWinner = true;
      while (notAWinner) {
        aGame.roll(dice());
        notAWinner = answeredCorrectly() ? aGame.wasCorrectlyAnswered() : aGame.wrongAnswer();
      }

    }
    String output = new String(baos.toByteArray());
    return output;
  }


}
