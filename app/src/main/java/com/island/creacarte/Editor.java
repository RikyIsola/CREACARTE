package com.island.creacarte;
import com.island.*;
import android.graphics.*;
import android.view.*;
import java.io.*;
import android.widget.*;
public class Editor extends Finestra
{
	private int rarita;
	private Oggetto immagine;
	private String cartella;
	private String titolo,livello,dimensioniTitolo,difesa,attacco,dimensioneDescrizione;
	private Bitmap img;
	private Gruppo g1;
	private boolean tipomagia;
	Editor(Schermo schermo,final String cartella)
	{
		super(schermo);
		this.cartella=cartella;
		rarita=Integer.valueOf(Memoria.leggi(cartella+"RARITA"));
		Gruppo g=new Gruppo(this,10,10);
		immagine=new Oggetto(g,5,0,10,10)
		{
			public void onDraw(Canvas c)
			{
				String[]file=Memoria.file(cartella+"DESCRIZIONI/");
				String[]lista=new String[file.length];
				for(int a=0;a<lista.length;a++)lista[a]=Memoria.leggi(cartella+"DESCRIZIONI/"+file[a]);
				c.drawBitmap(disegna(rarita,titolo,Float.valueOf(dimensioniTitolo),Integer.valueOf(livello),img,lista,Float.valueOf(dimensioneDescrizione),Integer.valueOf(attacco),Integer.valueOf(difesa),tipomagia),null,new Rect(0,0,c.getWidth(),c.getHeight()),new Paint());
			}
		};
		new Bottone(g,0,0,1,1).scrivi("C").setOnClickListener(rarita(0));
		new Bottone(g,1,0,2,1).scrivi("R").setOnClickListener(rarita(1));
		new Bottone(g,2,0,3,1).scrivi("E").setOnClickListener(rarita(2));
		new Bottone(g,3,0,4,1).scrivi("L").setOnClickListener(rarita(3));
		new Bottone(g,4,0,5,1).scrivi("M").setOnClickListener(magia);
		new Testo(g,0,1,5,2,"TITOLO").setOnClickListener(tit);
		livello=Memoria.leggi(cartella+"LIVELLO");
		titolo=Memoria.leggi(cartella+"TITOLO");
		dimensioniTitolo=Memoria.leggi(cartella+"DIMENSIONI TITOLO");
		dimensioneDescrizione=Memoria.leggi(cartella+"DIMENSIONI DESCRIZIONE");
		attacco=Memoria.leggi(cartella+"ATTACCO");
		difesa=Memoria.leggi(cartella+"DIFESA");
		tipomagia=Boolean.valueOf(Memoria.leggi(cartella+"MAGIA"));
		img=BitmapFactory.decodeFile(cartella+"IMMAGINE");
		new Testo(g,0,3,5,4,"LIVELLO").setOnClickListener(cfonttitolo);
		new Testo(g,0,2,5,3,"DIMENSIONI").setOnClickListener(cdimensionititolo);
		new Testo(g,0,4,5,5,"ATTACCO").setOnClickListener(cattacco);
		new Testo(g,0,5,5,6,"DIFESA").setOnClickListener(cdifesa);
		new Testo(g,0,6,5,7,"IMMAGINE").setOnClickListener(cimmagine);
		new Testo(g,0,9,5,10,"DIMENSIONI").setOnClickListener(cdimensionedescrizione);
		g1=new Gruppo(g,0,7,5,9,5,5);
		inizializza();
		g.aggiorna();
	}
	private void inizializza()
	{
		String[]lista=Memoria.file(cartella+"DESCRIZIONI/");
		new Bottone(g1,1,0,4,1).scrivi("NUOVO").setOnClickListener(nuovo);
		for(int a=0;a<lista.length;a++)
		{
			new Bottone(g1,1,a+1,3,a+2).scrivi(""+a).setOnClickListener(modifica(lista[a]));
			new Bottone(g1,3,a+1,4,a+2).scrivi("C").setOnClickListener(copia(lista[a]));
			new Bottone(g1,4,a+1,5,a+2).scrivi("X").setOnClickListener(cancella(lista[a]));
		}
		g1.aggiorna();
	}
	private void reinizializza()
	{
		g1.removeAllViews();
		inizializza();
	}
	static int colore(int rarita)
	{
		if(rarita==0)return Color.LTGRAY;
		else if(rarita==1)return Color.rgb(255,128,0);
		else if(rarita==2)return Color.rgb(143,0,255);
		else return Color.rgb(0,150,0);
	}
	static Bitmap disegna(int rarita,String nome,float dimensioniTitolo,int livello,Bitmap immagine,String[]descrizione,float dimensioneDescrizione,int att,int dif,boolean magia)
	{
		Bitmap b=Bitmap.createBitmap(59*10,86*10,Bitmap.Config.ARGB_8888);
		Canvas c=new Canvas(b);
		int unitax=c.getWidth()/59;
		int unitay=c.getHeight()/86;
		c.drawColor(Color.WHITE);
		Paint p=new Paint();
		p.setColor(colore(rarita));
		c.drawRect(0,0,unitax*3,c.getHeight(),p);
		c.drawRect(c.getWidth()-unitax*3,0,c.getWidth(),c.getHeight(),p);
		c.drawRect(0,0,c.getWidth(),unitay*3,p);
		c.drawRect(0,c.getHeight()-unitax*3,c.getWidth(),c.getHeight(),p);
		c.drawRect(unitax*3,unitay*8,c.getWidth()-unitax*3,unitay*11,p);
		c.drawRect(unitax*3,c.getHeight()-c.getHeight()/3-unitay*3,c.getWidth()-unitax*3,c.getHeight()-c.getHeight()/3,p);
		if(!magia)
		{
			c.drawRect(c.getWidth()/2,c.getHeight()-unitay*8,c.getWidth()/2+unitax*1,c.getHeight()-unitay*3,p);
			c.drawRect(unitax*3,c.getHeight()-unitay*8,c.getWidth()-unitax*3,c.getHeight()-unitay*11,p);
		}
		c.drawRect(c.getWidth()-unitax*11,unitay*3,c.getWidth()-unitax*8,unitay*8,p);
		p.setColor(Color.BLACK);
		p.setTextSize(unitay*5*dimensioniTitolo);
		p.setTextAlign(Paint.Align.CENTER);
		c.drawText(nome,unitax*25,unitay*8,p);
		c.drawBitmap(immagine,null,new Rect(unitax*3,unitay*11,c.getWidth()-unitax*3,c.getHeight()-c.getHeight()/3-unitay*3),p);
		p.setTextSize(unitay*5);
		c.drawText(String.valueOf(livello),c.getWidth()-unitax*5,unitay*8,p);
		if(!magia)
		{
			c.drawText("ATT: "+att,unitax*17,c.getHeight()-unitay*3,p);
			c.drawText("DIF: "+dif,c.getWidth()-unitax*17,c.getHeight()-unitay*3,p);
		}
		p.setTextSize(unitay*5*dimensioneDescrizione);
		for(int a=0;a<descrizione.length;a++)c.drawText(descrizione[a],c.getWidth()/2,c.getHeight()-c.getHeight()/3+(a+1)*unitay*5*dimensioneDescrizione,p);
		return b;
	}
	private View.OnClickListener rarita(final int n)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				rarita=n;
				Memoria.salva(cartella+"RARITA",String.valueOf(n));
				immagine.invalidate();
			}
		};
	}
	private View.OnClickListener tit=new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,titolo)
				{
					public void conferma(String t)
					{
						titolo=t;
						immagine.invalidate();
						Memoria.salva(cartella+"TITOLO",t);
					}
				};
			}
		};
	private View.OnClickListener cfonttitolo=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,livello)
			{
				public void conferma(String t)
				{
					livello=t;
					Memoria.salva(cartella+"LIVELLO",t);
					immagine.invalidate();
				}
			}.versioneNumerica();
		}
	};
	private View.OnClickListener cdimensionititolo=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,dimensioniTitolo)
			{
				public void conferma(String t)
				{
					dimensioniTitolo=t;
					Memoria.salva(cartella+"DIMENSIONI TITOLO",t);
					immagine.invalidate();
				}
			}.versioneNumerica();
		}
	};
	private View.OnClickListener cattacco=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,attacco)
			{
				public void conferma(String t)
				{
					attacco=t;
					Memoria.salva(cartella+"ATTACCO",t);
					immagine.invalidate();
				}
			}.versioneNumerica();
		}
	};
	private View.OnClickListener cdifesa=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,difesa)
			{
				public void conferma(String t)
				{
					difesa=t;
					Memoria.salva(cartella+"DIFESA",t);
					immagine.invalidate();
				}
			}.versioneNumerica();
		}
	};
	private View.OnClickListener cimmagine=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			new ListaFile(schermo())
			{
				public void fine(String file)
				{
					Bitmap b=BitmapFactory.decodeFile(file);
					img=b;
					try
					{
						File f=new File(cartella+"IMMAGINE");
						if(f.exists())f.delete();
						f.createNewFile();
						FileOutputStream output=new FileOutputStream(f);
						b.compress(Bitmap.CompressFormat.PNG,100,output);
						immagine.invalidate();
						output.flush();
						output.close();
					}
					catch(IOException e)
					{
						System.out.println(e);
					}
				}
			};
		}
	};
	private View.OnClickListener nuovo=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,"")
			{
				public void conferma(String t)
				{
					Memoria.salva(cartella+"DESCRIZIONI/"+Generatore.nuovoFile(cartella+"DESCRIZIONI/"),t);
					immagine.invalidate();
					reinizializza();
				}
			};
		}
	};
	private View.OnClickListener modifica(final String file)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,Memoria.leggi(cartella+"DESCRIZIONI/"+file))
				{
					public void conferma(String t)
					{
						Memoria.salva(cartella+"DESCRIZIONI/"+file,t);
						immagine.invalidate();
					}
				};
			}
		};
	}
	private View.OnClickListener cancella(final String file)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				Memoria.cancella(cartella+"DESCRIZIONI/"+file);
				immagine.invalidate();
				reinizializza();
			}
		};
	}
	private View.OnClickListener copia(final String file)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				Memoria.salva(cartella+"DESCRIZIONI/"+Generatore.nuovoFile(cartella+"DESCRIZIONI/"),Memoria.leggi(cartella+"DESCRIZIONI/"+file));
				immagine.invalidate();
				reinizializza();
			}
		};
	}
	private View.OnClickListener cdimensionedescrizione=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,Memoria.leggi(cartella+"DIMENSIONI DESCRIZIONE"))
			{
				public void conferma(String t)
				{
					dimensioneDescrizione=t;
					Memoria.salva(cartella+"DIMENSIONI DESCRIZIONE",t);
					immagine.invalidate();
				}
			}.versioneNumerica();
		}
	};
	private View.OnClickListener magia=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			tipomagia=!tipomagia;
			Memoria.salva(cartella+"MAGIA",String.valueOf(tipomagia));
			immagine.invalidate();
		}
	};
	public void sempreGrafico()
	{
		g1.sempre(0.01);
	}
	public void onStop()
	{
		((MainActivity)schermo()).reinizializza();
		super.onStop();
	}
}
