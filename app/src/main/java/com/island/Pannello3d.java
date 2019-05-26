package com.island;
import android.graphics.*;
import android.opengl.*;
import android.view.accessibility.*;
public class Pannello3d extends GLSurfaceView
{
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event)
	{
		return true;
	}
	private Schermo schermo;
	private Info info;
	public Pannello3d(final Gruppo gruppo,final double x,final double y,final double larghezza,final double altezza)
	{
		super(gruppo.getContext());
		schermo=gruppo.schermo();
		info=new Info(x,y,larghezza,altezza,gruppo.unitaX(),gruppo.unitaY());
		schermo.runOnUiThread(new Runnable()
			{
				public void run()
				{
					gruppo.addView(Pannello3d.this);
					setLayoutParams(info);
				}
			}
		);
	}
	public double tall()
	{
		return altezza()-y();
	}
	public double large()
	{
		return larghezza()-x();
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	}
	public Pannello3d y(double y)
	{
		info.altezza=y+info.altezza-info.y;
		info.y=y;
		return this;
	}
	public Pannello3d x(double x)
	{
		info.larghezza=x+info.larghezza-info.x;
		info.x=x;
		return this;
	}
	public Pannello3d trans(double transX,double transY)
	{
		info.trans(transX,transY);
		return this;
	}
	public double transX()
	{
		return info.transX;
	}
	public double transY()
	{
		return info.transY;
	}
	public Pannello3d larghezza(double larghezza)
	{
		info.larghezza=larghezza;
		return this;
	}
	public Pannello3d altezza(double altezza)
	{
		info.altezza=altezza;
		return this;
	}
	public Pannello3d large(double large)
	{
		info.larghezza=x()+large;
		return this;
	}
	public Pannello3d tall(double tall)
	{
		info.altezza=y()+tall;
		return this;
	}
	public Pannello3d antiTrans(boolean transX,boolean transY)
	{
		info.antiTrans(transX,transY);
		return this;
	}
	public double x()
	{
		return info.x;
	}
	public double y()
	{
		return info.y;
	}
	public double larghezza()
	{
		return info.larghezza;
	}
	public double altezza()
	{
		return info.altezza;
	}
	public double unitaX()
	{
		return info.unitaX;
	}
	public double unitaY()
	{
		return info.unitaY;
	}
	public Schermo schermo()
	{
		return schermo;
	}
	public void riprendi()
	{
		onResume();
	}
	public void pausa()
	{
		onPause();
	}
	public double largeMeta()
	{
		return large()/2;
	}
	public double tallMeta()
	{
		return tall()/2;
	}
	public double centroX()
	{
		return x()+largeMeta();
	}
	public double centroY()
	{
		return y()+tallMeta();
	}
	public void largeMeta(double largeMeta)
	{
		large(largeMeta*2);
	}
	public void tallMeta(double tallMeta)
	{
		tall(tallMeta*2);
	}
	public void centroX(double centroX)
	{
		x(centroX-largeMeta());
	}
	public void centroY(double centroY)
	{
		y(centroY-tallMeta());
	}
}
