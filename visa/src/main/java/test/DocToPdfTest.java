/**
 * DocToPdfTest.java
 * test
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package test;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @author   崔建斌
 * @Date	 2017年6月14日 	 
 */
public class DocToPdfTest {

	public static void main(String[] args) {
		String[] files = { "K:\\wordToPdf\\滞在予定表.pdf", "K:\\wordToPdf\\照会.pdf" };
		String savepath = "K:\\wordToPdf\\合并后的文件2.pdf";
		mergePdfFiles(files, savepath);
	}

	public static boolean mergePdfFiles(String[] files, String newfile) {
		boolean retValue = false;
		Document document = null;
		try {
			document = new Document(new PdfReader(files[0]).getPageSize(1));
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(newfile));
			document.open();
			for (int i = 0; i < files.length; i++) {
				PdfReader reader = new PdfReader(files[i]);
				int n = reader.getNumberOfPages();
				System.out.println("获取到的页数" + n);
				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}
			retValue = true;
			System.out.println("<<<合并完成!!!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
		return retValue;
	}
}
