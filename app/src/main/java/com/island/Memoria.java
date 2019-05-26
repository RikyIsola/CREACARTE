package com.island;
import android.content.*;
import java.io.*;
import java.util.*;
public class Memoria
{
	public static void salva(Schermo schermo,String dove,int cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putInt(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static void salva(Schermo schermo,String dove,String cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putString(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static void salva(Schermo schermo,String dove,boolean cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putBoolean(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static void salva(Schermo schermo,String dove,float cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putFloat(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static void salva(Schermo schermo,String dove,long cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putLong(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static int leggi(Schermo schermo,String dove,int altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getInt(dove,altrimenti);
	}
	public static boolean leggi(Schermo schermo,String dove,boolean altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getBoolean(dove,altrimenti);
	}
	public static float leggi(Schermo schermo,String dove,float altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getFloat(dove,altrimenti);
	}
	public static long leggi(Schermo schermo,String dove,long altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getLong(dove,altrimenti);
	}
	public static String leggi(Schermo schermo,String dove,String altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getString(dove,altrimenti);
	}
	public static void salva(String dove,String cosa)
	{
		try
		{
			File file=new File(dove);
			File cartella=file.getParentFile();
			if(file.exists())file.delete();
			cartella.mkdirs();
			file.createNewFile();
			FileOutputStream output=new FileOutputStream(file);
			PrintStream print=new PrintStream(output);
			print.print(cosa+System.lineSeparator());
			print.flush();
			print.close();
			output.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public static boolean esiste(String dove)
	{
		return new File(dove).exists();
	}
	public static String leggi(String dove)
	{
		String ritorno="";
		try
		{
			FileInputStream input=new FileInputStream(new File(dove));
			Scanner scanner=new Scanner(input);
			ritorno=scanner.nextLine();
			scanner.close();
			input.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		return ritorno;
	}
	public static void cancella(String dove)
	{
		File file=new File(dove);
		if(file.isDirectory())cancellaDirectory(dove);
		else file.delete();
	}
	private static void cancellaDirectory(String dove)
	{
		String[]file=file(dove);
		if(file!=null)for(int a=0;a<file.length;a++)cancella(dove+"/"+file[a]);
		new File(dove).delete();
	}
	public static String[]file(String dove)
	{
		final String[]ritorno=new File(dove).list();
		if(ritorno==null)return new String[0];
		else return ritorno;
	}
	public static void cancella(Schermo schermo,String dove)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
	}
	public static long dimensioni(String file)
	{
		File f=new File(file);
		long ritorno=0;
		if(f.isDirectory())
		{
			String[]lista=f.list();
			if(lista!=null)for(int a=0;a<lista.length;a++)ritorno+=dimensioni(file+"/"+lista[a]);
		}
		else ritorno=f.length();
		return ritorno;
	}
	public static String nome(String file)
	{
		return new File(file).getName();
	}
	public static void copia(String dove,String arrivo)
	{
		String[]files=file(dove);
		for(String string:files)
		{
			File file=new File(dove);
			if(file.isDirectory())copia(dove+string+"/",arrivo+string+"/");
			else salva(arrivo+string,leggi(dove+string));
		}
	}
	public static void taglia(String dove,String arrivo)
	{
		copia(dove,arrivo);
		cancella(dove);
	}
	public static String sopra(String file)
	{
		return new File(file).getParent();
	}
	public static String casuale(String dove)
	{
		String nome;
		while(Memoria.esiste(dove+(nome=""+new Random().nextInt())));
		return dove+nome;
	}
	public static boolean directory(String dove)
	{
		return new File(dove).isDirectory();
	}
	public static void creaDir(String dove)
	{
		new File(dove).mkdirs();
	}
}
