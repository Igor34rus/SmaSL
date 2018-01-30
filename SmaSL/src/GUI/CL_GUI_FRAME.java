package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class CL_GUI_FRAME extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// public static CL_GUI_FRAME app;
	private TrayIcon iconTr;
	private SystemTray sT = SystemTray.getSystemTray();
	public boolean chetTray = false; // переменная, чтобы был выв

	public CL_GUI_FRAME() throws IOException {
		super("SmartSapLogon");
		URL file = null;
		// try{
		// BufferedImage img;
		
		BufferedImage img;
		try {
			file = getClass().getClassLoader().getResource("ICON1.jpg");
			
			if (file==null){
				JOptionPane.showInputDialog("file==null", "zz");
				return;
				
			}
			
	
			img = ImageIO.read(file);
			iconTr = new TrayIcon(img, "Демонстрация сворачивания в трей"); // Ikonka.png
			iconTr.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					setVisible(true);
					setState(JFrame.NORMAL);
					removeTr();
				}
			});

			final JPopupMenu popup = _GetTrayRightClickPopup();

			// обработчик мыши
			MouseListener mouS = new MouseListener() {
				public void mouseClicked(MouseEvent ev) {

				}

				public void mouseEntered(MouseEvent ev) {
				}

				public void mouseExited(MouseEvent ev) {
				}

				public void mousePressed(MouseEvent ev) {
				}

				public void mouseReleased(MouseEvent ev) {
					if (ev.isPopupTrigger()) {
						popup.setLocation(ev.getX(), ev.getY());
						popup.setInvoker(popup);
						popup.setVisible(true);
					}
				}
			};
			iconTr.addMouseListener(mouS);

			MouseMotionListener mouM = new MouseMotionListener() {
				public void mouseDragged(MouseEvent ev) {
				}

				// при наведении
				public void mouseMoved(MouseEvent ev) {
					// boolean flg = false;
					iconTr.setToolTip("Двойной щелчок - развернуть");
				}
			};

			iconTr.addMouseMotionListener(mouM);
			addWindowStateListener(new WindowStateListener() {
				public void windowStateChanged(WindowEvent ev) {
					if (ev.getNewState() == JFrame.ICONIFIED) {
						setVisible(false);
						addTr();
					}
				}
			});
		} catch (IOException e) {
			JOptionPane.showInputDialog(e.getMessage(), "zxczxz");
			JOptionPane.showInputDialog("111zzxccxzxc", "zxczxz");
			e.printStackTrace();
			
			// JOptionPane.showMessageDialog(null, file );
			// TODO Auto-generated catch block
			return;
		}
	}

	private JPopupMenu _GetTrayRightClickPopup() {
		final String cmdExit = "Exit";

		JMenuItem defaultItem;

		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()) {
				case cmdExit:
					System.out.println("Exiting...");
					System.exit(0);
					break;
				}
			}
		};

		final JPopupMenu popup = new JPopupMenu();

		defaultItem = new JMenuItem(cmdExit);

		defaultItem.addActionListener(exitListener);
		popup.add(defaultItem);
		popup.addSeparator();

		// defaultItem = new JMenuItem("2222");
		// defaultItem.addActionListener(exitListener);
		// popup.add(defaultItem);

		return popup;
	}

	// метод удаления из трея
	private void removeTr() {
		sT.remove(iconTr);
	}

	// метод добавления в трей
	private void addTr() {
		try {
			sT.add(iconTr);
			if (chetTray == false) {
				iconTr.displayMessage("Прячется в трей", "SmaSL свернулась", TrayIcon.MessageType.INFO);
			}
			chetTray = true;
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
	}
	// public static void main(String[] args) throws IOException {
	// app = new CL_GUI_FRAME();
	// app.setVisible(true);
	// app.setAlwaysOnTop(true);
	// app.setSize(777, 777);
	//
	// // обработчик основного окна - здесь необходимо перечислить в
	// app.addWindowListener(new WindowListener() {
	// public void windowClosing(WindowEvent winEvent) {
	// System.exit(0);// при закрытии окна завершаем программу
	// }
	//
	// public void windowActivated(WindowEvent winEvent) {
	// }
	//
	// public void windowClosed(WindowEvent winEvent) {
	// }
	//
	// public void windowDeactivated(WindowEvent winEvent) {
	// }
	//
	// public void windowDeiconified(WindowEvent winEvent) {
	// }
	//
	// public void windowIconified(WindowEvent winEvent) {
	// }
	//
	// public void windowOpened(WindowEvent winEvent) {
	// }
	// });
	// }
}
