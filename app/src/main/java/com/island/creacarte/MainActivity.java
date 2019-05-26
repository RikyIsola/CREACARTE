package com.island.creacarte;
import android.graphics.*;
import android.os.*;
import android.view.*;
import com.island.*;
import java.io.*;
import android.widget.*;
public class MainActivity extends Schermo
{
	String cartella=Environment.getExternalStorageDirectory().getPath()+"/CREA CARTE/";
	String[]file;
	private Gruppo g;
	private boolean[]ok;
	private int conto;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		g=new Gruppo(this,10,10);
		inizializza();
		if(!Memoria.esiste(cartella+"RETRO.jpg"))Generatore.generaRetro(this);
    }
	public void sempreGrafico()
	{
		g.sempre(0.005);
	}
	private View.OnClickListener apri(final int a)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				new Editor(schermo(),cartella+file[a]+"/");
			}
		};
	}
	private View.OnClickListener elimina(final int a)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				Memoria.cancella(cartella+file[a]+"/");
				reinizializza();
			}
		};
	}
	private View.OnClickListener rinomina(final String nome,final int a)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,nome)
				{
					public void conferma(String testo)
					{
						if(testo.equals(""))testo=String.valueOf(System.currentTimeMillis());
						Memoria.salva(cartella+file[a]+"/TITOLO",testo);
						reinizializza();
					}
				};
			}
		};
	}
	private View.OnClickListener seleziona(final Bottone b,final int a)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				 boolean bol=ok[a];
				 if(!bol)
				 {
					 if(conto<9)
					 {
						 ok[a]=true;
						 b.colore(Color.DKGRAY,Color.LTGRAY);
						 conto++;
					 }
				 }
				 else
				 {
					 ok[a]=false;
					 conto--;
					 b.colore(Color.LTGRAY,Color.DKGRAY);
				 }
			}
		};
	}
	private View.OnClickListener nuovo=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			new Tastiera(schermo(),Color.GREEN,Color.BLACK,Color.LTGRAY,Color.DKGRAY,"")
			{
				public void conferma(String testo)
				{
					if(testo.equals(""))testo=String.valueOf(System.currentTimeMillis());
					Generatore.generaFile(MainActivity.this,testo);
					reinizializza();
				}
			};
		}
	};
	private View.OnClickListener stampa=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			String[]finale=new String[conto];
			int index=0;
			for(int a=0;a<ok.length;a++)if(ok[a])
			{
				finale[index]=cartella+file[a]+"/";
				index++;
			}
			Generatore.generaFoglioA4(MainActivity.this,finale);
		}
	};
	public void reinizializza()
	{
		g.removeAllViews();
		inizializza();
	}
	private void inizializza()
	{
		file=Memoria.file(cartella);
		ok=new boolean[file.length];
		conto=0;
		for(int a=0;a<file.length;a++)if(new File(cartella+file[a]).isDirectory()&&!file[a].equals("CARTE"))
		{
			String nome=Memoria.leggi(cartella+file[a]+"/TITOLO");
			int colore=Editor.colore(Integer.valueOf(Memoria.leggi(cartella+file[a]+"/RARITA")));
			new Bottone(g,1,a+1,5,a+2,colore,Color.DKGRAY).scrivi(nome).carattere(Double.valueOf(Memoria.leggi(cartella+file[a]+"/DIMENSIONI TITOLO"))*0.9).setOnClickListener(apri(a));
			new Bottone(g,5,a+1,6,a+2).scrivi("C").setOnClickListener(copia(cartella+file[a]+"/",Generatore.nuovaDir(cartella)));
			Bottone b=new Bottone(g,6,a+1,7,a+2);
			b.scrivi("S").setOnClickListener(seleziona(b,a));
			new Bottone(g,7,a+1,8,a+2).scrivi("N").setOnClickListener(rinomina(nome,a));
			new Bottone(g,8,a+1,9,a+2).scrivi("X").setOnClickListener(elimina(a));
		}
		else
		{
			file=Lista.<String>rimuovi(file,a);
			a--;
		}
		new Oggetto(g,0,file.length+1,10,file.length+2);
		new Bottone(g,1,0,9,1).scrivi("NUOVO").antiTrans(true,true).setOnClickListener(nuovo);
		new Bottone(g,1,9,9,10).scrivi("STAMPA").antiTrans(true,true).setOnClickListener(stampa);
		g.aggiorna();
	}
	private View.OnClickListener copia(final String base,final String arrivo)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				String[]file=Memoria.file(base);
				for(int a=0;a<file.length;a++)
				{
					File f=new File(base+file[a]);
					if(f.isDirectory())
					{
						copia(base+file[a]+"/",arrivo+file[a]+"/").onClick(null);
					}
					else
					{
						try
						{
							FileInputStream input=new FileInputStream(base+file[a]);
							File fil=new File(arrivo+file[a]);
							new File(arrivo).mkdirs();
							fil.createNewFile();
							FileOutputStream output=new FileOutputStream(fil);
							int letto;
							while((letto=input.read())!=-1)output.write(letto);
							output.flush();
							output.close();
							input.close();
						}
						catch(IOException e)
						{
							System.out.println(e);
						}
					}
				}
				if(p1!=null)reinizializza();
			}
		};
	}
}
