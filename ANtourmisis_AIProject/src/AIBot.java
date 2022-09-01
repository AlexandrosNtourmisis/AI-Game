import java.awt.*;
import java.util.ArrayList;

public abstract class AIBot {
    //Piece of square for the 8x8 game <to be accessed from MainGame class>
    protected int pieceOfSquare;

    //AI agent bot for game (General Bot)
    public AIBot(int pOfSquare){
        // general piece of square equals to piece of square to be used
        pieceOfSquare = pOfSquare;

    }

    //Declaration of three variables for:
//_______________________________________________________________

    // Who plays now .. W or B
    abstract public boolean whoPlaysNow();

    // Distinct W and B
    abstract public String SquarePieceIdentifier();

    // Play of the turn
    abstract public Point play(int[][] board);

//_______________________________________________________________

}

//-----------------------------------------------------------------GREEDY-AGENT------------------------------------------------------------------------------------------
// The class for the white agent that uses greedy algorithm
class Greedy_WhitePlayer extends AIBot {

    public Greedy_WhitePlayer(int piece) {

        //constructor for piece
        super(piece);

    }


    // When Black Player Plays:
    @Override
    // So the next player can play after the white's turns
    public boolean whoPlaysNow() {
        return false;
    }

    @Override
    // Identify that the white player played this turn
    public String SquarePieceIdentifier() {
        return "White Player";
    }

    @Override
    // Play with the heuristic that is based on finding the best move for this turn that is possible
    public Point play(int[][] gameBoard) {

        //See all the different possibilities
        ArrayList<Point> allPossibleMoves = GameBoard.possMoves(gameBoard, pieceOfSquare);
        //Make the positions --> null and the best value --> zero
        Point bMove = null;
        int bValue = 0;


        // find the best possible moves that can make
        for(Point moveToMake : allPossibleMoves) {
            //instatiates value to find the best value and then if it bigger then it assigns it to the best value and finds the best move to make at this turn
            int FoundValue = GameBoard.openSquares(gameBoard, pieceOfSquare, moveToMake.x, moveToMake.y).size();
            if(FoundValue > bValue){
                bValue = FoundValue;
                bMove = moveToMake;
            }
        }
        // return the best move for the greedy agent
        return bMove;

    }

}
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


//-----------------------------------------------------------------MIN-MAX-AGENT-----------------------------------------------------------------------------------------
// The class for the black agent that uses Min Max search
class MinMax_BlackPlayer extends AIBot {

//--------class variables----------------||

    // The evaluator of the bot
    private EvaluatorForHrstcs BotEval;
    // The depth of search that will be performed
    public static int depSearch;


//----------------------------------------||

    public MinMax_BlackPlayer(int piece, int depth) {


        //constructor for piece
        super(piece);
        //assign depth and evaluator of method
        this.depSearch = depth;
        BotEval = new minmaxHeur();
    }

//    public void setDepSearch(int depSearch){
//         this.depSearch = depSearch;
//    }
//
//    public int getDepSearch() {
//        return depSearch;
//    }


// When Black Player Plays:
    @Override
    // So the next player can play after the black's turns
    public boolean whoPlaysNow() {
        return false;
    }

    @Override
    // to identify that this is the black player
    public String SquarePieceIdentifier() {
        return "Black Player ";
    }

    @Override
    // calls the min max class to search for the min max values.
    public Point play(int[][] gameBoard) {
        return MinMax.search(gameBoard, pieceOfSquare, depSearch, BotEval);
    }
 //----------------------------------------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


//---------------------------------------------------------Min-Max-Class-With-Alpha-Beta-Cutoff--------------------------------------------------------------------------
/*
simple  MinMax was commented out to implement Alpha Beta cutoff
 */
class MinMax {

    // The nodes that will be explored from the min max algorithm
    static int nodesExplored = 0;

    //returns max score move
    public static Point search(int[][] gameBoard, int player, int depth, EvaluatorForHrstcs eval){
//----------------------Simple Min Max Code-----------------------------------------------------------------
//        public static int minmax(int[][] node, boolean max, int player,int depth, Evaluator eval){
//        nodesExplored++;
          // The depth limit reached evaluate OR terminal was reached
//        if(depth == 0 || Board.isGameFinished(node)){
//            return eval.eval(node, player);
//-----------------------------------------------------------------------------------------------------
        nodesExplored = 0;
        int bscore = Integer.MIN_VALUE;
        Point bmove = null;

        for(Point toMove : GameBoard.possMoves(gameBoard,player)){

            //New node and update the game board after the turn
            int[][] nNode = GameBoard.UpdateGameBoard(gameBoard,toMove,player);

            // Min value for alpha better... implement alpha beta
            int minmaxscore = AlphaBetaC(nNode,player,depth-1,false,Integer.MIN_VALUE,Integer.MAX_VALUE,eval);

//----------------------Simple Min Max Code-----------------------------------------------------------------
//            int minmaxscore = minmax(node, false, player, depth - 1, eval);
//                score = (int) max(score, minmaxscore);

//            }
//            return score;
//        }else{
//            //minimizing
//            score = Integer.MAX_VALUE;
//            for(Point toMove : Board.getAllPossibleMoves(node,player)){ //opponent turn
//                //create new node
////                int[][] nNode = Board.getNewBoardAfterMove(node,toMove,player);
//                //recursive call
//                int minmaxscore = minmax(node, true, player, depth - 1, eval);
//                score = (int) min(score, minmaxscore);


//            }
//            return score;

//        }
//    }
//-----------------------------------------------------------------------------------------------------
            // if the min max score that alpha beta cuts found is better than that the best score
            // --> assigns to it and assigns the specific toMove to the best toMove
            if(minmaxscore > bscore) {
                bscore = minmaxscore;
                bmove = toMove;
            }

        }
        //Prints the nodes that the min max algorithm explored
        System.out.println("Min Max algorithm explored " + nodesExplored + " nodes");
        System.out.println("-------------------------------------");
        return bmove;
    }


    // Alpha Beta Cut/pruning for min max
    private static int AlphaBetaC(int[][] boardNode, int player, int depth, boolean maximized, int a, int b, EvaluatorForHrstcs eval){
        //updates the nodes that were explored
        nodesExplored++;

        //The depth limit was reached evaluate OR terminal was reached
        if(depth == 0 || GameBoard.isGameEnded(boardNode)){
            return eval.heuristics(boardNode,player);
        }
        int otherPlayer = (player==1) ? 2 : 1;
        //Skip when no more moves are available
        if((maximized && !GameBoard.legalMoves(boardNode,player)) || (!maximized && !GameBoard.legalMoves(boardNode,otherPlayer))){
            return AlphaBetaC(boardNode,player,depth-1,!maximized,a,b,eval);
        }
        int value;
        if(maximized){

//            int otherPlayer = (player==1) ? 2 : 1;

//            if((maximized && !GameBoard.hasAnyMoves(boardNode,player)) || (!maximized && !GameBoard.hasAnyMoves(boardNode,otherPlayer))){
//                return AlphaBetaC(boardNode,player,depth-1,!maximized,a,b,eval);

            //maximizing the value
            // value = -00
            value = Integer.MIN_VALUE;
            for(Point toMove : GameBoard.possMoves(boardNode,player)){
                //creates a new boardNode
                int[][] nNode = GameBoard.UpdateGameBoard(boardNode,toMove,player);
                //This is a recursive call
                int bvalue = AlphaBetaC(nNode,player,depth-1,false,a,b,eval);
                if(bvalue > value) value = bvalue;
                //Updates a and b cutoff
                if(value > a) a = value;
                if(b <= a) break;
            }
        }else{
            //minimizing the value
            // value = +00
            value = Integer.MAX_VALUE;
            for(Point toMove : GameBoard.possMoves(boardNode,otherPlayer)){ //otherPlayer turn
                //create new boardNode
                int[][] nNode = GameBoard.UpdateGameBoard(boardNode,toMove,otherPlayer);
                int bvalue = AlphaBetaC(nNode,player,depth-1,true,a,b,eval);
                if(bvalue < value) value = bvalue;
                //Updates b and a cutoff
                if(value < b) b = value;
                if(b <= a) break;
            }
        }
        //returns value
        return value;
    }

}
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------Heurestic-for-Min-Max-Agent-----------------------------------------------------------------------------------
interface EvaluatorForHrstcs {
    int heuristics(int[][] gameBoard, int player);
}

class minmaxHeur implements EvaluatorForHrstcs {

    public int heuristics(int[][] gameBoard, int player){
        int mob = evalMobility(gameBoard,player);
        int dD = evalDiscDiff(gameBoard,player);
        int crn = evalCorner(gameBoard, player);
        int cb = cornerGrab(gameBoard, player);


        //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
        //
        // THIS IS WHAT BLACK USES AS A HEURISTIC
        //
        return   crn; //Mobility + Disc Difference: Best combination, will give total win over white
        //
        //ΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛ
    }

    // DISC DIFFERENCE HEURISTIC
    // Measures the difference in the number of discs on the board.
    // Has zero weight in the opening, but increases to a moderate
    // weight in the midgame, and to a significant weight in the endgame.
    public static int evalDiscDiff(int[][] board , int player){
        int oplayer = (player==1) ? 2 : 1;

        int mySC = GameBoard.countPlayerPieces(board,player);
        int opSC = GameBoard.countPlayerPieces(board,oplayer);

        return 100 * (mySC - opSC) / (mySC + opSC);
    }

    // MOBILITY HEURISTIC
    // Measures the number of moves the player is currently
    // able to make. Has significant weight in the opening game,
    // but diminishes to zero weight towards the endgame.
    public static int evalMobility(int[][] board , int player){
        int oplayer = (player==1) ? 2 : 1;

        int myMoveCount = GameBoard.possMoves(board,player).size();
        int opMoveCount = GameBoard.possMoves(board,oplayer).size();

        return 100 * (myMoveCount - opMoveCount) / (myMoveCount + opMoveCount + 1);
    }

    // CORNER GRAB HEURISTIC
    // Measures if the current player can take a
    // corner with its next move, Weighted highly at all times.
    public static int evalCorner(int[][] board , int player){
        int oplayer = (player==1) ? 2 : 1;

        int myCorners = 0;
        int opCorners = 0;

        if(board[0][0]==player) myCorners++;
        if(board[7][0]==player) myCorners++;
        if(board[0][7]==player) myCorners++;
        if(board[7][7]==player) myCorners++;

        if(board[0][0]==oplayer) opCorners++;
        if(board[7][0]==oplayer) opCorners++;
        if(board[0][7]==oplayer) opCorners++;
        if(board[7][7]==oplayer) opCorners++;

        return 100 * (myCorners - opCorners) / (myCorners + opCorners + 1);
    }

    public static int cornerGrab(int[][] board , int player){
        ArrayList<Point> moves = GameBoard.possMoves(board,player);

        for(Point m : moves){
            //if player have corner move return 1
            if(m.x == 0 && m.y == 0) return 100;
            if(m.x == 7 && m.y == 0) return 100;
            if(m.x == 0 && m.y == 7) return 100;
            if(m.x == 7 && m.y == 7) return 100;
        }

        return 0;
    }

}
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
