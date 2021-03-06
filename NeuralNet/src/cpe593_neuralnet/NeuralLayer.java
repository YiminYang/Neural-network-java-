package cpe593_neuralnet;

import java.util.Random;
/*
 * This class is about each Neural Layer, The neural layer has its input number, its neurons number
 * And vector of the output, delta for each neuron
 * Also two dimension data of the weight and momentum weight
 * First, initialize the weight by random number from -1 to 1
 */
public class NeuralLayer {
	public int num_inputs;
	public int num_neurons;
	public double[] output;
	public double[] delta;
	public double[][] momentum_weight;
	public double[][] weight;
	private static Random r=new Random();
	
	public NeuralLayer(int num_inputs,int num_neurons){//constructor and initialize the weight
		this.num_inputs=num_inputs;
		this.num_neurons=num_neurons;
		output= new double[num_neurons];
		delta= new double[num_neurons];
		momentum_weight=new double[num_neurons][num_inputs+1];
		weight=new double[num_neurons][num_inputs+1];
		initializeWeight();
	}
	//Two layers, so 2O(in*out)
	public void initializeWeight(){//complexity:  O(num_inputs*num_neurons)
		for(int i=0;i<weight.length;i++){
			//double value=r.nextDouble()*2-1;
			for (int j=0;j<weight[0].length;j++){
				//weight[i][j]=value;
				weight[i][j]=r.nextDouble()*2-1;
			}
		}
	}
	
	public int getNumNeurons(){
		return this.num_neurons;
	}
}
