package com.island;
import android.graphics.*;
import android.view.*;
import android.view.accessibility.*;
public class Gruppo extends ViewGroup
{
	private double precedenteX,precedenteY,defaultX,defaultY,piuX,piuY,transX,transY,unitaX,unitaY,maxX,maxY,schermoX,schermoY,velocitaX,velocitaY,menoX,menoY;
	private boolean antiScrollX,antiScrollY,toccato,antiPiu;
	private Gruppo gruppo;
	private Schermo schermo;
	private Info info;
	private Oggetto sfondo;
	public void removeAllViews()
	{
		sfondo=null;
		super.removeAllViews();
	}
	public Gruppo immagine(int immagine,double qualita)
	{
		if(sfondo==null)sfondo=new Oggetto(this,0,0,maxX(),maxY()).antiTrans(true,true);
		sfondo.immagine(immagine,qualita);
		return this;
	}
	public Gruppo immagine(Bitmap immagine,int nome,double qualita)
	{
		if(sfondo==null)sfondo=new Oggetto(this,0,0,maxX(),maxY()).antiTrans(true,true);
		sfondo.immagine(immagine,nome,qualita);
		return this;
	}
	public Gruppo immagine(int immagine)
	{
		return immagine(immagine,1);
	}
	public Gruppo immagine(Bitmap immagine,int nome)
	{
		return immagine(immagine,nome,1);
	}
	public int immagine()
	{
		return sfondo.immagine();
	}
	public Gruppo rotazione(double rotazione)
	{
		sfondo.rotazione(rotazione);
		return this;
	}
	public double rotazione()
	{
		return sfondo.rotazione();
	}
	public Gruppo colore(int colore)
	{
		if(sfondo==null)sfondo=new Oggetto(this,0,0,maxX(),maxY()).antiTrans(true,true);
		sfondo.colore(colore);
		return this;
	}
	public int colore()
	{
		return sfondo.colore();
	}
	public Gruppo alpha(int alpha)
	{
		sfondo.alpha(alpha);
		return this;
	}
	public int alpha()
	{
		return sfondo.alpha();
	}
	public Gruppo(final Gruppo gruppo,final double x,final double y,final double larghezza,final double altezza,double caselleX,double caselleY)
	{
		super(gruppo.getContext());
		this.gruppo=gruppo;
		schermoX=gruppo.unitaX*(larghezza-x);
		schermoY=gruppo.unitaY*(altezza-y);
		unitaX=gruppo.unitaX*(larghezza-x)/caselleX;
		unitaY=gruppo.unitaY*(altezza-y)/caselleY;
		maxX=caselleX;
		maxY=caselleY;
		schermo=gruppo.schermo;
		info=new Info(x,y,larghezza,altezza,gruppo.unitaX(),gruppo.unitaY());
		schermo.runOnUiThread(new Runnable()
			{
				public void run()
				{
					setLayoutParams(info);
					gruppo.addView(Gruppo.this);
				}
			}
		);
	}
	public Gruppo(final Schermo schermo,double caselleX,double caselleY)
	{
		super(schermo);
		int[]dimensioni=schermo.dimensioni();
		schermoX=dimensioni[0];
		schermoY=dimensioni[1];
		unitaX=dimensioni[0]/caselleX;
		unitaY=dimensioni[1]/caselleY;
		maxX=caselleX;
		maxY=caselleY;
		this.schermo=schermo;
		info=new Info(0,0,1,1,schermoX,schermoY);
		schermo.runOnUiThread(new Runnable()
			{
				public void run()
				{
					setLayoutParams(info);
					schermo.setContentView(Gruppo.this);
				}
			}
		);
	}
	public Gruppo(final Finestra finestra,double caselleX,double caselleY)
	{
		super(finestra.getContext());
		int[]dimensioni=finestra.dimensioni();
		schermoX=dimensioni[0];
		schermoY=dimensioni[1];
		unitaX=dimensioni[0]/caselleX;
		unitaY=dimensioni[1]/caselleY;
		maxX=caselleX;
		maxY=caselleY;
		schermo=finestra.schermo();
		info=new Info(0,0,1,1,schermoX,schermoY);
		schermo.runOnUiThread(new Runnable()
			{
				public void run()
				{
					setLayoutParams(info);
					finestra.setContentView(Gruppo.this);
				}
			}
		);
	}
	protected void onLayout(boolean change,int l,int t,int r,int d){}
	public Gruppo aggiorna()
	{
		if(gruppo!=null)
		{
			schermoX=gruppo.unitaX*(larghezza()-x());
			schermoY=gruppo.unitaY*(altezza()-y());
		}
		unitaX=schermoX/maxX;
		unitaY=schermoY/maxY;
		if(!antiPiu)
		{
			piuX=defaultX-maxX;
			piuY=defaultY-maxY;
		}
		for(int a=0;a<getChildCount();a++)if(getChildAt(a).getVisibility()==0)
			{
				final View oggetto=getChildAt(a);
				final Info info=(Info)oggetto.getLayoutParams();
				final double x,y,altezza,larghezza;
				double x2,y2;
				if(!info.antiTransX)x2=-transX+info.transX*unitaX;
				else x2=0;
				if(!info.antiTransY)y2=-transY+info.transY*unitaY;
				else y2=0;
				larghezza=info.larghezza*unitaX+x2;
				altezza=info.altezza*unitaY+y2;
				x=info.x*unitaX+x2;
				y=info.y*unitaY+y2;
				if(larghezza>0&&altezza>0&&x<maxX*unitaX&&y<maxY*unitaY)schermo.runOnUiThread(new Runnable()
						{
							public void run()
							{
								oggetto.layout((int)x,(int)y,(int)larghezza,(int)altezza);
							}
						}
					);
				else schermo.runOnUiThread(new Runnable()
						{
							public void run()
							{
								oggetto.layout(0,0,0,0);
							}
						}
					);
				info.schermo(unitaX,unitaY);
				if(!antiPiu)
				{
					if(info.larghezza-maxX()>piuX)piuX=info.larghezza-maxX();
					if(info.altezza-maxY()>piuY)piuY=info.altezza-maxY();
				}
				if(getChildAt(a)instanceof Gruppo)((Gruppo)getChildAt(a)).aggiorna();
			}
		if(!antiScrollX)
		{
			if(piuX<menoX*unitaX&&!antiPiu)piuX=menoX*unitaX;
			if(transX>piuX*unitaX)transX=piuX*unitaX;
			if(transX<menoX*unitaX)transX=menoX*unitaX;
		}
		if(!antiScrollY)
		{
			if(piuY<menoY*unitaY&&!antiPiu)piuY=menoY*unitaY;
			if(transY>piuY*unitaY)transY=piuY*unitaY;
			if(transY<menoY*unitaY)transY=menoY*unitaY;
		}
		return this;
	}
	public Gruppo migra(final Gruppo gruppo)
	{
		if(gruppo!=null)
		{
			info=new Info(x(),y(),larghezza(),altezza(),gruppo.unitaX(),gruppo.unitaY());
			schermo.runOnUiThread(new Runnable()
				{
					public void run()
					{
						Gruppo.this.gruppo.removeView(Gruppo.this);
						setLayoutParams(info);
						gruppo.addView(Gruppo.this);
					}
				}
			);
			this.gruppo=gruppo;
			schermoX=gruppo.unitaX*(larghezza()-x());
			schermoY=gruppo.unitaY*(altezza()-y());
			unitaX=gruppo.unitaX*(larghezza()-x())/maxX;
			unitaY=gruppo.unitaY*(altezza()-y())/maxY;
		}
		return this;
	}
	public Gruppo large(double large)
	{
		info.larghezza=x()+large;
		return this;
	}
	public Gruppo tall(double tall)
	{
		info.altezza=y()+tall;
		return this;
	}
	public Gruppo max(double maxX,double maxY)
	{
		if(sfondo!=null)sfondo.larghezza(maxX).altezza(maxY);
		this.maxX=maxX;
		this.maxY=maxY;
		return this;
	}
	public double maxX()
	{
		return maxX;
	}
	public double maxY()
	{
		return maxY;
	}
	public double unitaX()
	{
		return unitaX;
	}
	public double unitaY()
	{
		return unitaY;
	}
	public double transX()
	{
		return transX/unitaX;
	}
	public double transY()
	{
		return transY/unitaY;
	}
	public Gruppo limiti(double defaultX,double defaultY)
	{
		this.defaultX=defaultX;
		this.defaultY=defaultY;
		return this;
	}
	public Gruppo antiScroll(boolean antiScrollX,boolean antiScrollY)
	{
		this.antiScrollX=antiScrollX;
		this.antiScrollY=antiScrollY;
		return this;
	}
	public Gruppo trans(double transX,double transY)
	{
		this.transX=transX*unitaX;
		this.transY=transY*unitaY;
		return this;
	}
	public Gruppo y(double y)
	{
		info.altezza=y+info.altezza-info.y;
		info.y=y;
		return this;
	}
	public Gruppo x(double x)
	{
		info.larghezza=x+info.larghezza-info.x;
		info.x=x;
		return this;
	}
	public Gruppo larghezza(double larghezza)
	{
		info.larghezza=larghezza;
		schermoX=gruppo.unitaX*(larghezza-x());
		unitaX=gruppo.unitaX*(larghezza-x())/maxX;
		aggiorna();
		return this;
	}
	public Gruppo altezza(double altezza)
	{
		info.altezza=altezza;
		schermoY=gruppo.unitaY*(altezza-y());
		unitaY=gruppo.unitaY*(altezza-y())/maxY;
		aggiorna();
		return this;
	}
	public Gruppo antiTrans(boolean transX,boolean transY)
	{
		info.antiTrans(transX,transY);
		return this;
	}
	public double tall()
	{
		return altezza()-y();
	}
	public double large()
	{
		return larghezza()-x();
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
	public boolean onTouchEvent(MotionEvent event)
	{
		onTouchEvent(this,event);
		return true;
	}
	public static void onTouchEvent(Gruppo gruppo,MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			gruppo.precedenteX=event.getX();
			gruppo.precedenteY=event.getY();
			gruppo.toccato=true;
		}
		else if(event.getAction()==MotionEvent.ACTION_MOVE)
		{
			gruppo.scrolla(event.getX(),event.getY());
		}
		else if(event.getAction()==MotionEvent.ACTION_UP)gruppo.toccato=false;
	}
	public Schermo schermo()
	{
		return schermo;
	}
	private void scrolla(double x,double y)
	{
		velocitaX=precedenteX-x;
		velocitaY=precedenteY-y;
		if(!antiScrollX)transX+=velocitaX;
		if(!antiScrollY)transY+=velocitaY;
		precedenteX=x;
		precedenteY=y;
		if(transX>piuX*unitaX&&!antiScrollX)transX=piuX*unitaX;
		if(transY>piuY*unitaY&&!antiScrollY)transY=piuY*unitaY;
		if(transX<menoX*unitaX&&!antiScrollX)transX=menoX*unitaX;
		if(transY<menoY*unitaY&&!antiScrollY)transY=menoY*unitaY;
		aggiorna();
	}
	public Gruppo meno(double x,double y)
	{
		menoX=x;
		menoY=y;
		return this;
	}
	public Gruppo piu(double x,double y)
	{
		piuX=x;
		piuY=y;
		return antiPiu(true);
	}
	public Gruppo antiPiu(boolean antiPiu)
	{
		this.antiPiu=antiPiu;
		return this;
	}
	public void sempre(double decelerazione)
	{
		if(!toccato)
		{
			final int piuX,piuY;
			final double molt=decelerazione;
			final double soglia=molt/2;
			if(velocitaX<-soglia*unitaX)piuX=1;
			else if(velocitaX>soglia*unitaX)piuX=-1;
			else piuX=0;
			if(velocitaY<-soglia*unitaY)piuY=1;
			else if(velocitaY>soglia*unitaY)piuY=-1;
			else piuY=0;
			if(piuX!=0||piuY!=0)scrolla(precedenteX-(velocitaX+piuX*molt*unitaX),precedenteY-(velocitaY+piuY*molt*unitaY));
		}
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
