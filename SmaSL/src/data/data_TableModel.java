package data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class data_TableModel implements TableModel {
	// static public String[][] columnNames = { { "name", "���" },
	// { "text", "�����" },
	// { "sysname", "��������" },
	// { "client", "�������" },
	// { "user", "������������" },
	// { "pw", "������" }
	// };
	static public String[][] columnNames = { { "name", "Name" }, { "text", "Text" }, { "sysname", "SsysName" },
			{ "client", "Mandant" }, { "user", "UserName" }, { "pw", "Password" }, { "pw_h", "HPassword" } };
	private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
	private List<data_line> data_table;

	/*----------------------------------------------------------------------*/
	public data_line copy_row_append(int row) {
		data_line ls_data_new = new data_line();
		data_line ls_data_old = get_row(row);
		for (int i = 0; i < getColumnCount(); i++) {
			ls_data_new.set_value(i, ls_data_old.getValueAt(i).toString());
		}
		add_row(ls_data_new);
		return ls_data_new;
	}

	public data_TableModel(List<data_line> i_list_data) {
		this.data_table = i_list_data;
	}

	@Override
	public void addTableModelListener(TableModelListener lis) {
		// TODO Auto-generated method stub
		listeners.add(lis);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return data_line.gv_col_count;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return columnNames[columnIndex][1];
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data_table.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		data_line ls_data = data_table.get(rowIndex);
		return ls_data.getValueAt(columnIndex);
	}

	public String get_name_by_col(int iv_column) {
		// TODO Auto-generated method stub
		return columnNames[iv_column][0];
	};

	public int get_col_by_name(String iv_columnName) {
		// TODO Auto-generated method stub
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i][0] == iv_columnName.toLowerCase()) {
				return i;
			}
		}
		;
		return -1;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return true;
	}

	public data_line get_row(int rowIndex) {
		// TODO Auto-generated method stub
		return data_table.get(rowIndex);

	}

	public void add_row(data_line is_line) {
		// TODO Auto-generated method stub
		data_table.add(is_line);
	}

	@Override
	public void removeTableModelListener(TableModelListener lis) {
		// TODO Auto-generated method stub
		listeners.remove(lis);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		data_line ls_data = data_table.get(rowIndex);
		ls_data.set_value(columnIndex, aValue.toString());
		/*
		 * switch (columnIndex) { case 0: ls_data.name = aValue.toString();
		 * return; // �� - ��� ����� �� �������� case 1: ls_data.text =
		 * aValue.toString(); return; case 2: ls_data.sysname =
		 * aValue.toString(); return; case 3: ls_data.client =
		 * aValue.toString(); return; case 4: ls_data.user = aValue.toString();
		 * return; case 5: ls_data.pw = aValue.toString(); return; }
		 */
	}
}
