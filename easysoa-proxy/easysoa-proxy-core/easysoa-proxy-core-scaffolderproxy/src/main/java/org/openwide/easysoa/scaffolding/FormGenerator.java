package org.openwide.easysoa.scaffolding;

public interface FormGenerator {

	/**
	 * Generate an HTML form from a WSDL file
	 * @param xml XML or WSDL Source file name
	 * @param xsl XSLT Source file name
	 * @param html Output HTML file name
	 * @throws Exception
	 */
	public String generateHtmlFormFromWsdl(String wsdlXmlSource, String formWsdlXmlSource, String xsl, String html) /*throws Exception*/;
	
}