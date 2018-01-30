package OLD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/*------------------------------------------------*/
public class ___cl_saplogon {
	/*
	 * 1 name = "AED_100" 2 text = "Разработка"> 3 <sysname>AED</sysname> 4
	 * <client>110</client> 5 <user>DOCHIEVID</user> 6 <pw>jd9_dd0880aFD</pw>
	 */
	String PASS = "";
	String[] columnNames = { "Код", "Текст", "Системма", "Мандант", "Пользователь", "Пароль" };
	static ___cl_saplogon go_ssl;
	final int gc_lields = 6;
	final String gc_file_name = "Smart_SAP_Logon.xml";
	String gv_SAPgui_folder;
	Document go_doc;

	String[][] gtd_data; // ArrayList
	String[][] gtd_sys; // 0-имя системмы/1-флаг(пусто = не найден в файле
						// паролей)
	// public ArrayList<data_line> data_tm = new ArrayList<data_line>();

	/*---------------------------------------------------------------*/
	public static ___cl_saplogon factory() throws Exception {
		if (go_ssl == null) {
			go_ssl = new ___cl_saplogon();
			// go_ssl.get_PASS();
			go_ssl.get_Program_Files();
			go_ssl.getDocument();
			go_ssl.GetAllSys();
			go_ssl.CheckNewSys();
		}
		return go_ssl;
	};

	/*---------------------------------------------------------------*/
	private void CheckNewSys() {
		for (int i = 0; i < gtd_sys.length; i++) {
			String s2 = gtd_sys[i][0];
			if (s2 != null) {
				for (int j = 0; j < gtd_data.length; j++) {
					String s1 = gtd_data[j][getColNumByName("sysname")];
					if (s1.compareTo(s2) == 0) {
						gtd_sys[i][1] = "X";
					}
				}
			}
		}
		// /
		int new_sys = 0;
		for (int i = 0; i < gtd_sys.length; i++) {
			String s3 = gtd_sys[i][1];
			String s4 = gtd_sys[i][0];
			if (s3 == null) {
				if (s4 != null) {
					new_sys = new_sys + 1;
				}
			}
		}
		// /
		String[][] ltd_data = new String[gtd_data.length + new_sys][gc_lields];
		// gtd_data; ltd_data =
		// / перенесём старые данные
		int line = 0;
		for (int i = 0; i < gtd_data.length; i++) {
			ltd_data[i] = gtd_data[i];
			line = line + 1;
		}
		// / заполним новые
		for (int i = 0; i < gtd_sys.length; i++) {
			String s3 = gtd_sys[i][1];
			String s4 = gtd_sys[i][0];
			if (s3 == null) {
				if (s4 != null) {
					ltd_data[line][getColNumByName("sysname")] = gtd_sys[i][0];
					ltd_data[line][getColNumByName("sysname")] = gtd_sys[i][0];
					ltd_data[line][getColNumByName("sysname")] = gtd_sys[i][0];
					line = line + 1;
				}
			}
		}

		gtd_data = ltd_data;

	}

	private void GetAllSys() {
		GetAllSys_INI();
		GetAllSys_XML();
	}

	/*---------------------------------------------------------------*/
	private void GetAllSys_XML() {
		try {
			File fXmlFile = new File(System.getenv("APPDATA") + "/SAP/SapConfigFiles/SAPUILandscape.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("staff");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out.println("Staff id : " + eElement.getAttribute("id"));
					System.out.println(
							"First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
					System.out.println(
							"Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
					System.out.println(
							"Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
					System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ****************************************
	private void GetAllSys_INI() {
		int lv_sys_all = 0;
		int lv_sys_curr = 0;
		int zc = 0;

		File file = new File(System.getenv("APPDATA") + "/SAP/Common/saplogon.ini");
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
	 * объект Document, который является объектным представлением XML документа.
	 */
	private void getDocument() throws Exception {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setValidating(false);
			DocumentBuilder builder = f.newDocumentBuilder();
			go_doc = builder.parse(new File(gc_file_name));
			get_PASS();
			NodeList nods_sys = go_doc.getElementsByTagName("sys");
			gtd_data = new String[nods_sys.getLength()][gc_lields];

			for (int i = 0; i < nods_sys.getLength(); i++) {
				try {

					Node node_sys = nods_sys.item(i);
					NamedNodeMap ltd_atr = node_sys.getAttributes();

					Node lv_name = ltd_atr.getNamedItem("name");
					gtd_data[i][0] = lv_name.getNodeValue();

					Node lv_text = ltd_atr.getNamedItem("text");
					gtd_data[i][1] = lv_text.getNodeValue();

					NodeList ltd_chaild = node_sys.getChildNodes();
					for (int j = 0; j < ltd_chaild.getLength(); j++) {
						try {
							Node node_sys_atr = ltd_chaild.item(j);
							String lv_11 = node_sys_atr.getNodeName();
							int col = getColNumByName(lv_11);
							String val_Z = node_sys_atr.getTextContent();
							// if ( lv_11 == "PW" ){ val_Z =
							// go_ssl.decode(val_Z); }
							gtd_data[i][col] = val_Z;
						} catch (Exception e) {
						}
						/*
						 * <sysname>AED</sysname> <client>110</client>
						 * <user>DOCHIEVID</user> <pw>jd9_dd0880aFD</pw> </sys>
						 */
					}
				} catch (Exception e) {
				}

			}
		} catch (Exception exception) {
			String message = "XML parsing error!";
			throw new Exception(message);
		}
	};

	private void get_PASS() {
		try {
			Node nods_pas = go_doc.getElementsByTagName("pas").item(0);
			PASS = nods_pas.getTextContent();
		} catch (Exception e) {
		}
		if (PASS == "") {
			// PASS = main.get_PASS_dailog();
		}
	}

	private int getColNumByName(String ColName) {
		/*
		 * 1 name = "AED_100" 2 text = "Разработка"> 3 <sysname>AED</sysname> 4
		 * <client>110</client> 5 <user>DOCHIEVID</user> 6
		 * <pw>jd9_dd0880aFD</pw>
		 */
		int lv_return = 99;
		if (ColName.toLowerCase() == "name") { // аналог "case 1"
			lv_return = 0;
		} else if (ColName.toLowerCase() == "text") {
			lv_return = 1;
		} else if (ColName == "sysname") {
			lv_return = 2;
		} else if (ColName == "client") {
			lv_return = 3;
		} else if (ColName == "user") {
			lv_return = 4;
		} else if (ColName == "pw") {
			lv_return = 5;
		}
		;
		return lv_return;
	};

	public String getColumnName(int c) {
		switch (c) {
		case 0:
			return "name";
		case 1:
			return "text";
		case 2:
			return "sysname";
		case 3:
			return "client";
		case 4:
			return "user";
		case 5:
			return "pw";
		default:
			return "";
		}
	}

	/*
	 * @SuppressWarnings("unused") private String getNameByColNum(int ColNum) {
	 * 
	 * 1 name = "AED_100" 2 text = "Разработка"> 3 <sysname>AED</sysname> 4
	 * <client>110</client> 5 <user>DOCHIEVID</user> 6 <pw>jd9_dd0880aFD</pw>
	 * 
	 * String lv_return = ""; switch (ColNum) { case 0: lv_return = "name"; case
	 * 1: lv_return = "text"; case 2: lv_return = "sysname";
	 * 
	 * case 3: lv_return = "client";
	 * 
	 * case 4: lv_return = "user";
	 * 
	 * case 5: lv_return = "pw"; } ; return lv_return; };
	 */

	/**
	 * ------------------------------------------------------------------------
	 * ------
	 **/
	void start_SAP(int str_num) {
		String[] ls_data = go_ssl.gtd_data[str_num];
		String[] cmd = { gv_SAPgui_folder, "-sysname=" + ls_data[getColNumByName("sysname")],
				"-client=" + ls_data[getColNumByName("client")], "-user=" + ls_data[getColNumByName("user")],
				"-pw=" + ls_data[getColNumByName("pw")] };
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			// если все равно глючит хрень ниже надо раскомментировать
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
	private void get_Program_Files() {
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

	public void save(TableModel tab) {
		go_doc.setNodeValue("");

		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setValidating(false);
			DocumentBuilder builder = f.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element ssl_menu = doc.createElement("ssl_menu");
			doc.appendChild(ssl_menu);

			for (int i = 0; i < tab.getRowCount(); i++) {
				Element sys = doc.createElement("sys");
				String sys_id = tab.getValueAt(i, getColNumByName("name")).toString();
				if (sys_id != "") {
					sys.setAttribute("name", sys_id);
					sys.setAttribute("text", tab.getValueAt(i, getColNumByName("text")).toString());
					ssl_menu.appendChild(sys);
					for (int j = 2; j < tab.getColumnCount(); j++) {
						Element sysname = doc.createElement(getColumnName(j));
						String val = "";
						try {
							val = tab.getValueAt(i, j).toString();
						} catch (Exception e) {
						}
						// sysname.setNodeValue(val);
						// if ( sysname == "PW" ){ val = go_ssl.code(val);)
						sysname.appendChild(doc.createTextNode(val));
						sys.appendChild(sysname);
					}
				}
			}
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(gc_file_name);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	};

	/*   */
	@SuppressWarnings("resource")
	public void copy_old() throws Exception {
		FileChannel source = new FileInputStream(new File(gc_file_name)).getChannel();
		FileChannel dest = new FileOutputStream(new File(System.currentTimeMillis() + gc_file_name)).getChannel();
		try {
			source.transferTo(0, source.size(), dest);
		} finally {
			source.close();
			dest.close();
		}
		System.out.println("File '" + "' copied to '" + "'");
	}

	public String code(String str_in) {
		String wordOUT = "";
		String Return = "";
		byte byt[] = str_in.getBytes();

		for (int i = 0; i < byt.length; i++) {
			int cod = 500 + byt[i];
			wordOUT = wordOUT + cod;
		}

		int coutn = wordOUT.length() / 2;

		for (int i = 0; i < coutn; i++) {
			byte by = Byte.parseByte(wordOUT.substring(i * 2, i * 2 + 2));
			char cb = (char) (by & 0x00FF);
			Return = Return + cb;
		}
		return Return;
	}

	public String decode(String str_in) {
		String wordOUT = "";
		String Return = "";
		for (int i = 0; i < str_in.length(); i++) {
			String sumbol = str_in.substring(i);
			byte by = sumbol.getBytes()[0];
			// char cb = (char) (by & 0x00FF);
			wordOUT = wordOUT + by;
		}
		for (int i = 0; i < wordOUT.length() / 3; i++) {
			String sc = wordOUT.substring(i * 3, i * 3 + 3);
			int cod = Integer.parseInt(sc);
			cod = cod - 500;
			String sc_1 = String.valueOf(cod);
			byte by = Byte.parseByte(sc_1);
			char cb = (char) (by & 0x00FF);
			Return = Return + cb;
		}
		return Return;
	}
	/*
	 * public void setGo_data(List go_data) { this.go_data = go_data; }
	 * 
	 * public List getGo_data() { return go_data; }
	 */
}