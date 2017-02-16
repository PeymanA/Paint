package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	// all buttons declaration
	private JPanel buttonPanel;
	private JButton exitButton, lineButton, rectangleButton, circleButton,
			colorButton, deleteAllButton, selectButton;

	public static Graphics2D graph;
	public static int action = 2;
	public static boolean select = false;
	public static Color color = Color.black;

	public static void runMain() {
		new MainWindow();
	}

	public MainWindow() {

		JFrame frame = this;

		frame.setSize(800, 600);
		frame.setTitle("Paint");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		buttonPanel = new JPanel();
		Box box = Box.createHorizontalBox();

		// setup all buttons
		lineButton = setupButtons(2, "خط");
		rectangleButton = setupButtons(3, "دایره");
		circleButton = setupButtons(4, "مستطیل");
		colorButton = setupColorButton(5, "انتخاب رنگ");

		exitButton = new JButton();
		exitButton.setText("خروج");
		// this action listener closes the paint panel and opens login page
		// again
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DrawingBoard.shapes.removeAll(DrawingBoard.shapes);
				DrawingBoard.colors.removeAll(DrawingBoard.colors);
				Login.run();
				frame.dispose();
			}
		});
		
		deleteAllButton=new JButton();
		deleteAllButton.setText("حذف همه");
		deleteAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DrawingBoard.shapes.removeAll(DrawingBoard.shapes);
				DrawingBoard.colors.removeAll(DrawingBoard.colors);
				EntityManager.deleteAll();
				repaint();
			}
		});
		
		selectButton=new JButton();
		selectButton.setText("انتخاب");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select=!select;
			}
		});

		// add buttons to the box
		box.add(exitButton);
		box.add(lineButton);
		box.add(rectangleButton);
		box.add(circleButton);
		box.add(colorButton);
		box.add(deleteAllButton);
		box.add(selectButton);

		// add button box to the bottom panel which is the buttonPanel
		buttonPanel.add(box);
		// add drawing board and button panel to the frame
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		// drawingBoard.setBackground(Color.WHITE);
		DrawingBoard drawingBoard = new DrawingBoard();
		drawingBoard.setBackground(Color.WHITE);
		getContentPane().add(drawingBoard, BorderLayout.CENTER);

		this.setVisible(true);

	}

	// setup paint buttons; gives every button a specific action listener depend
	// on their action
	private JButton setupButtons(final int actionNumber, String text) {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = actionNumber;
			}
		});
		button.setText(text);
		return button;
	}

	// setup color button; i use swing default color chooser which gives user a
	// lot of options
	private JButton setupColorButton(final int actionNumber, String text) {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				color = JColorChooser.showDialog(null, "Pick a Color",
						Color.BLACK);
			}
		});
		button.setText(text);
		return button;
	}

}
