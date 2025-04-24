package com.william.puzzle.service;

import com.william.puzzle.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    private BoardServiceImpl boardService;

    @BeforeEach
    void setUp() {
        boardService = new BoardServiceImpl();
    }

    @Test
    void instantiateBoard_ReturnsSolvableBoard() {
        UUID gameId = UUID.randomUUID();
        Board board = boardService.instantiateBoard(gameId);

        assertNotNull(board);
        assertEquals(gameId, board.getGameId());
        assertEquals(16, board.getLineup().size());
        assertTrue(containsAllExpectedNumbers(board.getLineup()));
        assertFalse(board.isSolved());
    }

    @Test
    void isValidMove_ReturnsTrueForAdjacentSwap() {
        List<Integer> lineup = new ArrayList<>(List.of(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 0,
                13, 14, 15, 12
        ));
        Board board = Board.builder()
                .gameId(UUID.randomUUID())
                .lineup(lineup)
                .isSolved(false)
                .build();

        assertTrue(boardService.isValidMove(board, 10));
        assertTrue(boardService.isValidMove(board, 7));
        assertFalse(boardService.isValidMove(board, 0));
    }

    @Test
    void isValidMove_ReturnsFalse_WhenOutOfBounds() {
        List<Integer> lineup = new ArrayList<>(List.of(
                1, 2, 3, 0,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 4
        ));
        Board board = Board.builder()
                .gameId(UUID.randomUUID())
                .lineup(lineup)
                .isSolved(false)
                .build();

        assertFalse(boardService.isValidMove(board, -1));
        assertFalse(boardService.isValidMove(board, 16));
    }

    @Test
    void makeMove_SwapsTilesAndUpdatesSolvedState() {
        List<Integer> lineup = new ArrayList<>(List.of(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 0,
                13, 14, 15, 12
        ));
        Board board = Board.builder()
                .gameId(UUID.randomUUID())
                .lineup(lineup)
                .isSolved(false)
                .build();

        boardService.makeMove(board, 10);

        List<Integer> newLineup = board.getLineup();
        assertEquals(0, newLineup.get(10));
        assertEquals(11, newLineup.get(11));
    }

    @Test
    void makeMove_SetsSolvedTrue_WhenSolved() {
        List<Integer> solvedLineup = new ArrayList<>(List.of(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 0
        ));
        Board board = Board.builder()
                .gameId(UUID.randomUUID())
                .lineup(solvedLineup)
                .isSolved(false)
                .build();

        boardService.makeMove(board, 15);

        assertTrue(board.isSolved());
    }

    private boolean containsAllExpectedNumbers(List<Integer> lineup) {
        return lineup.containsAll(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, 11, 12, 13, 14, 15));
    }
}
