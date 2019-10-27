package Controllers;

public class CtrlProcesos {
    private static CtrlProcesos instance = null;

    public static CtrlProcesos getInstance()
    {
        if (instance == null)
            instance = new CtrlProcesos();

        return instance;
    }
}
