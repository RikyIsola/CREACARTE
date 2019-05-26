package com.island;
import java.lang.reflect.*;
public class Lista<T>
{
	public static<T>T[]aggiungi(T[]input,T aggiungi)
	{
		T[]output=(T[])Array.newInstance(input.getClass().getComponentType(),input.length+1);
		for(int a=0;a<output.length-1;a++)output[a]=input[a];
		output[output.length-1]=aggiungi;
		return output;
	}
	public static<T>T[]aggiungi(T[]input,T[]aggiungi)
	{
		T[]output=(T[])Array.newInstance(input.getClass().getComponentType(),input.length+aggiungi.length);
		for(int a=0;a<input.length;a++)output[a]=input[a];
		for(int a=0;a<aggiungi.length;a++)output[input.length+a]=aggiungi[a];
		return output;
	}
	public static<T>T[]rimuovi(T[]input,T rimuovi)
	{
		T[]output=(T[])Array.newInstance(input.getClass().getComponentType(),input.length-1);
		boolean fatto=false;
		for(int a=0;a<input.length;a++)
		{
			if(input[a]==rimuovi&&!fatto)fatto=true;
			else
			{
				if(fatto)output[a-1]=input[a];
				else output[a]=input[a];
			}
		}
		return output;
	}
	public static<T>T[]rimuovi(T[]input,T[]rimuovi)
	{
		T[]output=(T[])Array.newInstance(input.getClass().getComponentType(),input.length-rimuovi.length);
		int index=0;
		for(int a=0;a<input.length;a++)
		{
			boolean ok=true;
			for(T t:rimuovi)if(t==input[a])
			{
				ok=false;
				break;
			}
			if(ok)
			{
				output[index]=input[a];
				index++;
			}
		}
		return output;
	}
	public static<T>T[]rimuovi(T[]input,int index)
	{
		T[]output=(T[])Array.newInstance(input.getClass().getComponentType(),input.length-1);
		for(int a=0;a<input.length;a++)
		{
			if(a<index)output[a]=input[a];
			else if(a>index)output[a-1]=input[a];
		}
		return output;
	}
}
