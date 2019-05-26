package com.island;
import android.app.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.util.*;
import android.content.res.*;
public class Schermo extends Activity
{
	public Typeface typeface()
	{
		return typeface;
	}
	public int type()
	{
		return type;
	}
	public int rallentamentoMain()
	{
		return 0;
	}
	public int rallentamentoSpeed()
	{
		return 0;
	}
	public int rallentamentoSuono()
	{
		return 0;
	}
	public boolean immagini()
	{
		return true;
	}
	public boolean suoni()
	{
		return true;
	}
	public boolean debug()
	{
		return false;
	}
	public void font(Typeface typeface,int type)
	{
		this.type=type;
		this.typeface=typeface;
	}
	public int orientamento()
	{
		return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
	}
	public int tema()
	{
		return android.R.style.Theme_Black_NoTitleBar_Fullscreen;
	}
	private Handler h=new Handler();
	private boolean go;
	private Point p=new Point();
	private int frame,frameS,type;
	private Typeface typeface;
	private long tempo;
	private Processo processo;
	float volume=1;
	Processo suono;
	Suono[]suoni=new Suono[0];
	Finestra[]finestre=new Finestra[0];
	private Bitmap[]caricati=new Bitmap[0];
	private Rect[]risorse=new Rect[0];
	private View view;
	private Runtime runtime=Runtime.getRuntime();
	private int ramGiusta;
	public void sempreGrafico(){}
	public void sempre(){}
	public int[]dimensioni()
	{
		return new int[]{p.x,p.y};
	}
	public void displayNormale(Point p)
	{
		getWindowManager().getDefaultDisplay().getRealSize(p);
		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			int x=p.x;
			p.x=p.y;
			p.y=x;
		}
	}
	public void display(Point p)
	{
		displayNormale(p);
	}
	public void displayPiccolo(Point p)
	{
		displayPiccolo(p,240,320,120);
	}
	public void displayPiccolo(Point p,int x,int y,double dpi)
	{
		p.x=x;
		p.y=y;
		double rapporto=dpi()/dpi;
		p.x*=rapporto;
		p.y*=rapporto;
	}
	public void displayDenso(Point p)
	{
		p.x=240;
		p.y=320;
	}
	public void displayDensoGrande(Point p)
	{
		p.x=1600;
		p.y=2560;
	}
	public void displayGrande(Point p)
	{
		displayPiccolo(p,1600,2560,320);
	}
	public void setContentView(View view)
	{
		super.setContentView(view);
		this.view=view;
	}
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(tema());
		setRequestedOrientation(orientamento());
		display(p);
		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			int x=p.x;
			p.x=p.y;
			p.y=x;
		}
		ramGiusta=(int)(Math.sqrt(p.x*p.y)*16/Math.sqrt(240*320));
		processo=new Processo(this)
		{
			public void run()
			{
				loop();
			}
			public void sempre()
			{
				if(finestre!=null)
				{
					for(int a=0;a<rallentamentoSpeed();a++);
					for(Finestra finestra:finestre)finestra.sempre();
					Schermo.this.sempre();
				}
			}
		};
		suono=new Processo(this)
		{
			public void run()
			{
				loop();
			}
			public void sempre()
			{
				for(int a=0;a<rallentamentoSuono();a++);
			}
		};
	}
	protected void onDestroy()
	{
		super.onDestroy();
		for(Suono suono:suoni)suono.cancella();
		h=null;
		p=null;
		typeface=null;
		processo.chiudi();
		processo=null;
		suono.chiudi();
		suono=null;
		suoni=null;
		for(Finestra finestra:finestre)finestra.dismiss();
		finestre=null;
		for(Bitmap bitmap:caricati)bitmap.recycle();
		caricati=null;
		risorse=null;
		if(view!=null)((ViewManager)view.getParent()).removeView(view);
		view=null;
		runtime=null;
		System.gc();
	}
	protected void onPause()
	{
		super.onStop();
		go=false;
		processo.pausa();
		suono.pausa();
		for(Finestra finestra:finestre)finestra.pausa();
		for(Suono suono:suoni)suono.pausaSchermo();
	}
	protected void onResume()
	{
		super.onStart();
		go=true;
		processo.riprendi();
		suono.riprendi();
		h.postDelayed(new Runnable()
			{
				public void run()
				{
					if(go)
					{
						h.postDelayed(this,50);
						if(System.currentTimeMillis()>=tempo)
						{
							tempo=System.currentTimeMillis()+1000;
							frameS=frame;
							frame=0;
						}
						frame++;
						for(int a=0;a<rallentamentoMain();a++);
						sempreGrafico();
						for(Finestra finestra:finestre)finestra.sempreGrafico();
					}
				}
			},20);
		for(Finestra finestra:finestre)finestra.riprendi();
		for(Suono suono:suoni)suono.riprendiSchermo();
	}
	public Schermo schermo()
	{
		return this;
	}
	public Schermo toast(final String testo)
	{
		if(debug())runOnUiThread(new Runnable()
		{
			public void run()
			{
				Toast.makeText(Schermo.this,testo,0).show();
			}
		});
		return this;
	}
	public int frame()
	{
		return frameS;
	}
	public int velocitaSuono()
	{
		return suono.velocita();
	}
	public int velocita()
	{
		return processo.velocita();
	}
	protected void onSaveInstanceState(Bundle b)
	{
		super.onSaveInstanceState(b);
		for(Finestra finestra:finestre)finestra.salva(b);
	}
	public Schermo carica(final int risorsa,int x,int y)
	{
		return carica((Oggetto)null,risorsa,x,y);
	}
	public Schermo carica(final Bitmap immagine,final int risorsa,int x,int y)
	{
		return carica((Oggetto)null,immagine,risorsa,x,y);
	}
	public Schermo carica(final Oggetto oggetto,final int risorsa,final int x,final int y)
	{
		while(processo.handler()==null);
		if(processo!=null&&immagini()&&!esiste(risorsa,x,y))processo.handler().post(new Runnable()
			{
				public void run()
				{
					BitmapFactory.Options opt=new BitmapFactory.Options();
					opt.inMutable=true;
					opt.inJustDecodeBounds=true;
					BitmapFactory.decodeResource(getResources(),risorsa,opt);
					double scalatura=0.5;
					while(opt.outWidth/(scalatura*2)>x&&opt.outHeight/(scalatura*2)>y)scalatura*=2;
					opt.inSampleSize=(int)(scalatura*2);
					opt.inJustDecodeBounds=false;
					try
					{
						Bitmap b=BitmapFactory.decodeResource(getResources(),risorsa,opt);
						carica(oggetto,Bitmap.createScaledBitmap(b,x,y,true),risorsa,x,y);
						b.recycle();
					}catch(OutOfMemoryError e)
					{
						System.out.println(e);
					}
					catch(IllegalArgumentException e)
					{
						System.out.println(e);
					}
				}
			});
		return this;
	}
	public Schermo carica(final Oggetto oggetto,final Bitmap immagine,final int risorsa,final int x,final int y)
	{
		if(processo!=null&&immagini())processo.handler().post(new Runnable()
			{
				public void run()
				{
					if(risorse!=null)
					{
						boolean ok=true;
						for(int a=0;a<risorse.length;a++)
						{
							final Rect p=risorse[a];
							if(p.right==risorsa)
							{
								if(x>p.left||y>p.top)
								{
									Bitmap b=caricati[a];
									caricati[a]=immagine;
									b.recycle();
									risorse[a].left=x;
									risorse[a].top=y;
								}
								ok=false;
							}
						}
						if(ok)
						{
							caricati=Lista.aggiungi(caricati,immagine);
							risorse=Lista.aggiungi(risorse,new Rect(x,y,risorsa,0));
						}
						if(oggetto!=null)runOnUiThread(new Runnable()
						{
							public void run()
							{
								oggetto.invalidate();
							}
						});
					}
				}
			});
		return this;
	}
	public Schermo cancella(final int risorsa)
	{
		processo.handler().post(new Runnable()
			{
				public void run()
				{
					for(int a=0;a<risorse.length;a++)if(risorse[a].right==risorsa)
					{
						caricati[a].recycle();
						risorse=Lista.<Rect>rimuovi(risorse,a);
						caricati=Lista.<Bitmap>rimuovi(caricati,a);
						break;
					}
					System.gc();
				}
			});
		return this;
	}
	public boolean esiste(int risorsa,int x,int y)
	{
		for(int a=0;a<risorse.length;a++)
		{
			Rect p=risorse[a];
			if(p.right==risorsa&&p.left>=x&&p.top>=y&&!caricati[a].isRecycled())return true;
		}
		return false;
	}
	public boolean esiste(int risorsa)
	{
		for(Rect p:risorse)if(p.right==risorsa)return true;
		return false;
	}
	public Bitmap immagine(int nome)
	{
		for(int a=0;a<risorse.length;a++)if(risorse[a].right==nome&&!caricati[a].isRecycled())return caricati[a];
		return null;
	}
	public Schermo volume(float volume)
	{
		this.volume=volume;
		for(Suono suono:suoni)suono.volume();
		return this;
	}
	public long ram()
	{
		return (runtime.totalMemory()-runtime.freeMemory())/1048576;
	}
	public int ramGiusta()
	{
		return ramGiusta;
	}
	public int dpi()
	{
		return (int)getResources().getDisplayMetrics().xdpi;
	}
	public boolean libero()
	{
		if(finestre!=null)return finestre.length==0;
		else return true;
	}
	public Finestra finestra()
	{
		return finestre[finestre.length-1];
	}
	public Handler grafica()
	{
		return h;
	}
	public Handler thread()
	{
		return processo.handler();
	}
}
