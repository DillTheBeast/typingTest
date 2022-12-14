package typingTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private TextField inputWord;

    @FXML
    private ImageView correct;

    @FXML
    private ImageView incorrect;

    @FXML
    private Button playAgain;

    @FXML
    private Text instructions;

    ArrayList<String> words = new ArrayList<>();
    private int wordCounter = 0;
    private int first = 1;
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private int timer = 60;
    private int countAll = 0;
    private int counter = 0;
    private int idx = 0;

    @FXML
    void onPlayAgainClick(ActionEvent event) {
        wordCounter = 0;
        first = 1;
        timer = 60;
        countAll = 0;
        counter = 0;
        idx = 0;
        inputWord.setDisable(false);
        correct.setOpacity(0);
        incorrect.setOpacity(0);
        playAgain.setVisible(false);
        playAgain.setDisable(true);
        instructions.setVisible(true);
        seconds.setText("60");
        addToList();
        Collections.shuffle(words);
        currentWord.setText(words.get(wordCounter));
        nextWord.setText(words.get(wordCounter + 1));
        wordCounter++;
        inputWord.setText("");
        wpm.getText();
        wpm.setText(String.valueOf(idx));
        currentWord.setText(words.get(wordCounter));
        nextWord.setText(words.get(wordCounter + 1));
        executor.scheduleAtFixedRate(r, 0, 1, TimeUnit.SECONDS);
    }

    public void addToList() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("/Users/dillonmaltese/Documents/GitHub/typingTest/src/wordsList"));
        String line = reader.readLine();
        while(line != null) {
            words.add(line);
            line = reader.readLine();
        }
        reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            if(timer > -1 ) {
                seconds.setText(String.valueOf(timer));
                timer -= 1;
            }
            else { 
                if(timer == -1) {
                    inputWord.setDisable(true);
                    inputWord.setText("Game Over");
                }
                if(timer == -4) {
                    playAgain.setVisible(true);
                    playAgain.setDisable(false);
                    executor.shutdown();
                }
                timer -= 1;
            }
        }
    };
    
    Runnable fadeCorrect = new Runnable() {
        @Override
        public void run() {
            correct.setOpacity(0);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            correct.setOpacity(50);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            correct.setOpacity(100);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            correct.setOpacity(0);
        }
    };

    
    Runnable fadeWrong = new Runnable() {
        @Override
        public void run() {
            incorrect.setOpacity(0);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            incorrect.setOpacity(50);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            incorrect.setOpacity(100);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            incorrect.setOpacity(0);
        }
    };
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        correct.setOpacity(0);
        incorrect.setOpacity(0);
        playAgain.setVisible(false);
        playAgain.setDisable(true);
        seconds.setText("60");
        addToList();
        Collections.shuffle(words);
        currentWord.setText(words.get(wordCounter));
        nextWord.setText(words.get(wordCounter + 1));
        wordCounter++;
    }

    @FXML
    void startGame(KeyEvent ke) {
        if(ke.getCode().equals(KeyCode.SPACE)) {
            if(first == 1) {
                first = 0;
                instructions.setVisible(false);
                executor.scheduleAtFixedRate(r, 0, 1, TimeUnit.SECONDS);
                return;
            }
            else if(inputWord.getText().equals(" " + currentWord.getText())) {
                idx++;
                countAll++;

                Thread t = new Thread(fadeCorrect);
                t.start();
            }
            else {
                Thread t = new Thread(fadeWrong);
                t.start();
            }
            
            inputWord.setText("");
            wpm.getText();
            wpm.setText(String.valueOf(idx));
            wordCounter+=1;
            currentWord.setText(words.get(wordCounter));
            nextWord.setText(words.get(wordCounter + 1));
        }
    }
    }
    
