package com.mswiczar.guiatel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.net.ParseException;
import android.util.Log;

public class PaginasBlancas {
	
	public String ListaAnunciantes;
	public String ListaAbonados;
	
	private int findGetAnunciantesStart(String data)
	{
		String abusqueda = "var anunciantesSeleccionados = '";
		
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index + abusqueda.length();
		}
		return -1;
	}
	

	private int findGetAnunciantesEnd(String data)
	{
		String abusqueda = "';";
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index ;
		}
		return -1;

	}
		

	private int findGetAbonadosStart(String data)
	{
		String abusqueda = "var abonadosSeleccionados = '";
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index + abusqueda.length();
		}
		return -1;
	}

	
	private int findGetAbonadosEnd(String data)
	{
		String abusqueda = "';";
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index ;
		}
		return -1;

	}
		
	
	private int findStartRow(String data)
	{
		String abusqueda = "<H2 class=\"alta\">";
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index + abusqueda.length();
		}
		return -1;
	}

	private int findEndRow(String data)
	{
		String abusqueda = "</H2>";
		int index = data.indexOf(abusqueda);

		if (index!= -1)
		{
			return index ;
		}

		return -1;

	}
		

	private int findStartTel(String data)
	{
		String abusqueda = "<H3><STRONG>";
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index + abusqueda.length();
		}
		return -1;
	}

	
	private int findEndTel(String data)
	{
		String abusqueda = "</STRONG></H3>";
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index ;
		}
		return -1;

	}

	
	
	private int findStartDireccion(String data)
	{
		String abusqueda = "</H3>";
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index + abusqueda.length();
		}
		return -1;
	}

	
	private int findEndDireccion(String data)
	{
		String abusqueda = "</td>";
		int index = data.indexOf(abusqueda);
		if (index!= -1)
		{
			return index ;
		}
		return -1;

	}


    private int complete(String theresult, ArrayList<HashMap<String,String> > listResults )
    {
    
    	String stringNombre;
    	String stringTel;
    	String stringDireccion;
    	
    	int salida=0;
    	
    	
    	int posStartRow = findStartRow(theresult);
    	

    	int endRow;
    	String workingStr = theresult;
    	
    	int startTel;
    	int endTel;
    	
    	int startDire;
    	int endDire;
    	
	   	if (posStartRow !=-1)
	   	{
	   		while (posStartRow != -1 )
	   		{
	   	    	String aux  =workingStr.substring(posStartRow,workingStr.length());
	   	    	
	   	    	workingStr = aux;

	   	    	endRow = findEndRow(workingStr);

	   			if (endRow != -1)
	   			{
	   				stringNombre= workingStr.substring(0,endRow);
	   				
	   				workingStr = workingStr.substring(endRow,workingStr.length()-endRow);
	   				
	   				startTel = findStartTel(workingStr);
	   				
	   				
	   				if (startTel!=-1)
	   				{
	   		   	    	workingStr =workingStr.substring(startTel,workingStr.length()-startTel);
	   		   			endTel = findEndTel(workingStr);
	   		   			if (endTel!= -1)
	   		   			{
	   		   				stringTel= workingStr.substring(0,endTel);

	   		   				
	   		   				workingStr = workingStr.substring(endTel,workingStr.length()-endTel);
	   		   				
	   		   				startDire = findStartDireccion(workingStr);
	   		   				if (startDire!=-1)
	   		   				{
	   		   		   	    	workingStr =workingStr.substring(startDire,workingStr.length()-startDire);
	   		   		   	    	endDire = findEndDireccion(workingStr);
	   		   		   			if (endDire!= -1)
	   		   		   			{
	   		   		   				
	   		   		   				String auxStringDireccion = workingStr.substring(0,endDire);

	   		   		   				StringTokenizer st = new StringTokenizer(auxStringDireccion,"-");
	   		   		   				String n = st.nextToken();
	   		   		   				n = n.replaceAll("\n", "");
	   		   		   				n = n.replaceAll("\r", "");
	   		   		   				n = n.replaceAll("\t", "");

	   		   		   				stringDireccion =  n +"\n";
	   		   		   				while(st.hasMoreTokens())
	   		   		   				{
	   		   		   					n = (String)st.nextToken();
		   		   		   				n = n.replaceAll("\n", "");
		   		   		   				n = n.replaceAll("\r", "");
		   		   		   				n = n.replaceAll("\t", "");
		   		   		   				stringDireccion = stringDireccion+  n.trim() +" ";

	   		 		   	    		
	   		   		   				}
	   		   		   				
	   		   		   				
	   		   		   				
	   		   		   				HashMap<String , String> o = new HashMap<String , String>();
	   		   		   				o.put("nombre",stringNombre);
	   		   		   				o.put("tel",stringTel);
	   		   		   				
	   		   		   				stringDireccion = stringDireccion.replaceAll("          ", " ");
	   		   		   				stringDireccion = stringDireccion.replaceAll("         ", " ");
	   		   		   				stringDireccion = stringDireccion.replaceAll("        ", " ");
	   		   		   				stringDireccion = stringDireccion.replaceAll("       ", " ");
	   		   		   				stringDireccion = stringDireccion.replaceAll("      ", " ");
	   		   		   				stringDireccion = stringDireccion.replaceAll("     ", " ");
	   		   		   				stringDireccion = stringDireccion.replaceAll("    ", " ");
	   		   		   				stringDireccion = stringDireccion.replaceAll("   ", " ");
	   		   		   				stringDireccion = stringDireccion.replaceAll("  ", " ");

	   		   		   				o.put("direccion",stringDireccion);
	   		   		   				
	   		   		   				
	   		   		   				listResults.add(o);

	   		   		   				salida++;

	   		   		   			}

	   		   				}
	   		   			}
	   				}
	   				posStartRow= findStartRow(workingStr);
  		   			
	   			}

	   		}
	   	}
	   	
	   	

    	
    	return salida;
    	
    	
    	
    }
	
    public static String getContentCharSet(final HttpEntity entity) throws ParseException {

    	if (entity == null) { throw new IllegalArgumentException("HTTP entity may not be null"); }

    	String charset = null;

    	if (entity.getContentType() != null) {

    	HeaderElement values[] = entity.getContentType().getElements();

    	if (values.length > 0) {

    	NameValuePair param = values[0].getParameterByName("charset");

    	if (param != null) {

    	charset = param.getValue();

    	}

    	}

    	}

    	return charset;

    	}
    
    
    public static String _getResponseBody(final HttpEntity entity) throws IOException, ParseException {

    	if (entity == null) { throw new IllegalArgumentException("HTTP entity may not be null"); }

    	InputStream instream = entity.getContent();

    	if (instream == null) { return ""; }

    	if (entity.getContentLength() > Integer.MAX_VALUE) { throw new IllegalArgumentException(

    	"HTTP entity too large to be buffered in memory"); }

    	String charset = getContentCharSet(entity);

    	if (charset == null) {

    	charset = HTTP.DEFAULT_CONTENT_CHARSET;

    	}

    	Reader reader = new InputStreamReader(instream, charset);

    	StringBuilder buffer = new StringBuilder();

    	try {

    	char[] tmp = new char[1024];

    	int l;

    	while ((l = reader.read(tmp)) != -1) {

    	buffer.append(tmp, 0, l);

    	}

    	} finally {

    	reader.close();

    	}

    	return buffer.toString();

    	}

    	
    
    public static String getResponseBody(HttpResponse response) {

    	String response_text = null;

    	HttpEntity entity = null;

    	try {

    	entity = response.getEntity();

    	response_text = _getResponseBody(entity);

    	} catch (ParseException e) {

    	e.printStackTrace();

    	} catch (IOException e) {

    	if (entity != null) {

    	try {

    	entity.consumeContent();

    	} catch (IOException e1) {

    	}

    	}

    	}

    	return response_text;

    	}
	
	

	public int  buscarPorNombreXML(String nombre, int intProvincia ,String strProvincia,  int pagina , ArrayList<HashMap<String,String> > listResults )
	{
	
		String strBusqueda = nombre.replaceAll(" ", "-");
		String strURL;
			
		if (pagina==1)
		{
			strURL = "http://xmlsearch.paginasblancas.com.ar/XMLSearchBlancasTelefonica/search?apellido="+strBusqueda+"&provinciasId="+intProvincia;
		}
		else
		{
			strURL = "http://xmlsearch.paginasblancas.com.ar/XMLSearchBlancasTelefonica/search?apellido="+strBusqueda+"&paginado.paginaActual="+pagina+"&provinciasId="+intProvincia;
		}
				
				
    	XmlReaderToHashMap axmlreader =  new XmlReaderToHashMap();
   	    axmlreader.setTheURL(strURL);
   	  /*
   	    try
   	   {
  	    String  resultado = axmlreader.getCVSData();
    	   Log.v("Busqueda:" ,resultado );

   	   } catch(Exception e)
   	   {
   		   
   	   }
   	   */

   	    listResults.addAll(axmlreader.parse());

		return 0;
	}
    
    
	public int  buscarPorNombre(String nombre, int intProvincia ,String strProvincia,  int pagina , ArrayList<HashMap<String,String> > listResults )
	{
		
		String strBusqueda = nombre.replaceAll(" ", "-");
		
		String strURL;
		if (pagina ==1)
		{
				if (intProvincia==0)
				{
					strURL = "http://paginasblancas.com.ar/busqueda-nombre/argentina/"+strBusqueda;
					
				}
				else
				{
					strURL = "http://paginasblancas.com.ar/busqueda-nombre/argentina/"+strProvincia +"/"+strBusqueda;
				}
			
				//Log.v("La busqueda:",strURL);
	   	    	XmlReaderToHashMap axmlreader =  new XmlReaderToHashMap();
	   	   	    axmlreader.setTheURL(strURL);
	   	   	    try
	   	   	    {
	   	   	    	String  resultado = axmlreader.getCVSData();
	   	   	    	String  workingStr;
		   	    	//Log.v( "maps " , resultado);
	   	   	    	
	   	   	    	int posStartAnunciantes = findGetAnunciantesStart(resultado);
	   	    		//Log.v( "posStartAnunciantes " , " "+posStartAnunciantes);
	   	   	    	int posEndAnunciantes;
	   	   	    	if (posStartAnunciantes !=-1)
	   	   	    	{
	   	   	    		workingStr =resultado.substring(posStartAnunciantes,resultado.length()-posStartAnunciantes);
	   	   	    		posEndAnunciantes =findGetAnunciantesEnd(workingStr);
	   	   	    		if (posEndAnunciantes!=-1)
	   	   	    		{
	   	   	    			ListaAnunciantes = workingStr.substring(0,posEndAnunciantes);
	   	   	    		//	Log.v( "ListaAnunciantes " , ListaAnunciantes);
	   	   	    		}
	   	   	    	}
	   	   	    	

	   	   	    	int posStartAbonados = findGetAbonadosStart(resultado);
	   	    	//	Log.v( "posStartAbonados " , " "+posStartAbonados);
	   	   	    	int posEndAbonados;
	   	   	    	if (posStartAbonados !=-1)
	   	   	    	{
	   	   	    		workingStr =resultado.substring(posStartAbonados,resultado.length()-posStartAbonados);
	   	   	    		posEndAbonados =findGetAbonadosEnd(workingStr);
	   	   	    		if (posEndAbonados!=-1)
	   	   	    		{
	   	   	    			ListaAbonados = workingStr.substring(0,posEndAbonados);
	   	   	    		//	Log.v( "ListaAbonados " , ListaAbonados);
	   	   	    		}
	   	   	    	}
	   	   	    	
	   	   	    	
	   	   	    	return complete(resultado,listResults);

	   	   	    }
	   	   	    catch (Exception e)
	   	   	    {
		   	    	Log.v( "Error: " , e.getMessage());
		   	    	return 0;
	   	   	    }
		}
		else
		{
			
			if(intProvincia==0)
			{
				strURL = "http://paginasblancas.com.ar/busqueda-nombre/argentina/"+strBusqueda+"-pagina-"+pagina;
			}
			else 
			{
				strURL = "http://paginasblancas.com.ar/busqueda-nombre/argentina/"+strProvincia+"/"+strBusqueda+"-pagina-"+pagina; 
			}
			
			    HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost(strURL);
			    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
			    nameValuePairs.add(new BasicNameValuePair("paginado.paginaActual", ""+pagina));
			    nameValuePairs.add(new BasicNameValuePair("apellido", ""+strBusqueda));
			    nameValuePairs.add(new BasicNameValuePair("provinciasSeleccionadas", ""+intProvincia));
			    nameValuePairs.add(new BasicNameValuePair("anunciantesSeleccionados", ""));
			    nameValuePairs.add(new BasicNameValuePair("anuncianteSeleccionado", ListaAnunciantes));
			    nameValuePairs.add(new BasicNameValuePair("abonadosSeleccionados", ListaAbonados));

			    
			    try
			    {
			    	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    	HttpResponse response = httpclient.execute(httppost);
			    	String  resultado = getResponseBody(response);
	   	   	    	return complete(resultado,listResults);
			    	
			    }
			    catch (Exception e)
			    {
			    	Log.v("Exception " , e.getMessage());
			    }
			    
			
			

			
			
			
			
			
		
			
		}
		return 0;
	}
	
}
