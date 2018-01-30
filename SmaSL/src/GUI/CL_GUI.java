package GUI;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import SmaSL.CL_SmaSL;
import data.data_TableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
/* * */
public class CL_GUI {
	JTable go_table;
	JScrollPane go_TabPAN;
	CL_GUI_FRAME go_Frame;
	JCheckBox go_cb;

	// JTree go_tree;
	JButton but_save;
	CL_GUI_TREE go_gui_tree;
	boolean is_edit = false;
	/** 
	 * @throws IOException
	 **/
	public static void main(String[] args) {
		new GUI.CL_GUI();
	}	

	public CL_GUI() {
		try {
			_createSSL();
			createGUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void _createSSL() {
		
		CL_SmaSL lo_SSL = CL_SmaSL.factory( );
		if (lo_SSL.setting.PasswordNeed == true){
			String PWD = get_PASS_dailog();
			lo_SSL.setting.Password = PWD; 
		}
	}

	public void createGUI() throws IOException {

		createGUI_set_defoult();

		go_Frame = new CL_GUI_FRAME();
		go_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contain = go_Frame.getContentPane();

		go_Frame.setJMenuBar(createGUI_menuBar());

		contain.add(createGUI_TopPanel(), BorderLayout.BEFORE_FIRST_LINE);

		JPanel PAN = new JPanel();
		PAN.add(createGUI_TreePanel());
		PAN.add(createGUI_TabPanel_model());

		contain.add(PAN, BorderLayout.CENTER);

		change_editing();

		

	};
	private Component createGUI_TabPanel_model() {
		// TODO Auto-generated method stub

		CL_SmaSL lo_ssl = CL_SmaSL.factory();
		TableModel model = new data_TableModel(lo_ssl.data_tm);

		go_table = new JTable(model);
		go_table.setAutoCreateRowSorter(true);
		go_table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (me.getClickCount() == 2) {
					if (go_cb.isSelected() == false) {
						// int row_sel = table1.getSelectedRow();
						int row = go_table.rowAtPoint(me.getPoint());
						CL_SmaSL lo_ssl = CL_SmaSL.factory();
						lo_ssl.start_SAP(row);
					}
				}
			}
		});
		go_TabPAN = new JScrollPane(go_table);
		// JCheckBox cb = new JCheckBox("zzzz1");
		// PAN.getViewport().add(cb, BorderLayout.NORTH);
		go_TabPAN.getViewport().add(go_table, BorderLayout.SOUTH);
		go_TabPAN.setVisible(false);

		but_save = new JButton("Copy");
		but_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent act) {
				int lo_sel_row = go_table.getSelectedRow();
				data_TableModel tab = (data_TableModel) go_table.getModel();
				tab.copy_row_append(lo_sel_row);
				go_table.updateUI();
			}
		});
		go_TabPAN.add(but_save);

		return go_TabPAN;
	}

	private void createGUI_set_defoult() {
		/*
		 * try { javax.swing.UIManager.setLookAndFeel(UIManager.
		 * getSystemLookAndFeelClassName()); } catch (ClassNotFoundException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (InstantiationException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IllegalAccessException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch
		 * (UnsupportedLookAndFeelException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 * 
		 * // int reply = JOptionPane.showConfirmDialog(null, "Is Hello world?",
		 * "Title", JOptionPane.YES_NO_OPTION); // if (reply ==
		 * JOptionPane.YES_OPTION){ // //do something // }
		 */
	}

	/// -----------------
	/*
	 * private static JScrollPane createGUI_TabPanel() { go_table = new
	 * JTable(go_ssl.gtd_data, go_ssl.columnNames);
	 * go_table.addMouseListener(new MouseAdapter() { public void
	 * mousePressed(MouseEvent me) { JTable table1 = (JTable) me.getSource(); if
	 * (me.getClickCount() == 2 ) { if ( go_cb.isSelected() == false ){ int
	 * row_sel = table1.getSelectedRow(); go_ssl.start_SAP(row_sel); } } } });
	 * // JScrollPane PAN = new JScrollPane(go_table); JScrollPane PAN = new
	 * JScrollPane( go_table );
	 * 
	 * JCheckBox cb = new JCheckBox("zzzz");
	 * 
	 * PAN.getViewport( ).add(cb, BorderLayout.NORTH); PAN.getViewport(
	 * ).add(go_table, BorderLayout.SOUTH);
	 * 
	 * 
	 * 
	 * return PAN; }
	 */
	/// -----------------
	private JPanel createGUI_TopPanel() {
		JPanel PAN = new JPanel();
		go_cb = new JCheckBox("Edit mode is enabled");
		go_cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent act) {
				is_edit = !is_edit;
				change_editing();
			}
		});
		PAN.add(go_cb);
		return PAN;
	}

	private void change_editing() {
		but_save.setEnabled(is_edit);
		go_table.setEnabled(is_edit);
		go_TabPAN.setVisible(is_edit);

		go_Frame.pack();
		go_Frame.setLocationRelativeTo(null);
		go_Frame.setVisible(true);
	}

	/// -----------------
	private Component createGUI_TreePanel() {
		JTree lo_tree = CL_GUI_TREE.get_GUI_TREE().GetTree();
		JScrollPane PAN = new JScrollPane(lo_tree);
		// Dimension dem = new Dimension() ;
		// dem.width = 6010;
		// PAN.setBorder( new LineBorder(Color.RED));
		// PAN.setMinimumSize( new Dimension(300,500) );
		return PAN;

	}

	/**
	 * -------------------------------------------------------------------------
	 **/
	public JMenuBar createGUI_menuBar() {

		CL_SmaSL lo_ssl = CL_SmaSL.factory();

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem save = new JMenuItem("Save...");

		fileMenu.add(save);
		menuBar.add(fileMenu);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent act) {
				data_TableModel tab = (data_TableModel) go_table.getModel();
				CL_SmaSL lo_ssl = CL_SmaSL.factory();
				lo_ssl.save(tab);
			}
		});

		JMenu jmSetting = new JMenu("Setting");

		String jmSettingText = "Set Password";
		if (lo_ssl.setting.PasswordNeed = true) {
			jmSettingText = "Delite Password";
		}
		JCheckBoxMenuItem SetPWDSave = new JCheckBoxMenuItem("Password");
		SetPWDSave.setSelected(lo_ssl.setting.PasswordNeed);
		jmSetting.add(SetPWDSave);
		menuBar.add(jmSetting);

		SetPWDSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent act) {
				CL_SmaSL lo_ssl = CL_SmaSL.factory();
				
				if (lo_ssl.setting.PasswordNeed == false) {
					lo_ssl.setting.Password = get_PASS_dailog();
					lo_ssl.setting.PasswordNeed = true;
				}else{
					if (lo_ssl.setting.Password == get_PASS_dailog() ){
						lo_ssl.setting.Password = "";
						lo_ssl.setting.PasswordNeed = false;
					}
				}
			}
		});

		return menuBar;
	}

	public String get_PASS_dailog () {
		char[] password = null;
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter a password:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[] { "OK", "Cancel" };
		int option = JOptionPane.showOptionDialog(null, panel, "The title", JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (option == 0) // pressing OK button
		{
			password = pass.getPassword();
			System.out.println("Your password is: " + new String(password));
		}else{
			return null;
		}
		String pswd = new String(password);
		return pswd;
	}
}
