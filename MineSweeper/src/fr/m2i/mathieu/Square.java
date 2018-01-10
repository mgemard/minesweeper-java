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

    private boolean isOpen;
    private boolean bombClicked;
    private boolean clickable;

    private Rectangle border;
    private Text text;
    private Button button;

    private Callback callback;

    public Square(int i, int j, int squareSize) {
        this.i = i;
        this.j = j;
        this.squareSize = squareSize;
    }

    public void init(boolean b) {
        hasBomb = b;
        
        isOpen = false;
        bombClicked = false;
        clickable = true;
        text = new Text();

        border = new Rectangle(squareSize, squareSize, Color.LIGHTGREY);
        button = new Button();
        getChildren().clear();
        getChildren().addAll(border, button);

        setTranslateX(j * squareSize);
        setTranslateY(i * squareSize);
        
        button.setMaxWidth(squareSize);
        button.setMaxHeight(squareSize);
        button.setStyle("-fx-focus-color: transparent;-fx-background-insets: -1.4, 0, 1, 2;\r\n" + "");
        button.setOnMouseClicked(e -> click(e));

    }

    private void click(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY && clickable) {
            if (isOpen)
                return;
            if (hasBomb) {
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
                File file = new File("src/flag.png");
                ImageView image = new ImageView(new Image(file.toURI().toString()));
                image.setFitHeight(squareSize);
                image.setFitWidth(squareSize);
                button.setGraphic(image);
                button.setPadding(Insets.EMPTY);
            }
        }
    }

    public void open() {
        isOpen = true;
        button.setVisible(false);
        border.setStroke(Color.WHITE);

        text.setFont(Font.font(18));

        if (hasBomb) {
            if (bombClicked) {
                this.border = new Rectangle(squareSize, squareSize, Color.RED);
            }
            File file = new File("src/bomb.png");
            ImageView image = new ImageView(new Image(file.toURI().toString()));
            image.setFitHeight(squareSize);
            image.setFitWidth(squareSize);
            getChildren().clear();
            getChildren().addAll(border, image);
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
}
