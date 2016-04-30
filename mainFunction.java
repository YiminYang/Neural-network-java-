package cpe593_neuralnet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/*
 * This main class include Reading file, Setting training data and label, testing data and label
 * Determined the input_neuron, output neurons, the Architecture of neural net
 * Also some pre-processing to the dataset e.g. normalize the data, add some condition, separate the training data and testing data
 * After traning and testing, print the result and misclassification
 */

public class mainFunction {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("train.csv"));
		int input_neurons=784;
		int output_neurons=10;
		int lineToRead=10000;
		double[][] traindata= new double[lineToRead][input_neurons];
		double[][] traintarget=new double[lineToRead][output_neurons];
		br.readLine();//header
		/*
		 * Complexity here is 7000* O(input) input=28*28
		 */
		for(int linenum=0;linenum<lineToRead;linenum++){
			String s=br.readLine();
			if(s==null) break;
			String[] lineArray = s.split(",");
			traintarget[linenum][Integer.parseInt(lineArray[0])]=1;
	           
	        for(int k=1;k<lineArray.length;k++){
	            traindata[linenum][k-1]=getFeatureValue(Integer.parseInt(lineArray[k]));
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
		lineToRead=1000;
		//1000* O(input)
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
		br.close();
		int[] hidden_neurons={10};
		NeuralNet nn= new NeuralNet(input_neurons, hidden_neurons, output_neurons, 0.15, 0.85);
		nn.printArchitecture();
		long before=System.currentTimeMillis();
		nn.train(traindata, traintarget, 30);
		long after=System.currentTimeMillis();
		System.out.println("Training Time is "+((after-before)/1000.0)+" s");
		System.out.println("Training Data Accuracy");
		nn.test(traindata,traintarget);
		System.out.println("Test Data Accuracy");
		int[][] misclassification=nn.test(testdata,testtarget);
		for(int i=0;i<misclassification.length;i++){
			System.out.print("Case of misclassifier "+ i+" to,");
			int count=0;
			for(int j=0;j<misclassification[0].length;j++){
				//if(i!=j)
				count+=misclassification[i][j];
				System.out.print(j+"'case num:"+ misclassification[i][j]+"  ");
			}
			System.out.println(" ***Total case:"+ count);
		}
		//DrawWindow dw=new DrawWindow(nn);
	}

	private static void printImage(int[][] image) {//Print image to show the result
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
