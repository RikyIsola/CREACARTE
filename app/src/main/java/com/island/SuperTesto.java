package com.island;
import android.graphics.*;
import android.view.accessibility.*;
import android.widget.*;
import java.util.*;
public class SuperTesto extends Oggetto
{
	private String testo;
	private int colore;
	private int type;
	private Paint.Align textAlign=Paint.Align.CENTER;
	private Typeface typeface;
	private double textX=0.5;
	private double larghezzaX=1;
	private double carattere=1;
	private String[]finale=new String[0];
	public SuperTesto(Gruppo gruppo,double x,double y,double larghezza,double altezza,String testo,int colore,final int sfondo)
	{
		super(gruppo,x,y,larghezza,altezza);
		type=gruppo.schermo().type();
		typeface=gruppo.schermo().typeface();
		this.testo=testo;
		this.colore=colore;
		colore(sfondo);
	}
	public SuperTesto(Gruppo gruppo,double x,double y,double larghezza,double altezza,String testo)
	{
		this(gruppo,x,y,larghezza,altezza,testo,Color.WHITE,Color.TRANSPARENT);
	}
	public SuperTesto scrivi(String testo,int colore,final int sfondo)
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
	public SuperTesto scrivi(String testo,int colore)
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
	public SuperTesto scrivi(String testo)
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
	public SuperTesto scrivi(int colore,final int sfondo)
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
		paint.setTextSize((float)(tall()*unitaY()*carattere));
		paint.setTextScaleX((float)(unitaX()/unitaY()*larghezzaX));
		for(int a=0;a<finale.length;a++)if(finale[a]!=null)canvas.drawText(finale[a],(float)(canvas.getWidth()*textX),(a+1)*paint.getTextSize(),paint);
	}
	public String testo()
	{
		return testo;
	}
	public SuperTesto allineamento(Paint.Align allineamento)
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
	public SuperTesto larghezzaX(double larghezzaX)
	{
		this.larghezzaX=larghezzaX;
		return this;
	}
	public SuperTesto carattere(double carattere)
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
	public void aggiorna()
	{
		Paint paint=new Paint();
		paint.setTextAlign(textAlign);
		if(typeface!=null)paint.setTypeface(Typeface.create(typeface,type));
		String[]finale=new String[0];
		String aggiunta="";
		Scanner s=new Scanner(testo+" ");
		paint.setTextSize((float)(tall()*unitaY()*carattere));
		paint.setTextScaleX((float)(unitaX()/unitaY()*larghezzaX));
		while(s.hasNext())
		{
			float somma=paint.measureText(aggiunta);
			while(s.hasNext())
			{
				String nuovo=s.next()+" ";
				somma+=paint.measureText(nuovo);
				if(somma>=large()*unitaX()||!s.hasNext())
				{
					boolean esiste=false;
					int lunghezza=0;
					for(int b=0;b<aggiunta.length();b++)if(aggiunta.charAt(b)=="&".charAt(0))
						{
							esiste=true;
							lunghezza=b;
							char[]lista=new char[lunghezza];
							for(int c=0;c<lista.length;c++)lista[c]=aggiunta.charAt(c);
							String riga=new String(lista);
							char[]avanzi=new char[aggiunta.length()-riga.length()];
							for(int c=1;c<avanzi.length;c++)avanzi[c]=aggiunta.charAt(riga.length()+c);
							String altro=new String(avanzi)+nuovo;
							nuovo="";
							finale=Lista.aggiungi(finale,riga);
							aggiunta=altro;
							b=0;
						}
					if(esiste)break;
					else
					{
						finale=Lista.aggiungi(finale,aggiunta);
						aggiunta=nuovo;
						break;
					}
				}
				else aggiunta+=nuovo;
			}
		}
		finale=Lista.aggiungi(finale,aggiunta);
		s.close();
		this.finale=finale;
		schermo().runOnUiThread(new Runnable()
		{
			public void run()
			{
				invalidate();
			}
		});
	}
}
