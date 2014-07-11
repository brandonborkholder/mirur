package simple;

import java.util.ArrayList;
import java.util.List;

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
    	
    	List<Double> dblList = new ArrayList<>();
    	for (int i = 0; i < 50; i++){
    		dblList.add(Math.random());
    	}
    	
    	System.out.println("Debug here");
    }
}
