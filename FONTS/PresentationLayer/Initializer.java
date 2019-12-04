package PresentationLayer;

import Controllers.CtrlPresentacion;

public class Initializer {
    public static void main(String[] args) {
        CtrlPresentacion ctrlPresentacion = CtrlPresentacion.getInstance();
        ctrlPresentacion.crearVistaInicio();
        ctrlPresentacion.crearModeloParametros();
    }
}
