import javafx.scene.control.Button;

public abstract class Bot {
    public int[] move(Button[][] board) {
        int x, y;
        int i = 0;
        do {
            x = (int) (Math.random() * 8);
            y = (int) (Math.random() * 8);
            i++;
            System.out.println(x + " " + y);
        } while (!board[x][y].getText().equals("") && i < 5);
        return new int[]{x, y};
    }

    public int[] move(Button[][] board, String symbol) {
        return move(board);
    }

    public int[] move(Button[][] board, String symbol, int roundsLeft) {
        return move(board);
    }
}
