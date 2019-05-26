package com.island;
import android.graphics.*;
public class Gif extends Oggetto
{
	private Movie gif;
	private long tempo;
	private Bitmap bitmap;
	public Gif(Gruppo g,double x,double y,double larghezza,double altezza,int gif)
	{
		super(g,x,y,larghezza,altezza);
		this.gif=Movie.decodeStream(schermo().getResources().openRawResource(gif));
		tempo=System.currentTimeMillis();
		bitmap=Bitmap.createBitmap(this.gif.width(),this.gif.height(),Bitmap.Config.ARGB_8888);
	}
	public Gif sempre()
	{
		long tempo=System.currentTimeMillis()-this.tempo;
		if(tempo>=gif.duration())
		{
			this.tempo=System.currentTimeMillis();
			tempo=0;
		}
		gif.setTime((int)tempo);
		Canvas c=new Canvas(bitmap);
		for(int x=0;x<bitmap.getWidth();x++)for(int y=0;y<bitmap.getHeight();y++)bitmap.setPixel(x,y,Color.TRANSPARENT);
		gif.draw(c,0,0);
		schermo().runOnUiThread(new Runnable()
		{
			public void run()
			{
				invalidate();
			}
		});
		return this;
	}
	public void onDraw(Canvas c)
	{
		Paint p=new Paint();
		p.setAlpha(alpha());
		if(bitmap!=null&&!bitmap.isRecycled())c.drawBitmap(bitmap,null,new Rect(0,0,c.getWidth(),c.getHeight()),p);
	}
}
