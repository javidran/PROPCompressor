package DomainLayer.Proceso;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatosProcesoTest {
    private DatosProceso datosProceso;
    private long time;
    private long newSize;
    private long oldSize;
    private boolean esCompresion;

    @Before
    public void setUp() {
        time = 230412345;
        oldSize = 9482648;
        newSize = 5132492;
        esCompresion = true;
    }

    @Test
    public void testGetDiffSize() {
        datosProceso = new DatosProceso(time, oldSize, newSize, esCompresion);
        assertEquals(4350156, datosProceso.getDiffSize());
    }

    @Test
    public void testGetDiffSizeFailure() {
        newSize = 9482648;
        oldSize = 5872901;
        datosProceso = new DatosProceso(time, oldSize, newSize, esCompresion);
        assertEquals(-3609747, datosProceso.getDiffSize());
    }

    @Test
    public void testGetDiffSizeDecompress() {
        newSize = 9482648;
        oldSize = 5872901;
        datosProceso = new DatosProceso(time, oldSize, newSize, false);
        assertEquals(3609747, datosProceso.getDiffSize());
    }

    @Test
    public void testGetDiffSizeDecompressFailure() {
        oldSize = 9482648;
        newSize = 5872901;
        datosProceso = new DatosProceso(time, oldSize, newSize, false);
        assertEquals(-3609747, datosProceso.getDiffSize());
    }

    @Test
    public void testGetDiffSizePercentage() {
        datosProceso = new DatosProceso(time, oldSize, newSize, esCompresion);
        assertEquals(54.0, datosProceso.getDiffSizePercentage(), 0.0);
    }

    @Test
    public void testGetDiffSizePercentageFailure() {
        datosProceso = new DatosProceso(time, newSize, oldSize, esCompresion);
        assertEquals(184.0, datosProceso.getDiffSizePercentage(), 0.0);
    }

    @Test
    public void testGetDiffSizePercentageDecompress() {
        datosProceso = new DatosProceso(time, newSize, oldSize, false);
        assertEquals(54.0, datosProceso.getDiffSizePercentage(), 0.0);
    }

    @Test
    public void testGetDiffSizePercentageDecompressFailure() {
        datosProceso = new DatosProceso(time, oldSize, newSize, false);
        assertEquals(184.0, datosProceso.getDiffSizePercentage(), 0.0);
    }

    @Test
    public void testGetTiempo() {
        datosProceso = new DatosProceso(time, oldSize, newSize, esCompresion);
        assertEquals(time, datosProceso.getTiempo());
    }

    @Test
    public void testGetNewSize() {
        datosProceso = new DatosProceso(time, oldSize, newSize, esCompresion);
        assertEquals(newSize, datosProceso.getNewSize());
    }

    @Test
    public void testGetOldSize() {
        datosProceso = new DatosProceso(time, oldSize, newSize, esCompresion);
        assertEquals(oldSize, datosProceso.getOldSize());
    }

    @Test
    public void testIsSatisfactorio() {
        datosProceso = new DatosProceso(time, oldSize, newSize, esCompresion);
        assertTrue(datosProceso.isSatisfactorio());
    }

    @Test
    public void testIsSatisfactorioFailure() {
        datosProceso = new DatosProceso(time, newSize, oldSize, esCompresion);
        assertFalse(datosProceso.isSatisfactorio());
    }

    @Test
    public void testIsSatisfactorioDecompress() {
        datosProceso = new DatosProceso(time, newSize, oldSize, false);
        assertTrue(datosProceso.isSatisfactorio());
    }

    @Test
    public void testIsSatisfactorioDecompressFailure() {
        datosProceso = new DatosProceso(time, oldSize, newSize, false);
        assertFalse(datosProceso.isSatisfactorio());
    }
}