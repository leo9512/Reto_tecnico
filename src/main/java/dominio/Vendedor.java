package dominio;

import dominio.repositorio.RepositorioProducto;
import javafx.util.Pair;
import java.util.Calendar;
import java.util.Date;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";

    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        
    	this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;

    }

    public void generarGarantia(String codigo, String nombreCliente) {
    	
    	if (!tieneGarantia(codigo)){
    		
    		int numeroVocalesEnCodigo = calcularNumeroVocalesEnCodigo(codigo);
    		Date fechaActual = new Date();
    		
    		generarMensaje(numeroVocalesEnCodigo);
    		Pair<Double,Date> precioGarantiaYFecha = calcularPrecioGarantiaYFecha(codigo , fechaActual);
    		Double costoGarantia = precioGarantiaYFecha.getKey();
    		Date fechaFinGarantia = precioGarantiaYFecha.getValue();    		
    		ingresarGarantia(codigo, nombreCliente, costoGarantia, fechaFinGarantia);
    		
    	}
    }

	private void ingresarGarantia(String codigo, String nombreCliente, Double precioGarantia, Date fechaFinGarantia) {
		
		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
		Date fechaSolicitudGarantia = new Date();
		
		repositorioGarantia.agregar(
				new GarantiaExtendida(producto, fechaSolicitudGarantia, 
				fechaFinGarantia, precioGarantia, nombreCliente)
				);
	}

	private Date calcularFechaFinGarantia(Double porcentaje , Date fechaInicio){

		final Double PORCENTAJE_20 = 0.2;
		final int DOSCIENTOS_DIAS = 200;
		final int CIEN_DIAS = 100;
		Date fechaLimite;
			
		if (Double.compare(PORCENTAJE_20, porcentaje) == 0) {
			
			fechaLimite = retornarFechaDiasHabiles(DOSCIENTOS_DIAS, fechaInicio);
			
		} else {

			fechaLimite = retornarFechaDiasHabiles(CIEN_DIAS, fechaInicio);
		}
		
		return fechaLimite;
	}

	public Date retornarFechaDiasHabiles(int cantidadDias, Date fechaInicio) {
		
		Calendar calendarioActual = Calendar.getInstance();
		calendarioActual.setTime(fechaInicio);
		int diaActual = calendarioActual.get(Calendar.DAY_OF_WEEK);
		int contadorDias = 1;
		
		while(contadorDias < cantidadDias){
			calendarioActual.add(Calendar.DATE, 1);
			diaActual = calendarioActual.get(Calendar.DAY_OF_WEEK);
			if(diaActual != Calendar.MONDAY) contadorDias++;
		}
		
		if (diaActual == Calendar.SUNDAY) calendarioActual.add(Calendar.DATE, 2);
			
		return new Date(calendarioActual.getTimeInMillis());
		
	}

	public Pair<Double,Date> calcularPrecioGarantiaYFecha(String codigo, Date fechaInicio) {
		
		final Double PRECIO_LIMITE = 500000.0;
		final Double PORCENTAJE_10 = 0.1;
		final Double PORCENTAJE_20 = 0.2;
		Double precioProducto = repositorioProducto.obtenerPorCodigo(codigo).getPrecio();
		Double valorGarantia; 
		Date fechaFinGarantia;
		Pair<Double,Date> pair;
		
		if(precioProducto > PRECIO_LIMITE){
			valorGarantia = precioProducto * PORCENTAJE_20;
			fechaFinGarantia = calcularFechaFinGarantia(PORCENTAJE_20, fechaInicio);
			pair = new Pair<> (valorGarantia,fechaFinGarantia);
		}
		else{
			valorGarantia = precioProducto * PORCENTAJE_10;
			fechaFinGarantia = calcularFechaFinGarantia(PORCENTAJE_10, fechaInicio);
			pair = new Pair<> (valorGarantia,fechaFinGarantia);
		}
				
		return pair;
	}

	public void generarMensaje(int numeroVocalesEnCodigo) {
		
		final int VALOR_EXCEPCION = 3;		
		final String MENSAJE_EXCEPCION= "Este producto no cuenta con garantía extendida";
		
		if(numeroVocalesEnCodigo == VALOR_EXCEPCION){
			 throw new GarantiaExtendidaException(MENSAJE_EXCEPCION);
		}
	}

	public int calcularNumeroVocalesEnCodigo(String codigo) {
		
		String vocales = "aeiouAEIOU";
		int contador = 0;
		
		for(int i=0; i < codigo.length(); i++){
			
			if(vocales.indexOf(codigo.charAt(i))>-1){
				contador++;
			}
		}
		return contador;
	}
	

	public boolean tieneGarantia(String codigo) {
    	
        Producto producto = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
   
        return (producto != null);
    	
    }

}
