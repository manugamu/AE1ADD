package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import java.text.Normalizer;

public class Interfaz {

    private JFrame frmAe1AccesoADatos;
    private JTextField textFieldRuta;
    private JButton btnBuscar;
    private JLabel lblIntroduceRuta;
    private JTextArea txtAreaResultado;
    private JTextField textFieldCoincidencias;
    private JLabel lblConCoincidenciasCon;
    private JCheckBox chkMajuscules;
    private JCheckBox chkAccents;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Interfaz window = new Interfaz();
                    window.frmAe1AccesoADatos.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Interfaz() {
        initialize();
    }

    private void initialize() {
        frmAe1AccesoADatos = new JFrame();
        frmAe1AccesoADatos.setTitle("AE1 ACCESO A DATOS");
        frmAe1AccesoADatos.getContentPane().setBackground(new Color(152, 251, 152));
        frmAe1AccesoADatos.setBounds(100, 100, 908, 721);
        frmAe1AccesoADatos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmAe1AccesoADatos.getContentPane().setLayout(null);

        lblIntroduceRuta = new JLabel("Intrueix la ruta:");
        lblIntroduceRuta.setFont(new Font("Tahoma", Font.PLAIN, 22));
        lblIntroduceRuta.setBounds(26, 10, 204, 46);
        frmAe1AccesoADatos.getContentPane().add(lblIntroduceRuta);

        textFieldRuta = new JTextField();
        textFieldRuta.setFont(new Font("Tahoma", Font.PLAIN, 13));
        textFieldRuta.setBounds(211, 11, 378, 53);
        frmAe1AccesoADatos.getContentPane().add(textFieldRuta);
        textFieldRuta.setColumns(10);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnBuscar.setBounds(619, 11, 171, 53);
        frmAe1AccesoADatos.getContentPane().add(btnBuscar);
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarDirectoris();
            }
        });

        lblConCoincidenciasCon = new JLabel("Amb Coincidencies amb:");
        lblConCoincidenciasCon.setFont(new Font("Tahoma", Font.PLAIN, 22));
        lblConCoincidenciasCon.setBounds(26, 74, 250, 46);
        frmAe1AccesoADatos.getContentPane().add(lblConCoincidenciasCon);

        textFieldCoincidencias = new JTextField();
        textFieldCoincidencias.setFont(new Font("Tahoma", Font.PLAIN, 13));
        textFieldCoincidencias.setColumns(10);
        textFieldCoincidencias.setBounds(286, 74, 304, 53);
        frmAe1AccesoADatos.getContentPane().add(textFieldCoincidencias);

        txtAreaResultado = new JTextArea();
        txtAreaResultado.setLineWrap(true);
        txtAreaResultado.setFont(new Font("Monospaced", Font.BOLD, 13));
        txtAreaResultado.setForeground(new Color(0, 0, 0));
        txtAreaResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaResultado);
        scrollPane.setBounds(53, 157, 795, 480);
        frmAe1AccesoADatos.getContentPane().add(scrollPane);
        
        chkMajuscules = new JCheckBox("Busqueda Majuscules Estrictes");
        chkMajuscules.setFont(new Font("Tahoma", Font.PLAIN, 14));
        chkMajuscules.setBackground(new Color(152, 251, 152));
        chkMajuscules.setBounds(619, 98, 259, 39);
        frmAe1AccesoADatos.getContentPane().add(chkMajuscules);
        
        chkAccents = new JCheckBox("Busqueda Accents");
        chkAccents.setFont(new Font("Tahoma", Font.PLAIN, 14));
        chkAccents.setBackground(new Color(152, 251, 152));
        chkAccents.setBounds(619, 67, 194, 32);
        frmAe1AccesoADatos.getContentPane().add(chkAccents);
        
    }

    /**
     * Realitza la busca de directoris i arxius en la ruta proporcionada i compta les coincidencies si es proporciona un string de busqueda.
     */
    /**
     * Realitza la busca de directoris i arxius en la ruta proporcionada i compta les coincidencies si es proporciona un string de busqueda.
     */
    private void buscarDirectoris() {
        String ruta = textFieldRuta.getText().trim();
        String coincidencia = textFieldCoincidencias.getText();
        boolean buscarMajuscules = chkMajuscules.isSelected();
        boolean buscarAccents = chkAccents.isSelected();

        if (ruta.isEmpty()) {
            JOptionPane.showMessageDialog(frmAe1AccesoADatos, "Per favor, introdueix una ruta valida.", "Ruta Buida", JOptionPane.DEFAULT_OPTION);
            return;
        }

        File directori = new File(ruta);

        if (!directori.exists()) {
            JOptionPane.showMessageDialog(frmAe1AccesoADatos, "La ruta proporcionada no existeix.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!directori.isDirectory()) {
            JOptionPane.showMessageDialog(frmAe1AccesoADatos, "La ruta no es un directori.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        txtAreaResultado.setText("");

        String resultat;

        if (coincidencia.isEmpty()) {
            resultat = recorrerDirectoris(directori, 0, null, buscarMajuscules, buscarAccents);
        } else {
            resultat = recorrerDirectoris(directori, 0, coincidencia, buscarMajuscules, buscarAccents);
        }
        txtAreaResultado.setText(resultat);
    }

    /**
     * Recorre recursivament els directoris i arxius, i compta les coincidències si se li dona un parametre de busqueda.
     *
     * @param directori:  El directori a recorrer.
     * @param nivell:  El nivell de indentació.
     * @param coincidencia:  String amb la busqueda (pot ser null).
     * @param buscarMayusculas: Indica si se debe buscar con mayúsculas.
     * @param buscarAccents: Indica si se debe buscar con acentos.
     * @return : un String amb la estructura del directori i els arxius trobats (amb o sense coincidencies).
     */
    private String recorrerDirectoris(File directori, int nivell, String coincidencia, boolean buscarMajuscules, boolean buscarAccents) {
        StringBuilder resultat = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Indentacio pera el nivell actual
        for (int i = 0; i < nivell; i++) {
            resultat.append("|   ");
        }

        // Afexir el nom del directori
        if (nivell == 0) {
            resultat.append(directori.getAbsolutePath()).append("\n");
        } else {
            resultat.append("|-- \\").append(directori.getName()).append("\n");
        }

        File[] arxius = directori.listFiles();

        if (arxius != null) {
            for (File arxiu : arxius) {
                if (arxiu.isDirectory()) {
                    // Crida recursiva pera subdirectoris del directori principal.
                    resultat.append(recorrerDirectoris(arxiu, nivell + 1, coincidencia, buscarMajuscules, buscarAccents));
                } else {
                    // Indentacio
                    StringBuilder indentacio = new StringBuilder();
                    for (int i = 0; i < nivell + 1; i++) {
                        indentacio.append("|   ");
                    }

                    // Compta coincidencies si les troba
                    int numCoincidencies = 0;
                    if (coincidencia != null) {
                        numCoincidencies = contarCoincidencies(arxiu, coincidencia, buscarMajuscules, buscarAccents);
                    }

                    // Construieix la llinea de l'arxiu.
                    resultat.append(indentacio)
                             .append("|-- ").append(arxiu.getName())
                             .append(" (").append(obtindreTamany(arxiu.length()))
                             .append(" – ").append(format.format(arxiu.lastModified()))
                             .append(")");

                    if (coincidencia != null) {
                        resultat.append(" - ").append(numCoincidencies)
                                 .append(numCoincidencies == 1 ? " coincidencia" : " coincidencies");
                    }

                    resultat.append("\n");
                }
            }
        }

        return resultat.toString();
    }

    /**
     * Compta les coincidències en un arxiu donat.
     *
     * @param arxiu: El fitxer on es fa la cerca de coincidències.
     * @param coincidencia: El string amb la coincidència a buscar.
     * @param buscarMayusculas: Indica si se debe buscar con mayúsculas.
     * @param buscarAccents: Indica si se debe buscar con acentos.
     * @return: El nombre de coincidències trobades.
     */
    private int contarCoincidencies(File arxiu, String coincidencia, boolean buscarMajuscules, boolean buscarAccents) {
        int comptador = 0;

        // Normalitzar la coincidencia per fer la comparació sense accents i majuscules si es necessari
        String coincidenciaNorm = coincidencia;
        if (!buscarAccents) {
            coincidenciaNorm = Normalizer.normalize(coincidencia, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        }
        if (!buscarMajuscules) {
            coincidenciaNorm = coincidenciaNorm.toLowerCase();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arxiu))) {
            String llinia;
            while ((llinia = br.readLine()) != null) {
                String llineaNorm = llinia;
                // Normalitzacio de la llinia llegida
                if (!buscarAccents) {
                    llineaNorm = Normalizer.normalize(llinia, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                }
                if (!buscarMajuscules) {
                    llineaNorm = llineaNorm.toLowerCase();
                }

                int i = 0;
                // Busca coincidencies en cada lliiea
                while ((i = llineaNorm.indexOf(coincidenciaNorm, i)) != -1) {
                    comptador++;
                    i += coincidenciaNorm.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return comptador;
    }

    /**
     * Obtindre el tamany de l'arxiu en una cadena comprensible.
     *
     * @param tamany: El tamany de l'arxiu en bytes.
     * @return: Tamany formatat com a string.
     */
    private String obtindreTamany(long tamany) {
        if (tamany < 1024) {
            return tamany + " bytes";
        } else if (tamany < 1048576) {
            return tamany / 1024 + " KB";
        } else {
            return tamany / 1048576 + " MB";
        }
    }
}
