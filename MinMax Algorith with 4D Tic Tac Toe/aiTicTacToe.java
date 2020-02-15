import java.util.*;
public class aiTicTacToe {

    public int player; //1 for player 1 and 2 for player 2
    public int opponent;
    private List < List < positionTicTacToe > > winningLines = new ArrayList < > ();
    private final int DEPTH = 2;

    private int getStateOfPositionFromBoard(positionTicTacToe position, List < positionTicTacToe > board) {
        //a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
        int index = position.x * 16 + position.y * 4 + position.z;
        return board.get(index).state;
    }
    public positionTicTacToe myAIAlgorithm(List < positionTicTacToe > board, int player) {

        int Score;
        int idealScore = -10001; // try to beat this score (worst possible is -1000: enemy will win in next move)
        positionTicTacToe optimalMove = new positionTicTacToe(0, 0, 0); //resets optimal position to terminal node
        List < positionTicTacToe > openPositions = getOpenPositions(board);
        for (positionTicTacToe position: openPositions) { //for loop to iterate through the open positions
            List < positionTicTacToe > currentBoard = deepCopyATicTacToeBoard(board); //gets the copy of the current state of the board
            makeMove(position, player, currentBoard); // play our move that is optimal
            Score = minimax(currentBoard, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false); // request minimax on starting with opponent move
            if (Score > idealScore) { //checks our Score 
                idealScore = Score;
                optimalMove = position;
            }
        }

        return optimalMove;

    }
    //initialize minimax algorithm function
    public int minimax(List < positionTicTacToe > board, int depth, int alpha, int beta, boolean maximizing) {
        // base cases
        int winner = isEnded(board);
        if (winner == this.player) {
            return 10000; // maximizer wins
        }
        if (winner == this.opponent) {
            return -10000; //  minimizer wins
        }
        if (winner == -1) {
            return 0; // the game is tied
        }
        if (depth == 0) {
            return Heuristic(board); //goes to heuristic function
        }

        int value;
        List < positionTicTacToe > openPositions = getOpenPositions(board); // generate layer of possible moves (child nodes)

        if (maximizing) {
            value = Integer.MIN_VALUE; //  goal is beat the value here
            for (positionTicTacToe position: openPositions) { //get our open positions
                List < positionTicTacToe > currentBoard = deepCopyATicTacToeBoard(board); //current state of board
                makeMove(position, this.player, currentBoard); // play our position 
                value = Math.max(value, minimax(currentBoard, depth - 1, alpha, beta, false)); //get the max value of move
                alpha = Math.max(alpha, value); //get our alpha value
                if (alpha >= beta) { //if alpha is bigger than or equal to beta, break
                    break;
                }
            }
        } else {
            value = Integer.MAX_VALUE; // goal is to get less than the value here
            for (positionTicTacToe position: openPositions) {
                List < positionTicTacToe > currentBoard = deepCopyATicTacToeBoard(board);
                makeMove(position, this.opponent, currentBoard); // play opponent position
                value = Math.min(value, minimax(currentBoard, depth - 1, alpha, beta, true)); //get the minimum value
                alpha = Math.min(alpha, value); //get our alpha value from min
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return value;
    }

    private int Heuristic(List<positionTicTacToe> board) {
        int totalScore = 0; //initialize score as 0
        for (List <positionTicTacToe> winningLine : winningLines) { //for loop to run through our move for winning lines, if winning lines are filled, inrease total score based
            int numFilled = 0;
            for (positionTicTacToe point : winningLine) {
                int val = getStateOfPositionFromBoard(point, board); //our integer value is equal to the position of the board
                if (val == this.player) {
                    numFilled += 1;
                }
                if (val == this.opponent) {
                    numFilled = 0;
                    break;
                }
            }
            switch(numFilled) { //once we are at depth = 0, here we inrement total score at the leaf nodes
                case 0:
                    break;
                case 1:
                    totalScore += 1;
                    break;
                case 2:
                    totalScore += 10;
                    break;
                case 3:
                    totalScore += 100;
                    break;
            }
        }
        for (List <positionTicTacToe> winningLine : winningLines) { // run through the winning lines, seeng which position has been filled. If the numfilled = to a certain amount,decrease the score
            int numFilled = 0;
            for (positionTicTacToe point : winningLine) {
                int val = getStateOfPositionFromBoard(point, board);  //get board position
                if (val == this.opponent) {
                    numFilled += 1;
                }
                if (val == this.player) {
                    numFilled = 0;
                    break;
                }
            }
            switch(numFilled) { //decrease if our opponent can win based on their things, return the total score of the place
                case 0:
                    break;
                case 1:
                    totalScore -= 1;
                    break;
                case 2:
                    totalScore -= 10;
                    break;
                case 3:
                    totalScore -= 100;
                    break;
            }
        }
        return totalScore;
    }

    private List < List < positionTicTacToe > > initializeWinningLines() {
        //create a list of winning line so that the game will "brute-force" check if a player satisfied any  winning condition(s).
        List < List < positionTicTacToe > > winningLines = new ArrayList < List < positionTicTacToe > > ();

        //48 straight winning lines
        //z axis winning lines
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
                oneWinCondtion.add(new positionTicTacToe(i, j, 0, -1));
                oneWinCondtion.add(new positionTicTacToe(i, j, 1, -1));
                oneWinCondtion.add(new positionTicTacToe(i, j, 2, -1));
                oneWinCondtion.add(new positionTicTacToe(i, j, 3, -1));
                winningLines.add(oneWinCondtion);
            }
        //y axis winning lines
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
                oneWinCondtion.add(new positionTicTacToe(i, 0, j, -1));
                oneWinCondtion.add(new positionTicTacToe(i, 1, j, -1));
                oneWinCondtion.add(new positionTicTacToe(i, 2, j, -1));
                oneWinCondtion.add(new positionTicTacToe(i, 3, j, -1));
                winningLines.add(oneWinCondtion);
            }
        //x axis winning lines
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
                oneWinCondtion.add(new positionTicTacToe(0, i, j, -1));
                oneWinCondtion.add(new positionTicTacToe(1, i, j, -1));
                oneWinCondtion.add(new positionTicTacToe(2, i, j, -1));
                oneWinCondtion.add(new positionTicTacToe(3, i, j, -1));
                winningLines.add(oneWinCondtion);
            }

        //12 main diagonal winning lines
        //xz plane-4
        for (int i = 0; i < 4; i++) {
            List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
            oneWinCondtion.add(new positionTicTacToe(0, i, 0, -1));
            oneWinCondtion.add(new positionTicTacToe(1, i, 1, -1));
            oneWinCondtion.add(new positionTicTacToe(2, i, 2, -1));
            oneWinCondtion.add(new positionTicTacToe(3, i, 3, -1));
            winningLines.add(oneWinCondtion);
        }
        //yz plane-4
        for (int i = 0; i < 4; i++) {
            List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
            oneWinCondtion.add(new positionTicTacToe(i, 0, 0, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 1, 1, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 2, 2, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 3, 3, -1));
            winningLines.add(oneWinCondtion);
        }
        //xy plane-4
        for (int i = 0; i < 4; i++) {
            List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
            oneWinCondtion.add(new positionTicTacToe(0, 0, i, -1));
            oneWinCondtion.add(new positionTicTacToe(1, 1, i, -1));
            oneWinCondtion.add(new positionTicTacToe(2, 2, i, -1));
            oneWinCondtion.add(new positionTicTacToe(3, 3, i, -1));
            winningLines.add(oneWinCondtion);
        }

        //12 anti diagonal winning lines
        //xz plane-4
        for (int i = 0; i < 4; i++) {
            List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
            oneWinCondtion.add(new positionTicTacToe(0, i, 3, -1));
            oneWinCondtion.add(new positionTicTacToe(1, i, 2, -1));
            oneWinCondtion.add(new positionTicTacToe(2, i, 1, -1));
            oneWinCondtion.add(new positionTicTacToe(3, i, 0, -1));
            winningLines.add(oneWinCondtion);
        }
        //yz plane-4
        for (int i = 0; i < 4; i++) {
            List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
            oneWinCondtion.add(new positionTicTacToe(i, 0, 3, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 1, 2, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 2, 1, -1));
            oneWinCondtion.add(new positionTicTacToe(i, 3, 0, -1));
            winningLines.add(oneWinCondtion);
        }
        //xy plane-4
        for (int i = 0; i < 4; i++) {
            List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
            oneWinCondtion.add(new positionTicTacToe(0, 3, i, -1));
            oneWinCondtion.add(new positionTicTacToe(1, 2, i, -1));
            oneWinCondtion.add(new positionTicTacToe(2, 1, i, -1));
            oneWinCondtion.add(new positionTicTacToe(3, 0, i, -1));
            winningLines.add(oneWinCondtion);
        }

        //4 additional diagonal winning lines
        List < positionTicTacToe > oneWinCondtion = new ArrayList < positionTicTacToe > ();
        oneWinCondtion.add(new positionTicTacToe(0, 0, 0, -1));
        oneWinCondtion.add(new positionTicTacToe(1, 1, 1, -1));
        oneWinCondtion.add(new positionTicTacToe(2, 2, 2, -1));
        oneWinCondtion.add(new positionTicTacToe(3, 3, 3, -1));
        winningLines.add(oneWinCondtion);

        oneWinCondtion = new ArrayList < positionTicTacToe > ();
        oneWinCondtion.add(new positionTicTacToe(0, 0, 3, -1));
        oneWinCondtion.add(new positionTicTacToe(1, 1, 2, -1));
        oneWinCondtion.add(new positionTicTacToe(2, 2, 1, -1));
        oneWinCondtion.add(new positionTicTacToe(3, 3, 0, -1));
        winningLines.add(oneWinCondtion);

        oneWinCondtion = new ArrayList < positionTicTacToe > ();
        oneWinCondtion.add(new positionTicTacToe(3, 0, 0, -1));
        oneWinCondtion.add(new positionTicTacToe(2, 1, 1, -1));
        oneWinCondtion.add(new positionTicTacToe(1, 2, 2, -1));
        oneWinCondtion.add(new positionTicTacToe(0, 3, 3, -1));
        winningLines.add(oneWinCondtion);

        oneWinCondtion = new ArrayList < positionTicTacToe > ();
        oneWinCondtion.add(new positionTicTacToe(0, 3, 0, -1));
        oneWinCondtion.add(new positionTicTacToe(1, 2, 1, -1));
        oneWinCondtion.add(new positionTicTacToe(2, 1, 2, -1));
        oneWinCondtion.add(new positionTicTacToe(3, 0, 3, -1));
        winningLines.add(oneWinCondtion);

        return winningLines;

    }
    private int isEnded(List < positionTicTacToe > board) {
        //test whether the current game is ended

        //brute-force
        for (int i = 0; i < winningLines.size(); i++) {

            positionTicTacToe p0 = winningLines.get(i).get(0);
            positionTicTacToe p1 = winningLines.get(i).get(1);
            positionTicTacToe p2 = winningLines.get(i).get(2);
            positionTicTacToe p3 = winningLines.get(i).get(3);

            int state0 = getStateOfPositionFromBoard(p0, board);
            int state1 = getStateOfPositionFromBoard(p1, board);
            int state2 = getStateOfPositionFromBoard(p2, board);
            int state3 = getStateOfPositionFromBoard(p3, board);

            //if they have the same state (marked by same player) and they are not all marked.
            if (state0 == state1 && state1 == state2 && state2 == state3 && state0 != 0) {
                //someone wins
                p0.state = state0;
                p1.state = state1;
                p2.state = state2;
                p3.state = state3;
                return state0;
            }
        }
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).state == 0) {
                //game is not ended, continue
                return 0;
            }
        }
        return -1; //call it a draw
    }
    private List < positionTicTacToe > deepCopyATicTacToeBoard(List < positionTicTacToe > board) {
        //deep copy of game boards
        List < positionTicTacToe > copiedBoard = new ArrayList < positionTicTacToe > ();
        for (int i = 0; i < board.size(); i++) {
            copiedBoard.add(new positionTicTacToe(board.get(i).x, board.get(i).y, board.get(i).z, board.get(i).state));
        }
        return copiedBoard;
    }
    private boolean makeMove(positionTicTacToe position, int player, List < positionTicTacToe > tagergetBoard) {
        //make move on Tic-Tac-Toe board, given position and player
        //player 1 = 1, player 2 = 2

        //brute force (obviously not a wise way though)
        for (int i = 0; i < tagergetBoard.size(); i++) {
            if (tagergetBoard.get(i).x == position.x && tagergetBoard.get(i).y == position.y && tagergetBoard.get(i).z == position.z) //if this is the position
            {
                if (tagergetBoard.get(i).state == 0) {
                    tagergetBoard.get(i).state = player;
                    return true;
                } else {
                    System.out.println("Error: this is not a valid move.");
                }
            }

        }
        return false;
    }
    private int oppositePlayer(int player) {
        switch (player) {
            case 1:
                return 2;
            case 2:
                return 1;
        }
        return -1;
    }
    private List < positionTicTacToe > getOpenPositions(List < positionTicTacToe > board) {
        List < positionTicTacToe > openPositions = new ArrayList < > ();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    positionTicTacToe position = new positionTicTacToe(x, y, z);
                    if (getStateOfPositionFromBoard(position, board) == 0) {
                        openPositions.add(position);
                    }
                }
            }
        }
        return openPositions;
    }
    //allows us to play against the other AI players
    public aiTicTacToe(int setPlayer) {
        player = setPlayer;
        winningLines = initializeWinningLines();
        opponent = oppositePlayer(player);
    }
}
