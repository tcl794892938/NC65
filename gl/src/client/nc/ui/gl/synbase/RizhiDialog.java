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
		// ���öԻ�������
		this.setTitle("��־�ļ�");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(700, 550));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
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
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}

	public void showInfoMsg(String str){
		field.setText(str);
		this.showModal();
	}
	
}
