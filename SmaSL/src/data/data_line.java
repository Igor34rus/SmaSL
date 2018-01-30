package data;

public class data_line {
	static public int gv_col_count = 7;
	public String name;
	public String text;
	public String sysname;
	public String client;
	public String user;
	public String pw;
	public String pw_h;

	/*
	 * static public String[][] columnNames = { { "name", "Name" }, { "text",
	 * "Text" }, { "sysname", "SsysName" }, { "client", "Mandant" }, { "user",
	 * "UserName" }, { "pw", "Password" } };
	 */
	/**
	 * -------------------------------------------------------------------------
	 * -----------------------
	 **/
	public void set_value(int columnIndex, String value) {
		// TODO Auto-generated method stub
		switch (columnIndex) {
		case 0:
			name = value; break;
		case 1:
			text = value;break;
		case 2:
			sysname = value;break;
		case 3:
			client = value;break;
		case 4:
			user = value;break;
		case 5:
			pw = value;break;
		case 6:
			pw_h = value;break;
		}
	}

	public Object getValueAt(int columnIndex) {
		// TODO Auto-generated method stub
		switch (columnIndex) {
		case 0:
			return name; 
		case 1:
			return text;
		case 2:
			return sysname;
		case 3:
			return client;
		case 4:
			return user;
		case 5:
			return pw;
		case 6:
			return pw_h;
		}
		return "";
	}
	/*
	 * public String get_name_by_col(int iv_column) { // TODO Auto-generated
	 * method stub return columnNames[iv_column][0]; }
	 */
}