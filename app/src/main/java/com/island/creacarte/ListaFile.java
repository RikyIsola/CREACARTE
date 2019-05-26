package com.island.creacarte;
import com.island.*;
import android.os.*;
import java.io.*;
import android.view.*;
public abstract class ListaFile extends Finestra
{
	private String cartella=Environment.getExternalStorageDirectory().getPath()+"/";
	private Gruppo g;
	ListaFile(Schermo schermo)
	{
		super(schermo);
		g=new Gruppo(this,10,10);
		inizializza();
	}
	private void inizializza()
	{
		String[]file=Memoria.file(cartella);
		for(int a=0;a<file.length;a++)
		{
			String aggiunta="";
			boolean dir=false;
			if(new File(cartella+file[a]).isDirectory())
			{
				aggiunta="/";
				dir=true;
			}
			new Bottone(g,1,a,9,a+1).scrivi(file[a]+aggiunta).setOnClickListener(apri(file[a],dir));
		}
		g.aggiorna();
	}
	private void reinizializza()
	{
		g.removeAllViews();
		inizializza();
	}
	abstract void fine(String file)
	private View.OnClickListener apri(final String file,final boolean directory)
	{return new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			if(directory)
			{
				cartella+=file+"/";
				reinizializza();
			}
			else
			{
				fine(cartella+file);
				cancel();
			}
		}
	};
	}
	public void sempreGrafico()
	{
		g.sempre(0.01);
	}
}
