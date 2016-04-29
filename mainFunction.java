package cpe593_neuralnet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class mainFunction {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("TrainData.csv"));
		BufferedReader br2 = new BufferedReader(new FileReader("TrainLabel.csv"));

		int input_neurons=150;
		int output_neurons=9;
		int lineToRead=2000;
		double[][] traindata= new double[lineToRead][input_neurons];
		double[][] traintarget=new double[lineToRead][output_neurons];
		//br.readLine();//header
		/*
		 * Complexity here is 7000* O(input) input=28*28
		 */
		for(int linenum=0;linenum<lineToRead;linenum++){
			String s=br.readLine();
			String tar=br2.readLine();
			if(s==null) break;
			String[] lineArray = s.split(",");
			String[] targ= tar.split(",");
			//System.out.println(targ[0]);
			traintarget[linenum][Integer.parseInt(targ[0])-1]=1;
	           
	        for(int k=0;k<lineArray.length;k++){
	            //traindata[linenum][k]=(Double.parseDouble(lineArray[k]));
	            traindata[linenum][k]=(Double.parseDouble(lineArray[k]));

	        } 
	        /*
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
	        }*/
		}
		lineToRead=400;
		//1000* O(input)
		double[][] testdata= new double[lineToRead][input_neurons];
		double[][] testtarget=new double[lineToRead][output_neurons];
		for(int linenum=0;linenum<lineToRead;linenum++){
			String s=br.readLine();
			String tar=br2.readLine();
			if(s==null) break;
			String[] lineArray = s.split(",");
			String[] targ=tar.split(",");
			testtarget[linenum][Integer.parseInt(targ[0])-1]=1;
	           
	        for(int k=0;k<lineArray.length;k++){
	            //testdata[linenum][k]=getFeatureValue(Integer.parseInt(lineArray[k]));
	        	testdata[linenum][k]=(Double.parseDouble(lineArray[k]));
	        } 
		}
		br.close();
		br2.close();
		int[] hidden_neurons={15};
		NeuralNet nn= new NeuralNet(input_neurons, hidden_neurons, 9, 0.1, 0.95);
		nn.printArchitecture();
		nn.train(traindata, traintarget, 100);
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
