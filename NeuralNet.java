package cpe593_neuralnet;
/*
 * This class builds the NeuralNet
 * The NeuralNet includes the momentum and learningRate, input number, output number
 * And hidden layers and output layer and vector of input value
 * Two task of the neural net:  train and test
 * Three main function of training:  feed-forward back-propagation update-weight
 * In training task: print the error every several epochs 
 * In testing task: print the accuracy of the result including misclassification
 * Some supplementary function:  sigmoid, perceptron, print-Architecture, findMax 
 */
public class NeuralNet {
	private double momentum;
	private double learningRate;
	private int num_inputs;
	private int num_outputs;
	private NeuralLayer[] hiddenlayers;
	//private NeuralLayer hiddenlayer;
	private NeuralLayer outputlayer;
	private double[] input_value;
	
	
	public NeuralNet(int num_inputs,int[] hiddenlayer_neurons,int num_outputs, double momentum, double learningRate){
		this.num_inputs=num_inputs;
		this.num_outputs=num_outputs;
		this.learningRate=learningRate;
		this.momentum=momentum;
		hiddenlayers= new NeuralLayer[hiddenlayer_neurons.length];
		this.hiddenlayers[0]= new NeuralLayer(num_inputs,hiddenlayer_neurons[0]);
		for(int i=1;i<hiddenlayer_neurons.length;i++){
			this.hiddenlayers[i]= new NeuralLayer(hiddenlayer_neurons[i-1],hiddenlayer_neurons[i]);
		}
		outputlayer= new NeuralLayer(hiddenlayer_neurons[hiddenlayer_neurons.length-1],num_outputs);
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

		for(int i=0;i<hiddenlayers.length;i++){
			System.out.println("Number of neurons in hidden_layer_"+i+" is: "+ hiddenlayers[i].getNumNeurons());
		}

		System.out.println("Number of Output Neurons: "+this.num_outputs );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	public int findMax(double[] output){
		int maxindex=0;
		double maxvalue=0;
		for(int i=0;i<output.length;i++){
			if(output[i]>maxvalue){
				maxvalue=output[i];
				maxindex=i;
			}
		}
		return maxindex;
	}
	
	public void train(double[][]trainSamples, double[][] labels, int epoch){
		//Complexity: epoch * trainSample.length * (2*O(28*28*10)+3*O(10*10))
		for(int i=0;i<=epoch;i++){
			double error=0;
			for(int j=0;j<trainSamples.length;j++){
				double[] output=netFeedForward(trainSamples[j]);
				if(i%2==0){
					for(int k=0;k<output.length;k++){
						error+=0.5*(output[k]-labels[j][k])*(output[k]-labels[j][k]);
					}
				}
				netBackPropagation(labels[j]);
				updateWeights();
			}
			if(i%10==0){
				System.out.println("When epoch is "+i+",mean square error is "+error);
			}
		}
	}
	public int[][] test(double[][] testSample, double[][] testlabel){//O(testSample.length*O(28*28*10+10*10))
		int count=0;
		int[][] misclassification= new int[testlabel[0].length][testlabel[0].length];
		for(int i=0;i<testSample.length;i++){
			double[] output=netFeedForward(testSample[i]);
			int predict=this.findMax(output);
			if(testlabel[i][predict]==1){
				count++;
			}
			else{
				int origin=0;
				for(int j=0;j<testlabel[i].length;j++){
					if(testlabel[i][j]==1){
						origin=j;
						break;
					}
				}
				misclassification[origin][predict]+=1;
			}
		}
		System.out.println("Total case:"+testSample.length+", Accurate case:"+count+",Wrong case:"+(testSample.length-count));
		System.out.println("Accuracy is "+(int)(10000.0*count/testSample.length)/100.0+"%");
		return misclassification;
	}
	public int test(double[] onetestSample){
		double[] output=netFeedForward(onetestSample);
		int v=this.findMax(output);
		return v;
	}
	public double[] netFeedForward(double[] traindata){//Complexity: O(28*28*10) +O(10*10) input=28*28 hidden=10 output=10
		this.input_value=traindata;
		for(int k=0;k<hiddenlayers.length;k++){
			if(k==0){
				for(int i=0;i<hiddenlayers[k].num_neurons;i++){//O(28*28*10)
					double temp=0;
					for(int j=0;j<this.num_inputs;j++){
						temp+=input_value[j]*hiddenlayers[k].weight[i][j];
					}
					temp+=hiddenlayers[k].weight[i][this.num_inputs];
					hiddenlayers[k].output[i]= sigmoid(temp);
				}
			}
			else{
				for(int i=0;i<hiddenlayers[k].num_neurons;i++){//O(28*28*10)
					double temp=0;
					for(int j=0;j<hiddenlayers[k-1].num_neurons;j++){
						temp+=hiddenlayers[k-1].output[j] * hiddenlayers[k].weight[i][j];
					}
					temp+=hiddenlayers[k].weight[i][hiddenlayers[k-1].num_neurons];
					hiddenlayers[k].output[i]= sigmoid(temp);
				}
				//System.out.println("Now feed in "+k+" hiddenlayer");
			}
			
		}
		for(int i=0;i<outputlayer.num_neurons;i++){//O(10*10)
			double temp=0;
			for(int j=0;j<hiddenlayers[hiddenlayers.length-1].num_neurons;j++){
				temp+=hiddenlayers[hiddenlayers.length-1].output[j]*outputlayer.weight[i][j];
			}
			temp+=outputlayer.weight[i][hiddenlayers[hiddenlayers.length-1].num_neurons];
			outputlayer.output[i] = sigmoid(temp);
		}
		return outputlayer.output;
	}
	
	private void netBackPropagation(double[] label) {//Complexity: O(10)+O(100)
		for(int i=0;i<outputlayer.num_neurons;i++){//O(10) 
			outputlayer.delta[i]=outputlayer.output[i]*(1-outputlayer.output[i])*(label[i]-outputlayer.output[i]);
		}
		for(int k=hiddenlayers.length-1;k>=0;k--){
			if(k==hiddenlayers.length-1){
				for(int i=0;i<hiddenlayers[k].num_neurons;i++){//O(10*10)
					double temp=0;
					for(int j=0;j<outputlayer.num_neurons;j++){
						temp+=outputlayer.weight[j][i]*outputlayer.delta[j];
					}
					hiddenlayers[k].delta[i]=hiddenlayers[k].output[i]*(1-hiddenlayers[k].output[i])*temp;
				}
			}
			else{
				for(int i=0;i<hiddenlayers[k].num_neurons;i++){//O(10*10)
					double temp=0;
					for(int j=0;j<hiddenlayers[k+1].num_neurons;j++){
						temp+=hiddenlayers[k+1].weight[j][i] * hiddenlayers[k+1].delta[j];
					}
					hiddenlayers[k].delta[i]=hiddenlayers[k].output[i]*(1-hiddenlayers[k].output[i])*temp;
				}
				//System.out.println("Now BP in "+k+" hiddenlayer");
			}
		}
	}
	
	private void updateWeights() {// Complexity: O(28*28*10)+O(10*10)
		for(int k=0;k<hiddenlayers.length;k++){
			if(k==0){
				for(int i=0;i<hiddenlayers[k].num_neurons;i++){
					for(int j=0;j<this.num_inputs;j++){
						double change=this.learningRate* hiddenlayers[k].delta[i]*this.input_value[j];
						hiddenlayers[k].weight[i][j]+=change + this.momentum* hiddenlayers[k].momentum_weight[i][j];
						hiddenlayers[k].momentum_weight[i][j]=change;
					}
					hiddenlayers[k].weight[i][this.num_inputs]+= this.learningRate*hiddenlayers[k].delta[i]+this.momentum*hiddenlayers[k].momentum_weight[i][this.num_inputs];
				}
			}
			else{
				for(int i=0;i<hiddenlayers[k].num_neurons;i++){
					for(int j=0;j<hiddenlayers[k-1].num_neurons;j++){
						double change=this.learningRate* hiddenlayers[k].delta[i] * hiddenlayers[k-1].output[j];
						hiddenlayers[k].weight[i][j]+=change + this.momentum* hiddenlayers[k].momentum_weight[i][j];
						hiddenlayers[k].momentum_weight[i][j]=change;
					}
					hiddenlayers[k].weight[i][hiddenlayers[k-1].num_neurons]+= this.learningRate*hiddenlayers[k].delta[i]+this.momentum*hiddenlayers[k].momentum_weight[i][hiddenlayers[k-1].num_neurons];
				}
				//System.out.println("Now UpdataWeight in "+k+" hiddenlayer");
			}
		}
		for(int i=0;i<outputlayer.num_neurons;i++){
			for(int j=0;j<hiddenlayers[hiddenlayers.length-1].num_neurons;j++){
				double change=this.learningRate * outputlayer.delta[i] * hiddenlayers[hiddenlayers.length-1].output[j];
				outputlayer.weight[i][j]+= change+ this.momentum*outputlayer.momentum_weight[i][j];
				outputlayer.momentum_weight[i][j]=change;
			}
			outputlayer.weight[i][hiddenlayers[hiddenlayers.length-1].num_neurons]+=this.learningRate*outputlayer.delta[i]+this.momentum*outputlayer.momentum_weight[i][hiddenlayers[hiddenlayers.length-1].num_neurons];
		}
	}
	
}
