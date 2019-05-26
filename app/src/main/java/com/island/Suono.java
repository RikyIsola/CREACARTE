package com.island;
import android.content.res.*;
import android.media.*;
import java.io.*;
import android.widget.*;
public class Suono
{
	public Suono(final Schermo schermo,final int suono)
	{
		if(schermo.suoni())schermo.suono.handler().post(new Runnable()
		{
			public void run()
			{
				try
				{
					media=new MediaPlayer();
					AssetFileDescriptor afd=schermo.getResources().openRawResourceFd(suono);
					media.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
					afd.close();
					media.prepare();
					media.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
						{
							public void onCompletion(MediaPlayer p1)
							{
								rilascia();
							}
						}
					);
					schermo.suoni=Lista.aggiungi(schermo.suoni,Suono.this);
				}
				catch(IOException e)
				{
					System.out.println(e);
				}
			}
		});
		this.schermo=schermo;
	}
	private boolean suonando,pausa,pausaSchermo;
	private MediaPlayer media;
	private Schermo schermo;
	private float volume;
	public Suono pausa()
	{
		if(suonando)
		{
			suonando=false;
			pausa=true;
			controllo();
		}
		return this;
	}
	public Suono riprendi()
	{
		if(pausa)start();
		return this;
	}
	public Suono stop()
	{
		if(suonando)
		{
			suonando=false;
			pausa=false;
			controllo();
		}
		return this;
	}
	public Suono start()
	{
		if(!suonando)
		{
			suonando=true;
			pausa=false;
			controllo();
		}
		return this;
	}
	public Schermo schermo()
	{
		return schermo;
	}
	private Suono controllo()
	{
		if(suonando&&!pausaSchermo)schermo.suono.handler().post(new Runnable()
		{
			public void run()
			{
				if(media!=null)media.start();
			}
		});
		else if(pausaSchermo||pausa)schermo.suono.handler().post(new Runnable()
				{
					public void run()
					{
						try
						{
							if(media!=null)media.pause();
						}catch(IllegalStateException e)
						{
							System.out.println(e);
						}
					}
				});
		else schermo.suono.handler().post(new Runnable()
				{
					public void run()
					{
						if(media!=null)media.stop();
					}
				});
		return this;
	}
	public Suono volume(float volume)
	{
		this.volume=volume;
		return volume();
	}
	Suono volume()
	{
		final float finale=schermo.volume*volume;
		schermo.suono.handler().post(new Runnable()
			{
				public void run()
				{
					if(media!=null)media.setVolume(finale,finale);
				}
			});
		return this;
	}
	Suono pausaSchermo()
	{
		pausaSchermo=true;
		controllo();
		return this;
	}
	Suono riprendiSchermo()
	{
		pausaSchermo=false;
		controllo();
		return this;
	}
	Suono cancella()
	{
		if(schermo.suono!=null)schermo.suono.handler().post(new Runnable()
			{
				public void run()
				{
					if(media!=null)media.release();
				}
			});
		return null;
	}
	public Suono rilascia()
	{
		if(schermo.suono!=null)schermo.suono.handler().post(new Runnable()
			{
				public void run()
				{
					try
					{
						if(schermo.suoni!=null&&media!=null)schermo.suoni=Lista.rimuovi(schermo.suoni,Suono.this);
					}catch(ArrayIndexOutOfBoundsException e){System.out.println(e);}
				}
			});
		return cancella();
	}
	public Suono infinito(final boolean infinito)
	{
		schermo.suono.handler().post(new Runnable()
			{
				public void run()
				{
					if(media!=null)media.setLooping(infinito);
				}
			});
		return this;
	}
}
