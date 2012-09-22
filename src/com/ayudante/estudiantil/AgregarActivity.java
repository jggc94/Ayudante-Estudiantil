package com.ayudante.estudiantil;

import java.util.ArrayList;
import java.util.Calendar;

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

import com.ayudante.estudiantil.mundo.Ayudante;
import com.ayudante.estudiantil.mundo.Tarea;

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
	private ArrayList<Tarea> tareas;
	private Calendar calendario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agregar_activity);
		
		contexto = getApplicationContext();
		ayudante = new Ayudante();
		
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
						ayudante.agregarPregunta(fieldTitulo.getText().toString(), fieldTexto.getText().toString(), fecha);
					} catch (Exception e) 
					{
						Toast.makeText(contexto, getString(R.string.error)+ " : " + e.getMessage() , Toast.LENGTH_SHORT).show();
					}
					
					cargarDatos();
					Toast.makeText(contexto, getString(R.string.tareaAgregada), Toast.LENGTH_SHORT).show();
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
        
        tabla = (TableLayout) findViewById(R.id.tablelayoutVer);
		cargarDatos();
		
	}

	/**
	 * Metodo usado para cargar los datos y rellenar las filas con la info total
	 */
    public void cargarDatos()
    {
    	for (int i = 1; i < tabla.getChildCount(); i++) 
    	{
    		tabla.removeViewAt(i);
		}
         
    	tareas = ayudante.cargarArchivos();
    	
    	for (int i = 0; i < tareas.size(); i++) 
    	{
    		rellenarTablaConRows(tareas.get(i).getTitulo(), tareas.get(i).getTexto(), tareas.get(i).getFecha());
    	}
    }
    
    /**
     * Metodo Usado para rellenar cada row con cada info recibida
     * @param tituloP	- Titulo de la tarea
     * @param textoP	- Texto de la tarea
     * @param fechaP	- Fecha de entrega de la tarea 
     */
    public void rellenarTablaConRows(final String tituloP, final String textoP, final String fechaP)
    {	
    	row = new TableRow(contexto);
     	
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
		        .setTitle(R.string.eliminar)
		        .setMessage(R.string.eliminarMensaje + " "+row.getId())
		        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() 
		        {
		        	// Al darle click en si, se elimina
		            public void onClick(DialogInterface dialogC, int which) 
		            {
		            	try 
		            	{
							ayudante.eliminarTarea(tituloP,textoP,fechaP,row.getId());
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
