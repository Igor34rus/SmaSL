package SmaSL;

import java.util.ArrayList;

import java.io.File;
import java.io.InputStream;

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

import data.data_TableModel;
import data.data_line;
import data.data_setting;

import org.w3c.dom.Element;

/*------------------------------------------------*/
public class CL_SmaSL_XML {
	/*
	 * 1 name = "AED_100" 
	 * 2 text = "����������">
	 * 3 <sysname>AED</sysname> 
	 * 4 <client>110</client> 
	 * 5 <user>DOCHIEVID</user>
	 * 6 <pw>jd9_dd0880aFD</pw>
	 * 7 <pw_h>HJK*(()*</pw_h>
	 */
//	String PASS = "";

	final String gc_settingXML_Name = "setting";
	final String gc_pwdXML_Name = "password";
	final String gc_pwdXML_Need = "need";
	final String gc_pwdXML_pwd  = "pwd";
	final String gc_file_name = "SmaSL.xml";
	CL_SmaSL_PASSWORD go_pwdCoder ;
	Document go_doc;
	// �������)
	public ArrayList<data_line> data_tm = new ArrayList<data_line>();
	data_TableModel TabModel = new data_TableModel(data_tm);
	private data_setting go_setting = null;
	/*---------------------------------------------------------------*/
	public CL_SmaSL_XML( ) {
		getDocument();
	};
	/*---------------------------------------------------------------*/
	public ArrayList<data_line> get_data() {
		return data_tm;
	}

	/**
	 * ������ Document, ������� �������� ��������� �������������� XML ���������.
	 */
	private void getDocument() {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setValidating(false);
			DocumentBuilder builder = f.newDocumentBuilder();
			File XML_File = new File( gc_file_name );
			go_doc = builder.parse(XML_File);

			get_setting();
					
			NodeList nods_sys = go_doc.getElementsByTagName("sys");

			for (int i = 0; i < nods_sys.getLength(); i++) {
				try {
					data_line ls_str = new data_line();// ����� ������ - ������
					Node node_sys = nods_sys.item(i);
					NamedNodeMap ltd_atr = node_sys.getAttributes();
					Node lv_name = ltd_atr.getNamedItem("name");
					ls_str.name = lv_name.getNodeValue();
					Node lv_text = ltd_atr.getNamedItem("text");
					ls_str.text = lv_text.getNodeValue();
					NodeList ltd_chaild = node_sys.getChildNodes();
					for (int j = 0; j < ltd_chaild.getLength(); j++) {
						try {
							Node node_sys_atr = ltd_chaild.item(j);
							String lv_node_name = node_sys_atr.getNodeName();
							String lv_node_valu = node_sys_atr.getTextContent();
							int col = TabModel.get_col_by_name(lv_node_name);
							// int col = ls_str.get_col_by_name(lv_node_name);
							ls_str.set_value(col, lv_node_valu);
						} catch (Exception e) {
						}
						/*
						 * <sys name="AED_110" text="����������">
						 * <sysname>AED</sysname> <client>110</client>
						 * <user>DOCHIEVID</user> <pw>jd9_dd0880aFD</pw> </sys>
						 */
					}
					ls_str= _data_tm_Line(ls_str);
					data_tm.add(ls_str);
				} catch (Exception e) {
				}

			}
		} catch (Exception exception) {
			// String message = "XML parsing error!"; throw new
			// Exception(message);
		}
	};

	private data_line _data_tm_Line(data_line ls_str) {
		// TODO Auto-generated method stub
		if(go_setting.PasswordNeed == true){
			if(ls_str.pw_h != ""){
				ls_str.pw = go_pwdCoder.decript(ls_str.pw_h);
			}
		}
		return ls_str;
	}

	data_setting get_setting() {
		if ( go_setting == null){
			go_setting = new data_setting();
			try {
				Node nods_pas = go_doc.getElementsByTagName(gc_pwdXML_Name).item(0);
				NamedNodeMap ltd_atr = nods_pas.getAttributes();
				Node lv_pwd = ltd_atr.getNamedItem(gc_pwdXML_pwd);
				go_setting.set_value(0, lv_pwd.getNodeValue());
				Node lv_pwdNeed = ltd_atr.getNamedItem(gc_pwdXML_Need);
				go_setting.set_value(1, lv_pwdNeed.getNodeValue());				
			} 
			catch (Exception e) {
			}
		}
		go_pwdCoder = new CL_SmaSL_PASSWORD(go_setting.Password);
		return go_setting;
	}
	/**
	 * -----------------------
	 * -----------------------
	 **/
	public void save(data_TableModel tab, data_setting io_setting) {
		go_doc.setNodeValue("");

		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setValidating(false);
			DocumentBuilder builder = f.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			Element root = doc.createElement("SSL_DOC");			
			doc.appendChild(root);

			
			_save_setting(doc,root,io_setting);
			_save_system(doc,root,tab);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(gc_file_name);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void _save_system(Document io_doc,Element root,data_TableModel io_tab) {
		// TODO Auto-generated method stub
		Element ssl_menu = io_doc.createElement("ssl_menu");			
		root.appendChild(ssl_menu);

		for (int i = 0; i < io_tab.getRowCount(); i++) {
			Element sys = io_doc.createElement("sys");
			data_line ls_data = io_tab.get_row(i);
			String sys_id = ls_data.name;// tab.getValueAt(i,
											// getColNumByName("name")).toString();
//			if(go_setting.PasswordNeed = true){
//				ls_data.pw_h = go_pwdCoder.encript(ls_data.pw);
//			}

			if (sys_id != "") {
				sys.setAttribute("name", sys_id);
				sys.setAttribute("text", ls_data.text);
				ssl_menu.appendChild(sys);
				for (int j = 2; j < io_tab.getColumnCount(); j++) {
					Element sysname = io_doc.createElement(io_tab.get_name_by_col(j));
					String val = "";
					try {
						val = io_tab.getValueAt(i, j).toString();
					} catch (Exception e) {
					}
					sysname.appendChild(io_doc.createTextNode(val));
					sys.appendChild(sysname);
				}
			}
		}		
	}

	private void _save_setting(Document io_doc, Element root,data_setting io_setting) {
		Element ssl_sett = io_doc.createElement(gc_settingXML_Name);			
		root.appendChild(ssl_sett);

		Element pwdXML = io_doc.createElement(gc_pwdXML_Name);		
		pwdXML.setAttribute(gc_pwdXML_pwd,  io_setting.Password);
		pwdXML.setAttribute(gc_pwdXML_Need, String.valueOf(io_setting.PasswordNeed ));
		
		ssl_sett.appendChild(pwdXML);	
	};

}