import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main extends JFrame implements ActionListener {
    JTextField disp;

    DefaultListModel<String> hist = new DefaultListModel<>();
    JList<String> histList = new JList<>(hist);
    double a = 0, mem = 0;
    String op = "";
    boolean newNum = true;

    public Main() {
        setTitle("Pink Calc");
        setSize(500, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        disp = new JTextField("0");
        disp.setHorizontalAlignment(JTextField.RIGHT);
        disp.setFont(new Font("SansSerif", Font.BOLD, 28));
        disp.setEditable(false);
        disp.setBackground(new Color(255, 225, 235));
        add(disp, BorderLayout.NORTH);

        JPanel g = new JPanel(new GridLayout(6,5,5,5));
        g.setBackground(new Color(255,182,193));
        String[] keys = {
                "C","±","%","÷","√",
                "7","8","9","×","^",
                "4","5","6","-","!",
                "1","2","3","+","M+",
                "0",".","←","=","M-",
                "MC","MR","","",""
        };
        for (String k: keys) {
            if (k.isEmpty()) { g.add(new JLabel()); continue; }
            JButton b = new JButton(k);
            b.addActionListener(this);
            b.setBackground(new Color(255,200,210));
            g.add(b);
        }
        add(g, BorderLayout.CENTER);

        JScrollPane sp = new JScrollPane(histList);
        sp.setPreferredSize(new Dimension(150,0));
        add(sp, BorderLayout.EAST);

        setVisible(true);
    }

    double val() { return Double.parseDouble(disp.getText()); }
    void show(double v) { disp.setText((v==(long)v)?""+(long)v:""+v); }

    public void actionPerformed(ActionEvent e) {
        String k = ((JButton)e.getSource()).getText();

        if (k.matches("[0-9]"))
        {
            if (newNum || disp.getText().equals("0")) disp.setText(k);
            else disp.setText(disp.getText()+k);
            newNum = false;
            return;
        }

        if (k.equals("."))
        {
            if (!disp.getText().contains(".")) disp.setText(disp.getText()+".");
            newNum = false;
            return;
        }

        if (k.equals("C"))
        {
            disp.setText("0");
            a = 0;
            op = "";
            newNum = true;
            return;
        }

        if (k.equals("←"))
        {
            String s = disp.getText();
            if (s.length() > 1)
                disp.setText(s.substring(0, s.length()-1));
            else
                disp.setText("0");
            return;
        }

        if (k.equals("±"))
        {
            if (disp.getText().startsWith("-"))
                disp.setText(disp.getText().substring(1));
            else
                disp.setText("-" + disp.getText());
            return;
        }

        if (k.equals("%"))
        {
            show(val()/100);
            newNum = true;
            return;
        }

        if (k.equals("√"))
        {
            show(Math.sqrt(val()));
            hist.addElement("√ = " + disp.getText());
            newNum = true;
            return;
        }

        if (k.equals("!"))
        {
            int n = (int)val();
            double r = 1;
            for (int i=2; i<=n; i++) r *= i;
            show(r);
            hist.addElement(n + "! = " + disp.getText());
            newNum = true;
            return;
        }

        if (k.equals("M+"))
        {
            mem += val();
            return;
        }

        if (k.equals("M-"))
        {
            mem -= val();
            return;
        }

        if (k.equals("MC"))
        {
            mem = 0;
            return;
        }

        if (k.equals("MR"))
        {
            show(mem);
            newNum = true;
            return;
        }

        if ("÷×-+^".contains(k))
        {
            a = val();
            op = k;
            newNum = true;
            return;
        }

        if (k.equals("="))
        {
            double b = val(), r = a;
            switch(op)
            {
                case "+": r = a + b; break;
                case "-": r = a - b; break;
                case "×": r = a * b; break;
                case "÷":
                {
                    if (b == 0)
                        r = Double.NaN;
                    else
                        r = a / b;
                    break;
                }
                case "^": r = Math.pow(a, b); break;
            }
            show(r);
            hist.addElement(a + " " + op + " " + b + " = " + disp.getText());
            newNum=true;
            op="";
            return;
        }
    }


    public static void main(String[] args) {
        new Main();
    }
}
