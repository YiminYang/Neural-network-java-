package cpe593_neuralnet;
/*
 * This is the class to test how to read a file and how to pre-process the data
 */
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class readfile {
	public static void main(String[] args) throws IOException{
			BufferedImage bimage = null;
			try {
				bimage = ImageIO.read(new File("5.jpg"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 for(int i=0;i<=27;i++){
					for(int j=0;j<=27;j++){
						if(bimage.getRGB(j, i)==-16777216)
							System.out.print(1+" ");
						else
							System.out.print(0+" ");
						
					}System.out.println();
				}
	           
		  
	}
}
