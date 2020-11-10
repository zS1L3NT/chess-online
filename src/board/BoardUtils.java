package board;

import java.util.Arrays;
import java.lang.Integer;

public class BoardUtils {
    private static char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };

    public static boolean IN_FIRST_COL(int position) {
        return array_contains(fill_col(0), position);
    }

    public static boolean IN_SECOND_COL(int position) {
        return array_contains(fill_col(1), position);
    }

    public static boolean IN_SEVENTH_COL(int position) {
        return array_contains(fill_col(6), position);
    }

    public static boolean IN_EIGHTH_COL(int position) {
        return array_contains(fill_col(7), position);
    }

    public static boolean IN_SECOND_ROW(int position) {
        return array_contains(fill_row(8), position);
    }

    public static boolean IN_SEVENTH_ROW(int position) {
        return array_contains(fill_row(48), position);
    }

    private static int[] fill_col(int start) {
        int[] list = new int[8];
        for (int i = 0; i < 8; i++) {
            list[i] = start + (i * 8);
        }
        return list;
    }

    private static int[] fill_row(int start) {
        int[] list = new int[8];
        for (int i = 0; i < 8; i++) {
            list[i] = start + i;
        }
        return list;
    }

    public static boolean array_contains(int[] array, int item) {
        return Arrays.stream(array).anyMatch(n -> n == item);
    }

    public static boolean array_contains(char[] array, char item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == item)
                return true;
        }
        return false;
    }

    public final static boolean is_valid_position(int position) {
        return position < 64 && position >= 0;
    }

    public final static boolean is_valid_board_code(String code) {
        if (code.length() != 2)
            return false;

        char letter;
        int number;

        try {
            letter = code.substring(0, 1).toUpperCase().toCharArray()[0];
            number = Integer.parseInt(code.substring(1));
        } catch (Exception e) {
            return false;
        }

        if (!array_contains(letters, letter))
            return false;
        if (number <= 0 || number > 8)
            return false;

        return true;
    }

    public final static int to_position(String code) {
        if (!is_valid_board_code(code))
            return -999;
        char letter = code.substring(0, 1).toUpperCase().toCharArray()[0];
        int number = Integer.parseInt(code.substring(1));
        return ((8 - number) * 8) + String.valueOf(letters).indexOf(letter);
    }

    public final static String to_board_code(int position) {
        char letter = letters[position % 8];
        int number = 8 - ((position / 8));
        return Character.toString(letter) + number;
    }
}
