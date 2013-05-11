package com.converter.test;

import com.converter.pdfConverter.JComPDFConverter;
import com.converter.pdfConverter.JacobPDFConverter;
import com.converter.pdfConverter.OpenOfficePDFConverter;
import com.converter.pdfConverter.PDFConverter;
import com.converter.swfConverter.SWFConverter;
import com.converter.swfConverter.SWFToolsSWFConverter;

public class TestJacob {
	public static void main(String[]args) throws Exception{
		String inputFile = "D:\\test\\docTest.docx";
		String pdfFile = "D:\\test\\test.pdf";
		String swfFile = "D:\\test\\test.swf";
		JComPDFConverter.word2PDF(inputFile, pdfFile);
		//PDFConverter converter = new OpenOfficePDFConverter();
		//PDFConverter converter = new OpenOfficePDFConverter();
		//converter.convert2PDF(inputFile,pdfFile);
		//SWFConverter swfConverter = new SWFToolsSWFConverter();
		//swfConverter.convert2SWF(pdfFile, swfFile);
	}
}
