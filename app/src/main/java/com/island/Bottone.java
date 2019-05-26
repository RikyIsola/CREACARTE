package com.island;
import android.graphics.*;
import android.view.*;
import android.widget.*;
public class Bottone extends Testo
{
	private int sfondo,sfondoPremuto,sfondoRilascio,colore,colorePremuto,coloreRilascio;
	private boolean cliccabile=true;
	public Bottone(Gruppo gruppo,double x,double y,double larghezza,double altezza,int coloreRilascio,int colorePremuto)
	{
		super(gruppo,x,y,larghezza,altezza,"",Color.WHITE,Color.TRANSPARENT);
		this.colorePremuto=colorePremuto;
		this.coloreRilascio=coloreRilascio;
		colore=coloreRilascio;
	}
	public Bottone(Gruppo gruppo,double x,double y,double larghezza,double altezza)
	{
		this(gruppo,x,y,larghezza,altezza,Color.LTGRAY,Color.DKGRAY);
	}
	public Bottone colore(int coloreRilascio,int colorePremuto)
	{
		if(colore==this.colorePremuto)colore=colorePremuto;
		else colore=coloreRilascio;
		this.colorePremuto=colorePremuto;
		this.coloreRilascio=coloreRilascio;
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
	public Bottone cliccabile(boolean cliccabile)
	{
		this.cliccabile=cliccabile;
		return this;
	}
	public boolean cliccabile()
	{
		return cliccabile;
	}
	public int colorePremuto()
	{
		return colorePremuto;
	}
	public int coloreRilascio()
	{
		return coloreRilascio;
	}
	public void onDraw(Canvas canvas)
	{
		Paint paint=new Paint();
		paint.setColor(colore);
		final int larghezza=(int)(large()*unitaX());
		final int altezza=(int)(tall()*unitaY());
		canvas.drawRoundRect(new RectF(0,0,larghezza,altezza),(float)(dimensioni()/4),(float)(dimensioni()/4),paint);
		paint.setColor(Color.BLACK);
		paint.setAlpha(alpha());
		if(schermo().esiste(immagine()))canvas.drawBitmap(schermo().immagine(immagine()),null,new RectF((float)resto(true)/2,(float)resto(false)/2,(float)(dimensioni()+resto(true)/2),(float)(dimensioni()+resto(false)/2)),paint);
		else super.onDraw(canvas);
	}
	private double dimensioni()
	{
		if(tall()*unitaY()>large()*unitaX())return large()*unitaX();
		else return tall()*unitaY();
	}
	private double resto(boolean large)
	{
		if(large)return (large()*unitaX()-dimensioni());
		else return (tall()*unitaY()-dimensioni());
	}
	public boolean onTouchEvent(MotionEvent evento)
	{
		if(cliccabile)
		{
			if(evento.getAction()==MotionEvent.ACTION_DOWN)
			{
				colore=colorePremuto;
				sfondo=sfondoPremuto;
				schermo().runOnUiThread(new Runnable()
					{
						public void run()
						{
							invalidate();
						}
					}
				);
			}
			else if(evento.getAction()==MotionEvent.ACTION_UP)
			{
				colore=coloreRilascio;
				sfondo=sfondoRilascio;
				if(cliccando(this,evento))performClick();
				schermo().runOnUiThread(new Runnable()
					{
						public void run()
						{
							invalidate();
						}
					}
				);
			}
			return true;
		}
		else return super.onTouchEvent(evento);
	}
	public static boolean cliccando(Oggetto oggetto,MotionEvent evento)
	{
		if(evento.getX()<oggetto.large()*oggetto.unitaX()&&evento.getY()<oggetto.tall()*oggetto.unitaY()&&evento.getX()>0&&evento.getY()>0)return true;
		else return false;
	}
}
