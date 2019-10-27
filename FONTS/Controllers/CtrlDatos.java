package Controllers;

public class CtrlDatos {
    private static CtrlDatos instance = null;

    public static CtrlDatos getInstance()
    {
        if (instance == null)
            instance = new CtrlDatos();
        return instance;
    }

    private CtrlDatos() {
    }

    public void inicializarCtrlDatos() {
    }
}
