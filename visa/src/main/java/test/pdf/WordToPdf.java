/**
 * WordToPdf.java
 * test
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package test.pdf;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 * 使用pdfbox技术将同一个目录下的多个pdf文件进行合并
 * @author   崔建斌
 * @Date	 2017年6月14日 	 
 */
public class WordToPdf {
	public static void main(String[] args) {
		//pdf合并工具类
		PDFMergerUtility mergePdf = new PDFMergerUtility();
		String folder = "K:\\wordToPdf";//目录路径
		String destinationFileName = "mergedTest.pdf";
		String[] filesInFolder;
		try {
			filesInFolder = getFiles(folder);
			for (int i = 0; i < filesInFolder.length; i++) {
				//循环添加要合并的pdf存放的路径
				mergePdf.addSource(folder + File.separator + filesInFolder[i]);
			}
			//设置合并生成pdf文件名称
			mergePdf.setDestinationFileName(folder + File.separator + destinationFileName);
			//合并pdf
			mergePdf.mergeDocuments();
			System.out.println("<<<合并完成");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (COSVisitorException e) {
			e.printStackTrace();
		}
	}

	private static String[] getFiles(String folder) throws IOException {
		File _folder = new File(folder);
		String[] filesInFolder;
		if (_folder.isDirectory()) {
			filesInFolder = _folder.list();
			return filesInFolder;
		} else {
			throw new IOException("路径不是目录");
		}
	}

}