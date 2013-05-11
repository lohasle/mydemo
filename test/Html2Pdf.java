import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: fule
 * Date: 13-7-14
 * Time: 下午11:28
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p/>
 * 存在进程  占用的问题   空格？？？
 */
public class Html2Pdf {

    public static void testString2Pdf() throws ParserConfigurationException, IOException, SAXException, DocumentException {
        String path = "D:\\TempTest\\office\\convent\\html2pdf\\";
        StringBuffer buf = new StringBuffer();
        // buf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        buf.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        // put in some style
        buf.append("<head><style language='text/css'>");
        buf.append("h3 { border: 1px solid #aaaaff; background: #ccccff; ");
        buf.append("padding: 1em; text-transform: capitalize; font-family: sansserif; font-weight: normal;}");
        buf.append("p { margin: 1em 1em 4em 3em; } p:first-letter { color: red; font-size: 150%; }");
        buf.append("h2 { background: #5555ff; color: white; border: 10px solid black; padding: 3em; font-size: 200%; }");
        buf.append("</style></head>");
        // generate the body
        buf.append("<body>");
        for (int i = 10; i > 0; i--) {
            buf.append("<h3>" + i + " bottles of beer on the wall, "
                    + i + " bottles of beer卡卡卡___!</h3>");
            buf.append("<p>Take one down and pass it around, "
                    + (i - 1) + " bottles of beer on the wall</p>/n");
        }
        buf.append("<h2>No more bottles of beer on the wall, no more bottles of beer. ");
        buf.append("Go to the store and buy some more, 99 bottles of beer on the wall.卡卡卡</h2>");
        buf.append("</body>");
        buf.append("</html>");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(new StringBufferInputStream(buf.toString()));
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, null);
        String outputFile = path + "100bottles.pdf";
        OutputStream os = new FileOutputStream(outputFile);
        renderer.layout();
        renderer.createPDF(os);
        os.close();
    }

    public static void html2Pdf() throws IOException, DocumentException {
        String path = "D:\\TempTest\\office\\convent\\html2pdf\\";
        String inputFile = path + "forms.html";
        String url = new File(inputFile).toURI().toURL().toString();
        String outputFile = path + "forms.pdf";
        OutputStream os = new FileOutputStream(outputFile);
        ITextRenderer render = new ITextRenderer();
        render.setDocument(url);
        render.layout();
        render.createPDF(os);
        os.close();
    }

    public static void htmlToPdf2() throws Exception {
        String outputFile = "D:\\TempTest\\office\\convent\\html2pdf\\中文.pdf";
        OutputStream os = new FileOutputStream(outputFile);
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //simsun宋体
        fontResolver.addFont("C:/Windows/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        StringBuffer html = new StringBuffer();
        // DOCTYPE 必需写否则类似于 这样的字符解析会出现错误
        html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">").
                append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />")
                .append("<style type=\"text/css\" mce_bogus=\"1\">body {font-family: SimSun;}</style>")
                .append("</head>")
                .append("<body>");
        html.append("<div>支持中文！</div>");
        html.append("<p align='center' color='red'>支持中文！_________________</p>");
        html.append("</body></html>");
        renderer.setDocumentFromString(html.toString());
        // 解决图片的相对路径问题
        // renderer.getSharedContext().setBaseURL("file:/F:/teste/html/");
        renderer.layout();
        renderer.createPDF(os);
        System.out.println("======转换成功!");
        os.close();
    }

    /**
     * 注意 html  文件的编码
     * @throws Exception
     */
    public static void htmlToPdf3() throws Exception {
        String inputFile = "D:\\TempTest\\office\\convent\\html2pdf\\付乐.html";
        String outFile = "D:\\TempTest\\office\\convent\\html2pdf\\付乐.pdf";
        OutputStream os = new FileOutputStream(outFile);
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("C:/Windows/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        String url = new File(inputFile).toURI().toURL().toString();
        System.out.println("=============url: " + url);
        renderer.setDocument(url);
        renderer.layout();
        renderer.createPDF(os);
        System.out.println("======转换成功!");
        os.close();
    }


    public static void main(String[] args) {
     /*   try {
            html2Pdf();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DocumentException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
       /* try {
            testString2Pdf();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DocumentException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

        /*try {
            htmlToPdf2();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
        try {
            htmlToPdf3();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
