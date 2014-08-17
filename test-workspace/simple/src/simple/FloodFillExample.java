package simple;

public class FloodFillExample {
    public static void main(String[] args) {
        int[][] surface = {
                { 0, 0, 0, 0, 0 },
                { 0, 1, 1, 0, 0 },
                { 0, 1, 0, 0, 1 },
                { 1, 1, 1, 0, 0 },
                { 0, 0, 0, 0, 0 },
        };
        int n = surface.length;

        flood(surface, n / 2, n / 2);
    }

    private static void flood(int[][] surface, int i, int j) {
        if (i < 0 || j < 0 || i >= surface.length || j >= surface[0].length ||
            surface[i][j] != 0) {
            return;
        }

        if (surface[i][j] == 0) {
            surface[i][j] = 2;
        }

        flood(surface, i - 1, j);
        flood(surface, i, j + 1);
        flood(surface, i + 1, j);
        flood(surface, i, j - 1);
    }
}
