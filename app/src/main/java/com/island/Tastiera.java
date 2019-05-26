package com.island;
import android.view.*;
import android.graphics.*;
public abstract class Tastiera extends Finestra
{
	public Tastiera(Schermo schermo,int sfondo,String testo)
	{
		this(schermo,sfondo,Color.BLACK,testo);
	}
	public Tastiera(Schermo schermo,int sfondo,int coloreTesto,String testo)
	{
		this(schermo,sfondo,coloreTesto,Color.LTGRAY,Color.DKGRAY,testo);
	}
	public Tastiera(Schermo schermo,int sfondo,int coloreTesto,int colore1,int colore2,String testo)
	{
		super(schermo);
		this.testo=testo;
		this.sfondo=sfondo;
		this.coloreTesto=coloreTesto;
		this.colore1=colore1;
		this.colore2=colore2;
		gruppo=new Gruppo(this,10,10);
		gruppo.setBackgroundColor(sfondo);
		scritta=new Testo(gruppo,0,3,10,4,testo,coloreTesto,sfondo);
		for(int a=0;a<caratteri.length;a++)new Bottone(gruppo,a%10,a/10+6,a%10+1,a/10+7,colore1,colore2).scrivi(caratteri[a],coloreTesto).setOnClickListener(scrittura);
		new Bottone(gruppo,8,9,9,10,colore1,colore2).scrivi("<",coloreTesto).setOnClickListener(scrittura);
		new Bottone(gruppo,9,9,10,10,colore1,colore2).scrivi(" ",coloreTesto).setOnClickListener(scrittura);
		gruppo.aggiorna();
	}
	private int sfondo,coloreTesto,colore1,colore2;
	private String testo;
	private Testo scritta;
	private Gruppo gruppo;
	private String[]caratteri={"1","2","3","4","5","6","7","8","9","0","Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M",".","-"};
	private View.OnClickListener scrittura=new View.OnClickListener()
	{
		public void onClick(View p1)
		{
			Bottone t=(Bottone)p1;
			if(t.testo()=="<")
			{
				if(testo.length()>0)
				{
					char[]ritorno=new char[testo.length()-1];
					for(int a=0;a<ritorno.length;a++)ritorno[a]=testo.charAt(a);
					testo=new String(ritorno);
				}
			}
			else testo+=t.testo();
			scritta.scrivi(testo);
		}
	};
	public void onBackPressed()
	{
		conferma(testo);
		super.onBackPressed();
	}
	public Tastiera versioneNumerica()
	{
		gruppo.removeAllViews();
		gruppo=new Gruppo(this,3,10);
		gruppo.setBackgroundColor(sfondo);
		scritta=new Testo(gruppo,0,3,3,4,testo,coloreTesto,sfondo);
		for(int a=0;a<10;a++)new Bottone(gruppo,a%3,a/3+6,a%3+1,a/3+7,colore1,colore2).scrivi(caratteri[a],coloreTesto).setOnClickListener(scrittura);
		new Bottone(gruppo,1,9,2,10,colore1,colore2).scrivi(".",coloreTesto).setOnClickListener(scrittura);
		new Bottone(gruppo,2,9,3,10,colore1,colore2).scrivi("<",coloreTesto).setOnClickListener(scrittura);
		gruppo.aggiorna();
		return this;
	}
	protected abstract void conferma(String testo)
}
