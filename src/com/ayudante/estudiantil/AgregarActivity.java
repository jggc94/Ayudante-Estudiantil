package com.ayudante.estudiantil;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dataframework.DataFramework;
import com.android.dataframework.Entity;
import com.ayudante.estudiantil.mundo.Ayudante;

public class AgregarActivity extends Activity
{
	private static final int DIALOG_FECHA = 0;
	private int anioP;
    private int mesP;
    private int diaP;
    private StringBuilder fechaString;
    private String fecha;
    
	private EditText fieldTitulo, fieldTexto;
	private Button btnAgregar,btnFecha;
	private TableLayout tabla;
	private TableRow row;
	private TextView lblTitulo,lblTexto,lblFecha;
	private Context contexto;
	private Ayudante ayudante;
	private Calendar calendario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agregar_activity);
		
		contexto = this;
		ayudante = new Ayudante();
		tabla = (TableLayout) findViewById(R.id.tablelayoutVer);
		cargarDatos();
		
		fieldTitulo = (EditText) findViewById(R.id.fieldTitulo);
		fieldTexto = (EditText) findViewById(R.id.fieldTexto);
		
		btnAgregar = (Button) findViewById(R.id.btnAgregarNueva);
		btnAgregar.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				if(!fieldTitulo.getText().toString().equals("") && !fieldTexto.getText().toString().equals("") && btnFecha.getText().toString().compareToIgnoreCase("Fecha") != 0)
				{
					try 
					{
						int y = ayudante.agregarPregunta(fieldTitulo.getText().toString(), fieldTexto.getText().toString(), fecha);

				    	if(y == 1)
				    	{
							cargarDatos();
							fieldTitulo.setText("");
							fieldTexto.setText("");
							Toast.makeText(contexto, getString(R.string.tareaAgregada), Toast.LENGTH_SHORT).show();		
				    	}
				    	else
				    	{
				    		Toast.makeText(contexto, getString(R.string.error), Toast.LENGTH_SHORT).show();
				    	}
					} catch (Exception e) 
					{
						Toast.makeText(contexto, getString(R.string.error)+ " : " + e.getMessage() , Toast.LENGTH_SHORT).show();
					}
					
					
				}
				else
				{
					Toast.makeText(contexto, getString(R.string.relleneCampos), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnFecha = (Button) findViewById(R.id.btnFecha);
		btnFecha.setOnClickListener(new OnClickListener() 
		{
			@SuppressWarnings("deprecation")
			public void onClick(View v) 
			{
				showDialog(DIALOG_FECHA);
			}
		});
		
		calendario = Calendar.getInstance();
        anioP = calendario.get(Calendar.YEAR);
        mesP = calendario.get(Calendar.MONTH);
        diaP = calendario.get(Calendar.DAY_OF_MONTH);
        
        updateDisplay();
        
        
		
		
	}

	/**
	 * Metodo usado para cargar los datos y rellenar las filas con la info total
	 */
    public void cargarDatos()
    {
    	tabla.removeViews(1, tabla.getChildCount()-1);

    	List<Entity> list = DataFramework.getInstance().getEntityList("tarea");
		@SuppressWarnings("rawtypes")
		Iterator iter = list.iterator();
		
		// Mientras exista uno mas
		while (iter.hasNext()) 
		{
			Entity  entityCargar = (Entity)iter.next();
			String tituloP = entityCargar.getString("TITULO");
			String textoP = entityCargar.getString("TEXTO");
			String fechaP = entityCargar.getString("FECHA");
         	
			rellenarTablaConRows(tituloP, textoP, fechaP,entityCargar.getId());
		}
    }
    
    /**
     * Metodo Usado para rellenar cada row con cada info recibida
     * @param tituloP	- Titulo de la tarea
     * @param textoP	- Texto de la tarea
     * @param fechaP	- Fecha de entrega de la tarea 
     */
    public void rellenarTablaConRows(final String tituloP, final String textoP, final String fechaP, final long id)
    {	    	
    	row = new TableRow(contexto);
    	row.setMinimumHeight(40);
    	
    	lblTitulo = new TextView(contexto);
    	lblTitulo.setWidth(100);
        lblTitulo.setText(tituloP);
        lblTitulo.setTextSize(10);
        lblTitulo.setTextColor(Color.BLACK);
        lblTitulo.setGravity(Gravity.CENTER);

        lblTexto = new TextView(contexto);
        lblTexto.setWidth(100);
        lblTexto.setText(textoP);
        lblTexto.setTextSize(10);
        lblTexto.setWidth(100);
        lblTexto.setTextColor(Color.BLACK);
        lblTexto.setGravity(Gravity.CENTER);
        
        lblFecha = new TextView(contexto);
        lblFecha.setWidth(100);
        lblFecha.setText(fechaP);
        lblFecha.setTextSize(10);
        lblFecha.setWidth(100);
        lblFecha.setTextColor(Color.BLACK);
        lblFecha.setGravity(Gravity.CENTER);
        
        row.addView(lblTitulo);
        row.addView(lblTexto);
        row.addView(lblFecha);
 		row.setOnClickListener(new OnClickListener() 
 		{
			public void onClick(View v) 
			{
				Toast.makeText(contexto, getString(R.string.enunciadoTarea) + textoP, Toast.LENGTH_SHORT).show();
			}
		});
 		
 		row.setOnLongClickListener(new OnLongClickListener() 
 		{
			public boolean onLongClick(View v) 
			{
				new AlertDialog.Builder(contexto)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(getString(R.string.eliminar))
		        .setMessage(getString(R.string.eliminarMensaje) + " : " + tituloP)
		        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() 
		        {
		        	// Al darle click en si, se elimina
		            public void onClick(DialogInterface dialogC, int which) 
		            {
		            	try 
		            	{
							ayudante.eliminarTarea(tituloP,textoP,fechaP,id);
							cargarDatos();
						} catch (Exception e) 
						{
							Toast.makeText(contexto, getString(R.string.error) + " : " + e.getMessage() , Toast.LENGTH_SHORT).show();
						}
		            	
		            }

		        })
		        // De lo contrario al dar click en NO entonces se cierra el dialog
		        .setNegativeButton(R.string.no, null)
		        .show();
				return false;
			}
		});
 		
        tabla.addView(row);
    }
    
    /**
     * Metodo usado al seleccionar una fecha en el dialog
     */
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() 
    {
    	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
    	{
    		anioP = year;
    		mesP = monthOfYear;
            diaP = dayOfMonth;
            updateDisplay();
            displayToast();
    	}
	};
	
	private void updateDisplay() 
	{
		fechaString = (new StringBuilder().append(mesP + 1).append("/").append(diaP).append("/").append(anioP).append(" "));
		fecha = fechaString.toString();
		btnFecha.setText(fechaString);
	}
	
	/**
	 * Metodo usado para mostrar la fecha seleccionada
	 */
	private void displayToast() 
	{
        Toast.makeText(this, new StringBuilder().append(getString(R.string.fechaSeleccionada)).append(fechaString),  Toast.LENGTH_SHORT).show();
    }
	
	/**
	 * Metodo usado para mostrar un dialog(Segun id)
	 */
	protected Dialog onCreateDialog(int id) 
	{
        switch (id) 
        {
        	case DIALOG_FECHA:
            return new DatePickerDialog(this,dateSetListener,anioP, mesP, diaP);
        }
        return null;
    }	
}
