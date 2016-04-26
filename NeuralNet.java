package cpe593_neuralnet;

public class NeuralNet {
	private double momentum;
	private double learningRate;
	private int num_inputs;
	private int num_outputs;
	//private NeuralLayer[] hiddenlayers;
	private NeuralLayer hiddenlayer;
	private NeuralLayer outputlayer;
	private double[] input_value;
	
	
	public NeuralNet(int num_inputs,int hiddenlayer_neurons,int num_outputs, double momentum, double learningRate){
		this.num_inputs=num_inputs;
		this.num_outputs=num_outputs;
		this.learningRate=learningRate;
		this.momentum=momentum;
		hiddenlayer= new NeuralLayer(num_inputs,hiddenlayer_neurons);
		outputlayer= new NeuralLayer(hiddenlayer_neurons,num_outputs);
		input_value= new double[num_inputs];
	}
	
	public double sigmoid(double v){
		return 1.0 / (1.0 + Math.pow(Math.E, -v));
	}
	public double preceptron(double v,double threshold){
		int value=0;
		if(v>threshold){
			value=1;
		}
		else
			value=-1;
		return value;
	}
	
	
	public void printArchitecture(){
		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Number of Input Neurons: "+this.num_inputs);
		System.out.println("Number of Hidden Layers: "+ 1);

		
		System.out.println("Number of neurons in hidden layer: "+ hiddenlayer.getNumNeurons());

		System.out.println("Number of Output Neurons: "+this.num_outputs );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	public int FindMax(double[] output){
		int maxindex=0;
		double maxvalue=0;
		for(int i=1;i<=output.length;i++){
			if(output[i-1]>maxvalue){
				maxvalue=output[i-1];
				maxindex=i;
			}
		}
		return maxindex;
	}
	
	public void train(double[][]trainSamples, double[][] labels, int epoch){
		for(int i=0;i<=epoch;i++){
			double error=0;
			for(int j=0;j<trainSamples.length;j++){
				double[] output=netFeedForward(trainSamples[j]);
				for(int k=0;k<output.length;k++){
					error+=0.5*(output[k]-labels[j][k])*(output[k]-labels[j][k]);
				}
				netBackPropagation(labels[j]);
				updateWeights();
			}
			if(i%2==0){
				System.out.println("When epoch is "+i+",mean square error is "+error);
			}
		}
	}
	public void test(double[][] testSample, double[][] testlabel){
		int count=0;
		for(int i=0;i<testSample.length;i++){
			double[] output=netFeedForward(testSample[i]);
			int v=this.FindMax(output);
			if(testlabel[i][v-1]==1){
				count++;
			}
		}
		System.out.println("Accuracy is "+(int)(10000.0*count/testSample.length)/100.0+"%");
	}
	public int test(double[] onetestSample){
		double[] output=netFeedForward(onetestSample);
		int v=this.FindMax(output);
		return v;
	}
	public double[] netFeedForward(double[] traindata){
		this.input_value=traindata;
		for(int i=0;i<hiddenlayer.num_neurons;i++){
			double temp=0;
			for(int j=0;j<this.num_inputs;j++){
				temp+=input_value[j]*hiddenlayer.weight[i][j];
			}
			temp+=hiddenlayer.weight[i][this.num_inputs];
			hiddenlayer.output[i]= sigmoid(temp);
		}
		
		for(int i=0;i<outputlayer.num_neurons;i++){
			double temp=0;
			for(int j=0;j<hiddenlayer.num_neurons;j++){
				temp+=hiddenlayer.output[j]*outputlayer.weight[i][j];
			}
			temp+=outputlayer.weight[i][hiddenlayer.num_neurons];
			outputlayer.output[i] = sigmoid(temp);
		}
		return outputlayer.output;
	}
	
	private void netBackPropagation(double[] label) {
		for(int i=0;i<outputlayer.num_neurons;i++){
			outputlayer.delta[i]=outputlayer.output[i]*(1-outputlayer.output[i])*(label[i]-outputlayer.output[i]);
		}
		for(int i=0;i<hiddenlayer.num_neurons;i++){
			double temp=0;
			for(int j=0;j<outputlayer.num_neurons;j++){
				temp+=outputlayer.weight[j][i]*outputlayer.delta[j];
			}
			hiddenlayer.delta[i]=hiddenlayer.output[i]*(1-hiddenlayer.output[i])*temp;
		}
	}
	
	private void updateWeights() {
		for(int i=0;i<hiddenlayer.num_neurons;i++){
			for(int j=0;j<this.num_inputs;j++){
				double change=this.learningRate* hiddenlayer.delta[i]*this.input_value[j];
				hiddenlayer.weight[i][j]+=change + this.momentum* hiddenlayer.momentum_weight[i][j];
				hiddenlayer.momentum_weight[i][j]=change;
			}
			hiddenlayer.weight[i][this.num_inputs]+= this.learningRate*hiddenlayer.delta[i]+this.momentum*hiddenlayer.momentum_weight[i][this.num_inputs];
		}
		
		for(int i=0;i<outputlayer.num_neurons;i++){
			for(int j=0;j<hiddenlayer.num_neurons;j++){
				double change=this.learningRate* outputlayer.delta[i]*hiddenlayer.output[j];
				outputlayer.weight[i][j]+= change+ this.momentum*outputlayer.momentum_weight[i][j];
				outputlayer.momentum_weight[i][j]=change;
			}
			outputlayer.weight[i][hiddenlayer.num_neurons]+=this.learningRate*outputlayer.delta[i]+this.momentum*outputlayer.momentum_weight[i][hiddenlayer.num_neurons];
		}
	}
	
}
