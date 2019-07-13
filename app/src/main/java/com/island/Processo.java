package com.island;
import android.os.*;
public abstract class Processo extends Thread
{
	private Handler handler;
	private Looper looper;
	private Schermo schermo;
	private int frame,frameS;
	private long precedente;
	private boolean go;
	public Processo()
	{
		start();
	}
	public Processo(Schermo schermo)
	{
		this();
		this.schermo=schermo;
	}
	public Schermo schermo()
	{
		return schermo;
	}
	public abstract void run();
	public Processo loop()
	{
		Looper.prepare();
		handler=new Handler();
		looper=Looper.myLooper();
		Looper.loop();
		return this;
	}
	public Processo pausa()
	{
		go=false;
		return this;
	}
	public Processo riprendi()
	{
		go=true;
		while(handler==null);
		handler.postDelayed(r,50);
		return this;
	}
	public Handler handler()
	{
		return handler;
	}
	public void chiudi()
	{
		pausa();
		while(looper==null);
		looper.quit();
	}
	public void sempre(){}
	public int velocita()
	{
		return frameS*5;
	}
	private Runnable r=new Runnable()
	{
		public void run()
		{
			if(go)
			{
				handler.postDelayed(this,50);
				if(System.currentTimeMillis()>=precedente)
				{
					precedente=System.currentTimeMillis()+1000;
					frameS=frame;
					frame=0;
				}
				frame++;
				sempre();
			}
		}
	};
}
