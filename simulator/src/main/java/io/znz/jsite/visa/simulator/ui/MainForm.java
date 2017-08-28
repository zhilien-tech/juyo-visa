/*
 * Created by JFormDesigner on Sat Mar 11 16:29:45 CST 2017
 */

package io.znz.jsite.visa.simulator.ui;

import io.znz.jsite.visa.simulator.util.HttpClientUtil;
import io.znz.jsite.visa.simulator.util.ResultObject;
import io.znz.jsite.visa.simulator.util.ZFile;
import io.znz.jsite.visa.simulator.util.ZipUtils;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.zip.ZipOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.nutz.lang.Files;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jgoodies.forms.factories.Borders;
import com.uxuexi.core.common.util.Util;

/**
 * @author 朱晓川
 */
public class MainForm extends JPanel {

	private static final String TASK_FETCH_URI = "visa/simulator/fetchJapan";
	private static final String ENCODING = "UTF-8";

	private static final String TASK_SUBMITING_URI = "visa/simulator/ds160Japan/";
	private static final String VISA_UPLOAD_URI = "visa/simulator/UploadJapan/";

	private static final String AGENT_DOWNLOAD_URL = "visa/simulator/agentDownload.html";

	private static final String FEED_ERROR_URL = "visa/simulator/japanErrorHandle/";

	private static final String HISTORY = "history";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");

	private static final int SUCCESS = 0;

	public MainForm() {
		initComponents();
		pyFile.setText(System.getProperty("user.dir") + "/conf/japan.py");
		host.setText("http://218.244.148.21");
		port.setText("9004");
		upload.setEnabled(false);//禁用上传按钮
		filesPicker.setEnabled(false); //禁用选择文件上传的按钮
	}

	//拼接 url 地址
	String getBaseUrl() {
		return "http://" + host.getText() + ":" + port.getText() + "/";
	}

	/**
	 * 将命令行的错误信息输出到日志区(逐行)
	 */
	void transferLog(final InputStream inStream) {
		new Thread(new Runnable() {
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
				try {
					String line;
					while ((line = br.readLine()) != null) {
						log(line + "\n");
					}
				} catch (IOException e) {
					System.out.println("从BufferedReader读取错误：" + e);
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(br);
				}
			}
		}).start();
	}

	//向控制台输出日志
	void log(final String msg) {
		if (StringUtils.isBlank(msg))
			return;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textArea.append(dateFormat.format(new Date()) + ":" + msg + "\n");
			}
		});
	}

	//执行命令行
	int executeCommand(final String command) {
		int result = 0;
		try {
			Process p = Runtime.getRuntime().exec(command);
			transferLog(p.getInputStream());
			transferLog(p.getErrorStream());
			result = p.waitFor();
			log("执行python命令返回结果:" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 封装文件选择器调用
	 *
	 * @param text  选择按钮的提示文字
	 * @param multi 是否多选
	 * @param ext   支持的文件扩展名
	 * @return 选择的文件数组
	 */
	@Deprecated
	private File[] openFilePicker(String text, boolean multi, final String... ext) {
		Preferences pref = Preferences.userRoot().node(this.getClass().getName());
		String history = pref.get(HISTORY, FileUtils.getUserDirectoryPath());
		JFileChooser jfc = new JFileChooser(history);//在当前目录下，创建文件选择器
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setMultiSelectionEnabled(multi);
		jfc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isFile()) {
					boolean result = false;
					for (String s : ext) {
						if (result = f.getName().toLowerCase().endsWith(s.toLowerCase())) {
							break;
						}
					}
					return result;
				}
				return true;
			}

			@Override
			public String getDescription() {
				StringBuilder sb = new StringBuilder();
				for (String s : ext) {
					sb.append("*." + s + ";");
				}
				return sb.toString();
			}
		});
		int nRetVal = jfc.showDialog(this, text);
		File files[] = {};
		if (nRetVal == JFileChooser.APPROVE_OPTION) {
			files = multi ? jfc.getSelectedFiles() : new File[] { jfc.getSelectedFile() };
		}
		for (File file : files) {
			pref.put(HISTORY, file.getPath());
		}
		return files;
	}

	//选择 Pathon 脚本文件的位置按钮点击事件
	private void pyPickerActionPerformed(ActionEvent e) {
		File files[] = openFilePicker("选择Python脚本", false, "py");
		for (File file : files) {
			pyFile.setText(file.getAbsolutePath());
		}
	}

	//检测是否有可执行的任务
	ResultObject<Map, Object> checkTaskAvail() {
		log("检测是否有可执行的任务...");
		String json = HttpClientUtil.get(getBaseUrl() + TASK_FETCH_URI);
		ResultObject<Map, Object> task = JSON.parseObject(json, ResultObject.class);

		if (task.getCode() == ResultObject.ResultCode.SUCCESS) {
			final String oid = String.valueOf(task.getAttributes().get("oid"));
			final String ordernumber = String.valueOf(task.getAttributes().get("ordernumber"));
			log("检测到任务:" + ordernumber);
			return task;
		} else {
			log("暂无可执行的任务,10秒后再次检测");
			//没有任务
			try {
				Thread.currentThread().sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	//执行按钮点击事件
	private void executeTaskActionPerformed(ActionEvent actionEvent) {
		executeTask.setEnabled(false);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						excuteTask();
					}
				} catch (Exception exception) {
					//控制台输出异常信息
					MainForm.this.log(ExceptionUtils.getStackTrace(exception));
				}
			}
		}).start();

	}

	/**执行填表任务*/
	boolean excuteTask() throws IOException {
		long startTime = System.currentTimeMillis();
		ResultObject<Map, Object> task = null;
		//循环检测任务
		while (true) {
			try {
				task = checkTaskAvail();
			} catch (Exception e) {
				e.printStackTrace();
				log("========================任务检测异常，请检查服务器响应==========================");
				try {
					log("...10秒后再次检测");
					Thread.currentThread().sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			if (!Util.isEmpty(task)) {
				break;
			}
		}

		final String oid = String.valueOf(task.getAttributes().get("oid"));
		final String ordernumber = String.valueOf(task.getAttributes().get("ordernumber"));

		//准备提交填表任务
		String ds160rs = HttpClientUtil.get(getBaseUrl() + TASK_SUBMITING_URI + oid);
		ResultObject<Map, Object> ds160ro = JSON.parseObject(ds160rs, ResultObject.class);
		if (ds160ro.getCode() == ResultObject.ResultCode.SUCCESS) {
			log("任务码:" + ordernumber + " 提交中...");
		} else {
			log("任务码:" + ordernumber + "准备提交失败");
			return false;
		}

		log("========================开始执行任务:" + ordernumber + "===========================");
		String remoteFileUrl = String.valueOf(task.getData().get("excelUrl"));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				taskId.setText(oid);
			}
		});

		/*
		 * 把用户签证的json数据信息保存到本地以供python脚本使用。
		 * 在用户当前工作目录内创建名为tmp的文件夹用于保存文件
		 */
		File tmp = new File(System.getProperty("user.dir") + "/tmp");
		if (!tmp.exists())
			tmp.mkdirs();

		//保存名录excel文件
		String suffix = Files.getSuffix(remoteFileUrl);
		String localExcelUrl = tmp.getAbsolutePath() + File.separator + oid + suffix;
		String requestUrl = getBaseUrl() + AGENT_DOWNLOAD_URL;
		HttpClientUtil.agentPostDownload(requestUrl, remoteFileUrl, localExcelUrl);

		//替换json 中的文件为绝对路径，保存json文件
		task.getData().put("excelUrl", localExcelUrl);
		log(localExcelUrl);
		File target = new File(tmp, oid + ".json");
		//保存json文件，使用UTF-8编码
		FileUtils.writeStringToFile(target, JSON.toJSONString(task.getData()), ENCODING);
		log("任务数据文件已保存至:" + target.getAbsolutePath());

		//执行命令行
		File python = new File(pyFile.getText());
		if (python.exists()) {
			//执行脚本，提交任务，第三个参数为任务码
			String cmd = "python " + pyFile.getText() + " " + target.getAbsolutePath() + " " + oid;
			command.setText(cmd);
			int execResult = executeCommand(cmd);
			//如果命令行意外退出
			if (SUCCESS != execResult) {
				IdentityHashMap<String, String> params = new IdentityHashMap<String, String>();
				params.put("errorCode", "0");
				params.put("errorMsg", "未知错误");
				String feedErrorUrl = getBaseUrl() + FEED_ERROR_URL + oid;
				HttpClientUtil.post(feedErrorUrl, params);
			}
		} else {
			JOptionPane.showMessageDialog(new JLabel(), "自动化脚本不存在");
		}
		log("========================任务:" + ordernumber + " 执行完毕===========================");
		log("========================总计用时:" + ((System.currentTimeMillis() - startTime) / 1000)
				+ "秒===========================");
		return true;
	}

	/**
	 * 上传文件的按钮点击事件,自动上传归国报告，因此废弃此方法
	 */
	@Deprecated
	private void uploadActionPerformed(ActionEvent e) {
		if (StringUtils.isBlank(resultFiles.getText())) {
			JOptionPane.showMessageDialog(new JLabel(), "请先选择上传文件!");
			return;
		}
		final String tid = taskId.getText();
		if (StringUtils.isBlank(tid)) {
			JOptionPane.showMessageDialog(new JLabel(), "任务码不能为空!");
			return;
		}
		executeTask.setEnabled(false);//为了防止重新执行任务禁用执行 btn
		upload.setEnabled(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String files[] = resultFiles.getText().split(";");
				//这里是打包zip文件的方法
				List<ZFile> zfiles = Lists.newArrayList();
				for (String f : files) {
					File file = new File(f);
					InputStream is;
					try {
						is = new FileInputStream(file);
						String fileName = file.getName();

						ZFile zf = new ZFile();
						zf.setInput(is);
						zf.setFileName(fileName);
						zf.setRelativePathInZip(tid + "/");
						zfiles.add(zf);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}

				File tmp = new File(System.getProperty("user.dir") + "/tmp");
				if (!tmp.exists())
					tmp.mkdirs();

				try {
					String zipPath = System.getProperty("user.dir") + "/tmp/" + tid + ".zip";
					ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath));

					//将文件进行打包(将数据写入zos)
					ZipUtils.zipFile(zfiles, zos);
					zos.flush();
					zos.close();

					File zipFile = new File(zipPath);

					IdentityHashMap params = new IdentityHashMap();
					String json = HttpClientUtil.upload(getBaseUrl() + VISA_UPLOAD_URI + tid, zipFile, params);
					ResultObject<String, ?> ro = JSON.parseObject(json, ResultObject.class);
					if (ro.getCode() == ResultObject.ResultCode.SUCCESS) {
						log("任务码为" + tid + "的任务执行完毕");
					} else {
						log("任务码为" + tid + "的任务执行失败,请核实后再尝试,原因:" + ro.getMsg());
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				executeTask.setEnabled(true);
				upload.setEnabled(true);
				taskId.setText("");//清空任务码
			}
		}).start();
	}

	/**
	 * 选择上传文件的按钮点击事件
	 * <p>
	 * 自动上传归国报告，废弃此方法
	 */
	@Deprecated
	private void filesPickerActionPerformed(ActionEvent e) {
		File files[] = openFilePicker("选择帰国報告文件", true, "pdf");
		if (files.length == 0)
			return;
		StringBuilder sb = new StringBuilder();
		StringBuilder tmp = new StringBuilder();
		for (File file : files) {
			sb.append(file.getAbsolutePath()).append(";");
			tmp.append(StringUtils.substringAfterLast(file.getName(), "."));
		}
		if (!tmp.toString().matches("(?=.*pdf)^.*$")) {
			JOptionPane.showMessageDialog(new JLabel(), "文件必须是pdf类型!");
			return;
		}
		if (StringUtils.isNotBlank(sb)) {
			resultFiles.setText(sb.toString());
		}
		checkUploadBtnEnable();
	}

	//检查上传按钮的可用状态
	private void checkUploadBtnEnable() {
		if (StringUtils.isNotBlank(taskId.getText()) && StringUtils.isNotBlank(resultFiles.getText())) {
			upload.setEnabled(true);
		} else {
			upload.setEnabled(false);
		}
	}

	//任务码输入框的输入监控
	private void taskIdKeyReleased(KeyEvent e) {
		checkUploadBtnEnable();
	}

	//控制台添加右键菜单
	private void textAreaMousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			textArea.requestFocus();
			copy.setEnabled(StringUtils.isNotBlank(textArea.getSelectedText()));
			popupMenu.show(textArea, e.getX(), e.getY());
		}
	}

	//复制选中的文字到剪切板
	private void copyActionPerformed(ActionEvent e) {
		textArea.copy();
	}

	//清空控制台
	private void clearActionPerformed(ActionEvent e) {
		textArea.setText("");
	}

	//全选
	private void allActionPerformed(ActionEvent e) {
		textArea.selectAll();
	}

	//初始化界面，以及界面元素的监听事件
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		label2 = new JLabel();
		pyFile = new JTextField();
		pyPicker = new JButton();
		label1 = new JLabel();
		host = new JTextField();
		label4 = new JLabel();
		port = new JTextField();
		executeTask = new JButton();
		label6 = new JLabel();
		command = new JTextField();
		panel3 = new JPanel();
		label5 = new JLabel();
		resultFiles = new JTextField();
		filesPicker = new JButton();
		label3 = new JLabel();
		taskId = new JTextField();
		upload = new JButton();
		panel4 = new JPanel();
		scrollPane1 = new JScrollPane();
		textArea = new JTextArea();
		popupMenu = new JPopupMenu();
		copy = new JMenuItem();
		clear = new JMenuItem();
		all = new JMenuItem();

		//======== this ========
		setBorder(Borders.DLU2);
		setLayout(new GridBagLayout());
		((GridBagLayout) getLayout()).columnWidths = new int[] { 600, 0 };
		((GridBagLayout) getLayout()).rowHeights = new int[] { 0, 0, 200, 0 };
		((GridBagLayout) getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
		((GridBagLayout) getLayout()).rowWeights = new double[] { 0.0, 0.0, 1.0, 1.0E-4 };

		//======== panel1 ========
		{
			panel1.setBorder(new CompoundBorder(new TitledBorder("\u4efb\u52a1"), Borders.DLU2));
			panel1.setLayout(new GridBagLayout());
			((GridBagLayout) panel1.getLayout()).columnWidths = new int[] { 0, 0, 0, 0 };
			((GridBagLayout) panel1.getLayout()).rowHeights = new int[] { 0, 0, 0, 0, 0 };
			((GridBagLayout) panel1.getLayout()).columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0E-4 };
			((GridBagLayout) panel1.getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0E-4 };

			//---- label2 ----
			label2.setText("PY\u811a\u672c");
			panel1.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			//---- pyFile ----
			pyFile.setEditable(false);
			pyFile.setToolTipText("\u811a\u672c\u6587\u4ef6\u4f4d\u7f6e,\u9ed8\u8ba4\u5728 jar \u6587\u4ef6\u540c\u76ee\u5f55\u7684 conf \u4e0b");
			panel1.add(pyFile, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			//---- pyPicker ----
			pyPicker.setText("\u6d4f\u89c8...");
			pyPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					pyPickerActionPerformed(e);
				}
			});
			panel1.add(pyPicker, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

			//---- label1 ----
			label1.setText("\u670d\u52a1\u5668");
			panel1.add(label1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			//---- host ----
			host.setToolTipText("\u57df\u540d\u6216\u8005 IP \u5730\u5740");
			host.setText("127.0.0.1");
			panel1.add(host, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			//---- label4 ----
			label4.setText("\u7aef\u53e3\u53f7");
			panel1.add(label4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			//---- port ----
			port.setToolTipText("\u9ed8\u8ba480\u7aef\u53e3\u53ef\u4e0d\u586b");
			panel1.add(port, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			//---- executeTask ----
			executeTask.setText("\u6267\u884c");
			executeTask.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					executeTaskActionPerformed(e);
				}
			});
			panel1.add(executeTask, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

			//---- label6 ----
			label6.setText("\u547d\u4ee4\u884c");
			panel1.add(label6, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

			//---- command ----
			command.setText("ping -c 10 www.baidu.com");
			command.setToolTipText("\u6d4b\u8bd5\u4e13\u7528");
			panel1.add(command, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

		//======== panel3 ========
		{
			panel3.setBorder(new CompoundBorder(new TitledBorder("\u4e0a\u4f20"), new EmptyBorder(5, 5, 5, 5)));
			panel3.setLayout(new GridBagLayout());
			((GridBagLayout) panel3.getLayout()).columnWidths = new int[] { 0, 0, 0, 0 };
			((GridBagLayout) panel3.getLayout()).rowHeights = new int[] { 0, 0, 0 };
			((GridBagLayout) panel3.getLayout()).columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0E-4 };
			((GridBagLayout) panel3.getLayout()).rowWeights = new double[] { 0.0, 0.0, 1.0E-4 };

			//---- label5 ----
			label5.setText("\u6587\u4ef6\u96c6");
			panel3.add(label5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			//---- resultFiles ----
			resultFiles.setEditable(false);
			resultFiles
					.setToolTipText("DS-160\u5728\u7ebf\u7b7e\u8bc1\u7533\u8bf7\u5f97\u5230\u7684 pdf \u548c bat \u6587\u4ef6");
			panel3.add(resultFiles, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			//---- filesPicker ----
			filesPicker.setText("\u6d4f\u89c8...");
			filesPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					filesPickerActionPerformed(e);
				}
			});
			panel3.add(filesPicker, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

			//---- label3 ----
			label3.setText("\u4efb\u52a1\u7801");
			panel3.add(label3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

			//---- taskId ----
			taskId.setToolTipText("\u5355\u72ec\u4e0a\u4f20\u6587\u4ef6\u65f6\u523b\u624b\u52a8\u586b\u5199\u4efb\u52a1\u7801");
			taskId.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					taskIdKeyReleased(e);
				}
			});
			panel3.add(taskId, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

			//---- upload ----
			upload.setText("\u4e0a\u4f20");
			upload.setEnabled(false);
			upload.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					uploadActionPerformed(e);
				}
			});
			panel3.add(upload, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		add(panel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

		//======== panel4 ========
		{
			panel4.setBorder(new CompoundBorder(new TitledBorder("\u63a7\u5236\u53f0"), new EmptyBorder(5, 5, 5, 5)));
			panel4.setLayout(new GridBagLayout());
			((GridBagLayout) panel4.getLayout()).columnWidths = new int[] { 0, 0 };
			((GridBagLayout) panel4.getLayout()).rowHeights = new int[] { 0, 0 };
			((GridBagLayout) panel4.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
			((GridBagLayout) panel4.getLayout()).rowWeights = new double[] { 1.0, 1.0E-4 };

			//======== scrollPane1 ========
			{

				//---- textArea ----
				textArea.setBackground(Color.black);
				textArea.setForeground(Color.white);
				textArea.setEditable(false);
				textArea.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						textAreaMousePressed(e);
					}
				});
				scrollPane1.setViewportView(textArea);
			}
			panel4.add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		add(panel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		//======== popupMenu ========
		{

			//---- copy ----
			copy.setText("\u590d\u5236");
			copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit()
					.getMenuShortcutKeyMask()));
			copy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					copyActionPerformed(e);
				}
			});
			popupMenu.add(copy);
			popupMenu.addSeparator();

			//---- clear ----
			clear.setText("\u6e05\u7a7a");
			clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit()
					.getMenuShortcutKeyMask()));
			clear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					clearActionPerformed(e);
				}
			});
			popupMenu.add(clear);

			//---- all ----
			all.setText("\u5168\u9009");
			all.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit()
					.getMenuShortcutKeyMask()));
			all.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					allActionPerformed(e);
				}
			});
			popupMenu.add(all);
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JLabel label2;
	private JTextField pyFile;
	private JButton pyPicker;
	private JLabel label1;
	private JTextField host;
	private JLabel label4;
	private JTextField port;
	private JButton executeTask;
	private JLabel label6;
	private JTextField command;
	private JPanel panel3;
	private JLabel label5;
	private JTextField resultFiles;
	private JButton filesPicker;
	private JLabel label3;
	private JTextField taskId;
	private JButton upload;
	private JPanel panel4;
	private JScrollPane scrollPane1;
	private JTextArea textArea;//控制台日志区
	private JPopupMenu popupMenu;
	private JMenuItem copy;
	private JMenuItem clear;
	private JMenuItem all;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
