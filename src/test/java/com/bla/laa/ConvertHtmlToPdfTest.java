package com.bla.laa;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class ConvertHtmlToPdfTest {

	@Test
	public void testName() throws Exception {
		ConvertHtmlToPdf htmlToPdf = new ConvertHtmlToPdf();

		String unicodeStr = "Atsižvelgiant į pirmiau nurodytą Bendrovės teisėtą interesą";
		String unicodeHtml = "<p style=\"font-family: Arial,serif \" >" + unicodeStr + "</p>";
		byte pdfArray[] = htmlToPdf.convertHtmlToPdf(unicodeHtml.getBytes());

		String extractedString = extractTextFromPdf(pdfArray);
		assertEquals(unicodeStr, extractedString);
	}


	private String extractTextFromPdf(byte pdfAsByteArray[]) throws IOException, IOException {
		PdfReader reader = new PdfReader(pdfAsByteArray);
		TextRenderListener listener = new TextRenderListener();
		PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);
		PdfDictionary pageDic = reader.getPageN(1);
		PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);

		byte array[] = ContentByteUtils.getContentBytesForPage(reader, 1);
		processor.processContent(array, resourcesDic);

		return listener.getSb();
	}

}


