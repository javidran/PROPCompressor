import Controllers.CtrlPresentacion;

public class PROPresor {
    public static void main(String[] args) {
        CtrlPresentacion ctrlPresentacion = CtrlPresentacion.getInstance();
        ctrlPresentacion.crearVistaInicio();
        ctrlPresentacion.crearModeloParametros();
    }
}
