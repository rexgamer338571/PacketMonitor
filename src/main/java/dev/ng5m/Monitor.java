package dev.ng5m;

import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class Monitor {
    private final JFrame frame;
    private final JPanel scrollablePanel;
    private final JScrollPane scrollPane;
    private final JTextArea mainFormField;
    private final JPanel mainFormPanel;

    private static final Color BG_COLOR = new Color(43, 43, 43);
    private static final Color FG_COLOR = new Color(187, 187, 187);
    private static final Color BB_COLOR = new Color(60, 63, 65);

    public Monitor() {
        frame = new JFrame("Packet Monitor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(600, 300));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);

                PluginMain.MONITORS.remove(Monitor.this);
                PluginMain.PACKETS.clear();
            }
        });

        frame.getContentPane().setBackground(BG_COLOR);

        scrollablePanel = new JPanel();
        scrollablePanel.setLayout(new BoxLayout(scrollablePanel, BoxLayout.Y_AXIS));
        scrollablePanel.setBackground(BG_COLOR);

        scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(BG_COLOR));
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.getVerticalScrollBar().setBackground(BG_COLOR);
        scrollPane.getVerticalScrollBar().setForeground(FG_COLOR);

        mainFormPanel = new JPanel();
        mainFormPanel.setLayout(new GridLayout(1, 1, 5, 5));
        mainFormPanel.setBackground(BG_COLOR);

        mainFormField = new JTextArea();
        mainFormField.setEditable(false);
        mainFormField.setFont(new Font("Calibri", Font.PLAIN, 30));
        mainFormField.setBackground(BG_COLOR);
        mainFormField.setForeground(FG_COLOR);
        mainFormField.setCaretColor(FG_COLOR);

        mainFormPanel.add(mainFormField);

        frame.add(scrollPane, BorderLayout.WEST);
        frame.add(mainFormPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public void notifyMonitor() {
        scrollablePanel.removeAll();

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        int value = verticalScrollBar.getValue();
        value = Math.min(value, verticalScrollBar.getMaximum());

        for (PacketWrapper packetWrapper : PluginMain.PACKETS) {
            JButton button = jButton(packetWrapper);

            scrollablePanel.add(button);
            scrollablePanel.revalidate();
            scrollablePanel.repaint();

            int finalValue = value;
            SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(finalValue));
        }
    }

    @NotNull
    private JButton jButton(PacketWrapper packetWrapper) {
        Object packet = packetWrapper.packet();

        String[] s = packet.getClass().getName().split("\\.");
        String name = s[s.length - 1].replace("Packet", "");

        JButton button = new JButton();
        button.setText(name);
        button.setBackground(BB_COLOR);
        button.setForeground(FG_COLOR);
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(name + "\n\nPlayer: " + packetWrapper.player() + "\n\nData: \n");
            for (var key : packetWrapper.data().keySet()) {
                sb.append(key).append(": ").append(packetWrapper.data().get(key)).append("\n");
            }
            if (!packetWrapper.superData().keySet().isEmpty()) {
                sb.append("\nSuper:\n");

                for (var superKey : packetWrapper.superData().keySet()) {
                    sb.append(superKey).append(": ").append(packetWrapper.superData().get(superKey)).append("\n");
                }
            }

            for (Component component : mainFormPanel.getComponents()) {
                if (component instanceof JButton)
                    mainFormPanel.remove(component);
            }

            mainFormField.setText(sb.toString());
        });
        return button;
    }

    private void updateMainForm(String elementName) {
        mainFormField.setText(elementName);
    }
}
