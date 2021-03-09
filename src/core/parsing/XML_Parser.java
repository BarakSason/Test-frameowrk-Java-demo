package core.parsing;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XML_Parser {
	public static void parse_xml_response(String xml_string, String xml_tag) {
		try {
			/* Processing xml in file */
//			File inputFile = new File("/root/response.xml");
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(inputFile);
//			doc.getDocumentElement().normalize();
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//			NodeList nList = doc.getElementsByTagName("cliOutput");
//			System.out.println("----------------------------");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml_string));
			Document document = builder.parse(is);

			NodeList volumes = document.getElementsByTagName("volume");
			for (int i = 0; i < volumes.getLength(); i++) {
				Node vol = volumes.item(i);
				if (vol.getNodeType() == Node.ELEMENT_NODE) {
					Element eVol = (Element) vol;

					System.out.println("Displaying " + xml_tag + " list for vol "
							+ eVol.getElementsByTagName("volName").item(0).getTextContent() + ":");
					NodeList vol_paths = eVol.getElementsByTagName(xml_tag);
					for (int j = 0; j < vol_paths.getLength(); j++) {
						System.out.println(eVol.getElementsByTagName(xml_tag).item(j).getTextContent());
					}
					System.out.println();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
