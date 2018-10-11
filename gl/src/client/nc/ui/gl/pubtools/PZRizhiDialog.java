package nc.ui.gl.pubtools;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JTextArea;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.bill.BillCardPanel;

public class PZRizhiDialog extends UIDialog {

	private static final long serialVersionUID = 1L;

	public PZRizhiDialog(Container parent) {
		super(parent);
		initDialog();
	}

	private JTextArea field;

	private void initDialog() {
		// ���öԻ�������
		this.setTitle("��־�ļ�");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(750, 600));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());

		field = new JTextArea();
		field.setEditable(false);
		field.setLineWrap(true);
		field.setText("");
		UIScrollPane scroll = new UIScrollPane(field);
		scroll.setPreferredSize(new Dimension(720, 560));
		UIPanel a_panel = new UIPanel();
		a_panel.add(scroll);
		this.add(a_panel, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * ����ִ�ļ���ʾ����
	 */
	public void showRizhiInfomation(String yearMonth, BillCardPanel bpanel,String corp,String fpath) {

		String textContest = "";// �ı�����
		String filepath = fpath+corp+"-"+yearMonth+".txt";
		
		String line = null;
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "utf-8"));
			while ((line = in.readLine()) != null) {
				textContest += line + "\n";
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			MessageDialog.showErrorDlg(bpanel, "����", "��֧�ֵı����ʽ��");
			return;
		} catch (FileNotFoundException e) {
			MessageDialog.showErrorDlg(bpanel, "����", "��־�ļ������ڻ���ɾ����");
			return;
		} catch (IOException e) {
			MessageDialog.showErrorDlg(bpanel, "����", e.getMessage());
			return;
		}
		field.setText(textContest);
		this.showModal();
	}

}
