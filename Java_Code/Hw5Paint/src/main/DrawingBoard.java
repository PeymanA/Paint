package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class DrawingBoard extends JComponent {

	// list of colors and shapes
	public static ArrayList<Shape> shapes = new ArrayList<Shape>();
	public static ArrayList<Color> colors = new ArrayList<Color>();

	// start and end points
	private Point start;
	private Point end;

	public DrawingBoard() {

		// action listener to zoom
		this.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				
				int a = -e.getWheelRotation();
				ArrayList<Shape> temp = new ArrayList<Shape>();
				for (Shape s : shapes) {
					int startX = (int) s.getBounds2D().getMinX();
					int startY = (int) s.getBounds2D().getMinY();
					int endX = (int) s.getBounds2D().getMaxX();
					int endY = (int) s.getBounds2D().getMaxY();
					if (startX < e.getX())
						startX -= (e.getX() - startX) / 50 * a;
					else
						startX += (startX - e.getX()) / 50 * a;
					if (startY < e.getY())
						startY -= (e.getY() - startY) / 50 * a;
					else
						startY += (startY - e.getY()) / 50 * a;
					if (endX < e.getX())
						endX -= (e.getX() - endX) / 50 * a;
					else
						endX += (endX - e.getX()) / 50 * a;
					if (endY < e.getY())
						endY -= (e.getY() - endY) / 50 * a;
					else
						endY += (endY - e.getY()) / 50 * a;

					// System.out.println(s.getBounds2D().getX() + " " +
					// s.getBounds2D().getMinX() + " " +
					// s.getBounds2D().getMaxX());
					Shape shape = null;
					if (s.toString().substring(14, 18).equals("Line")) {
						shape = drawLine(startX, startY, endX, endY);
					} else

					if (s.toString().substring(14, 18).equals("Elli")) {
						shape = drawEllipse(startX, startY, endX, endY);
					} else

					if (s.toString().substring(14, 18).equals("Rect")) {
						shape = drawRectangle(startX, startY, endX, endY);
					}
					temp.add(shape);
					// System.out.println(s.toString().substring(14, 18)
					// + "\na = " + a);
				}
				shapes = temp;
				repaint();
			}
		});
		

		// action listener to get start points of the shape
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!MainWindow.select) {
					if (MainWindow.action != 1) {
						start = new Point(e.getX(), e.getY());
						end = start;
						repaint();
					}
				} else {
					changeColor(e.getPoint());
				}
			}

			// action listener to get end points of the shape
			public void mouseReleased(MouseEvent e) {
				if (!MainWindow.select) {
					Shape shape = null;

					if (MainWindow.action == 2) {
						// Create a new line using x & y coordinates
						shape = drawLine(start.x, start.y, e.getX(), e.getY());
					} else

					if (MainWindow.action == 3) {
						// Create a new circle using x & y coordinates
						shape = drawEllipse(start.x, start.y, e.getX(),
								e.getY());
					} else

					if (MainWindow.action == 4) {
						// Create a new rectangle using x & y coordinates
						shape = drawRectangle(start.x, start.y, e.getX(),
								e.getY());
					}

					// add shapes and colors into database and lists
					shapes.add(shape);
					colors.add(MainWindow.color);
					writeToDatabase(shape.getBounds().toString(),
							MainWindow.color.toString(), MainWindow.action);

					start = null;
					end = null;
					repaint();
				}
			}

		});

		// this action listener draw a temp shape while user is drawing
		// to help user
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				end = new Point(e.getX(), e.getY());
				repaint();
			}
		});
	}

	// this is the main method which draw all shapes
	public void paint(Graphics g) {
		MainWindow.graph = (Graphics2D) g;

		// better rendering
		MainWindow.graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Iterator<Color> colorShape = colors.iterator();

		// draw all shapes in list
		for (Shape s : shapes) {
			Color currentColor = colorShape.next();

			MainWindow.graph.setColor(currentColor);
			MainWindow.graph.draw(s);
			// MainWindow.graph.fill(s);
		}

		if (start != null && end != null) {
			Shape shape = null;

			if (MainWindow.action == 2) {
				shape = drawLine(start.x, start.y, end.x, end.y);
			} else if (MainWindow.action == 3) {
				shape = drawEllipse(start.x, start.y, end.x, end.y);
			} else if (MainWindow.action == 4) {
				shape = drawRectangle(start.x, start.y, end.x, end.y);
			}

			MainWindow.graph.draw(shape);
		}
	}

	// method to draw rectangle
	private Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2) {

		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int width = Math.abs(x1 - x2);
		int height = Math.abs(y1 - y2);
		return new Rectangle2D.Float(x, y, width, height);

	}

	// method to draw circle
	private Ellipse2D.Float drawEllipse(int x1, int y1, int x2, int y2) {

		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int width = Math.abs(x1 - x2);
		// by commenting this, my app only draw circles and not ellipses
		// anymore!
		int height = Math.abs(y1 - y2);
		return new Ellipse2D.Float(x, y, width, height);

	}

	// method to draw line
	private Line2D.Float drawLine(int x1, int y1, int x2, int y2) {
		return new Line2D.Float(x1, y1, x2, y2);
	}

	// this function get bounds and color of each shape and write them into
	// database using 2 string that indicate bounds and colors
	// exapmple of shape: java.awt.Rectangle[x=248,y=205,width=8,height=169]
	// exapmple of color: java.awt.Color[r=0,g=0,b=0]
	private void writeToDatabase(String shape, String color, int type) {
		String temp;
		String[] tempHelp;

		tempHelp = shape.split("\\[");
		StringBuilder sbShape = new StringBuilder(tempHelp[1]);
		sbShape.deleteCharAt(sbShape.length() - 1);
		temp = sbShape.toString();

		String[] bounds = temp.split(",");
		int x = Integer.parseInt(bounds[0].split("=")[1]);
		int y = Integer.parseInt(bounds[1].split("=")[1]);
		int height = Integer.parseInt(bounds[2].split("=")[1]);
		int weight = Integer.parseInt(bounds[3].split("=")[1]);

		tempHelp = color.split("\\[");
		StringBuilder sbColor = new StringBuilder(tempHelp[1]);
		sbColor.deleteCharAt(sbColor.length() - 1);
		temp = sbColor.toString();

		String[] rgb = temp.split(",");
		int r = Integer.parseInt(rgb[0].split("=")[1]);
		int g = Integer.parseInt(rgb[1].split("=")[1]);
		int b = Integer.parseInt(rgb[2].split("=")[1]);

		EntityManager.addIntoDatabase(x, y, height, weight, r, g, b, type);
	}

	
	//this method indicate whether the given point is in the shape or not
	// if it is then change the shape's color
	private void changeColor(Point p) {

		try {
			Iterator<Shape> shapeIter = shapes.iterator();
			Shape shape = shapeIter.next();
			for (int i = 0; i < shapes.size(); i++) {
				if (shape.contains(p)) {
					colors.add(MainWindow.color);
					shapes.add(shape);
					repaint();
					break;
				}
				shape = shapeIter.next();
			}
		} catch (Exception e) {
		}

	}

}
