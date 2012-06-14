package com.bla.laa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class ConvertHtmlToPdf
{
    private static final String FONT_FILE = "ARIAL.TTF";
    private static final String BOLD_FONT_FILE = "ARIALB.TTF";
    private static final String CHARSET_NAME = "UTF-8";

    public static void main(String args[]) throws IOException, DocumentException
    {
        new ConvertHtmlToPdf().initAndConvert();
    }

    private void initAndConvert() throws IOException, DocumentException
    {
        byte htmlAsArray[] = getInputFile();
        byte pdfAsArray[] = convertHtmlToPdf(htmlAsArray);
        saveOutputFile(pdfAsArray);
    }

    public byte[] convertHtmlToPdf(byte htmlArray[]) throws DocumentException, IOException
    {
        System.out.println("country code : " + System.getProperty("user.country"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlArray);
        Reader reader = new InputStreamReader(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        URL fontResource = getClass().getResource(FONT_FILE);
        // feki constructor usage for not scanning windows/fonts
        XMLWorkerFontProvider xmlWorkerFontProvider = new XMLWorkerFontProvider(fontResource.getPath());
        xmlWorkerFontProvider.register(fontResource.getPath());

        URL fontBResource = getClass().getResource(BOLD_FONT_FILE);
        xmlWorkerFontProvider.register(fontBResource.getPath());
        xmlWorkerFontProvider.defaultEmbedding = true;

        CssAppliersImpl cssAppliers = new CssAppliersImpl(xmlWorkerFontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.charSet(Charset.forName(CHARSET_NAME));
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, writer);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdfWriterPipeline);

        XMLWorkerHelper xmlWorker = XMLWorkerHelper.getInstance();
        CSSResolver cssResolver = xmlWorker.getDefaultCssResolver(true);

        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser xmlParser = new XMLParser(worker, Charset.forName(CHARSET_NAME));
        xmlParser.parse(reader);

        reader.close();
        document.close();
        return outputStream.toByteArray();
    }

    private void saveOutputFile(byte[] pdfAsArray) throws IOException
    {
        FileOutputStream outputStream = new FileOutputStream("test.pdf");
        outputStream.write(pdfAsArray);
        outputStream.close();
    }

    private byte[] getInputFile() throws IOException
    {
        File inputHtml = new File("example.htm");
        FileInputStream fin = new FileInputStream(inputHtml);
        byte fileContent[] = new byte[(int) inputHtml.length()];
        fin.read(fileContent);

        return fileContent;
    }
}
