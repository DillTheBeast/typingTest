package typingTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class Controller implements Initializable{

    @FXML
    private Text wpm;

    @FXML
    private Text seconds;

    @FXML
    private Text accuracy;

    @FXML
    private Text currentWord;

    @FXML
    private Text nextWord;

    @FXML
    private Text inputWord;

    @FXML
    private ImageView correct;

    @FXML
    private ImageView incorrect;

    @FXML
    private Button playAgain;

    ArrayList<String> words = new ArrayList<>();
    private int wordCounter = 0;

    public void addToList() {
        //add words to the array list
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("wordsList"));
            String line = reader.readLine();
            while(line != null) {
                words.add(line);
                //read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playAgain.setVisible(false);     
        playAgain.setDisable(true);
        seconds.setText("60");
        addToList();
        Collections.shuffle(words);
        currentWord.setText(words.get(wordCounter));   
        currentWord.setText(words.get(wordCounter+1));
        wordCounter++;
    }

    public void startGame(KeyEvent ke) {
        if(first == 1) {
            first = 0;
            executor.scheduleAtFixedRate(r, 0, 1, TimeUnit.SECONDS);
        }
        if(ke.getCode().equals(KeyCode.SPACE)) {
            countAll++;
            if(currentWord.getText().equals(inputWord.getText())) {
                counter++;
                wpm.setText(String.valueOf(counter));

                Thread t = new Thread(fadeCorrect);
                t.start();
            }
            else {
                Thread t = new Thread(fadeWrong);
                t.start();
            }
        }
    }
}
