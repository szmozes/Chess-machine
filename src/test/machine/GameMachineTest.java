package machine;

import org.junit.Before;
import org.junit.Test;

public class GameMachineTest {
    Table table;
    StandardGameAgainstMachine game;

//	 A - Ally
//	 E - Enemy
//	 O - Test

//	   ___________________
//	   |\|0|1|2|3|4|5|6|7|
//	   |0|_|_|K|_|_|_|_|_|
//	   |1|_|_|_|_|_|_|_|_|
//	   |2|_|_|_|_|_|_|R|_|
//	   |3|_|_|_|_|_|_|_|_|
//	   |4|_|_|_|_|B|_|_|_|
//	   |5|_|_|_|_|_|_|_|_|
//	   |6|_|_|_|_|_|_|_|_|
//	   |7|_|_|_|_|_|_|_|_|

    @Before
    public void setUp() {
        game = new StandardGameAgainstMachine();
        table = game.table;
        clearTable(table);
    }

    @Test
    public void attemptTest() {
        TestWriter.writeTestTitle("Attempt Test");
        table.placePiece(new Bishop(Color.WHITE), 4, 4);
        table.placePiece(new Knight(Color.BLACK), 0, 2);
        table.placePiece(new Rook(Color.BLACK), 2, 6);
        TestWriter.writeTable(table);
        int[] bestMove = new int[4];
        double state = game.attempt(2, bestMove);
        TestWriter.writeString("best move value: " + state + " from: [" + bestMove[0] + ", " + bestMove[1] + "] to: [" + bestMove[2] + ", " + bestMove[3] + "]\n");
        TestWriter.writeString(game.table.whoTurns + " turns");
    }


    //	___________________
//  |\|0|1|2|3|4|5|6|7|
//	|0|_|_|_|_|_|_|_|_|
//	|1|_|_|_|_|_|_|_|_|
//	|2|_|K|_|_|_|P|_|_|
//	|3|_|_|_|_|_|_|R|_|
//	|4|_|_|_|_|_|_|_|_|
//	|5|_|_|_|_|Q|_|_|_|
//	|6|_|_|_|_|_|_|_|_|
//	|7|_|_|_|_|_|_|_|_|
    @Test
    public void makeMoveTest1() {
        TestWriter.writeTestTitle("Make Move Test");
        table.placePiece(new Queen(Color.WHITE), 5, 4);
        table.placePiece(new Knight(Color.BLACK), 2, 1);
        table.placePiece(new Rook(Color.BLACK), 3, 6);
        table.placePiece(new Pawn(Color.BLACK), 2, 5);
        TestWriter.writeString("before making a move:");
        TestWriter.writeTable(table);
        game.makeMove(1);
        TestWriter.writeString("after making a move:");
        TestWriter.writeTable(table);
    }

    //	___________________
//  |\|0|1|2|3|4|5|6|7|
//	|0|_|_|_|_|_|_|_|_|
//	|1|_|_|_|_|_|_|_|_|
//	|2|_|B|_|_|_|P|_|_|
//	|3|_|_|_|_|_|_|R|_|
//	|4|_|_|_|_|_|_|_|_|
//	|5|_|_|_|_|Q|_|_|_|
//	|6|_|_|_|_|_|_|_|_|
//	|7|_|_|_|_|_|_|_|_|
    @Test
    public void makeMoveTest2() {
        TestWriter.writeTestTitle("Make Move Test 2");
        table.placePiece(new Queen(Color.WHITE), 5, 4);
        table.placePiece(new Bishop(Color.BLACK), 2, 1);
        table.placePiece(new Rook(Color.BLACK), 3, 6);
        table.placePiece(new Pawn(Color.BLACK), 2, 5);
        TestWriter.writeString("before making a move:");
        TestWriter.writeTable(table);
        game.makeMove(2);
        TestWriter.writeString("after making a move:");
        TestWriter.writeTable(table);
    }

    private void clearTable(Table table) {
        for (int i = 0; i < table.height; i++) {
            for (int j = 0; j < table.width; j++) {
                table.fields[i][j].piece = null;
            }
        }
    }
}