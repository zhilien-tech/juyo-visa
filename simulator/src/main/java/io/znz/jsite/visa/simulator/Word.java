package io.znz.jsite.visa.simulator;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Chaly on 2017/5/2.
 */
public class Word {

    //身元保証書
    public static void main(String[] args) throws Exception {
        // 1) Load ODT file and set Velocity template engine and cache it to the registry
        InputStream in = Word.class.getClassLoader().getResourceAsStream("note.docx");
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
        // 2) Create Java model context
        IContext context = report.createContext();
        context.put("name", "world");
        context.put("count", "100");
        // 3) Set PDF as format converter
        OutputStream out = new FileOutputStream(new File("/Users/Chaly/WorkSpace/JSiteV5/simulator/src/main/resources/note_Out.pdf"));
        report.process(context,out);
        PdfOptions pdfOptions = PdfOptions.create().fontProvider(new IFontProvider() {
            @Override
            public Font getFont(String familyName, String encoding, float size, int style, Color color) {
                try {
                    BaseFont bf = BaseFont.createFont(
                        Word.class.getClassLoader().getResource("simsun.ttf").getFile(),
                        BaseFont.IDENTITY_H, BaseFont.EMBEDDED
                    );
                    Font f = new Font(bf, size, style, color);
                    f.setFamily(familyName);
                    return f;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        Options options = Options.getTo(ConverterTypeTo.PDF).subOptions(pdfOptions);

        report.convert(context, options, out);
        System.exit(0);
    }
}
