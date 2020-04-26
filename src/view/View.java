package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class View extends JFrame {
	private JPanel middle;
	private JPanel bottom;
	private JPanel left;
	private JPanel grid;

	private JPanel allUnits;
	private JPanel activeUnits;
	private JScrollPane logs;
	private JScrollPane activeDisasterss;
	private JScrollPane infos;
	private JTextArea log;
	// private JTextArea title;
	private JLabel generalInfo;
	private JTextArea activeDisasters;
	private int cycle;
	private JTextArea info;
	private JButton[][] buttons;
	private JPanel mainButtons;

	public View() {
		// Designing the JFrame
		buttons = new JButton[10][10];
		this.setTitle("Game of Disasters");
		this.setSize(1000, 1000);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		this.getContentPane().setLayout(new BorderLayout());

		// The Content
		this.middle = new JPanel();
		this.bottom = new JPanel();
		this.bottom.setPreferredSize(new Dimension(1000, 200));
		this.left = new JPanel();

		this.add(middle, BorderLayout.CENTER);
		this.add(left, BorderLayout.WEST);
		this.add(bottom, BorderLayout.SOUTH);

		this.middle.setLayout(new GridLayout(0, 1));
		this.bottom.setLayout(new GridLayout(1, 0));
		this.left.setLayout(new GridLayout(0, 1));

		// Middle/Rescue Panel
		this.grid = new JPanel();
		this.grid.setLayout(new GridLayout(10, 10));
		grid.setBackground(Color.BLACK);
		middle.add(grid);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JButton b = new JButton();
				if (i == 0 && j == 0) {

					ImageIcon base = new ImageIcon("base.png");
					b.setBackground(Color.BLACK);
					b.setForeground(Color.white);
					b.setIcon(base);
					b.setToolTipText("The Base");
					b.setBorderPainted(false);

				} else {
					ImageIcon icon = new ImageIcon("grid.png");
					b.setBackground(Color.BLACK);
					b.setIcon(icon);
					b.setForeground(Color.black);
					b.setBorderPainted(false);

				}
				grid.add(b);
				buttons[i][j] = b;
			}
		}
		// Left/Units Panel
		left.setBackground(Color.BLACK);
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 13);
		Font general = new Font(Font.MONOSPACED, Font.BOLD, 18);
		generalInfo = new JLabel();
		generalInfo.setFont(general);
		generalInfo.setForeground(Color.RED);
		generalInfo.setHorizontalAlignment(SwingConstants.CENTER);
		generalInfo.setVerticalAlignment(SwingConstants.CENTER);
		left.add(generalInfo);
		generalInfo.setBackground(Color.BLACK);
		JLabel av = new JLabel("Available Units:");
		av.setBackground(Color.BLACK);
		av.setFont(font);
		av.setForeground(Color.gray);
		left.add(av);
		allUnits = new JPanel();
		allUnits.setLayout(new GridLayout(0, 3));
		JScrollPane allUnitss=new JScrollPane(allUnits);
		left.add(allUnitss);
		JLabel ac = new JLabel("Active Units:");
		ac.setBackground(Color.BLACK);
		ac.setForeground(Color.GRAY);
		ac.setFont(font);
		left.add(ac);
		activeUnits = new JPanel();
		activeUnits.setLayout(new GridLayout(0, 3));
		JScrollPane activeUnitss=new JScrollPane(activeUnits);
		left.add(activeUnitss);
		allUnits.setForeground(Color.LIGHT_GRAY);
		allUnits.setBackground(Color.BLACK);
		activeUnits.setForeground(Color.LIGHT_GRAY);
		activeUnits.setBackground(Color.BLACK);
		/*allUnitsScroll=new JScrollPane(allUnits);
		activeUnitsScroll=new JScrollPane(activeUnits);*/
		
		// Bottom/Info Pane
		mainButtons = new JPanel();
		mainButtons.setLayout(new GridLayout(0, 1));
		log = new JTextArea();
		// generalInfo = new JTextArea();
		info = new JTextArea();
		activeDisasters = new JTextArea();
		infos = new JScrollPane(info);
		logs = new JScrollPane(log);
		activeDisasterss = new JScrollPane(activeDisasters);
		this.log.setEditable(false);
		// this.generalInfo.setEditable(false);
		this.activeDisasters.setEditable(false);
		this.info.setEditable(false);
		log.setForeground(Color.LIGHT_GRAY);
		log.setBackground(Color.BLACK);
		info.setForeground(Color.LIGHT_GRAY);
		info.setBackground(Color.BLACK);
		activeDisasters.setForeground(Color.LIGHT_GRAY);
		activeDisasters.setBackground(Color.BLACK);
		// bottom.add(generalInfo);
		bottom.add(infos);
		bottom.add(activeDisasterss);
		bottom.add(logs);
		bottom.add(mainButtons);
		log.setFont(font);
		info.setFont(font);
		activeDisasters.setFont(font);
	}

	public JPanel getMainButtons() {
		return mainButtons;
	}

	public JButton[][] getButtons() {
		return buttons;
	}

	public void addCell(JButton b, int r, int c) {
		this.grid.remove(10 * r + c);
		this.grid.add(b, 10 * r + c);
		buttons[r][c] = b;
	}

	public JPanel getLeft() {
		return left;
	}

	public void removeUnit(JButton b) {
		allUnits.remove(b);
	}

	public void addUnit(JButton b) {
		// b.setPreferredSize(new Dimension (60,60));
		allUnits.add(b);
	}

	public JPanel getBottom() {
		return bottom;
	}

	public void endGame(int casualties, int all) {
		updateActiveDisasters(new ArrayList<String>());
		updateActiveUnits(new ArrayList<JButton>());

		displayPopUp(("We're in the endgame now" + "\n" + "Casualties: "
				+ casualties + " Out of : " + all));
		this.emptyGrid();
		this.dispose();

	}

	public void emptyGrid() {
		this.grid.removeAll();
		this.grid.repaint();
		for (int i = 0; i < 100; i++) {
			JButton b = new JButton();
			ImageIcon icon = new ImageIcon("black.png");
			b.setIcon(icon);
			b.setBorderPainted(false);
			b.setBackground(Color.WHITE);
			b.repaint();
			this.grid.add(b);
			this.grid.repaint();
			this.grid.validate();

		}
		this.grid.repaint();
	}

	public void updateInfo(String s) {
		info.setText(s);
	}

	public JPanel getGrid() {
		return grid;
	}

	public void updateGeneralInfo(int casualties) {
		this.generalInfo.setText("Current cycle: " + ++cycle + "   "
				+ "Number of casualties: " + casualties);
	}

	public void updateLog(ArrayList<String> log) {
		String s = "Log: " + "\n" + "\n";
		for (int i = 0; i < log.size(); i++) {
			s += "-" + log.get(i) + "\n" + "\n";
		}
		this.log.setText(s);
	}

	public void updateActiveDisasters(ArrayList<String> active) {
		String s = "Active Disasters: " + "\n" + "\n";
		for (int i = 0; i < active.size(); i++) {
			s += "*" + active.get(i) + "\n" + "\n";
		}
		this.activeDisasters.setText(s);
	}

	public void updateActiveUnits(ArrayList<JButton> active) {
		activeUnits.removeAll();
		for (int i = 0; i < active.size(); i++) {
			this.activeUnits.add(active.get(i));
		}
	}

	public void displayPopUp(String s) {
		JOptionPane.showMessageDialog(null, s);
	}
}
