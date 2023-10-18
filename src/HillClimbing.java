import java.util.Random;
import java.util.Vector;

import javafx.scene.control.Button;

public class HillClimbing extends Bot {
    @Override
    public int[] move(Button[][] board, String symbol){
        String enemy = symbol.equals("X") ? "O" : "X";
        int[][] matrix_value = new int[8][8];
        matrix_value = getObjective(board, matrix_value, enemy);
        int[] a = getBestMove(matrix_value);
        return a;
    }

    public int isUpfilled(int rowIndex, int colIndex, Button[][] board, String enemy) {
        if ((board[rowIndex - 1][colIndex].getText().equals(enemy))){
            return 1;
        } else {
            return 0;
        }

    }
    public int isLeftfilled(int rowIndex, int colIndex, Button[][] board, String enemy){
        if ((board[rowIndex][colIndex - 1].getText().equals(enemy))){
            return 1;
        } else {
            return 0;
        }
    }
    public int isRightfilled(int rowIndex, int colIndex, Button[][] board, String enemy){
        if ((board[rowIndex][colIndex + 1].getText().equals(enemy))){
            return 1;
        } else {
            return 0;
        }
    }
    public int isDownfilled(int rowIndex, int colIndex, Button[][] board, String enemy){
        if ((board[rowIndex + 1][colIndex].getText().equals(enemy))){
            return 1;
        } else {
            return 0;
        }
    }
    public int getMaxMatriks(int[][] matrix){
        int maxElement = matrix[0][0];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] >= maxElement) {
                    maxElement = matrix[i][j];
                }
            }
        }
        return maxElement;
    }

    public int[] getBestMove(int[][] matrix){
        int maxElement = getMaxMatriks(matrix);
        Vector<int[]> possible_move = new Vector<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] >= maxElement) {
                    int[] temp = {i, j};
                    possible_move.add(temp);
                }
            }

        }
        Random random = new Random();
        int randomIndex = random.nextInt(possible_move.size());
        return (possible_move.get(randomIndex));
    }
    public int[][] getObjective(Button[][] board, int[][] matrix_value, String enemy){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getText().equals("")){
                    if (i > 0){
                        if (i == 7){
                            if (j == 0){
                                matrix_value[i][j] = isUpfilled(i, j, board, enemy) + isRightfilled(i, j, board, enemy);
                            }
                            else if (j == 7){
                                matrix_value[i][j] = isUpfilled(i, j, board, enemy) + isLeftfilled(i, j, board, enemy);
                            }
                            else {
                                matrix_value[i][j] = isUpfilled(i, j, board, enemy) + isLeftfilled(i, j, board, enemy) + isRightfilled(i, j, board, enemy);
                            }
                        }
                        else {
                            if (j == 0){
                                matrix_value[i][j] = isUpfilled(i, j, board, enemy) + isDownfilled(i, j, board, enemy) + isRightfilled(i, j, board, enemy);
                            }
                            else if (j == 7) {
                                matrix_value[i][j] = isUpfilled(i, j, board, enemy) + isLeftfilled(i, j, board, enemy) + isDownfilled(i, j, board, enemy);
                            } else {
                                matrix_value[i][j] = isUpfilled(i, j, board, enemy) + isLeftfilled(i, j, board, enemy) + isDownfilled(i, j, board, enemy) + isRightfilled(i, j, board, enemy);
                            }

                        }
                    } else {
                        if (j == 0){
                            matrix_value[i][j] = isDownfilled(i, j, board, enemy) + isRightfilled(i, j, board, enemy);
                        }
                        else if (j == 7){
                            matrix_value[i][j] = isDownfilled(i, j, board, enemy) + isLeftfilled(i, j, board, enemy);
                        }
                        else {
                            matrix_value[i][j] = isDownfilled(i, j, board, enemy) + isLeftfilled(i, j, board, enemy) + isRightfilled(i, j, board, enemy);
                        }
                    }
                }
            }
        }
        return matrix_value;
    }
}
