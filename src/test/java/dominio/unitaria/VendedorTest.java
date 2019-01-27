package dominio.unitaria;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import dominio.Vendedor;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.Producto;
import dominio.repositorio.RepositorioProducto;
import javafx.util.Pair;
import dominio.repositorio.RepositorioGarantiaExtendida;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	@Test
	public void productoYaTieneGarantiaTest() {
		
		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();		
		Producto producto = productoTestDataBuilder.build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertTrue(existeProducto);
	}
	
	@Test
	public void productoNoTieneGarantiaTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean existeProducto =  vendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertFalse(existeProducto);
	}
	
	@Test
	public void costoGarantiaCuandoProductoSuperaPrecio500000Test(){
		
		//arrange
		final Double MAYOR_QUINIENTOS_MIL = 500010.0;
		final Double COSTO_GARANTIA = 100002.0;
		
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();		
		Producto producto = productoTestDataBuilder.conPrecio(MAYOR_QUINIENTOS_MIL).build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		when(repositorioProducto.obtenerPorCodigo(producto.getCodigo())).thenReturn(producto);
		
		//act
		Pair<Double, Date> costoYFechaFinGarantia = vendedor
				.calcularPrecioGarantiaYFecha(producto.getCodigo(), new Date());
		
		//assert
		assertEquals(costoYFechaFinGarantia.getKey(),COSTO_GARANTIA);
	}
	
	@Test
	public void fechaFinGarantiaCuandoProductoSuperaPrecio500000Test(){
		
		//arrange
		final Double MAYOR_QUINIENTOS_MIL = 500010.0;
		
		final Date FECHA_INICIO = new GregorianCalendar(2019, Calendar.JANUARY, 27).getTime();
		final Date FECHA_FINAL = new GregorianCalendar(2019, Calendar.SEPTEMBER, 17).getTime();
		
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();		
		Producto producto = productoTestDataBuilder.conPrecio(MAYOR_QUINIENTOS_MIL).build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		when(repositorioProducto.obtenerPorCodigo(producto.getCodigo())).thenReturn(producto);
		
		//act
		Pair<Double, Date> costoYFechaFinGarantia = vendedor
				.calcularPrecioGarantiaYFecha(producto.getCodigo(), FECHA_INICIO);
		final Date FECHA_FINALIZACION = costoYFechaFinGarantia.getValue();
		
		//assert
		
		assertTrue(FECHA_FINAL.compareTo(FECHA_FINALIZACION) == 0);
	}
	
	@Test
	public void costoGarantiaCuandoProductoPrecedePrecio500000Test(){
		
		//arrange
		final Double MENOR_QUINIENTOS_MIL = 400000.0;
		final Double COSTO_GARANTIA = 40000.0;
		
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();		
		Producto producto = productoTestDataBuilder.conPrecio(MENOR_QUINIENTOS_MIL).build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		when(repositorioProducto.obtenerPorCodigo(producto.getCodigo())).thenReturn(producto);
		
		//act
		Pair<Double, Date> costoYFechaFinGarantia = vendedor
				.calcularPrecioGarantiaYFecha(producto.getCodigo(), new Date());
		
		//assert
		assertEquals(costoYFechaFinGarantia.getKey(),COSTO_GARANTIA);
	}
	
	@Test
	public void fechaFinGarantiaCuandoProductoPrecedePrecio500000Test(){
		
		//arrange
		final Double MENOR_QUINIENTOS_MIL = 400000.0;
		final Date FECHA_INICIO = new GregorianCalendar(2019, Calendar.JANUARY, 27).getTime();
		final Date FECHA_FINAL = new GregorianCalendar(2019, Calendar.MAY, 23).getTime();
		
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();		
		Producto producto = productoTestDataBuilder.conPrecio(MENOR_QUINIENTOS_MIL).build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		when(repositorioProducto.obtenerPorCodigo(producto.getCodigo())).thenReturn(producto);
		
		//act
		Pair<Double, Date> costoYFechaFinGarantia = vendedor
				.calcularPrecioGarantiaYFecha(producto.getCodigo(), FECHA_INICIO);
		final Date FECHA_FINALIZACION = costoYFechaFinGarantia.getValue();
		
		//assert
		assertTrue(FECHA_FINAL.compareTo(FECHA_FINALIZACION) == 0);
	}

	@Test
	public void expiracionGarantiaCuandoFinalizaDomingoTest(){
		
		//arrange
		final Date FECHA_INICIO = new GregorianCalendar(2019, Calendar.JANUARY, 28).getTime();
		final Date FECHA_FINAL = new GregorianCalendar(2019, Calendar.FEBRUARY, 05).getTime();
		final int MAXIMO_DIAS = 7;
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
				
		//act
		final Date FECHA_FINALIZACION = vendedor.retornarFechaDiasHabiles(MAXIMO_DIAS,FECHA_INICIO);
		
		//assert
		assertTrue(FECHA_FINALIZACION.compareTo(FECHA_FINAL) == 0);
	}
	
	@Test
	public void lunesNoHabilExpiracionGarantiaTest(){
		
		//arrange
		final Date FECHA_INICIO = new GregorianCalendar(2019, Calendar.JANUARY, 28).getTime();
		final Date FECHA_FINAL = new GregorianCalendar(2019, Calendar.FEBRUARY, 05).getTime();
		final int MAXIMO_DIAS = 8;
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
				
		//act
		final Date FECHA_FINALIZACION = vendedor.retornarFechaDiasHabiles(MAXIMO_DIAS,FECHA_INICIO);
		
		//assert
		assertTrue(FECHA_FINALIZACION.compareTo(FECHA_FINAL) == 0);
	}
	
	@Test
	public void contarVocalesCodigoProductoTest(){
		
		//arrange
		final String CODIGO = "1ACTURYei87";
		final int NUMERO_VOCALES = 4;
		
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();		
		Producto producto = productoTestDataBuilder.conCodigo(CODIGO).build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
				
		//act
		final int CANTIDAD_VOCALES = vendedor.calcularNumeroVocalesEnCodigo(producto.getCodigo());
		
		//assert
		assertTrue(CANTIDAD_VOCALES == NUMERO_VOCALES);
	}
	
	@Test
	public void retornarExcepcionCodigoTresVocalesTest(){
		
		//arrange
		final String CODIGO = "1ACTURYI87";
		final String MENSAJE_EXCEPCION= "Este producto no cuenta con garantía extendida";
		final String MENSAJE= "Para retornar Excepción el código debe contener 3 vocales.";
		
		
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();		
		Producto producto = productoTestDataBuilder.conCodigo(CODIGO).build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		final int CANTIDAD_VOCALES = vendedor.calcularNumeroVocalesEnCodigo(producto.getCodigo());
				
		try{
			//act
			vendedor.generarMensaje(CANTIDAD_VOCALES);
			fail(MENSAJE);
			
		}catch(GarantiaExtendidaException e){
			//assert
			assertTrue(e.getMessage().equals(MENSAJE_EXCEPCION));
		}
	}
}
