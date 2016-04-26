package cpe593_neuralnet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class mainFunction {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("train.csv"));
		int input_neurons=28*28;
		int output_neurons=10;
		int lineToRead=3500;
		double[][] traindata= new double[lineToRead][input_neurons];
		double[][] traintarget=new double[lineToRead][output_neurons];
		br.readLine();//header
		for(int linenum=0;linenum<lineToRead;linenum++){
			String s=br.readLine();
			if(s==null) break;
			String[] lineArray = s.split(",");
			traintarget[linenum][Integer.parseInt(lineArray[0])]=1;
	           
	        for(int k=1;k<lineArray.length;k++){
	            traindata[linenum][k-1]=getFeatureValue(Integer.parseInt(lineArray[k]));
	        } 
	        if(linenum%500==0){
	            int m = 28;
	            int n = 28;
		        int[][] image = new int[m][n];
		        for (int i = 0; i < m; i++) {
		            for (int j = 0; j < n; j++) {
		                image[i][j] = getFeatureValue(Integer.parseInt(lineArray[i * m + j + 1]));
		            }
		        }
		        //printImage(image);
	        }
		}
		lineToRead=500;
		double[][] testdata= new double[lineToRead][input_neurons];
		double[][] testtarget=new double[lineToRead][output_neurons];
		for(int linenum=0;linenum<lineToRead;linenum++){
			String s=br.readLine();
			if(s==null) break;
			String[] lineArray = s.split(",");
			testtarget[linenum][Integer.parseInt(lineArray[0])]=1;
	           
	        for(int k=1;k<lineArray.length;k++){
	            testdata[linenum][k-1]=getFeatureValue(Integer.parseInt(lineArray[k]));
	        } 
		}
		
		NeuralNet nn= new NeuralNet(input_neurons, 15, 10, 0.2, 0.8);
		nn.train(traindata, traintarget, 40);
		System.out.println("Training Data Accuracy");
		nn.test(traindata,traintarget);
		System.out.println("Test Data Accuracy");
		nn.test(testdata,testtarget);
		DrawWindow5 dw=new DrawWindow5(nn);
	}

	private static void printImage(int[][] image) {
		for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                if (image[i][j] > 0) {
                    System.out.print(1);
                } else {
                    System.out.print(0);
                }
            }
            System.out.println();
        }
        System.out.println("---------");
    }
		
	
	private static int getFeatureValue(int parseInt) {
		if (parseInt >= 100) {
            return 1;
        }
        return 0;
	}
}
