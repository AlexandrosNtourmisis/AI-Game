import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Alexandros Ntourmisis - 198515
 * @dateUpdated 15/12/2020
 * @version v1 (for AI project)
 * --------------------------------------------------------------------------
 * Extra Details
 * --------------------------------------------------------------------------
 * Instructor: Dr. Vogiatzis
 * Course: Artificial Intelligence Principle - ITC 4680 - Fall Semester
 * College: American College of Greece
 * Purpose: The purpose of this project is to test two search strategies
 * --------------------------------------------------------------------------
 * --Rules--
 * Players: two-player based game
 * Board: 8x8 squares
 * Game Style: Round Based
 * Goal to Win: Control more squares than the opponent until game ends
 * Instructions:
 * 1. Each round, player places a piece on a square
 * 2. Rows and Columns [1,1] - Game Board Coordinates
 * 3. Game ends when no one can make any new move
 * 4. Black Agent plays first in the central squares (I implemented a feature
 * that the four central squares are already filled equally like othello/reversi)
 * 5. If a player captures a line for example 120 - places the point in the
 * empty square --> 121 -captures the other square as well --> 111.
 *
 *
 */

public class MainGame extends PlayGame {

    public MainGame() throws FileNotFoundException {
        super();


    }

    public static void main(String[] args) throws FileNotFoundException {
        new MainGame();
    }
}

class PlayGame {

    //Timer for playing
    Timer timer = new Timer();
    // Game's board, score and total score for the end
    int[][] gameBoard;
    int player1_Score = 0;
    int player2_Score = 0;
    int player1_tScore = 0;
    int player2_tScore = 0;
    //who plays ... start value on 1 so black player can play first
    //Can change the depth of search for Min Max
    AIBot player1 = new MinMax_BlackPlayer(1,6);
    AIBot player2= new Greedy_WhitePlayer(2);
    int playerPlaying = 1;
    //For the central squares
    public void squaresVal(int i, int j, int value){
        gameBoard[i][j] = value;
    }

    public void ai_Agents(AIBot agent){
        Point square = agent.play(gameBoard);
        int i = square.x;
        int j = square.y;
        //print the piece's board coordinates
        System.out.println("The " + agent.SquarePieceIdentifier() + " placed the piece in: ");
        System.out.println("Row: "+ i );
        System.out.println("Column: "+ j);
        //update board
        gameBoard = GameBoard.UpdateGameBoard(gameBoard,square, playerPlaying);
        //change player playing
        playerPlaying = (playerPlaying == 1) ? 2 : 1;
    }

    //Initiate Game
    public PlayGame() throws FileNotFoundException {
       Output();
        // Create Game Board 8x8
        gameBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j]=0;
            }
        }
        //Central Four Squares values already -- like the Reversi game
        squaresVal(3,4,1);
        squaresVal(4,3,1);
        squaresVal(3,3,2);
        squaresVal(4,4,2);
        //
        giveScore();
        Playing();
    }

    // starts for every player
    public void Playing(){
//        MinMax_BlackPlayer mclass = new MinMax_BlackPlayer(1,6);
//        mclass.getDepSearch();
        int  depth = MinMax_BlackPlayer.depSearch;

        System.out.println("=====================================");
        System.out.println("\n         -Board-                      \n");
        //display board with pieces
        for(int i = 0; i<8; i++)
        {
            for(int j = 0; j<8; j++)
            {
                System.out.print("  "+gameBoard[i][j]);
            }
            System.out.println();
        }
        System.out.println("\n=====================================");
        //checks legal moves
        if(GameBoard.legalMoves(gameBoard,1) || GameBoard.legalMoves(gameBoard,2)) {
            //calls this method to give points for every piece
            giveScore();
            //Black Player plays
            if (playerPlaying == 1) {
                if(GameBoard.legalMoves(gameBoard,1)) {
                    if (player1.whoPlaysNow()) {

                    } else {
                        // timer to play... can be used also to make the game more realistic by increasing time
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                ai_Agents(player1);
                                Playing();
                            }
                        }, 0*1000);
                    }
                }else{
                    //Black player has no legal moves to make
                    System.out.println("No moves for Black Player");
                    //give turn to player 2
                    playerPlaying = 2;
                    Playing();
                }
                //White Player plays
            } else {
                if(GameBoard.legalMoves(gameBoard,2)) {
                    if (player2.whoPlaysNow()) {

                    } else {
                        // timer to play... can be used also to make the game more realistic by increasing time
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                ai_Agents(player2);
                                Playing();
                            }
                        }, 0*1000);
                    }
                }else{
                    //Black player has no legal moves to make
                    System.out.println("No moves for Player 2");
                    //give turn to player 2
                    playerPlaying = 1;
                    Playing();
                }
            }
        }else{
            //GAME FINISHED as player 1 and player 2 has no legal moves left to play
            System.out.println("\n\n================================");
            System.out.println("            RESULTS");
            int winner = GameBoard.whoWON(gameBoard);
            if(winner==1) player1_tScore++;
            else if(winner==2) player2_tScore++;
            giveScore();
            //Print the results
            if(player1_Score > player2_Score){
                System.out.println("-------------------------------");
                System.out.println("And the winner is: Black Player");
                //  System.out.println("-------------------------------");
                // System.out.println("By using Min Max search");
                System.out.println("\nAlgorithm: Min Max (Alpha Beta)");
                // System.out.println("-------------------------------");
                System.out.println("\nSearch depth = " + depth);
                // System.out.println("-------------------------------");
                System.out.println("\nPlayer | Black " + "||" + " White |" );
                System.out.println("Score  |  "+ player1_Score + "   ||   " + player2_Score + "  |" );
                System.out.println("profit |  " + (player1_Score - player2_Score) + "   ||  " + (player2_Score - player1_Score + "  |" ));
                System.out.println("\n================================");
            }else if(player1_Score < player2_Score){
                System.out.println("-------------------------------");
                System.out.println("And the winner is: White Player");
                System.out.println("\nMethod: Greedy");
                System.out.println("\nPlayer | Black " + "||" + " White |" );
                System.out.println("Score  |  "+ player1_Score + "   ||   " + player2_Score + "  |" );
                System.out.println("profit |  " + (player1_Score - player2_Score) + "   ||  " + (player2_Score - player1_Score + "  |" ));
            }else{
                System.out.println("-------------------------------");
                System.out.println("The game ended in DRAW");
                System.out.println("\nPlayer | Black " + "||" + " White |" );
                System.out.println("Score  |  "+ player1_Score + "   ||   " + player2_Score + "  |" );
                System.out.println("profit |  " + (player1_Score - player2_Score) + "   ||  " + (player2_Score - player1_Score + "  |" ));
            }


            System.exit(1);

        }
    }

    //Gives score to the players
    public void giveScore(){
        int player1 = 0;
        int player2 = 0;
        //checks the value of the pieces and gives points depending on whether it is black or white
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(gameBoard[i][j] == 1) player1++;
                if(gameBoard[i][j] == 2) player2++;
            }
        }
        //assigns the current scores to the general scores
        player1_Score =player1;
        player2_Score =player2;
    }

    public void Output() throws FileNotFoundException {
        //Delete already content in the .Data file
        new PrintStream(new FileOutputStream("NtourmisisAlexandros-steps-Fall2020.data", false));
        //Write the output to the .data file
        PrintStream output = new PrintStream(new FileOutputStream("NtourmisisAlexandros-steps-Fall2020.data", true));
        System.setOut(output);
    }

}


class GameBoard {
    //---------------------------legal-Moves-------------------------------------
    public static ArrayList<Point> possMoves(int[][] gameBoard, int player){
        //a array list for possible moves to be used for loop
        ArrayList<Point> pMoves = new ArrayList<>();


//        ArrayList<Point> nPosMoves = new ArrayList<>();
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                if(canP(gameBoard,player,i,j)){
//                    pMoves.add(new Point(i,j));
//                    nPosMoves.add(new Point(i,j));
//                }
//            }
//        }

        //loops to find possible moves
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //if player can play to the coordinates
                if(move(gameBoard,player,i,j)){
                    //add the new coordinates
                    pMoves.add(new Point(i,j));
                }
            }
        }
        return pMoves;
    }


    public static boolean legalMoves(int[][] gameBoard, int player){
        return possMoves(gameBoard,player).size() > 0;
    }
//---------------------------------------------------------------------------

    //Searches for open squares
    public static ArrayList<Point> openSquares(int[][] gameBoard, int player, int i, int j){
        ArrayList<Point> allOpenSquares = new ArrayList<>();

        int move_i , move_j;
        int other_Player = ((player == 1) ? 2 : 1);

//-------------------------Movements ----------------------------------------------------------------------

        //STRAIGHT MOVEMENTS------------------------------------------------
        //Upwards Movement for player
        ArrayList<Point> upMove = new ArrayList<>();
        //Moving up [i-1,j]
        move_i = i - 1;
        move_j = j;
        // Checks if it is opponents
        while(move_i>0 && gameBoard[move_i][move_j] == other_Player){
            //adds the piece
            upMove.add(new Point(move_i,move_j));
            move_i--;
        }

        if(move_i>=0 && gameBoard[move_i][move_j] == player && upMove.size()>0){
            //adds it to open squares
            allOpenSquares.addAll(upMove);

        }

        //Downwards Movement for player
        ArrayList<Point> downMove = new ArrayList<>();
        //Moving up [i+1,j]
        move_i = i + 1;
        move_j = j;
        // Checks if it is opponents
        while(move_i<7 && gameBoard[move_i][move_j] == other_Player){
            downMove.add(new Point(move_i,move_j));
            move_i++;
        }
        if(move_i<=7 && gameBoard[move_i][move_j] == player && downMove.size()>0){
            //adds it to open squares
            allOpenSquares.addAll(downMove);
        }

        //Right Movement for player
        ArrayList<Point> rightMove = new ArrayList<>();
        move_i = i;
        move_j = j + 1;
        while(move_j<7 && gameBoard[move_i][move_j] == other_Player){
            // Checks if it is opponents
            rightMove.add(new Point(move_i,move_j));
            move_j++;
        }
        if(move_j<=7 && gameBoard[move_i][move_j] == player && rightMove.size()>0){
            //adds it to open squares
            allOpenSquares.addAll(rightMove);
        }

        //Left Movement for player
        ArrayList<Point> leftMove = new ArrayList<>();
        move_i = i;
        move_j = j - 1;
        while(move_j>0 && gameBoard[move_i][move_j] == other_Player){
            // Checks if it is opponents
            leftMove.add(new Point(move_i,move_j));
            move_j--;
        }
        if(move_j>=0 && gameBoard[move_i][move_j] == player && leftMove.size()>0){
            //adds it to open squares
            allOpenSquares.addAll(leftMove);
        }
        //------------------------------------------------------------------


        //DIAGONAL MOVEMENTS------------------------------------------------
        //move up right
        ArrayList<Point> upRightM = new ArrayList<>();
        move_i = i - 1;
        move_j = j + 1;

        // Checks if it is opponents
        while(move_i>0 && move_j<7 && gameBoard[move_i][move_j] == other_Player){
            upRightM.add(new Point(move_i,move_j));
            move_i--;
            move_j++;
        }
        if(move_i>=0 && move_j<=7 && gameBoard[move_i][move_j] == player && upRightM.size()>0){
            allOpenSquares.addAll(upRightM);
        }

        //move up left
        ArrayList<Point> upLeftM = new ArrayList<>();
        move_i = i - 1;
        move_j = j - 1;

        // Checks if it is opponents
        while(move_i>0 && move_j>0 && gameBoard[move_i][move_j] == other_Player){
            upLeftM.add(new Point(move_i,move_j));
            move_i--;
            move_j--;
        }
        if(move_i>=0 && move_j>=0 && gameBoard[move_i][move_j] == player && upLeftM.size()>0){
            allOpenSquares.addAll(upLeftM);
        }

        //move down right
        ArrayList<Point> downRightM = new ArrayList<>();
        move_i = i + 1;
        move_j = j + 1;

        // Checks if it is opponents
        while(move_i<7 && move_j<7 && gameBoard[move_i][move_j] == other_Player){
            downRightM.add(new Point(move_i,move_j));
            move_i++;
            move_j++;
        }
        if(move_i<=7 && move_j<=7 && gameBoard[move_i][move_j] == player && downRightM.size()>0){
            allOpenSquares.addAll(downRightM);
        }

        //move down left
        ArrayList<Point> downLeftM = new ArrayList<>();
        move_i = i + 1;
        move_j = j - 1;

        // Checks if it is opponents
        while(move_i<7 && move_j>0 && gameBoard[move_i][move_j] == other_Player){
            downLeftM.add(new Point(move_i,move_j));
            move_i++;
            move_j--;
        }
        if(move_i<=7 && move_j>=0 && gameBoard[move_i][move_j] == player && downLeftM.size()>0){
            allOpenSquares.addAll(downLeftM);
        }
        //------------------------------------------------------------------

        //all open pieces of squares
        return allOpenSquares;
//---------------------------------------------------------------------------------------------------------
    }

    //plays if there are open pieces left
    public static boolean move(int[][] gameBoard, int player, int i, int j){

//-------------------------Movements ----------------------------------------------------------------------
        if(gameBoard[i][j] != 0) return false;

        int move_i , move_2 , counter;

        int otherPlayer = ((player == 1) ? 2 : 1);

        //Up movement [i-1,j]
        move_i = i - 1;
        move_2 = j;
        counter = 0;

        //decrease i
        while(move_i>0 && gameBoard[move_i][move_2] == otherPlayer){
            move_i--;
            counter++;

        }
        if(move_i>=0 && gameBoard[move_i][move_2] == player && counter>0) return true;

        //Down movement [i+1,j]
        move_i = i + 1;
        move_2 = j;
        counter = 0;
        //increase i
        while(move_i<7 && gameBoard[move_i][move_2] == otherPlayer){
            move_i++;
            counter++;
        }
        if(move_i<=7 && gameBoard[move_i][move_2] == player && counter>0) return true;

        //Right movement [i,j+1]
        move_i = i;
        move_2 = j + 1;
        counter = 0;
        // increase j
        while(move_2<7 && gameBoard[move_i][move_2] == otherPlayer){
            move_2++;
            counter++;
        }
        if(move_2<=7 && gameBoard[move_i][move_2] == player && counter>0) return true;

        //Left movement [i,j-1]
        move_i = i;
        move_2 = j - 1;
        counter = 0;
        // decrease j
        while(move_2>0 && gameBoard[move_i][move_2] == otherPlayer){
            move_2--;
            counter++;
        }
        if(move_2>=0 && gameBoard[move_i][move_2] == player && counter>0) return true;

        //Up + Right movement [i-1,j+1]
        move_i = i - 1;
        move_2 = j + 1;
        counter = 0;
        //decrease i and increase j
        while(move_i>0 && move_2<7 && gameBoard[move_i][move_2] == otherPlayer){
            move_i--;
            move_2++;
            counter++;
        }
        if(move_i>=0 && move_2<=7 && gameBoard[move_i][move_2] == player && counter>0) return true;

        //Up + Left movement [i-1,j-1]
        move_i = i - 1;
        move_2 = j - 1;
        counter = 0;
        //decrease i and j
        while(move_i>0 && move_2>0 && gameBoard[move_i][move_2] == otherPlayer){
            move_i--;
            move_2--;
            counter++;
        }
        if(move_i>=0 && move_2>=0 && gameBoard[move_i][move_2] == player && counter>0) return true;

        //Down + Right movement [i+1,j+1]
        move_i = i + 1;
        move_2 = j + 1;
        counter = 0;
        // iincrease i and j
        while(move_i<7 && move_2<7 && gameBoard[move_i][move_2] == otherPlayer){
            move_i++;
            move_2++;
            counter++;
        }
        if(move_i<=7 && move_2<=7 && gameBoard[move_i][move_2] == player && counter>0) return true;

        //Down + Left movement [i+1,j-1]
        move_i = i + 1;
        move_2 = j - 1;
        counter = 0;
        //increase i and decrease j
        while(move_i<7 && move_2>0 && gameBoard[move_i][move_2] == otherPlayer){
            move_i++;
            move_2--;
            counter++;
        }
        if(move_i<=7 && move_2>=0 && gameBoard[move_i][move_2] == player && counter>0) return true;
//---------------------------------------------------------------------------------------------------------

        //No move left therefore return false for the movement
        return false;

    }

    //Updates the new board from the old one
    public static int[][] UpdateGameBoard(int[][] gameBoard, Point move , int player){
        //Copy the previous game board state
        int[][] UpdatedGameBoard = new int[8][8];
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 8; l++) {
                UpdatedGameBoard[k][l] = gameBoard[k][l];
            }
        }
        //updates the pieces on the board
        UpdatedGameBoard[move.x][move.y] = player;
        //Change pieces colours when captured
        ArrayList<Point> changePieces = GameBoard.openSquares(UpdatedGameBoard,player,move.x,move.y);
        for(Point piecesOnBoard : changePieces){
            UpdatedGameBoard[piecesOnBoard.x][piecesOnBoard.y] = player;

        }

        return UpdatedGameBoard;
    }

    public static boolean isGameEnded(int[][] gameBoard){
        //If the black player and the white player have no moves left
        return !(legalMoves(gameBoard,1) || legalMoves(gameBoard,2));
    }

    // finds who won by counting the pieces on the board
    public static int whoWON(int[][] board){
        // if the game is not ended yet
        if(!isGameEnded(board))
            return -1;
        else{
            //Ccounts pieces on the board
            int pieces_p1 = countPlayerPieces(board,1);
            int pieces_p2 = countPlayerPieces(board,2);
            // if the players have equal pieces --> draw
            if(pieces_p1 == pieces_p2){
                return 0;

                // There are more black pieces on the board
                // Black Agent - Min Max search wins
            }else if(pieces_p1 > pieces_p2){
                return 1;

                // There are more white pieces on the board
                // White Agent - greedy  wins
            }else{
                return 2;
            }
        }
    }



    // counts the player's pieces
    public static int countPlayerPieces(int[][] gameBoard, int player){
        int p_pieces = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(gameBoard[i][j] == player) p_pieces++;
            }
        }
        return p_pieces;
    }
//----------------------------------------------------




}

