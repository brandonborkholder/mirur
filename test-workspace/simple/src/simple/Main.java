package simple;

public class Main {
    public static void main(String[] args) {
    	double[] random = new double[500];
    	for (int i = 0; i < random.length; i++) {
    		random[i] = Math.random();
    	}

    	float[][] surf = new float[500][500];
    	for (int i = 0; i < surf.length; i++) {
    		for (int j = 0; j < surf[0].length; j++) {
    			surf[i][j] = (float) Math.random() * i;
    		}
    	}
    	
    	System.out.println("Debug here");
    }
}
