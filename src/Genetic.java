import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genetic extends Bot {
    private String botSymbol = "O";
    private String enemySymbol = "X";
    private static int POPULATION_SIZE = 10;

    @Override
    public int[] move(Button[][] board, String symbol, int roundLeft) {

        String symbolEnemy = symbol.equals("X") ? "O" : "X";
        this.botSymbol = symbol;
        this.enemySymbol = symbolEnemy;
        int[] bestMove;
        do {
            List<Point> population = initializePopulation(board);
            
            List<Integer> fitnessScores = new ArrayList<>();
            for (int i = 0; i < POPULATION_SIZE; i++) {
                fitnessScores.add(getFitness(population.get(i), board));
            }
            List<Point> selectedParents = selection(population, fitnessScores);
            List<Point> offspring = new ArrayList<>();
            for (int i = 0; i < selectedParents.size(); i++) {
                Point temp = crossover(selectedParents.get(i), selectedParents.get((i + 1) % selectedParents.size()));
                offspring.add(mutate(temp));
            }
            bestMove = getBestMove(offspring, board);
        } while (!isValidMove(new Point(bestMove[0], bestMove[1]), board));
        return bestMove;
    }

    private int[] getBestMove(List<Point> offspring, Button[][] board) {
        int[] bestMove = new int[2];
        int bestFitness = Integer.MIN_VALUE;
        for (Point p : offspring) {
            int fitness = getFitness(p, board);
            if (fitness > bestFitness) {
                bestMove[0] = p.getY();
                bestMove[1] = p.getX();
                bestFitness = fitness;
            }
        }
        return bestMove;
    }

    private List<Point> initializePopulation(Button[][] board) {
        List<Point> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[] coords = getRandomMove(board);
            int x = coords[0];
            int y = coords[1];
            population.add(new Point(x, y));
        }
        return population;
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
            return new int[]{0, 0};
        }
    }

    private List<Point> selection(List<Point> population, List<Integer> fitnessScores) {
        double totalFitness = 0;
        for (double fitness : fitnessScores) {
            totalFitness += fitness;
        }
    
        List<Point> selectedParents = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            double randomValue = Math.random() * totalFitness;
            double cumulativeFitness = 0;
            for (int j = 0; j < population.size(); j++) {
                cumulativeFitness += fitnessScores.get(j);
                if (cumulativeFitness >= randomValue) {
                    selectedParents.add(population.get(j));
                    break;
                }
            }
        }
    
        return selectedParents;
    }
    
    private Point crossover(Point a, Point b) {
        return new Point(a.getY(), b.getX());
    }

    private Point mutate(Point p) {
        return new Point(p.getY(), (int) (Math.random() * 8));
    }

    private boolean isValidMove(Point p, Button[][] buttons) {
        return buttons[p.getY()][p.getX()].getText().equals("");
    }

    public int getFitness(Point p, Button[][] board){
        Button[][] tempboard = new Button[8][8];
        String temp1;
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 8; l++) {
                temp1 = board[k][l].getText();
                tempboard[k][l] = new Button();
                tempboard[k][l].setText(temp1);
            }
        }
        tempboard[p.getY()][p.getX()].setText(this.botSymbol);
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tempboard[i][j].getText().equals(this.botSymbol)) {
                    count += 1;
                } else if (tempboard[i][j].getText().equals(this.enemySymbol)){
                    count -= 1;
                } else{

                }

            }
        }
        return count;
    }
}

class Point {
    private int x;
    private int y;

    public Point(int y, int x) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}