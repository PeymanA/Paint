package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

@SuppressWarnings("serial")
public class Login extends JFrame {

	private static Login frame;

	private JPanel contentPane;
	private JTextField tfUsername;
	private JPasswordField tfPassword;
	private JLabel lblNotification;

	/**
	 * Launch the application.
	 */
	public static void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		loginPanel();
	}

	/**
	 * Create login panel
	 */
	public void loginPanel() {
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(129, 86, 66, 14);
		contentPane.add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(129, 127, 66, 14);
		contentPane.add(lblPassword);

		tfUsername = new JTextField();
		tfUsername.setBounds(205, 83, 105, 20);
		contentPane.add(tfUsername);
		tfUsername.setColumns(10);

		tfPassword = new JPasswordField();
		tfPassword.setBounds(205, 124, 105, 20);
		contentPane.add(tfPassword);

		lblNotification = new JLabel("");
		lblNotification.setBackground(Color.RED);
		lblNotification.setBounds(129, 236, 181, 14);
		contentPane.add(lblNotification);

		JButton btnLogin = new JButton("LogIn");
		btnLogin.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				if (tfUsername.getText() != "" && tfPassword.getText() != "") {

					boolean login = login(tfUsername.getText(),
							tfPassword.getText());
					if (login == true) {

						// closes the login page and start paint app
						try{
							EntityManager.readFromDatabase();
						}finally{
							
						}
						MainWindow.runMain();
						frame.dispose();

					} else {

						lblNotification.setText("Wrong Username or Password!");

					}

				}

				else {

					lblNotification.setText("Enter Username and Password!");

				}

			}
		});
		btnLogin.setBounds(173, 166, 89, 23);
		contentPane.add(btnLogin);

	}

	// Check if username and password are registerd in database
	public static boolean login(String userName, String passWord) {

		User u = EntityManager.checkUser(userName, passWord);

		if (u != null) {
			//EntityManager.readFromDatabase();
			return true;
		}

		return false;
	}

}
