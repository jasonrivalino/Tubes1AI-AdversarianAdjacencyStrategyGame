import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Minimax extends Bot {
    private String symbol       = "O";
    private String symbolEnemy  = "X";
    private int depth   = 2;
    private int limit   = 5;
    @Override
    public int[] move(Button[][] board, String symbol, int roundsLeft) {
        if (roundsLeft < this.depth){
            this.depth = roundsLeft;
        }
        final int finalDepth = this.depth;
        String symbolEnemy = symbol.equals("X") ? "O" : "X";
        this.symbol = symbol;
        this.symbolEnemy = symbolEnemy;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<int[]> future = executor.submit(new Callable<int[]>() {
            @Override
            public int[] call() throws Exception {                
                Button[][] tempboard = new Button[8][8];
                String temp1;
                int maxelement = Integer.MIN_VALUE;
                int[] coordinate = new int[2];
                for (int i = 0; i < 8; i++) { // cek kosong
                    for (int j = 0; j < 8; j++) { // cek kosong
                        if (board[i][j].getText().equals("")){
                            for (int k = 0; k < 8; k++) {
                                for (int l = 0; l < 8; l++) {
                                    temp1 = board[k][l].getText();
                                    tempboard[k][l] = new Button();
                                    tempboard[k][l].setText(temp1);
                                }
                            }
                            tempboard[i][j].setText(symbol);
                            updateGameBoard(i,j, tempboard,symbolEnemy);
                            int temp = minimax(tempboard, finalDepth, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                            if (temp > maxelement){
                                maxelement = temp;
                                coordinate[0] = i;
                                coordinate[1] = j;
                            }
                        }
                    }
                }
                return coordinate;
            }
        });

        int[] coordinate;
        try {
            coordinate = future.get(this.limit, TimeUnit.SECONDS); // menunggu hingga 5 detik
        } catch (InterruptedException | ExecutionException e) {
            // Terjadi kesalahan saat eksekusi
            coordinate = getRandomMove(board);
        } catch (TimeoutException e) {
            // Melebihi batas waktu 5 detik
            future.cancel(true); // Menghentikan eksekusi
            coordinate = getRandomMove(board);
        }
        
        executor.shutdown(); // Menghentikan executor service
        return coordinate;
    }

    private List<int[]> getValidMoves(Button[][] board) {
        List<int[]> validMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getText().equals("")) {
                    validMoves.add(new int[]{i, j});
                }
            }
        }
        return validMoves;
    }

    private int[] getRandomMove(Button[][] board) {
        List<int[]> validMoves = getValidMoves(board);
        if (!validMoves.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(validMoves.size());
            return validMoves.get(randomIndex);
        } else {
            return null;
        }
    }

    public int getObjective2(Button[][] board){
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getText().equals(this.symbol)) {
                    count += 1;
                } else if (board[i][j].getText().equals(this.symbolEnemy)){
                    count -= 1;
                } else{

                }

            }
        }
        return count;
    }

    public int minimax(Button[][] buttons, int depth, boolean isMaximizing, int alpha, int beta) {
        Button[][] tempboard = new Button[8][8];
        String temp1;

        if (depth == 0){
            int[][] matrix_value = new int[8][8];
            int bestmove = getObjective2(buttons);
            return bestmove;
        }
        else if (isMaximizing) { // bot
            int maxScore = Integer.MIN_VALUE;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (buttons[i][j].getText().equals("")) {

                        for (int k = 0; k < 8; k++) {
                            for (int l = 0; l < 8; l++) {
                                temp1 = buttons[k][l].getText();
                                tempboard[k][l] = new Button();
                                tempboard[k][l].setText(temp1);
                            }
                        }

                        tempboard[i][j].setText(this.symbol);
                        updateGameBoard(i, j, tempboard,this.symbolEnemy);
                        int score = minimax(tempboard, depth - 1, false, alpha, beta);
                        maxScore = Math.max(maxScore, score);
                        alpha = Math.max(alpha, score);
                        if (beta <= alpha) {
                             break;
                        }
                    }
                }
            }
            return maxScore;

        } else { // player
            int minScore = Integer.MAX_VALUE;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (buttons[i][j].getText().equals("")) {
                        for (int k = 0; k < 8; k++) {
                            for (int l = 0; l < 8; l++) {
                                temp1 = buttons[k][l].getText();
                                tempboard[k][l] = new Button();
                                tempboard[k][l].setText(temp1);
                            }
                        }

                        tempboard[i][j].setText(this.symbolEnemy);
                        updateGameBoard(i, j, tempboard,this.symbol);
                        int score = minimax(tempboard, depth - 1, true, alpha, beta);
                        minScore = Math.min(minScore, score);
                        beta = Math.min(beta, score);
                        if (beta <= alpha) {
                             break;
                        }
                    }
                }
            }
            return minScore;
        }
    }

    private void updateGameBoard(int i, int j, Button[][] board, String enemy) {
        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0)
            startRow = i;
        else
            startRow = i - 1;

        if (i + 1 >= 8)
            endRow = i;
        else              
            endRow = i + 1;

        if (j - 1 < 0)  
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= 8) 
            endColumn = j;
        else
            endColumn = j + 1;

        for (int x = startRow; x <= endRow; x++) {
            setPlayerScore(x, j, board, enemy);
        }

        for (int y = startColumn; y <= endColumn; y++) {
            setPlayerScore(i, y, board, enemy);
        }
    }

    private void setPlayerScore(int i, int j, Button[][] board, String enemy){
        if (enemy.equals(this.symbol)) {
            if (board[i][j].getText().equals(this.symbol)) {
                board[i][j].setText(this.symbolEnemy);

            }
        } else if (board[i][j].getText().equals(this.symbolEnemy)) {
            board[i][j].setText(this.symbol);

        }
    }
}