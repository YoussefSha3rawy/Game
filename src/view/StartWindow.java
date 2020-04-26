package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
@SuppressWarnings("serial")
public class StartWindow extends JFrame {
	private JLabel title;
	private JPanel main;
	private JTextArea info;
	private JButton start;

	public StartWindow() {
		this.setTitle("Game of Disasters");
		this.setBounds(200, 20, 1000, 1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		title = new JLabel();
		title.setText("GAME OF DISASTERS");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.CENTER);
		Font titleFont = new Font(Font.SERIF, Font.BOLD, 80);
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 18);
		title.setFont(titleFont);
		title.setForeground(Color.RED);
		ImageIcon startIcon=new ImageIcon("play.png");
		start = new JButton("Start Game");
		start.setFont(font);
		start.setForeground(Color.WHITE);
		start.setBackground(Color.WHITE);
		start.setBorderPainted(false);
		start.setIcon(startIcon);
		start.setPreferredSize(new Dimension(300,100));
		start.setVerticalTextPosition(SwingConstants.TOP);
		start.setHorizontalTextPosition(SwingConstants.CENTER);
		info = new JTextArea();
		info.setEditable(false);
		info.setPreferredSize(new Dimension(300,250));
		info.setBackground(Color.WHITE);
		info.setText("GAME RULES: "+"\n"+"A series of disasters will be executed in different"+"\n"+ "cycles of the game." + "\n"
				+ "Your target is to try and"+"\n"+ "reach the end of the game"+"\n"+ "with the least number of"+"\n"+ "casualties");
		info.setLineWrap(true);
		info.setFont(font);
		info.setForeground(Color.ORANGE);
		main=new JPanel();
		main.setBackground(Color.WHITE);
		main.setLayout(new FlowLayout());
		main.add(start);
		main.add(info);
		this.setLayout(new GridLayout(0, 1));
		this.add(title);
		this.add(main);
		this.setVisible(true);
	}

	public JButton getStart() {
		return start;
	}
}
