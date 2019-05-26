package com.island;
import android.graphics.*;
import android.widget.*;
public class Testo extends Oggetto
{
	private String testo;
	private int colore;
	private int type;
	private Paint.Align textAlign=Paint.Align.CENTER;
	private Typeface typeface;
	private double textX=0.5;
	private double larghezzaX=1;
	private double carattere=1;
	public Testo(Gruppo gruppo,double x,double y,double larghezza,double altezza,String testo,int colore,final int sfondo)
	{
		super(gruppo,x,y,larghezza,altezza);
		type=gruppo.schermo().type();
		typeface=gruppo.schermo().typeface();
		this.testo=testo;
		this.colore=colore;
		colore(sfondo);
	}
	public Testo(Gruppo gruppo,double x,double y,double larghezza,double altezza,String testo)
	{
		this(gruppo,x,y,larghezza,altezza,testo,Color.WHITE,Color.TRANSPARENT);
	}
	public Testo scrivi(String testo,int colore,final int sfondo)
	{
		this.testo=testo;
		this.colore=colore;
		schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					invalidate();
				}
			}
		);
		return this;
	}
	public Testo scrivi(String testo,int colore)
	{
		String vecchio=this.testo;
		this.testo=testo;
		this.colore=colore;
		if(!vecchio.equals(testo))schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					invalidate();
				}
			}
		);
		return this;
	}
	public Testo scrivi(String testo)
	{
		String vecchio=this.testo;
		this.testo=testo;
		if(!vecchio.equals(testo))schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					invalidate();
				}
			}
		);
		return this;
	}
	public Testo scrivi(int colore,final int sfondo)
	{
		this.colore=colore;
		colore(sfondo);
		schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					invalidate();
				}
			}
		);
		return this;
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Paint paint=new Paint();
		paint.setTextAlign(textAlign);
		paint.setColor(colore);
		if(typeface!=null)paint.setTypeface(Typeface.create(typeface,type));
		paint.setTextSize((float)(canvas.getHeight()/2*carattere));
		paint.setTextScaleX((float)(unitaX()/unitaY()*larghezzaX));
		if(testo!=null)canvas.drawText(testo,(float)(canvas.getWidth()*textX),(float)(canvas.getHeight()/1.5),paint);
	}
	public String testo()
	{
		return testo;
	}
	public Testo allineamento(Paint.Align allineamento)
	{
		if(allineamento==Paint.Align.CENTER)textX=0.5;
		else if(allineamento==Paint.Align.LEFT)textX=0;
		else textX=1;
		this.textAlign=allineamento;
		return this;
	}
	public Paint.Align textAlign()
	{
		return textAlign;
	}
	public Testo larghezzaX(double larghezzaX)
	{
		this.larghezzaX=larghezzaX;
		return this;
	}
	public Testo carattere(double carattere)
	{
		this.carattere=carattere;
		return this;
	}
	public int coloreTesto()
	{
		return colore;
	}
	public void coloreTesto(int colore)
	{
		this.colore=colore;
	}
}
