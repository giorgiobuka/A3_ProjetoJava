import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SistemaLinearGUI extends JFrame {

    private JTextField[][] coeficientes;
    private JTextField[] termosIndependentes;
    private JTextArea resultadoArea;
    private int tamanho;

    public SistemaLinearGUI() {
        setTitle("Resolução de Sistema Linear");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        solicitarTamanhoSistema();
    }

    private void solicitarTamanhoSistema() {
        String input = JOptionPane.showInputDialog("Quantas variáveis?");
        try {
            tamanho = Integer.parseInt(input);
            criarInterface();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
            System.exit(0);
        }
    }

    private void criarInterface() {
        JPanel inputPanel = new JPanel(new GridLayout(tamanho, tamanho + 1));
        coeficientes = new JTextField[tamanho][tamanho];
        termosIndependentes = new JTextField[tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                coeficientes[i][j] = new JTextField(3);
                inputPanel.add(coeficientes[i][j]);
            }
            termosIndependentes[i] = new JTextField(3);
            inputPanel.add(termosIndependentes[i]);
        }

        JButton resolverBtn = new JButton("Resolver");
        resolverBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resolverSistema();
            }
        });

        resultadoArea = new JTextArea(8, 40);
        resultadoArea.setEditable(false);

        add(new JScrollPane(inputPanel), BorderLayout.NORTH);
        add(resolverBtn, BorderLayout.CENTER);
        add(new JScrollPane(resultadoArea), BorderLayout.SOUTH);

        setVisible(true);
    }

    private void resolverSistema() {
        double[][] A = new double[tamanho][tamanho];
        double[] B = new double[tamanho];

        try {
            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {
                    A[i][j] = Double.parseDouble(coeficientes[i][j].getText());
                }
                B[i] = Double.parseDouble(termosIndependentes[i].getText());
            }

            double[] solucao = eliminacaoDeGauss(A, B);
            StringBuilder resultado = new StringBuilder("Solução:\n");
            for (int i = 0; i < tamanho; i++) {
                resultado.append("x").append(i + 1).append(" = ").append(solucao[i]).append("\n");
            }
            resultadoArea.setText(resultado.toString());

        } catch (NumberFormatException e) {
            resultadoArea.setText("Erro: preencha todos os campos corretamente.");
        }
    }

    private double[] eliminacaoDeGauss(double[][] A, double[] B) {
        int n = B.length;

        for (int i = 0; i < n; i++) {
            double max = Math.abs(A[i][i]);
            int linhaMax = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(A[k][i]) > max) {
                    max = Math.abs(A[k][i]);
                    linhaMax = k;
                }
            }

            double[] temp = A[i];
            A[i] = A[linhaMax];
            A[linhaMax] = temp;

            double t = B[i];
            B[i] = B[linhaMax];
            B[linhaMax] = t;

            for (int k = i + 1; k < n; k++) {
                double fator = A[k][i] / A[i][i];
                B[k] -= fator * B[i];
                for (int j = i; j < n; j++) {
                    A[k][j] -= fator * A[i][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = B[i];
            for (int j = i + 1; j < n; j++) {
                x[i] -= A[i][j] * x[j];
            }
            x[i] /= A[i][i];
        }

        return x;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaLinearGUI());
    }
}
