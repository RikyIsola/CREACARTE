package com.island.creacarte;
import android.graphics.*;
import java.io.*;
import android.os.*;
import java.util.*;
import com.island.*;
import android.widget.*;
public class Generatore
{
	static void generaFoglioA4(MainActivity main,String[]file)
	{
		Bitmap b=Bitmap.createBitmap(2100,2970,Bitmap.Config.ARGB_8888);
		Canvas c=new Canvas(b);
		c.drawColor(Color.WHITE);
		int unitax=c.getWidth()/210;
		int unitay=c.getHeight()/297;
		int xbase=unitax*16;
		int ybase=unitay*19;
		for(int a=0;a<file.length;a++)
		{
			String[]f=Memoria.file(file[a]+"DESCRIZIONI/");
			String[]lista=new String[f.length];
			for(int x=0;x<lista.length;x++)lista[x]=Memoria.leggi(file[a]+"DESCRIZIONI/"+f[x]);
			String titolo=Memoria.leggi(file[a]+"TITOLO");
			Bitmap bit=Editor.disegna(Integer.valueOf(Memoria.leggi(file[a]+"RARITA")),titolo,Float.valueOf(Memoria.leggi(file[a]+"DIMENSIONI TITOLO")),Integer.valueOf(Memoria.leggi(file[a]+"LIVELLO")),BitmapFactory.decodeFile(file[a]+"IMMAGINE"),lista,Float.valueOf(Memoria.leggi(file[a]+"DIMENSIONI DESCRIZIONE")),Integer.valueOf(Memoria.leggi(file[a]+"ATTACCO")),Integer.valueOf(Memoria.leggi(file[a]+"DIFESA")),Boolean.valueOf(Memoria.leggi(file[a]+"MAGIA")));
			File fil=new File(main.cartella+"CARTE/");
			fil.mkdirs();
			fil=new File(main.cartella+"CARTE/"+titolo+".jpg");
			if(fil.exists())fil.delete();
			try
			{
				fil.createNewFile();
				FileOutputStream output=new FileOutputStream(fil);
				bit.compress(Bitmap.CompressFormat.JPEG,100,output);
				output.flush();
				output.close();
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
			c.drawBitmap(bit,null,new Rect(xbase+unitax*60*(a%3),ybase+unitay*87*(a/3),xbase+unitax*60*(a%3)+unitax*59,ybase+unitay*87*(a/3)+unitay*86),new Paint());
		}
		File f=new File(main.cartella+"FOGLIO.jpg");
		if(f.exists())f.delete();
		try
		{
			f.createNewFile();
			FileOutputStream output=new FileOutputStream(f);
			b.compress(Bitmap.CompressFormat.JPEG,100,output);
			output.flush();
			output.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	static void generaRetro(MainActivity main)
	{
		Bitmap b=Bitmap.createBitmap(2100,2970,Bitmap.Config.ARGB_8888);
		Canvas c=new Canvas(b);
		c.drawColor(Color.WHITE);
		int unitax=c.getWidth()/210;
		int unitay=c.getHeight()/297;
		int xbase=unitax*15;
		int ybase=unitay*19;
		Bitmap img=BitmapFactory.decodeResource(main.getResources(),R.drawable.retro);
		for(int a=0;a<9;a++)
		{
			c.drawBitmap(img,null,new Rect(xbase+unitax*60*(a%3),ybase+unitay*87*(a/3),xbase+unitax*60*(a%3)+unitax*59,ybase+unitay*87*(a/3)+unitay*86),new Paint());
		}
		File f=new File(main.cartella+"RETRO.jpg");
		if(f.exists())f.delete();
		try
		{
			f.createNewFile();
			FileOutputStream output=new FileOutputStream(f);
			b.compress(Bitmap.CompressFormat.JPEG,100,output);
			output.flush();
			output.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	static void generaFile(MainActivity main,String nome)
	{
		String s=nuovaDir(main.cartella);
		Memoria.salva(s+"RARITA","0");
		Memoria.salva(s+"TITOLO",nome);
		Memoria.salva(s+"LIVELLO","0");
		Memoria.salva(s+"DIMENSIONI TITOLO","1");
		Memoria.salva(s+"ATTACCO","0");
		Memoria.salva(s+"DIFESA","0");
		Memoria.salva(s+"MAGIA",String.valueOf(false));
		Memoria.salva(s+"DIMENSIONI DESCRIZIONE","1");
		Bitmap b=Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888);
		Memoria.salva(s+"IMMAGINE","");
		try
		{
			FileOutputStream output=new FileOutputStream(new File(s+"IMMAGINE"));
			b.compress(Bitmap.CompressFormat.PNG,100,output);
			output.flush();
			output.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		b.recycle();
		new Editor(main,s);
	}
	static String nuovaDir(String main)
	{
		String f;
		boolean ok;
		do
		{
			f=main+String.valueOf(new Random().nextInt());
			ok=true;
			String[]file=Memoria.file(main);
			for(String s:file)if(s==f)ok=false;
		}while(!ok);
		f+="/";
		return f;
	}
	static String nuovoFile(String main)
	{
		String f;
		String ritorno;
		boolean ok;
		do
		{
			ritorno=String.valueOf(new Random().nextInt());
			f=main+ritorno;
			ok=true;
			String[]file=Memoria.file(main);
			for(String s:file)if(s==f)ok=false;
		}while(!ok);
		f+="/";
		return ritorno;
	}
}
