package data;

public class data_setting {
	static public int gv_col_count = 2;
	public String Password;
	public boolean PasswordNeed;
	/* **/
	public void set_value(int columnIndex, String value) {
		// TODO Auto-generated method stub
		switch (columnIndex) {
		case 0:
			Password = value; break;
		case 1:			
			if(value.equals("true")){
				PasswordNeed = true;	
			}else{
				PasswordNeed = false;
			}
			break;
		}
	}

	public Object getValueAt(int columnIndex) {
		// TODO Auto-generated method stub
		switch (columnIndex) {
		case 0:
			return Password; 
		case 1:
			return PasswordNeed;	
		}
		return "";
	}	
}