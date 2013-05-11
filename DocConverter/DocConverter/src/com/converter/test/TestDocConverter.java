package com.converter.test;

import com.converter.docConverter.DocConverter;
import com.converter.pdfConverter.JComPDFConverter;
import com.converter.pdfConverter.JacobPDFConverter;
import com.converter.pdfConverter.OpenOfficePDFConverter;
import com.converter.pdfConverter.PDFConverter;
import com.converter.swfConverter.SWFConverter;
import com.converter.swfConverter.SWFToolsSWFConverter;

public class TestDocConverter {
	public static void main(String[]args){
		//PDFConverter pdfConverter = new OpenOfficePDFConverter();
		//PDFConverter pdfConverter = new JacobPDFConverter();
		PDFConverter pdfConverter = new JComPDFConverter();
		SWFConverter swfConverter = new SWFToolsSWFConverter();
		DocConverter converter = new DocConverter(pdfConverter,swfConverter);
		String txtFile = "D:\\test\\txtTest.txt";
		String docFile = "D:\\test\\docTest.docx";
		String xlsFile = "D:\\test\\xlsTest.xlsx";
		String pptFile = "D:\\test\\pptTest.pptx";
		converter.convert(txtFile);
		converter.convert(docFile);
		converter.convert(xlsFile);
		converter.convert(pptFile);
	}
}
