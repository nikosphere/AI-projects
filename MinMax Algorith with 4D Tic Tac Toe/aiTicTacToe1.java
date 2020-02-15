import java.util.*;
public class aiTicTacToe1 {

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

        // set start time of the turn

        int thisScore;
        int bestScore = -71; // try to beat this score (worst possible is -65: enemy will win in next move)
        positionTicTacToe bestMove = new positionTicTacToe(0, 0, 0);
        List < positionTicTacToe > openPositions = getOpenPositions(board);
        for (positionTicTacToe position: openPositions) {
            List < positionTicTacToe > currentBoard = deepCopyATicTacToeBoard(board);
            makeMove(position, player, currentBoard); // play our move
            thisScore = minimax(currentBoard, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false); // request minimax on starting with opponent move
            if (thisScore > bestScore) {
                bestScore = thisScore;
                bestMove = position;
            }
        }

        // calculate time of the turn and save in ai1TurnTimes
        
        return bestMove;

    }
    public int minimax(List < positionTicTacToe > board, int depth, int alpha, int beta, boolean maximizing) {
        // base cases
        int winner = isEnded(board);
        if (winner == this.player) {
            return 70; // maximizer has won
        }
        if (winner == this.opponent) {
            return -70; //  minimizer has won
        }
        if (winner == -1) {
            return 0; // tie game
        }
        if (depth == 0) {
            return 0; // no heuristic
        }

        int value;
        List < positionTicTacToe > openPositions = getOpenPositions(board); // generate layer of possible moves (child nodes)

        if (maximizing) {
            value = Integer.MIN_VALUE; //  try to beat this minimum value
            for (positionTicTacToe position: openPositions) {
                List < positionTicTacToe > currentBoard = deepCopyATicTacToeBoard(board);
                makeMove(position, this.player, currentBoard); // play our position
                value = Math.max(value, minimax(currentBoard, depth - 1, alpha, beta, false));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
        } else {
            value = Integer.MAX_VALUE; // try to get less than this value
            for (positionTicTacToe position: openPositions) {
                List < positionTicTacToe > currentBoard = deepCopyATicTacToeBoard(board);
                makeMove(position, this.opponent, currentBoard); // play opponent position
                value = Math.min(value, minimax(currentBoard, depth - 1, alpha, beta, true));
                alpha = Math.min(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return value;
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

                //print the satisified winning line (one of them if there are several)
                // p0.printPosition();
                // p1.printPosition();
                // p2.printPosition();
                // p3.printPosition();
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
    public aiTicTacToe1(int setPlayer) {
        player = setPlayer;
        winningLines = initializeWinningLines();
        opponent = oppositePlayer(player);
    }
}
