package com.william.puzzle.service;

import com.william.puzzle.model.Board;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class BoardServiceImpl implements BoardService {
    public Board instantiateBoard(UUID gameId) {
        var lineup = generateLineup();
        while (!hasEvenNumberOfInversions(lineup)) {
            lineup = generateLineup();
        };

        return Board.builder()
                .gameId(gameId)
                .lineup(lineup)
                .isSolved(isPuzzleSolved(lineup))
                .build();
    }

    public boolean isValidMove(Board board, Integer fromSquare) {
        var lineup = board.getLineup();
        var emptySquare = lineup.indexOf(0);
        return isWithinBounds(fromSquare, lineup) && isFromAdjacentSquare(emptySquare, fromSquare);
    }

    public void makeMove(Board board, Integer fromSquare) {
        var lineup = board.getLineup();
        var emptySquare = lineup.indexOf(0);

        Collections.swap(lineup, emptySquare, fromSquare);
        board.setSolved(isPuzzleSolved(lineup));
    }

    private boolean isWithinBounds(Integer fromSquare, List<Integer> lineup) {
        return fromSquare >= 0 && fromSquare < lineup.size();
    }

    private boolean isFromAdjacentSquare(Integer emptySquare, Integer fromSquare) {
        int indexDistance = Math.abs(emptySquare - fromSquare);
        return indexDistance == 1 || indexDistance == 4;
    }

    private List<Integer> generateLineup() {
        var lineup = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
        Collections.shuffle(lineup);
        lineup.add(0);

        return lineup;
    }

    // For a grid of even-sized rows and columns with the empty spot on the last row,
    // there must be an even number of inversions for the puzzle to be solvable
    private boolean hasEvenNumberOfInversions(List<Integer> lineup) {
        return countInversions(lineup) % 2 == 0;
    }

    private Integer countInversions(List<Integer> lineup) {
        var numberOfInversions = 0;
        for (var i = 0; i < lineup.size(); i++) {
            for (var j = i + 1; j < lineup.size(); j++) {
                var left = lineup.get(i);
                var right = lineup.get(j);
                if (left != 0 && right != 0 && left > right) {
                    numberOfInversions++;
                }
            }
        }

        return numberOfInversions;
    }

    private boolean isPuzzleSolved(List<Integer> lineup) {
        for (var i = 0; i < lineup.size() - 1; i++) {
            if (lineup.get(i) != i + 1) {
                return false;
            }
        }
        return lineup.getLast().equals(0);
    }
}
