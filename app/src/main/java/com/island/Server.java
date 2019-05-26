package com.island;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class Server
{
	private Lan lan;
	public Server(final String ip,final int porta)
	{
		lan=new Lan()
		{
			private String comando;
			private String var1;
			private String[]nomi;
			private List<ResolveInfo>lista;
			private PackageManager pm;
			public void leggi(final String s,final Socket id)
			{
				if(comando==null)
				{
					if(s.equals("TOAST"))comando=s;
					else if(s.equals("SCHERMO"))comando=s;
					else if(s.equals("APRI"))comando=s;
					else if(s.equals("APRIFILE"))comando=s;
					else if(s.equals("DIRECTORY"))comando=s;
					else if(s.equals("MINIFILE"))comando=s;
					else if(s.equals("APPLICAZIONI"))
					{
						pm=schermo().getPackageManager();
						Intent i=new Intent(Intent.ACTION_MAIN,null);
						i.addCategory(Intent.CATEGORY_LAUNCHER);
						lista=pm.queryIntentActivities(i,0);
						nomi=new String[lista.size()];
						for(int a=0;a<lista.size();a++)manda(nomi[a]=lista.get(a).loadLabel(pm).toString());
						manda("FINE");
					}
				}
				else if(comando.equals("TOAST"))
				{
					schermo().runOnUiThread(new Runnable()
						{
							public void run()
							{
								Toast.makeText(schermo(),s,0).show();
							}
						});
					comando=null;
				}
				else if(comando.equals("SCHERMO"))
				{
					View v;
					if(schermo().libero())v=schermo().getWindow().getDecorView().getRootView();
					else v=schermo().finestra().getWindow().getDecorView().getRootView();
					v.setDrawingCacheEnabled(true);
					final int dimensioni=Integer.valueOf(s);
					Bitmap b=Bitmap.createScaledBitmap(v.getDrawingCache(),dimensioni,dimensioni,false);
					v.setDrawingCacheEnabled(false);
					manda("SCHERMO");
					try
					{
						Thread.sleep(1);
					}
					catch(InterruptedException e)
					{
						System.out.println(e);
					}
					try
					{
						OutputStream output=id.getOutputStream();
						int primo=b.getPixel(0,0);
						int n=0;
						int r=Color.red(primo);
						int g=Color.green(primo);
						int blu=Color.blue(primo);
						for(int x=0;x<b.getWidth();x++)for(int y=0;y<b.getHeight();y++)
							{
								int pixel=b.getPixel(x,y);
								n++;
								int nr=Color.red(pixel);
								int ng=Color.green(pixel);
								int nb=Color.blue(pixel);
								if(r!=nr||g!=ng||blu!=nb||n==255||(x==b.getWidth()-1&&y==b.getHeight()-1))
								{
									output.write(n);
									output.write(r);
									output.write(g);
									output.write(blu);
									n=0;
									r=nr;
									g=ng;
									blu=nb;
								}
							}
					}
					catch(IOException e)
					{
						System.out.println(e);
					}
					b.recycle();
					comando=null;
				}
				else if(comando.equals("APRI"))
				{
					for(int a=0;a<nomi.length;a++)if(nomi[a].equals(s))
						{
							schermo().startActivity(pm.getLaunchIntentForPackage(lista.get(a).activityInfo.packageName));
							break;
						}
					comando=null;
				}
				else if(comando.equals("DIRECTORY"))
				{
					String[]file=new File(s).list();
					if(file==null)file=new String[0];
					for(String f:file)
					{
						if(new File(s+f).isDirectory())manda(f+"/");
						else manda(f);
					}
					manda("FINE");
					comando=null;
				}
				else if(comando.equals("APRIFILE"))
				{
					File f=new File(s);
					try
					{
						OutputStream output=id.getOutputStream();
						FileInputStream input=new FileInputStream(f);
						long dim=f.length();
						manda(String.valueOf(dim));
						try
						{
							Thread.sleep(5);
						}
						catch(InterruptedException e)
						{
							System.out.println(e);
						}
						for(long a=0;a<dim;a++)output.write(input.read());
						input.close();
						comando=null;
					}
					catch(IOException e)
					{
						System.out.println(e);
					}
				}
				else if(comando.equals("MINIFILE"))
				{
					if(var1==null)var1=s;
					else
					{
						try
						{
							final int dimensioni=Integer.valueOf(var1);
							BitmapFactory.Options opt=new BitmapFactory.Options();
							opt.inJustDecodeBounds=true;
							FileInputStream input=new FileInputStream(new File(s));
							BitmapFactory.decodeStream(input,null,opt);
							input.close();
							double scalatura=0.5;
							while(opt.outWidth/(scalatura*2)>dimensioni&&opt.outHeight/(scalatura*2)>dimensioni)scalatura*=2;
							opt.inSampleSize=(int)(scalatura*2);
							opt.inJustDecodeBounds=false;
							input=new FileInputStream(new File(s));
							Bitmap b1=BitmapFactory.decodeStream(input,null,opt);
							input.close();
							Bitmap b=Bitmap.createScaledBitmap(b1,dimensioni,dimensioni,false);
							b1.recycle();
							manda("SCHERMO");
							try
							{
								Thread.sleep(1);
							}
							catch(InterruptedException e)
							{
								System.out.println(e);
							}
							OutputStream output=id.getOutputStream();
							int primo=b.getPixel(0,0);
							int n=0;
							int r=Color.red(primo);
							int g=Color.green(primo);
							int blu=Color.blue(primo);
							for(int x=0;x<b.getWidth();x++)for(int y=0;y<b.getHeight();y++)
								{
									int pixel=b.getPixel(x,y);
									n++;
									int nr=Color.red(pixel);
									int ng=Color.green(pixel);
									int nb=Color.blue(pixel);
									if(r!=nr||g!=ng||blu!=nb||n==255||(x==b.getWidth()-1&&y==b.getHeight()-1))
									{
										output.write(n);
										output.write(r);
										output.write(g);
										output.write(blu);
										n=0;
										r=nr;
										g=ng;
										blu=nb;
									}
								}
							b.recycle();
							comando=null;
							var1=null;
						}
						catch(IOException e)
						{
							System.out.println(e);
						}
					}
				}
			}
		};
		final Handler h=new Handler();
		h.postDelayed(new Runnable()
			{
				public void run()
				{
					if(!lan.connesso())lan.inizioClient(ip,porta);
					h.postDelayed(this,10000);
				}
			},1);
	}
	public void schermo(Schermo schermo)
	{
		lan.schermo(schermo);
	}
}
