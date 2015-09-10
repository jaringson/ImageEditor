
public class RGBImage {

	public short[][] red, green, blue;
	int numrows, numcolumns;
	public RGBImage(short[][] r, short[][] g, short[][] b)
	{
		red = r;
		green = g;
		blue = b;
		numrows = r.length;
		numcolumns = r[0].length;
		//System.out.println(numrows);
		//System.out.println(numcolumns);
	}
	public RGBImage()
	{
		
	}
	public short[][] getRed()
	{
		return red;
	}
	public short[][] getGreen()
	{
		return green;
	}
	public short[][] getBlue()
	{
		return blue;
	}
	
}