package GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import SmaSL.CL_SmaSL;

public class CL_GUI_TREE {
	static private CL_GUI_TREE go_GUI_TREE;
	JTree go_tree;
	private DefaultMutableTreeNode RootNode;

	public static CL_GUI_TREE get_GUI_TREE() {
		if (go_GUI_TREE == null) {
			go_GUI_TREE = new CL_GUI_TREE();
		}
		return go_GUI_TREE;
	}

	public JTree GetTree() {
		if (go_tree == null) {
			_CreateTree();
		}
		return go_tree;
	}

	private void _CreateTree() {
		CL_SmaSL lo_ssl = CL_SmaSL.factory();

		RootNode = new DefaultMutableTreeNode("Системмы");
		go_tree = new JTree(RootNode);

		DefaultMutableTreeNode level1a;

		for (int i = 0; i < lo_ssl.data_tm.size(); i++) {
			String lv_syname = lo_ssl.data_tm.get(i).name;
			String lv_DirName = lv_syname;

			DefaultMutableTreeNode DirNode = GetDirNode(lv_DirName);
			// data_line asd = lo_ssl.data_tm.get(i);
			level1a = new DefaultMutableTreeNode(lo_ssl.data_tm.get(i).name);
			DirNode.add(level1a);
		}
		expandAllNodes(go_tree, 0, go_tree.getRowCount());
		go_tree.addMouseListener(new MouseAdapter() {
			// private String ss;

			@Override
			public void mousePressed(MouseEvent me) {
				if (me.getClickCount() == 2) {
					TreePath lv_row = go_tree.getPathForLocation(me.getX(), me.getY());
					Object lv_sel = lv_row.getLastPathComponent();
					String ss = lv_sel.toString();
					CL_SmaSL lo_ssl = CL_SmaSL.factory();
					for (int i = 0; i < lo_ssl.data_tm.size(); i++) {
						if (lo_ssl.data_tm.get(i).name == ss) {
							lo_ssl.start_SAP(i);
						}
					}

				}
			}
		});
	}

	private DefaultMutableTreeNode GetDirNode(String iv_DirName) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode ro_node = null;
		Enumeration e = RootNode.breadthFirstEnumeration();
		String lv_DirName = iv_DirName.substring(0, 2);
		while (e.hasMoreElements()) {
			ro_node = (DefaultMutableTreeNode) e.nextElement();
			String as2 = ro_node.getUserObject().toString().substring(0, 2);
			if (lv_DirName.equals(as2)) {
				return ro_node;
			}
		}

		// can't finde => create it
		ro_node = new DefaultMutableTreeNode(lv_DirName);

		RootNode.add(ro_node);
		return ro_node;
	}

	private static void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
		for (int i = startingIndex; i < rowCount; ++i) {
			tree.expandRow(i);
		}

		if (tree.getRowCount() != rowCount) {
			expandAllNodes(tree, rowCount, tree.getRowCount());
		}
	}

}
