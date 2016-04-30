package cpe593_neuralnet;
/*
 * This is the class to read the image file in order to display it 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawingArea extends JPanel {
	private BufferedImage bimage=null;
	public DrawingArea() throws IOException{
		bimage= ImageIO.read(new File("5.jpg"));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bimage, 0, 0, null);
	}
}
