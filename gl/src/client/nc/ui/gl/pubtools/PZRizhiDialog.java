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
		// 设置对话框主题
		this.setTitle("日志文件");
		// 设置最适合的大小
		this.setSize(new Dimension(750, 600));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
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
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * 将回执文件显示出来
	 */
	public void showRizhiInfomation(String yearMonth, BillCardPanel bpanel,String corp,String fpath) {

		String textContest = "";// 文本内容
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
			MessageDialog.showErrorDlg(bpanel, "错误", "不支持的编码格式！");
			return;
		} catch (FileNotFoundException e) {
			MessageDialog.showErrorDlg(bpanel, "错误", "日志文件不存在或已删除！");
			return;
		} catch (IOException e) {
			MessageDialog.showErrorDlg(bpanel, "错误", e.getMessage());
			return;
		}
		field.setText(textContest);
		this.showModal();
	}

}
