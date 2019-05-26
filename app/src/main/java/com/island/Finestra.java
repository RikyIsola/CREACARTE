package com.island;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
public abstract class Finestra extends Dialog
{
	public Finestra(Schermo schermo,int larghezza,int altezza)
	{
		this(schermo,larghezza,altezza,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	}
	public Finestra(Schermo schermo,int larghezza,int altezza,int tema)
	{
		super(schermo,tema);
		dimensioni=new int[]{larghezza,altezza};
		this.schermo=schermo;
		for(Finestra f:schermo.finestre)if(f.getClass().getName().equals(getClass().getName()))return;
		getWindow().setLayout(larghezza,altezza);
		show();
		schermo.finestre=Lista.aggiungi(schermo.finestre,this);
	}
	public Finestra(Schermo schermo)
	{
		this(schermo,schermo.dimensioni()[0],schermo.dimensioni()[1]);
	}
	public Finestra(Schermo schermo,int tema)
	{
		this(schermo,schermo.dimensioni()[0],schermo.dimensioni()[1],tema);
	}
	private int[]dimensioni;
	private Schermo schermo;
	private View view;
	public void setContentView(View view)
	{
		super.setContentView(view);
		this.view=view;
	}
	public int[]dimensioni()
	{
		return dimensioni;
	}
	public Schermo schermo()
	{
		return schermo;
	}
	public void salva(Bundle b){}
	public void leggi(Bundle b){}
	public void sempre(){}
	public void sempreGrafico(){}
	public void onStop()
	{
		schermo.finestre=Lista.rimuovi(schermo.finestre,this);
		dimensioni=null;
		schermo=null;
		if(view!=null)((ViewManager)view.getParent()).removeView(view);
		view=null;
		System.gc();
	}
	public void pausa(){}
	public void riprendi(){}
	public boolean debug()
	{
		return schermo.debug();
	}
	public long ram()
	{
		return schermo.ram();
	}
	public int velocita()
	{
		return schermo.velocita();
	}
	public int frame()
	{
		return schermo.frame();
	}
	public int velocitaSuono()
	{
		return schermo.velocitaSuono();
	}
	public int ramGiusta()
	{
		return schermo.ramGiusta();
	}
	public boolean libero()
	{
		for(int a=0;a<schermo.finestre.length;a++)if(schermo.finestre[a]==this&&a==schermo.finestre.length-1)return true;
		return false;
	}
	public Handler grafica()
	{
		return schermo.grafica();
	}
	public Handler thread()
	{
		return schermo.thread();
	}
	public Finestra toast(final String testo)
	{
		schermo().toast(testo);
		return this;
	}
}
