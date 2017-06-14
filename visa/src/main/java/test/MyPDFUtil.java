/**
 * MyPDFUtil.java
 * test
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

/**
 * 页码较多的pdf文件切割
 * @author   崔建斌
 * @Date	 2017年6月14日 	 
 */
public class MyPDFUtil {
	public static void main(String[] args) {
		//测试截取mergedTest.pdf文件(总共有6页),从第一页开始截取到第三页结束,会生成一个新的pdf文件且只有mergedTest.pdf文件的前三页
		partitionPdfFile("K:\\wordToPdf\\mergedTest.pdf", "新文件.pdf", 1, 3);
	}

	/** 
	 * 截取pdfFile的第from页至第end页，组成一个新的文件名 
	 * @param pdfFile 
	 * @param subfileName 
	 * @param from 
	 * @param end 
	 */
	public static void partitionPdfFile(String pdfFile, String newFile, int from, int end) {
		Document document = null;
		PdfCopy copy = null;
		try {
			PdfReader reader = new PdfReader(pdfFile);
			int n = reader.getNumberOfPages();
			if (end == 0) {
				end = n;
			}
			ArrayList<String> savepaths = new ArrayList<String>();
			String staticpath = pdfFile.substring(0, pdfFile.lastIndexOf("\\") + 1);
			System.out.println("staticpath:" + staticpath);
			String savepath = staticpath + newFile;
			System.out.println("savepath:" + savepath);
			savepaths.add(savepath);
			document = new Document(reader.getPageSize(1));
			copy = new PdfCopy(document, new FileOutputStream(savepaths.get(0)));
			document.open();
			for (int j = from; j <= end; j++) {
				document.newPage();
				PdfImportedPage page = copy.getImportedPage(reader, j);
				copy.addPage(page);
			}
			System.out.println("<<<结束");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
	}
}
