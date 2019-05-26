package com.island;
import android.graphics.*;
import android.view.*;
import android.view.accessibility.*;
import android.widget.*;
public class Oggetto extends View
{
	public static double distanza(double x,double y,double larghezza,double altezza,double baseX,double baseY,double baseLarghezza,double baseAltezza)
	{
		return Math.sqrt(Math.pow(Oggetto.zero(Oggetto.distanzaX(x,larghezza,baseX,baseLarghezza)),2)+Math.pow(Oggetto.zero(Oggetto.distanzaY(y,altezza,baseY,baseAltezza)),2));
	}
	public static double distanzaX(double x,double larghezza,double baseX,double baseLarghezza)
	{
		return Math.abs(x+(larghezza-x)/2-(baseX+(baseLarghezza-baseX)/2))-(larghezza-x)/2-(baseLarghezza-baseX)/2;
	}
	public static double distanzaY(double y,double altezza,double baseY,double baseAltezza)
	{
		return Math.abs(y+(altezza-y)/2-(baseY+(baseAltezza-baseY)/2))-(altezza-y)/2-(baseAltezza-baseY)/2;
	}
	public static double zero(double valore)
	{
		if(valore<0)return 0;
		else return valore;
	}
	private int immagine;
	private int alpha=255;
	private int colore=Color.TRANSPARENT;
	private Schermo schermo;
	Info info;
	private double rotazione;
	public Oggetto rotazione(double rotazione)
	{
		this.rotazione=rotazione;
		schermo().runOnUiThread(new Runnable()
		{
			public void run()
			{
				invalidate();
			}
		});
		return this;
	}
	public double rotazione()
	{
		return rotazione;
	}
	public double distanza(Oggetto oggetto)
	{
		return distanza(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza());
	}
	public int alpha()
	{
		return alpha;
	}
	public Oggetto alpha(int alpha)
	{
		this.alpha=alpha;
		return this;
	}
	public double distanza(double x,double y,double larghezza,double altezza)
	{
		return distanza(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza);
	}
	public int immagine()
	{
		return immagine;
	}
	public Oggetto immagine(int immagine,double qualita)
	{
		this.immagine=immagine;
		if(!schermo.esiste(immagine,(int)(large()*unitaX()*qualita),(int)(tall()*unitaY()*qualita)))schermo.carica(this,immagine,(int)(large()*unitaX()*qualita),(int)(tall()*unitaY()*qualita));
		else schermo.runOnUiThread(new Runnable()
		{
			public void run()
			{
				invalidate();
			}
		});
		return this;
	}
	public Oggetto immagine(Bitmap immagine,int nome,double qualita)
	{
		this.immagine=nome;
		if(!schermo.esiste(nome,(int)(large()*unitaX()*qualita),(int)(tall()*unitaY()*qualita)))schermo.carica(this,immagine,nome,(int)(large()*unitaX()),(int)(tall()*unitaY()));
		else schermo.runOnUiThread(new Runnable()
				{
					public void run()
					{
						invalidate();
					}
				});
		return this;
	}
	public Oggetto immagine(int immagine)
	{
		return immagine(immagine,1);
	}
	public Oggetto immagine(Bitmap immagine,int nome)
	{
		return immagine(immagine,nome,1);
	}
	public Oggetto colore(int colore)
	{
		this.colore=colore;
		schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					invalidate();
				}
			});
		return this;
	}
	public int colore()
	{
		return colore;
	}
	public Oggetto(final Gruppo gruppo,final double x,final double y,final double larghezza,final double altezza)
	{
		super(gruppo.getContext());
		schermo=gruppo.schermo();
		info=new Info(x,y,larghezza,altezza,gruppo.unitaX(),gruppo.unitaY());
		schermo.runOnUiThread(new Runnable()
			{
				public void run()
				{
					gruppo.addView(Oggetto.this);
					setLayoutParams(info);
				}
			}
		);
	}
	public double tall()
	{
		return altezza()-y();
	}
	public double large()
	{
		return larghezza()-x();
	}
	public boolean distanzia(Oggetto oggetto,double velocita)
	{
		return distanzia(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza(),velocita);
	}
	public boolean distanzia(double x,double y,double larghezza,double altezza,double velocita)
	{
		boolean ritorno;
		if(ritorno=toccando(x,y,larghezza,altezza))
		{
			final double centroX=x+(larghezza-x)/2;
			final double centroY=y+(altezza-y)/2;
			if(x()>centroX)x(x()+velocita);
			if(larghezza()<centroX)x(x()-velocita);
			if(y()>centroY)y(y()+velocita);
			if(altezza()<centroY)y(y()-velocita);
		}
		return ritorno;
	}
	public boolean toccando(double x,double y,double larghezza,double altezza,double raggio)
	{
		if(distanza(x,y,larghezza,altezza)<=raggio)return true;
		return false; 
	}
	public boolean toccando(Oggetto oggetto,double raggio)
	{
		return toccando(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza(),raggio);
	}
	public boolean toccando(Oggetto oggetto)
	{
		return toccando(oggetto,0);
	}
	public boolean toccando(double x,double y,double larghezza,double altezza)
	{
		return toccando(x,y,larghezza,altezza,0);
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Matrix m=new Matrix();
		m.postRotate((float)rotazione,canvas.getWidth()/2,canvas.getHeight()/2);
		canvas.concat(m);
		canvas.drawColor(colore);
		if(schermo.esiste(immagine))
		{
			Bitmap b=schermo.immagine(immagine);
			if(!b.isRecycled())
			{
				try
				{
					Paint p=new Paint();
					p.setAlpha(alpha);
					canvas.drawBitmap(b,null,new Rect(0,0,canvas.getWidth(),canvas.getHeight()),p);
				}
				catch(RuntimeException e)
				{
					System.out.println(e);
				}
			}
		}
	}
	public Oggetto y(double y)
	{
		info.altezza=y+info.altezza-info.y;
		info.y=y;
		return this;
	}
	public Oggetto x(double x)
	{
		info.larghezza=x+info.larghezza-info.x;
		info.x=x;
		return this;
	}
	public Oggetto trans(double transX,double transY)
	{
		info.trans(transX,transY);
		return this;
	}
	public double transX()
	{
		return info.transX;
	}
	public double transY()
	{
		return info.transY;
	}
	public Oggetto larghezza(double larghezza)
	{
		info.larghezza=larghezza;
		return this;
	}
	public Oggetto altezza(double altezza)
	{
		info.altezza=altezza;
		return this;
	}
	public Oggetto large(double large)
	{
		info.larghezza=x()+large;
		return this;
	}
	public Oggetto tall(double tall)
	{
		info.altezza=y()+tall;
		return this;
	}
	public Oggetto antiTrans(boolean transX,boolean transY)
	{
		info.antiTrans(transX,transY);
		return this;
	}
	public double x()
	{
		return info.x;
	}
	public double y()
	{
		return info.y;
	}
	public double larghezza()
	{
		return info.larghezza;
	}
	public double altezza()
	{
		return info.altezza;
	}
	public double unitaX()
	{
		return info.unitaX;
	}
	public double unitaY()
	{
		return info.unitaY;
	}
	public Schermo schermo()
	{
		return schermo;
	}
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event)
	{
		return true;
	}
	public double largeMeta()
	{
		return large()/2;
	}
	public double tallMeta()
	{
		return tall()/2;
	}
	public double centroX()
	{
		return x()+largeMeta();
	}
	public double centroY()
	{
		return y()+tallMeta();
	}
	public void largeMeta(double largeMeta)
	{
		large(largeMeta*2);
	}
	public void tallMeta(double tallMeta)
	{
		tall(tallMeta*2);
	}
	public void centroX(double centroX)
	{
		x(centroX-largeMeta());
	}
	public void centroY(double centroY)
	{
		y(centroY-tallMeta());
	}
}
