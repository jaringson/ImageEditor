import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math.*;


public class ImageEditor {

	
	public static RGBImage read(String filename) throws IOException{

		Scanner reader = new Scanner (new BufferedInputStream(new FileInputStream(filename)));
		reader.useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");
		String p3 = reader.next();
		//System.out.println(p3);
		//String header = reader.nextLine();
		int numcolumns = reader.nextInt();
		//System.out.println(numcolumns);
		int numrows = reader.nextInt();
		//System.out.println(numrows);
		int size = reader.nextInt();
		//System.out.println(size);
		
		short[][] r = new short[numrows][numcolumns];
		short[][] g = new short[numrows][numcolumns];
		short[][] b = new short[numrows][numcolumns];
		int num;
		int loc = 0; // will range to rowdim*columndim;
		int row;
		int column;
		for(int i = 0; i < numrows;i++){
			for(int j = 0; j < numcolumns;j++){
				for(int k = 0; k<3;k++){
					num = reader.nextInt();
					//System.out.println(num);
					if(k == 0)
						r[i][j] = (short) num;
					if(k== 1)
						g[i][j] = (short) num;
					if(k== 2)
						b[i][j] = (short) num;
				}
			}
		}
		reader.close();
		return new RGBImage(r, g, b);
	}
	
	
	
	public static RGBImage read2(String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename)));
		// deal with header
		String p3 = reader.readLine();
		reader.readLine();
		String dimensions = reader.readLine();
		Pattern dimensionPattern = Pattern.compile("(\\d+) (\\d+)");
		Matcher dimensionMatcher = dimensionPattern.matcher(dimensions);
		String range = reader.readLine();
		if(dimensionMatcher.matches()){
			int numcolumns = Integer.parseInt(dimensionMatcher.group(1));
			int numrows = Integer.parseInt(dimensionMatcher.group(2));
			short[][] r = new short[numcolumns][numrows];
			short[][] g = new short[numcolumns][numrows];
			short[][] b = new short[numcolumns][numrows];
			String line;
			String[] triples;
			for(int row=0;row<numcolumns;row++){
					line = reader.readLine();
					//if(line == null) System.out.println("died with row = "+row+", column = "+column);
					triples = line.split("\\s+");
					if((triples.length % 3)!=0){
						throw new IOException("this is not a rgb ppm file!");
					}
					for(int i=0;i<triples.length;i+=3){
						r[row][i/3] = (short)Integer.parseInt(triples[i]);
						g[row][i/3] = (short)Integer.parseInt(triples[i+1]);
						b[row][i/3] = (short)Integer.parseInt(triples[i+2]);
					}					
			}
			return new RGBImage(r, g, b);
		} else {
			throw new IOException("could not read this; maybe it's not an ascii rgb ppm?"+dimensions);
		}
	}
	
	public static void  writeGrayscale(RGBImage image, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		// write header
		int rowdimension = image.numrows;
		int columndimension = image.numcolumns;
		writer.write("P3");
		writer.newLine();
		writer.write("# Created by Jaron Ellingson");
		writer.newLine();
		writer.write(image.numcolumns+" "+image.numrows);
		writer.newLine();
		writer.write("255");
		writer.newLine();
		int count = 0;
		//System.out.println(image.numrows);
		//System.out.println(image.numcolumns);
		for(int row=0;row<rowdimension;row++){
			for(int column=0;column<columndimension;column++){
				//System.out.println(count);
				int avg = (image.getRed()[row][column] + image.getGreen()[row][column] + image.getBlue()[row][column] )/ 3;
				writer.write(avg+"\n");
				writer.write(avg+"\n");
				writer.write(avg+"\n");
				count++;
				//if(column < columndimension - 1)writer.write(",");
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
	public static void  writeInvert(RGBImage image, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		// write header
		int rowdimension = image.numrows;
		int columndimension = image.numcolumns;
		writer.write("P3");
		writer.newLine();
		writer.write("# Created by Jaron Ellingson");
		writer.newLine();
		writer.write(image.numcolumns+" "+image.numrows);
		writer.newLine();
		writer.write("255");
		writer.newLine();
		int[] array = new int[256];
		for(int i = 0; i<256;i++)
		{
			array[i] = 255-i;
		}
		for(int row=0;row<rowdimension;row++){
			for(int column=0;column<columndimension;column++){
				writer.write(array[image.getRed()[row][column]]+"\n");
				writer.write(array[image.getGreen()[row][column]]+"\n");
				writer.write(array[image.getBlue()[row][column]]+"\n");
				//if(column < columndimension - 1)writer.write(",");
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
	
	static int findMaxDiff(int x, int y, int z)
	{
		
		if(Math.abs(x) > Math.abs(y) && Math.abs(x) > Math.abs(z))
		{
			return x;
		}
		else if(Math.abs(y)> Math.abs(x) && Math.abs(y)>Math.abs(z))
		{
			return y;
		}
		else if(Math.abs(z)> Math.abs(x) && Math.abs(z)>Math.abs(y))
		{
			return z;
		}
		else if (Math.abs(x) == Math.abs(y))
		{
			return x;
		}
		else if (Math.abs(x) == Math.abs(z))
		{
			return x;
		}
		else if (Math.abs(y) == Math.abs(z))
		{
			return y;
		}
		else
			return z;
		
		
	}
	public static void  writeEmboss(RGBImage image, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		// write header
		int rowdimension = image.numrows;
		int columndimension = image.numcolumns;
		writer.write("P3");
		writer.newLine();
		writer.write("# Created by Jaron Ellingson");
		writer.newLine();
		writer.write(image.numcolumns+" "+image.numrows);
		writer.newLine();
		writer.write("255");
		writer.newLine();
		
		for(int row=0;row<rowdimension;row++){
			for(int column=0;column<columndimension;column++){
				if(row == 0 || column == 0)
				{
					writer.write(128+"\n");
					writer.write(128+"\n");
					writer.write(128+"\n");
				}
				else
				{
					int redDiff = image.getRed()[row][column] - image.getRed()[row-1][column-1];
					int greenDiff = image.getGreen()[row][column] - image.getGreen()[row-1][column-1];
					int blueDiff = image.getBlue()[row][column] - image.getBlue()[row-1][column-1];
					int maxDiff = findMaxDiff(redDiff, greenDiff,blueDiff);
					int v = 128 + maxDiff;
					if(v < 0 )
					{
						v= 0;
					}
					else if(v > 255)
					{
						v =255;
					}
			
					writer.write(v+"\n");
					writer.write(v+"\n");
					writer.write(v+"\n");
				}
				
				//if(column < columndimension - 1)writer.write(",");
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
	public static void  writeMotionblur(RGBImage image, String filename, int blurLen) throws IOException{
		if(blurLen < 0)
		{
			throw new IOException("Blur Length "+blurLen+" needs to greater than 0");
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		// write header
		int rowdimension = image.numrows;
		int columndimension = image.numcolumns;
		writer.write("P3");
		writer.newLine();
		writer.write("# Created by Jaron Ellingson");
		writer.newLine();
		writer.write(image.numcolumns+" "+image.numrows);
		writer.newLine();
		writer.write("255");
		writer.newLine();
		
		for(int row=0;row<rowdimension;row++){
			for(int column=0;column<columndimension;column++){
				int avgRed = 0;
				int r;
				for(r = 0; r < blurLen; r++)
				{
					int newColumn = r + column;
					if(newColumn >= columndimension)
						break;
					avgRed = avgRed +  image.getRed()[row][newColumn];
				}
				if(r == 0)
					r = 1;
				avgRed = avgRed / r;
				
				int avgGreen = 0;
				int g;
				for(g= 0; g <blurLen; g++)
				{
					int newColumn = g + column;
					if(newColumn >= columndimension )
						break;
					avgGreen = avgGreen +  image.getGreen()[row][newColumn];
				}
				if(g == 0)
					g = 1;
				avgGreen = avgGreen / g;
				
				int avgBlue = 0;
				int b;
				for(b= 0; b <blurLen; b++)
				{
					int newColumn = b + column;
					if(newColumn >= columndimension)
						break;
					avgBlue = avgBlue +  image.getBlue()[row][newColumn];
				}
				if(b == 0)
					b = 1;
				avgBlue = avgBlue / b;
				
			
				writer.write(avgRed+"\n");
				writer.write(avgGreen+"\n");
				writer.write(avgBlue+"\n");
				
				
				
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
	
	public static void main(String[] args) throws Exception{
		/*Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(comman)"cd"*/
		RGBImage image = ImageEditor.read(args[0]);
		//System.out.println(args[2]);
		//String temp = args[2];
		//System.out.println()
		
		if(args[2].equals("grayscale"))
			ImageEditor.writeGrayscale(image, args[1]);
		if(args[2].equals("invert"))
			ImageEditor.writeInvert(image, args[1]);
		if(args[2].equals("emboss"))
			ImageEditor.writeEmboss(image, args[1]);
		if(args[2].equals("motionblur"))
			ImageEditor.writeMotionblur(image, args[1], Integer.parseInt(args[3]));
	
	}
	
}
