package SmaSL;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import data.data_TableModel;
import data.data_line;
import data.data_setting;

/*------------------------------------------------*/
public class CL_SmaSL {
	/*
	 * 1 name = "AED_100" test fghfhgfg
	 * 2 text = "����������">
	 * 3 <sysname>AED</sysname> 
	 * 4 <client>110</client> 
	 * 5 <user>DOCHIEVID</user>
	 * 6 <pw>jd9_dd0880aFD</pw>
	 * 7 <pw_h>HJK*(()*</pw_h>
	 */
	static  CL_SmaSL go_ssl;
	CL_SmaSL_PASSWORD go_pwd;
	private CL_SmaSL_XML GO_SSL_XML;
	private String gv_SAPgui_folder;
	 
	private String[][] gtd_sys; // 0-��� ��������/1-����(����� = �� ������ �
								// �����
	// �������)
	public ArrayList<data_line> data_tm = new ArrayList<data_line>();
	public data_setting setting = new data_setting();
	private String zz;
	/*---------------------------------------------------------------*/
	public CL_SmaSL( ) {
		
		GO_SSL_XML = new CL_SmaSL_XML( );
		data_tm = GO_SSL_XML.get_data();
		setting = GO_SSL_XML.get_setting();
	
		get_SAPgui_folder();
		// go_ssl.getDocument();
		GetAllSys();
		CheckNewSys();
	}

	/*---------------------------------------------------------------*/
	
	public static CL_SmaSL factory( ) {
		if (go_ssl == null) {
			go_ssl = new CL_SmaSL( );
		}
		return go_ssl;
	};
	/*---------------------------------------------------------------*/
	private void CheckNewSys() {
		if (gtd_sys == null) {
			return;
		}
		;

		data_line ls_data;

		for (int i = 0; i < gtd_sys.length; i++) {
			String s2 = gtd_sys[i][0];
			if (s2 != null) {
				for (int j = 0; j < data_tm.size(); j++) {
					ls_data = data_tm.get(j);
					String s1 = ls_data.sysname;
					if (s1.compareTo(s2) == 0) {
						gtd_sys[i][1] = "X";
					}
				}
				if (gtd_sys[i][1] != "X") {
					ls_data = new data_line();
					ls_data.name = s2;
					ls_data.text = s2;
					ls_data.sysname = s2;
					data_tm.add(ls_data);
					/* ls_data.�������; ls_data.������������; ls_data.������; */
				}
			}
		}
	}

	/*---------------------------------------------------------------*/
	// ****************************************
	private void GetAllSys() {
		GetAllSys_INI();
		GetAllSys_XML();
	}

	private void GetAllSys_XML() {
		try {
			String FileName = System.getenv("ProgramFiles") + "/SAP/SapConfigFiles/SAPUILandscape.xml";
			File fXmlFile = new File(FileName);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			// System.out.println("Root element :" +
			// doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Service");

			// System.out.println("----------------------------");

			int lv_sys_all = nList.getLength();

			gtd_sys = new String[lv_sys_all][2];

			for (int lv_sys_curr = 0; lv_sys_curr < lv_sys_all; lv_sys_curr++) {
				Node nNode = nList.item(lv_sys_curr);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					gtd_sys[lv_sys_curr][0] = eElement.getAttribute("name");
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void GetAllSys_INI() {
		int lv_sys_all = 0;
		int lv_sys_curr = 0;
		int zc = 0;
		
		zz = System.getenv("SAPLOGON_INI_FILE");
		
		File file = new File(System.getenv("SAPLOGON_INI_FILE"));
		if (file == null) {
			file = new File(System.getenv("APPDATA") + "/SAP/Common/saplogon.ini");
		}
		if (file == null) {
			return;
		}
		Scanner input;
		// *----------------------------
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		;
		// *----------------------------
		String nextToken1;
		while (input.hasNext()) {
			nextToken1 = input.next();
			if (gtd_sys == null) {
				if (nextToken1.compareTo("[Configuration]") == 0) {
					nextToken1 = input.next();
					lv_sys_all = Integer.parseInt(nextToken1.substring(nextToken1.indexOf("=") + 1));
					gtd_sys = new String[lv_sys_all][2];
				}
			}
			if (nextToken1.compareTo("[MSSysName]") == 0) {
				zc = 1;
				while (zc > 0) {
					String nextToken = input.next();
					zc = nextToken.indexOf("=");
					if (zc > 0) {
						gtd_sys[lv_sys_curr][0] = nextToken.substring(zc + 1);
						lv_sys_curr = lv_sys_curr + 1;
					}
				}
				;
				input.close();
				return;
			}
		}
		input.close();
	}

	/**
	 * ------------------------------------------------------------------------
	 * ------
	 **/
	public void start_SAP(int str_num) {
		data_line ls_data = data_tm.get(str_num);
		String[] cmd = { gv_SAPgui_folder, "-sysname=" + ls_data.sysname, "-client=" + ls_data.client,
				"-user=" + ls_data.user, "-pw=" + ls_data.pw };
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.getInputStream().close();
			p.getOutputStream().close();
			p.getErrorStream().close();
		} catch (java.io.IOException e) {
			System.out.println("Cannot execute: " + cmd);
		}
		Thread.currentThread().interrupt();
	};

	/**
	 * ------------------------------------------------------------------------
	 * ------
	 **/
	private void get_SAPgui_folder() {
		String arch = System.getenv("PROCESSOR_ARCHITECTURE");
		String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
		String realArch = arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ? "64" : "32";
		gv_SAPgui_folder = "C:\\";
		if (realArch == "64") {
			gv_SAPgui_folder = gv_SAPgui_folder + "Program Files (x86)";
		} else {
			gv_SAPgui_folder = gv_SAPgui_folder + "Program Files";
		}
		gv_SAPgui_folder = gv_SAPgui_folder + "\\SAP\\FrontEnd\\SAPgui\\sapshcut.exe";
	};

	public void save(data_TableModel tab) {
		GO_SSL_XML.save(tab,setting);
	};

	
	
	/*
	 * public void copy_old() throws Exception { FileChannel source = new
	 * FileInputStream(new File(gc_file_name)) .getChannel(); FileChannel dest =
	 * new FileOutputStream(new File(System .currentTimeMillis() +
	 * gc_file_name)).getChannel(); try { source.transferTo(0, source.size(),
	 * dest); } finally { source.close(); dest.close(); } System.out.println(
	 * "File '" + "' copied to '" + "'"); }
	 */
	/*
	 * public String code(String str_in) { String wordOUT = ""; String Return =
	 * ""; byte byt[] = str_in.getBytes();
	 * 
	 * for (int i = 0; i < byt.length; i++) { int cod = 500 + byt[i]; wordOUT =
	 * wordOUT + cod; }
	 * 
	 * int coutn = wordOUT.length() / 2;
	 * 
	 * for (int i = 0; i < coutn; i++) { byte by =
	 * Byte.parseByte(wordOUT.substring(i * 2, i * 2 + 2)); char cb = (char) (by
	 * & 0x00FF); Return = Return + cb; } return Return; }
	 * 
	 * public String decode(String str_in) { String wordOUT = ""; String Return
	 * = ""; for (int i = 0; i < str_in.length(); i++) { String sumbol =
	 * str_in.substring(i); byte by = sumbol.getBytes()[0]; // char cb = (char)
	 * (by & 0x00FF); wordOUT = wordOUT + by; } for (int i = 0; i <
	 * wordOUT.length() / 3; i++) { String sc = wordOUT.substring(i * 3, i * 3 +
	 * 3); int cod = Integer.parseInt(sc); cod = cod - 500; String sc_1 =
	 * String.valueOf(cod); byte by = Byte.parseByte(sc_1); char cb = (char) (by
	 * & 0x00FF); Return = Return + cb; } return Return; }
	 */
}