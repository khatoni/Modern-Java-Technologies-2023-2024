public class JumpGame {
    private static boolean contains(int[][] arr, int column) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][column] == 1) return true;
        }
        return false;
    }

    public static boolean canWin(int[] array) {
        int[][] arr = new int[array.length][array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i][0] = 1;
        }
        for (int i = 0; i < array.length; i++) {
            if (contains(arr, i)) {
                for (int j = 0; j <= array[i]; j++) {
                    if (i + j < array.length) {
                        arr[i][i + j] = 1;
                    }
                }
            } else {
                return false;
            }
        }
        /*for(int i=0;i<array.length;i++) {
            for(int j=0;j<array.length;j++) {
                System.out.print(arr[i][j]);
            }
            System.out.print('\n');
        }*/
        if (contains(arr, array.length - 1)) {
            return true;
        }
        return false;
    }
}
