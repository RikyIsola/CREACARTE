package com.island;
import android.graphics.*;
public class Barra extends Testo
{
	private double caricamento;
	private int colore;
	private boolean verticale;
	public Barra(Gruppo gruppo,double x,double y,double larghezza,double altezza,int colore,final int sfondo)
	{
		super(gruppo,x,y,larghezza,altezza,"",Color.TRANSPARENT,Color.TRANSPARENT);
		this.colore=colore;
		colore(sfondo);
	}
	public Barra verticale(boolean verticale)
	{
		this.verticale=verticale;
		return this;
	}
	public boolean verticare()
	{
		return verticale;
	}
	public Barra colore(int colore,int sfondo)
	{
		this.colore=colore;
		colore(sfondo);
		return this;
	}
	public int colore()
	{
		return colore;
	}
	public void onDraw(Canvas canvas)
	{
		Paint paint=new Paint();
		paint.setColor(colore);
		if(verticale)canvas.drawRect(0,0,canvas.getWidth(),(float)(canvas.getHeight()/100*caricamento),paint);
		else canvas.drawRect(0,0,(float)(canvas.getWidth()/100*caricamento),canvas.getHeight(),paint);
		super.onDraw(canvas);
	}
	public Barra caricamento(double caricamento)
	{
		this.caricamento=caricamento;
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
	public double caricamento()
	{
		return caricamento;
	}
}
