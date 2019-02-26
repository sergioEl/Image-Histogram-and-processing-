// Skeletal program for the "Image Histogram" assignment
// Written by:  Minglun Gong
// Seokho Han 201761541
// Teng Hong Lee 201723459
// Utsav Ashish Koju 201763299

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;

// Main class
public class ImageHistogram extends Frame implements ActionListener {
	BufferedImage input;
	int width, height;
	TextField texRad, texThres;
	ImageCanvas source, target;
	PlotCanvas plot;

	// Constructor
	public ImageHistogram(String name) {
		super("Image Histogram");
		// load image
		try {
			input = ImageIO.read(new File(name));
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
		width = input.getWidth();
		height = input.getHeight();
		// prepare the panel for image canvas.
		Panel main = new Panel();
		source = new ImageCanvas(input);
		plot = new PlotCanvas();
		target = new ImageCanvas(input);
		main.setLayout(new GridLayout(1, 3, 10, 10));
		main.add(source);
		main.add(plot);
		main.add(target);
		// prepare the panel for buttons.
		Panel controls = new Panel();
		Button button = new Button("Display Histogram");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Histogram Stretch");
		button.addActionListener(this);
		controls.add(button);
		controls.add(new Label("Cutoff fraction:"));
		texThres = new TextField("10", 2);
		controls.add(texThres);
		button = new Button("Aggressive Stretch");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Histogram Equalization");
		button.addActionListener(this);
		controls.add(button);
		// add two panels
		add("Center", main);
		add("South", controls);
		addWindowListener(new ExitListener());
		setSize(width*2+400, height+100);
		setVisible(true);
	}
	class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	// Action listener for button click events
	public void actionPerformed(ActionEvent e) {
		// example -- compute the average color for the image
		if ( ((Button)e.getSource()).getLabel().equals("Display Histogram") ) {
			int red=0, green=0, blue=0;
			//RGB frequency
			int[] rFre = new int[256];
			int[] gFre = new int[256];
			int[] bFre = new int[256];
			for ( int y=0, i=0 ; y<height ; y++ ){
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					//frequency calculate
					if (red != 0){rFre[red]++;}
					if (green != 0){gFre[green]++;}
					if (blue != 0){bFre[blue]++;}
				}
			}
			System.out.println("R: " + Arrays.toString(rFre) + " \nG: " + Arrays.toString(gFre)+ " \nB: " + Arrays.toString(bFre));
			plot.drawHistogram(rFre, gFre, bFre);
		}
		
		if ( ((Button)e.getSource()).getLabel().equals("Histogram Stretch") ){
			System.out.println("Histogram Stretch is called!\n cutoff : " + texThres.getText());

			int red=0, green=0, blue=0;
			int min = 0;
			int max = 255;
			//RGB frequency
			int[] rFre = new int[256];
			int[] gFre = new int[256];
			int[] bFre = new int[256];
			
			//for stretch histo
			int[] new_rFre = new int[256];
            int[] new_gFre = new int[256];
            int[] new_bFre = new int[256];
			
			for ( int y=0, i=0 ; y<height ; y++ ){
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					
					//frequency calculate
					if (red != 0){rFre[red]++;}
					if (green != 0){gFre[green]++;}
					if (blue != 0){bFre[blue]++;}
					
					 //stretch them to 0 to 255
	                red = (int) (1.0*( red - min) / (max - min) * 255);
	                green = (int) (1.0*( green - min) / (max - min) * 255);
	                blue = (int) (1.0*( blue - min) / (max - min) * 255);
	 
	                if(red> 255) red=255;
	                if(green> 255) green=255;
	                if(blue> 255) blue=255;
	                
	              //new_histo calculate
					if (red != 0){new_rFre[red]++;}
					if (green != 0){new_gFre[green]++;}
					if (blue != 0){new_bFre[blue]++;}
					
				}
			}
			//print the RGB array
			System.out.println("R: " + Arrays.toString(new_rFre) +
					" \nG: " + Arrays.toString(new_gFre)+ " \nB: " +
					Arrays.toString(new_bFre));
			plot.drawHistogram(new_rFre, new_gFre, new_bFre);
			
		}
		if (((Button)e.getSource()).getLabel().equals("Aggressive Stretch")) {
			int cutoffvalue = Integer.parseInt(texThres.getText());
			int fraction = 255 * (cutoffvalue/200);
			int zero = 0 + fraction;
			int twofiftyfive = 255 - (255 * fraction/100);
			int red=0, green=0, blue=0;
			int min = 0;
			int max = 150;
			//RGB frequency
			int[] rFre = new int[256];
			int[] gFre = new int[256];
			int[] bFre = new int[256];

			//for stretch histo
			int[] new_rFre = new int[256];
			int[] new_gFre = new int[256];
			int[] new_bFre = new int[256];

			for ( int y = 0, i=0 ; y<height ; y++ ){
				for ( int x= fraction ; x<width - fraction ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();

					//frequency calculate
					if (red != 0){rFre[red]++;}
					if (green != 0){gFre[green]++;}
					if (blue != 0){bFre[blue]++;}

					//stretch them to 0 to 255
					red = (int) (1.0*( red - min) / (max - min) * 255);
					green = (int) (1.0*( green - min) / (max - min) * 255);
					blue = (int) (1.0*( blue - min) / (max - min) * 255);

					if(red> 255) red=255;
					if(green> 255) green=255;
					if(blue> 255) blue=255;

					//new_histo calculate
					if (red != 0){new_rFre[red]++;}
					if (green != 0){new_gFre[green]++;}
					if (blue != 0){new_bFre[blue]++;}

				}
			}
			//print the RGB array
			System.out.println("R: " + Arrays.toString(new_rFre) +
					" \nG: " + Arrays.toString(new_gFre)+ " \nB: " +
					Arrays.toString(new_bFre));
			plot.drawHistogram(new_rFre, new_gFre, new_bFre);
		}

		if ( ((Button)e.getSource()).getLabel().equals("Histogram Equalization") ){
			int[] h = CalculateHist();
			int factor = width*height;
			int k = 0;
			long sum = 0;
			int pixel[];
			float scale = (float) 255.0/factor;
			int[] rFre = new int[256];
			int[] gFre = new int[256];
			int[] bFre = new int[256];
			for(int x = 0; x<h.length; x++) {
				sum+=h[x];
				int value = (int) (scale*sum);
				h[x] = (value>255)?255:value;
			}
			for(int i = 0; i<width; i++) {
				for(int j = 0; j<height; j++) {
					pixel = input.getRaster().getPixel(i,j,new int[3]);
					k = h[pixel[0]];
					Color color = new Color(k,k,k);
					int rgb = color.getRGB();
					BufferedImage he = input;
					he.setRGB(i,j,rgb);

					target.resetImage(he);

					//frequency calculate
					if (i != 0){rFre[i]++;}
					if (j != 0){gFre[j]++;}
					if (k != 0){bFre[k]++;}

					//plot.drawHistogram(color.getRed(), color.getGreen(), color.getBlue());
				}
			}
			plot.drawHistogram(rFre, gFre, bFre);
		}
		
	}
	public static void main(String[] args) {
		new ImageHistogram(args.length==1 ? args[0] : "baboon.png");
	}

	public int[] CalculateHist() {
		int k;
		int pixel[];
		int levels[] = new int[256];
		for(int i = 0; i<width; i++) {
			for(int j = 0; j<height; j++) {
				pixel = input.getRaster().getPixel(i,j,new int[3]);
				levels[pixel[0]]++;
			}
		}
		return levels;
	}
}

// Canvas for plotting histogram
class PlotCanvas extends Canvas {
	// lines for plotting axes and mean color locations
	LineSegment x_axis, y_axis;
	LineSegment[] red = new LineSegment[256], green = new LineSegment[256], blue = new LineSegment[256];
	boolean showMean = false;

	public PlotCanvas() {
		x_axis = new LineSegment(Color.BLACK, -10, 0, 256+10, 0);
		y_axis = new LineSegment(Color.BLACK, 0, -10, 0, 200+10);
	}
	/*
	// set mean image color for plot
	public void setMeanColor(Color clr) {
		red = new LineSegment(Color.RED, clr.getRed(), 0, clr.getRed(), 100);
		green = new LineSegment(Color.GREEN, clr.getGreen(), 0, clr.getGreen(), 100);
		blue = new LineSegment(Color.BLUE, clr.getBlue(), 0, clr.getBlue(), 100);
		showMean = true;
		repaint();
	}
	*/
	public void drawHistogram(int[] r, int[] g, int[] b){
		System.out.println("drawHistogram() is called!");
		int diFactor = 8;
		for(int i = 0; i < 255; i++){
		    red[i] = new LineSegment(Color.RED, i, r[i]/diFactor, i+1, r[i+1]/diFactor);
		    green[i] = new LineSegment(Color.GREEN, i, g[i]/diFactor, i+1, g[i+1]/diFactor);
		    blue[i] = new LineSegment(Color.BLUE, i, b[i]/diFactor, i+1, b[i+1]/diFactor);
	    	showMean = true;
		    repaint();
		}
	}
	// redraw the canvas
	public void paint(Graphics g) {
		//HSH
		System.out.println("paint() is called ");
		// draw axis
		int xoffset = (getWidth() - 256) / 2;
		//HSH
		System.out.println("x OFF : " + xoffset + " Width " + getWidth() + "  " + getHeight());
		int yoffset = (getHeight() - 200) / 2;
		x_axis.draw(g, xoffset, yoffset, getHeight());
		y_axis.draw(g, xoffset, yoffset, getHeight());
		if ( showMean ) {
			for(int i = 0; i< 254; i++){
				red[i].draw(g, xoffset, yoffset, getHeight());
				green[i].draw(g, xoffset, yoffset, getHeight());
				blue[i].draw(g, xoffset, yoffset, getHeight());
			}
			
		}
	}

	public float rgbTOhsl(int r, int g, int b) {
		float h, s, l, max, min, diff, red, green, blue;
		red = (float) r/255;
		green = (float) g/255;
		blue = (float) b/255;
		max = Math.max(red, Math.max(green, blue));
		min = Math.min(red, Math.min(green, blue));
		diff = max-min;
		if(max == min)
			h = 0;
		else if(max == red)
			h = (60 * ((green - blue)/diff)+360)%360;
		else if(max == green)
			h = (60 * ((blue - red)/diff)+120)%360;
		else if(max == blue)
			h = (60 * ((red-green)/diff)+240)%360;
		if(max == 0)
			s = 0;
		else
			s = diff/max;
		return (max+min)/2;
	}
}

// LineSegment class defines line segments to be plotted
class LineSegment {
	// location and color of the line segment
	int x0, y0, x1, y1;
	Color color;
	// Constructor
	public LineSegment(Color clr, int x0, int y0, int x1, int y1) {
		color = clr;
		this.x0 = x0; this.x1 = x1;
		this.y0 = y0; this.y1 = y1;
	}
	public void draw(Graphics g, int xoffset, int yoffset, int height) {
		g.setColor(color);
		g.drawLine(x0+xoffset, height-y0-yoffset, x1+xoffset, height-y1-yoffset);
	}
}
