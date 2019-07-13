package com.island;
import android.net.wifi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import android.app.*;
import android.os.*;
public abstract class Lan
{
	private ServerSocket server;
	private Socket[]connessi;
	private Processo[]mandareServer,ricevereServer;
	private PrintStream[]outputServer;
	private Socket client;
	private Processo accettazione,mandare,ricevere;
	private PrintStream output;
	private Schermo schermo;
	public Lan()
	{
		this(null);
	}
	public Lan(Schermo schermo)
	{
		this.schermo=schermo;
	}
	public static String local()
	{
		return"127.0.0.1";
	}
	public Socket client()
	{
		while(client==null);
		return client;
	}
	public String ip(Schermo schermo)
	{
		return android.text.format.Formatter.formatIpAddress(((WifiManager)schermo.getSystemService(Activity.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
	}
	public Socket connesso(int id)
	{
		while(connessi==null||connessi.length<=id||connessi[id]==null);
		return connessi[id];
	}
	public int inizioServer(int porta,final int membri)
	{
		try
		{
			server=new ServerSocket(porta);
			connessi=new Socket[0];
			mandareServer=new Processo[0];
			ricevereServer=new Processo[0];
			outputServer=new PrintStream[0];
			accettazione=new Processo()
			{
				public void run()
				{
					loop();
				}
				public void sempre()
				{
					if(connessi.length<membri)
					{
						try
						{
							final Socket socket=server.accept();
							Processo mandare=new Processo()
							{
								public void run()
								{
									loop();
								}
							}.riprendi();
							Processo ricevere=new Processo()
							{
								public void run()
								{
									try
									{
										Scanner input=new Scanner(socket.getInputStream());
										while(true)
										{
											final String messaggio=input.nextLine();
											leggi(messaggio,socket);
										}
									}
									catch(final IOException e)
									{
										System.out.println(e);
									}
									catch(NoSuchElementException e)
									{
										System.out.println(e);
									}
									catch(NullPointerException e)
									{
										System.out.println(e);
									}
									int id=-1;
									while(id==-1)for(int a=0;a<connessi.length;a++)if(connessi[a]==socket)id=a;
									uscito(id);
								}
							};
							connessi=Lista.aggiungi(connessi,socket);
							mandareServer=Lista.aggiungi(mandareServer,mandare);
							ricevereServer=Lista.aggiungi(ricevereServer,ricevere);
							outputServer=Lista.aggiungi(outputServer,new PrintStream(socket.getOutputStream()));
						}
						catch(IOException e)
						{
							System.out.println(e);
						}
					}
				}
			}.riprendi();
			return server.getLocalPort();
		}
		catch(final IOException e)
		{
			System.out.println(e);
			return 0;
		}
	}
	public void inizioClient(final String ip,final int porta)
	{
		new Processo()
		{
			public void run()
			{
				try
				{
					client=new Socket(ip,porta);
					output=new PrintStream(client.getOutputStream());
					ricevere=new Processo()
					{
						public void run()
						{
							try
							{
								Scanner input=new Scanner(client.getInputStream());
								while(true)leggi(input.nextLine(),client);
							}
							catch(IOException e)
							{
								System.out.println(e);
							}
							catch(NoSuchElementException e)
							{
								System.out.println(e);
							}
							fine();
						}
					};
					mandare=new Processo()
					{
						public void run()
						{
							loop();
						}
					}.riprendi();
				}
				catch(final IOException e)
				{
					System.out.println(e);
				}
			}
		};
	}
	public boolean connesso()
	{
		return client!=null;
	}
	public void chiudiServer()
	{
		try
		{
			accettazione.chiudi();
			server.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	private void uscito(int id)
	{
		try
		{
			Socket socket=connessi[id];
			Processo mandare=mandareServer[id];
			Processo ricevere=ricevereServer[id];
			PrintStream output=outputServer[id];
			connessi=Lista.rimuovi(connessi,socket);
			mandareServer=Lista.rimuovi(mandareServer,mandare);
			ricevereServer=Lista.rimuovi(ricevereServer,ricevere);
			outputServer=Lista.rimuovi(outputServer,output);
			try
			{
				mandare.chiudi();
				output.flush();
				socket.close();
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println(e);
		}
	}
	public void pausa()
	{
		if(accettazione!=null)accettazione.pausa();
	}
	public void riprendi()
	{
		if(accettazione!=null)accettazione.riprendi();
	}
	public void fine()
	{
		try
		{
			if(outputServer!=null)for(OutputStream o:outputServer)o.flush();
			if(connessi!=null)for(Socket s:connessi)s.close();
			if(mandareServer!=null)for(Processo p:mandareServer)p.chiudi();
			if(output!=null)output.flush();
			if(client!=null)
			{
				client.close();
				client=null;
			}
			if(accettazione!=null)accettazione.chiudi();
			if(mandare!=null)mandare.chiudi();
			if(server!=null)server.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public abstract void leggi(String messaggio,Socket socket);
	public void manda(final String messaggio)
	{
		while(mandare==null||mandare.handler()==null);
		mandare.handler().post(new Runnable()
		{
			public void run()
			{
				output.print(messaggio+System.lineSeparator());
			}
		});
	}
	public void manda(final String messaggio,final Socket socket)
	{
		try
		{
			int id=-1;
			while(id==-1)for(int a=0;a<connessi.length;a++)if(connessi[a]==socket)id=a;
			final int fid=id;
			if(mandareServer[id].handler()!=null)mandareServer[id].handler().post(new Runnable()
					{
						public void run()
						{
							try
							{
								outputServer[fid].print(messaggio+System.lineSeparator());
							}
							catch(ArrayIndexOutOfBoundsException e)
							{
								System.out.println(e);
							}
						}
					});
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			
		}
	}
	public Schermo schermo()
	{
		return schermo;
	}
	public Lan schermo(Schermo schermo)
	{
		this.schermo=schermo;
		return this;
	}
	public int connessi()
	{
		if(connessi!=null)return connessi.length;
		else return 0;
	}
	public int velocita()
	{
		while(mandare==null);
		return mandare.velocita();
	}
	public int velocita(Socket socket)
	{
		int id=-1;
		while(id==-1)for(int a=0;a<connessi.length;a++)if(connessi[a]==socket)id=a;
		return mandareServer[id].velocita();
	}
}
