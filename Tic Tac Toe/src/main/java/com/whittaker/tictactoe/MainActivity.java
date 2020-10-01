package com.whittaker.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TextView messageTextView;
    private Button newGameButton;
    private Button[][] gameGrid = new Button[3][3];
    private boolean player1Turn = true;
    private int numTurns = 0;
    private int i, j;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageTextView = findViewById(R.id.messageTextView);
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
        newGameButton = findViewById(R.id.newGameButton);

        //sets separate listener for the newGameButton
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });

        //sets the gameGrid to corresponding squares
        //sets a separate click listener for each square
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                String squareID = "square" + ((3 * i) + j + 1);
                int squareRID = getResources().getIdentifier(squareID, "id", getPackageName());
                gameGrid[i][j] = findViewById(squareRID);
                gameGrid[i][j].setOnClickListener(this);
            }
        }


    }

    //runs every time a square is clicked
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        //if the square clicked is full then don't do anything
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        //sets the button text to X or O depending on which turn it is
        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        //used to determine if there is a tie
        numTurns++;

        //displays endgame message or updates messageTextView
        if (checkWinner()) {
            if (player1Turn) {
                messageTextView.setText(R.string.xWinsStringR);
            } else {
                messageTextView.setText(R.string.oWinsStringR);
            }
        } else if (numTurns == 9) {
            messageTextView.setText(R.string.tieStringR);
        } else {
            player1Turn = !player1Turn;
            displayTurn();
        }
    }

    //scans the board to see if someone won
    private boolean checkWinner() {

        //used to compare each square based on String values
        String[][] gameGridString = new String[3][3];

        //filling String 2D array
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                gameGridString[i][j] = gameGrid[i][j].getText().toString();
            }
        }

        for (i = 0; i < 3; i++) {

            //scanning each row
            if (gameGridString[i][0].equals(gameGridString[i][1])
                    && gameGridString[i][0].equals(gameGridString[i][2])
                    && !gameGridString[i][0].equals(""))
                return true;

            //scanning each column
            if (gameGridString[0][i].equals(gameGridString[1][i])
                    && gameGridString[0][i].equals(gameGridString[2][i])
                    && !gameGridString[0][i].equals(""))
                return true;

            //scanning LTR diagonal
            if (gameGridString[0][0].equals(gameGridString[1][1])
                    && gameGridString[0][0].equals(gameGridString[2][2])
                    && !gameGridString[0][0].equals(""))
                return true;

            //scanning RTL diagonal
            if (gameGridString[0][2].equals(gameGridString[1][1])
                    && gameGridString[0][2].equals(gameGridString[2][0])
                    && !gameGridString[0][2].equals(""))
                return true;
        }
        return false;
    }

    //updates messageTextView based on player1Turn bool
    private void displayTurn() {
        if (player1Turn) {
            messageTextView.setText(R.string.xTurnString);
        } else {
            messageTextView.setText(R.string.oTurnString);
        }
    }

    //reset the game
    private void newGame() {

        //sets all gameGrid squares to blank
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                gameGrid[i][j].setText("");
            }
        }

        numTurns = 0;
        player1Turn = true;
        displayTurn();
    }

    //saves number of turns take and who's turn it is
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putInt("numTurns", numTurns);
        editor.putBoolean("player1Turn", player1Turn);
        editor.apply();
    }

    //retrieves number of turns take and who's turn it is
    @Override
    public void onResume() {
        super.onResume();
        numTurns = savedValues.getInt("numTurns", 0);
        player1Turn = savedValues.getBoolean("player1Turn", true);
    }

}