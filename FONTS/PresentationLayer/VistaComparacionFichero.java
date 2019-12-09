package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Clase para la implementación de la vista que permite comparar un fichero original con el resultante de aplicar la comprsesión y descompresión con un algoritmo determinado.
 */
public class VistaComparacionFichero extends JDialog {
    public JTable original;

    /**
     * Botón para finalizar la visualización de la vista
     */
    private JButton finalizarButton;
    private JPanel panel;
    private JLabel tiempoC;
    private JLabel OldSizeC;
    private JLabel NewSizeC;
    private JLabel DiffSizeC;
    private JLabel RatioC;
    private JLabel tiempoD;
    private JLabel OldSizeD;
    private JLabel NewSizeD;
    private JLabel DiffSizeD;
    private JLabel RatioD;
    private JScrollPane originalPanel;
    private JScrollPane resultadoPanel;
    public JTable resultado;
    private JLabel velocidadC;
    private JLabel velocidadD;

    /**
     * Constructora de la vista para la comparación de un fichero con ese mismo procesado.
     * <p>
     *     Esta vista no solo permite la visualización de ambos textos sinó que tambien da al usuario datos del proceso de compresión y descompresión
     * </p>
     * @param owner vista propietaria de esta nueva vista
     * @param dp array que contiene los datos de la compresión y la descompresión del archivo mostrado
     */
    public VistaComparacionFichero(Frame owner, DatosProceso[] dp){
        super (owner, "Comparar textos",true);
        setContentPane(panel);
        originalPanel.getVerticalScrollBar().setUnitIncrement(16);
        originalPanel.getHorizontalScrollBar().setUnitIncrement(16);
        resultadoPanel.getVerticalScrollBar().setUnitIncrement(16);
        resultadoPanel.getHorizontalScrollBar().setUnitIncrement(16);

        DecimalFormat df = new DecimalFormat("#.####");
        tiempoC.setText(df.format((double)dp[0].getTiempo() / 1000000000.0));
        OldSizeC.setText(Long.toString(dp[0].getOldSize()));
        NewSizeC.setText(Long.toString(dp[0].getNewSize()));
        DiffSizeC.setText(Long.toString(dp[0].getDiffSize()));
        RatioC.setText(Double.toString(dp[0].getDiffSizePercentage()));
        velocidadC.setText(df.format((double)dp[0].getVelocidad() * 1000000000.0));

        tiempoD.setText(df.format((double)dp[1].getTiempo() / 1000000000.0));
        OldSizeD.setText(Long.toString(dp[1].getOldSize()));
        NewSizeD.setText(Long.toString(dp[1].getNewSize()));
        DiffSizeD.setText(Long.toString(dp[1].getDiffSize()));
        RatioD.setText(Double.toString(dp[1].getDiffSizePercentage()));
        velocidadD.setText(df.format((double)dp[1].getVelocidad() * 1000000000.0));

        finalizarButton.addActionListener(e -> dispose());
    }

//    public void aplicaTextoOriginal(TableModel model) {
//        original.setModel(model);
//        TableColumn tc = original.getColumnModel().getColumn(0);
//        tc.setPreferredWidth(getWidestLine(model));
//    }

    public void aplicaTextoOriginal(TableModel model) {
        original.setModel(model);
        int row = getWidestLine(model);
        TableModel tm = original.getModel();
        TableColumn tc = original.getColumnModel().getColumn(0);
        TableCellRenderer tcr = original.getDefaultRenderer(tm.getColumnClass(0));
        Component c = tcr.getTableCellRendererComponent(original,tm.getValueAt(row,0),false,false,row,0);
        tc.setPreferredWidth(c.getPreferredSize().width);
    }

    public void aplicaTextoResultante(TableModel model) {
        resultado.setModel(model);
        TableColumn tc = resultado.getColumnModel().getColumn(0);
        tc.setPreferredWidth(getWidestLine(model));
    }

    private int getWidestLine(TableModel tableModel) {
        int max = 0;
        int row = 0;
        for(int i=0; i<tableModel.getRowCount(); ++i) {
            String line = (String) tableModel.getValueAt(i, 0);
            if(max < line.length()) {
                max = line.length();
                row = i;
            }
        }
        return row;
    }
}
