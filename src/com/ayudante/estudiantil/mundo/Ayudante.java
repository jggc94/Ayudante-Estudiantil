package com.ayudante.estudiantil.mundo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.android.dataframework.DataFramework;
import com.android.dataframework.Entity;

public class Ayudante 
{	
	public Ayudante()
	{
		
	}

	/**
	 * 
	 * @param contexto		Contexto donde esta la actividad
	 * @param packageName	Nombre del paquete actual
	 * @return	1 - Si Abre le base de datos.
	 *  		2 - Si hay error.
	 */
	public int abrirBaseDeDatos(Context contexto, String packageName)
	{
		try 
		{
			DataFramework.getInstance().open(contexto, packageName);
			return 1;
		} catch (XmlPullParserException e) 
		{
			e.printStackTrace();
			return 2;
		} catch (IOException e) {
			return 2;
		}
		
	}
	
	public Tarea buscarPregunta(String titulo,String texto, String fecha) throws Exception
	{
		List<Entity> list = DataFramework.getInstance().getEntityList("tarea", "TITULO = '" + titulo+ "'");
        @SuppressWarnings("rawtypes")
        Iterator iter = list.iterator();
        Tarea tBuscada = null;
        		
        // Mientras exista uno mas
        while (iter.hasNext()) 
        {
        	Entity ent = (Entity)iter.next();
        	String tituloP = ent.getString("TITULO");
        	String textoP = ent.getString("TEXTO");
        	String fechaP = ent.getString("FECHA");
        	
        	tBuscada = new Tarea(tituloP, textoP, fechaP);
        }
		
        return tBuscada;
	}
	/**
	 * 
	 * @param titulo	- Titulo de la tarea
	 * @param texto		- Texto de la tarea
	 * @param fecha		- Fecha de la tarea
	 * @return	1 - si agrego la pregunta
	 * 			2 - si no agrego la pregunta
	 */			
	public int agregarPregunta(String titulo,String texto, String fecha) throws Exception
	{
		Entity entityAgregar = new Entity("tarea");
		entityAgregar.setValue("TITULO", titulo);
		entityAgregar.setValue("TEXTO", texto);
		entityAgregar.setValue("FECHA", fecha);
		entityAgregar.save();
		
		Tarea tBuscada = buscarPregunta(titulo, texto, fecha);
		
		if(tBuscada != null)
		{
			return 1;
		}
		else
		{
			return 2;
		}
		
	}
	
	public ArrayList<Tarea> cargarArchivos()
	{
		List<Entity> list = DataFramework.getInstance().getEntityList("tarea");
		@SuppressWarnings("rawtypes")
		Iterator iter = list.iterator();
		
		ArrayList<Tarea> preguntas = new ArrayList<Tarea>();
		
		// Mientras exista uno mas
		while (iter.hasNext()) 
		{
			Entity  entityCargar = (Entity)iter.next();
			String tituloP = entityCargar.getString("TITULO");
			String textoP = entityCargar.getString("TEXTO");
			String fechaP = entityCargar.getString("FECHA");
         	
			Tarea tareaI = new Tarea(tituloP, textoP, fechaP);
			preguntas.add(tareaI);
		}
		
		return preguntas;
	}

	public void eliminarTarea(String tituloP, String textoP, String fechaP,long id) throws Exception
	{
		Entity entityEliminar = new Entity("tarea", id);
		entityEliminar.delete();
	}
}
