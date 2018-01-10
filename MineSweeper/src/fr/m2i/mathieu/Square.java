package fr.m2i.mathieu;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Square extends StackPane {

    private int i, j;
    private int squareSize;
    private boolean hasBomb;
    private int nbNeighbors;

    private boolean isOpen = false;
    private boolean bombClicked = false;
    private boolean clickable = true;

    private Rectangle border;
    private Text text = new Text();
    private Button button = new Button();

    private Callback callback;

    public Square(int i, int j, int squareSize) {

        this.i = i;
        this.j = j;
        this.squareSize = squareSize;
        this.border = new Rectangle(squareSize, squareSize, Color.LIGHTGREY);

        getChildren().addAll(border, button);

        setTranslateX(j * squareSize);
        setTranslateY(i * squareSize);
        button.setMaxWidth(squareSize);
        button.setMaxHeight(squareSize);
        button.setStyle("-fx-focus-color: transparent;-fx-background-insets: -1.4, 0, 1, 2;\r\n" + "");
        button.setOnMouseClicked(e -> click(e));

        // label.setMaxWidth(mineSize-2);
        // label.setAlignment(Pos.CENTER);

        // setHalignment(label, HPos.CENTER);

        // setAlignment(Pos.CENTER_RIGHT);
        // setFillWidth(label, true);
        // label.setMaxWidth(Double.MAX_VALUE);
        //// border.setStroke(Color.LIGHTGRAY);
        //
        // text.setFont(Font.font(18));
        //// text.setText(hasBomb ? "X" : "");
        // text.setText("X");
        // text.setVisible(false);
        //
        // Button button = new Button("X");
        // button.setOnMouseClicked(e -> click());
        //
        // getChildren().addAll(button);

    }

    private void click(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY && clickable) {
            System.out.println("Left button clicked");
            if (isOpen)
                return;
            if (hasBomb) {
                // text.setText("X");
                bombClicked = true;
                callback.gameOver();
            } else if (nbNeighbors == 0) {
                callback.openNeighbors(i, j);
            } else {
                open();
            }

        } else if (e.getButton() == MouseButton.SECONDARY) {
            System.out.println("Right button clicked");
            clickable = !clickable;
            if (clickable) {
                button.setText("");
                button.setGraphic(null);
            } else {
                // button.setText("!");
                File file = new File("src/flag.png");
                ImageView image = new ImageView(new Image(file.toURI().toString()));

                image.setFitHeight(squareSize);
                image.setFitWidth(squareSize);
                // button.setAlignment(Pos.BASELINE_LEFT);
                button.setGraphic(image);
                // button.setContentDisplay(ContentDisplay.CENTER);
                button.setPadding(Insets.EMPTY);

                // getChildren().clear();
                // getChildren().addAll(border, text);

            }
        }
    }

    public void open() {
        //
        // if (hasBomb) {
        // System.out.println("Game Over");
        // scene.setRoot(createContent());
        // return;
        // }
        //
        isOpen = true;
        button.setVisible(false);
        border.setStroke(Color.WHITE);

        text.setFont(Font.font(18));

        if (hasBomb) {
            // text.setText("X");

            // setStyle("-fx-padding:0px;");
            if (bombClicked) {
                this.border = new Rectangle(squareSize, squareSize, Color.RED);
            }

            // button.setText("!");
            File file = new File("src/bomb.png");
            ImageView image = new ImageView(new Image(file.toURI().toString()));

            image.setFitHeight(squareSize);
            image.setFitWidth(squareSize);
            // button.setAlignment(Pos.BASELINE_LEFT);

            getChildren().clear();
            getChildren().addAll(border, image);

            if (bombClicked) {
                // text.setFill(Color.RED);
            }
        } else if (nbNeighbors != 0) {
            text.setText(String.valueOf(this.nbNeighbors));
            text.setFont(Font.font(null, FontWeight.BOLD, 16));

            switch (this.nbNeighbors) {
            case 1:
                text.setFill(Color.BLUE);
                break;
            case 2:
                text.setFill(Color.rgb(0, 128, 0));
                break;
            case 3:
                text.setFill(Color.RED);
                break;
            case 4:
                text.setFill(Color.rgb(75, 0, 130));
                break;
            case 5:
                text.setFill(Color.rgb(128, 0, 00));
                break;
            case 6:
                text.setFill(Color.rgb(43, 145, 145));
                break;
            case 7:
                text.setFill(Color.BLACK);
                break;
            case 8:
                text.setFill(Color.DARKGRAY);
                break;

            default:
                break;
            }

            text.setVisible(true);
            setStyle("-fx-padding:0px;");

            getChildren().clear();
            getChildren().addAll(border, text);

            setTranslateX(j * squareSize);
            setTranslateY(i * squareSize);

            // border.setFill(null);
            //
            // if (text.getText().isEmpty()) {
            // getNeighbors(this).forEach(Tile::open);
            // }
        }
    }

    public void setNbNeighbors(int nbNeighbors) {
        this.nbNeighbors = nbNeighbors;
    }

    public int getNbNeighbors() {
        return nbNeighbors;
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    public void register(Callback callback) {
        this.callback = callback;
    }

    public void reset(boolean b) {
        this.hasBomb = b;
        // TODO Auto-generated method stub
        
    }
}
