import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class Main extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    // Панель отрисовки столбиков
    static class BarsPanel extends JPanel {
        int[] arr;
        int highlightA = -1; // индекс левого соседа
        int highlightB = -1; // индекс правого соседа

        BarsPanel(int[] arr) {
            this.arr = arr;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (arr == null || arr.length == 0) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int n = arr.length;

            // Вычисляем ширину столбиков и отступы
            int barWidth = Math.max(2, width / Math.max(1, n));
            int maxVal = 1;
            for (int v : arr) maxVal = Math.max(maxVal, v);

            // Рисуем ось X как справочный фон
            g2.setColor(new Color(240, 240, 240));
            g2.fillRect(0, height - 20, width, 20);

            // Рисуем каждый столбик
            for (int i = 0; i < n; i++) {
                int val = arr[i];
                // Высота в пикселях пропорциональна значению
                int barHeight = (int) ((height - 40) * (val / (double) maxVal));
                int x = i * barWidth;
                int y = height - 20 - barHeight;

                // Цвет по умолчанию
                Color color = new Color(70, 130, 180); // steel blue
                // Подсветка пары соседей, которую сравниваем
                if (i == highlightA || i == highlightB) {
                    color = new Color(220, 20, 60); // crimson
                }

                g2.setColor(color);
                g2.fillRect(x + 1, y, barWidth - 2, barHeight);

                // Тонкая обводка
                g2.setColor(Color.DARK_GRAY);
                g2.drawRect(x + 1, y, barWidth - 2, barHeight);
            }
        }

        void setHighlights(int a, int b) {
            this.highlightA = a;
            this.highlightB = b;
            repaint();
        }

        void clearHighlights() {
            setHighlights(-1, -1);
        }
    }

    private final BarsPanel barsPanel;
    private int[] arr;
    private final JButton startBtn = new JButton("Старт");
    private final JButton resetBtn = new JButton("Сброс");
    private final JSlider speedSlider = new JSlider(1, 100, 30);

    // Состояние пузырьковой сортировки (индексы внешнего и внутреннего циклов)
    private int outerI = 0;
    private int innerJ = 0;
    private Timer timer;

    public Main() {
        super("Визуализация сортировки (пузырёк, явная проверка соседей)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        arr = makeRandomArray(60, 10, 100); // 60 столбиков, значения 10..100
        barsPanel = new BarsPanel(arr);

        JPanel controls = buildControls();

        setLayout(new BorderLayout());
        add(barsPanel, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        setupTimer();
        setVisible(true);
    }

    private JPanel buildControls() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speedSlider.setPreferredSize(new Dimension(200, 40));
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setMajorTickSpacing(25);
        speedSlider.setMinorTickSpacing(5);

        startBtn.addActionListener(this::onStart);
        resetBtn.addActionListener(this::onReset);

        p.add(new JLabel("Скорость:"));
        p.add(speedSlider);
        p.add(startBtn);
        p.add(resetBtn);
        return p;
    }private void setupTimer() {
    // Timer управляет шагами сортировки; задержка = обратная скорость
    timer = new Timer(delayFromSlider(), e -> stepBubbleSort());
}

private int delayFromSlider() {
    // Чем больше значение на слайдере, тем меньше задержка
    int s = speedSlider.getValue(); // 1..100
    return Math.max(5, 300 - (s * 2)); // диапазон примерно 5..300 мс
}

private void onStart(ActionEvent e) {
    if (timer.isRunning()) {
        // Пауза
        timer.stop();
        startBtn.setText("Старт");
    } else {
        // Перезаписываем задержку при изменении скорости
        timer.setDelay(delayFromSlider());
        timer.start();
        startBtn.setText("Пауза");
    }
}

private void onReset(ActionEvent e) {
    timer.stop();
    startBtn.setText("Старт");
    arr = makeRandomArray(60, 10, 100);
    barsPanel.arr = arr;
    barsPanel.clearHighlights();
    outerI = 0;
    innerJ = 0;
    barsPanel.repaint();
}

private int[] makeRandomArray(int n, int minVal, int maxVal) {
    Random rnd = new Random();
    int[] a = new int[n];
    for (int i = 0; i < n; i++) {
        a[i] = rnd.nextInt(maxVal - minVal + 1) + minVal;
    }
    return a;
}

// Один пошаговый шаг пузырьковой сортировки с явной проверкой соседей
private void stepBubbleSort() {
    int n = arr.length;

    // Если внешний цикл завершён — массив отсортирован
    if (outerI >= n - 1) {
        barsPanel.clearHighlights();
        timer.stop();
        startBtn.setText("Старт");
        return;
    }

    // Внутренний цикл: проверяем соседние элементы arr[j] и arr[j+1]
    if (innerJ < n - 1 - outerI) {
        int aIdx = innerJ;
        int bIdx = innerJ + 1;

        // Подсветка пары соседей
        barsPanel.setHighlights(aIdx, bIdx);

        int left = arr[aIdx];
        int right = arr[bIdx];

        // ЯВНАЯ проверка соседей: если левый больше правого — меняем местами
        if (left > right) {
            arr[aIdx] = right;
            arr[bIdx] = left;
        }

        innerJ++;
        barsPanel.repaint();
    } else {
        // Завершили проход: самый большой элемент "всплыл" вправо
        innerJ = 0;
        outerI++;
    }
}
}