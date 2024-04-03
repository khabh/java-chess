package chess.view;

import chess.model.position.Position;

import java.util.Scanner;

public class InputView {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int POSITION_LENGTH = 2;
    private static final int FILE_INDEX = 0;
    private static final int RANK_INDEX = 1;
    private static final int FILE_START_ASCII = 'a' - 1;
    private static final int RANK_START_ASCII = '1' - 1;

    public String askBoardName() {
        System.out.println("입장할 방의 이름을 입력해 주세요.");
        return SCANNER.next();
    }

    public String askSearchedBoardName() {
        System.out.println("결과를 조회할 방의 이름을 입력해 주세요.");
        return SCANNER.next();
    }

    public Command askCommand() {
        String input = SCANNER.next();
        return Command.findBy(input);
    }

    public Position askPosition() {
        String input = SCANNER.next();
        validatePositionLength(input);
        return convertToPosition(input);
    }

    private void validatePositionLength(String input) {
        if (input.length() != POSITION_LENGTH) {
            throw new IllegalArgumentException("올바르지 않은 위치 입력입니다.");
        }
    }

    private Position convertToPosition(String input) {
        int file = input.charAt(FILE_INDEX) - FILE_START_ASCII;
        int rank = input.charAt(RANK_INDEX) - RANK_START_ASCII;
        return Position.of(file, rank);
    }
}
