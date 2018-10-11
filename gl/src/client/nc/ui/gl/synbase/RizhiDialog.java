package nc.ui.gl.synbase;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JTextArea;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

public class RizhiDialog extends UIDialog {

	private static final long serialVersionUID = 1L;

	public RizhiDialog(Container parent) {
		super(parent);
		initDialog();
	}

	private JTextArea field;

	private void initDialog() {
		// 设置对话框主题
		this.setTitle("日志文件");
		// 设置最适合的大小
		this.setSize(new Dimension(700, 550));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());

		field = new JTextArea();
		field.setEditable(false);
		field.setLineWrap(true);
		field.setText("");
		UIScrollPane scroll = new UIScrollPane(field);
		scroll.setPreferredSize(new Dimension(700, 550));
		UIPanel a_panel = new UIPanel();
		a_panel.add(scroll);
		this.add(a_panel, BorderLayout.CENTER);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}

	public void showInfoMsg(String str){
		field.setText(str);
		this.showModal();
	}
	
}
