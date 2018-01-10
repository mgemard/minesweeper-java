package fr.m2i.mathieu;

import java.io.File;
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Game extends Application implements Callback {

    private static final int SQUARE_SIZE = 20;
    // private static final int NB_SQUARE_X = 50;
    // private static final int NB_SQUARE_Y = 95;
    private static final int NB_SQUARE_X = 30;
    private static final int NB_SQUARE_Y = 30;

    private static final int HEIGHT = SQUARE_SIZE * NB_SQUARE_X;
    private static final int WIDTH = SQUARE_SIZE * NB_SQUARE_Y;
    private static final int SCORE_WIDTH = 2 * SQUARE_SIZE;
    private Square[][] squares = new Square[NB_SQUARE_X][NB_SQUARE_Y];
    private boolean[][] squaresToOpen;

    private Pane board;
    private Button buttonNewGame;

    public Game() {
    }

    public void startGame() {
        launch();
    }


    @Override
    public void start(Stage primaryStage) {
        try {
           
            primaryStage.setTitle("Minesweeper");

            GridPane gridPane = new GridPane();

            Pane score = new Pane();
            score.setMinHeight(SCORE_WIDTH);

            // Text text = new Text("hi");
            // text.setFill(Color.BLACK);
            // text.setVisible(true);
            // score.getChildren().add(text);
            //

            // button.setText("!");

            buttonNewGame = new Button();
            buttonNewGame.setOnMouseClicked(e -> newGame());
            
            score.getChildren().add(buttonNewGame);

            board = new Pane();
            board.setPrefSize(WIDTH, HEIGHT);
            
            for (int i = 0; i < NB_SQUARE_X; i++) {
                for (int j = 0; j < NB_SQUARE_Y; j++) {
                    Random rand = new Random();
                    Square square = new Square(i, j, SQUARE_SIZE);
                    square.register(this);
                    this.squares[i][j] = square;
                }
            }
            for (int i = 0; i < NB_SQUARE_X; i++) {
                for (int j = 0; j < NB_SQUARE_Y; j++) {
                    this.squares[i][j].setNbNeighbors(getNbNeighbors(i, j));
                    board.getChildren().add(this.squares[i][j]);

                }
            }
            
            gridPane.add(score, 0, 0, 1, 1);
            gridPane.add(board, 0, 1, 1, 1);

            // Color color = Color.rgb(0, 0, 0, 0.35);
            // BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY,
            // Insets.EMPTY);
            // root.setBackground(new Background(fill));

            Scene scene = new Scene(gridPane, WIDTH, HEIGHT + SCORE_WIDTH);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
            
            
            
            
            initGame();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private void initGame() {
        setSmileyImage("smiley_happy");
        
        for (int i = 0; i < NB_SQUARE_X; i++) {
            for (int j = 0; j < NB_SQUARE_Y; j++) {
                Random rand = new Random();
                squares[i][j].reset(rand.nextFloat() < 0.1);
            }
        }
   
        
    }


    private void newGame() {
        initGame();
    }

    private int getNbNeighbors(int i, int j) {
        int nbNeighbors = 0;
        if (i > 0) {
            nbNeighbors += squares[i - 1][j].hasBomb() ? 1 : 0;
            if (j > 0) {
                nbNeighbors += squares[i - 1][j - 1].hasBomb() ? 1 : 0;
            }
            if (j < NB_SQUARE_Y - 1) {
                nbNeighbors += squares[i - 1][j + 1].hasBomb() ? 1 : 0;
            }
        }
        if (i < NB_SQUARE_X - 1) {
            nbNeighbors += squares[i + 1][j].hasBomb() ? 1 : 0;
            if (j > 0) {
                nbNeighbors += squares[i + 1][j - 1].hasBomb() ? 1 : 0;
            }
            if (j < NB_SQUARE_Y - 1) {
                nbNeighbors += squares[i + 1][j + 1].hasBomb() ? 1 : 0;
            }
        }
        if (j > 0) {
            nbNeighbors += squares[i][j - 1].hasBomb() ? 1 : 0;
        }
        if (j < NB_SQUARE_Y - 1) {
            nbNeighbors += squares[i][j + 1].hasBomb() ? 1 : 0;
        }

        // System.out.println(i + ":" + j + " " + nbNeighbors);
        return nbNeighbors;
    }

    @Override
    public void gameOver() {
        
//
        
        setSmileyImage("smiley_sad");
        System.out.println("game over");
        for (int i = 0; i < NB_SQUARE_X; i++) {
            for (int j = 0; j < NB_SQUARE_Y; j++) {
                squares[i][j].open();
            }
        }

    }

    @Override
    public void openNeighbors(int x, int y) {
        System.out.println("no neighbors clicked " + x + ":" + y);
        squaresToOpen = new boolean[NB_SQUARE_X][NB_SQUARE_Y];
        // squaresToOpen[x][y] = true;

        open(x, y);
    }

    public void open(int x, int y) {
        if (x < 0 || y < 0 || x >= NB_SQUARE_X || y >= NB_SQUARE_Y || squaresToOpen[x][y])
            return;
        squaresToOpen[x][y] = true;
        squares[x][y].open();
        if (squares[x][y].getNbNeighbors() == 0) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    // if (i >= 0 && j >= 0 && i <= NB_SQUARE_X - 1 && j <= NB_SQUARE_Y - 1) {
                    open(i, j);
                    // }
                }
            }
        }

    }
    
    public void setSmileyImage(String smiley) {
        File file = new File("src/"+smiley+".png");
        ImageView image = new ImageView(new Image(file.toURI().toString()));
        image.setFitHeight(1.5 * SQUARE_SIZE);
        image.setFitWidth(1.5 * SQUARE_SIZE);
        buttonNewGame.setGraphic(image);
        buttonNewGame.setPadding(Insets.EMPTY);
    }
}
