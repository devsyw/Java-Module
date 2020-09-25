package module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlMapperChange {
	
	//필터
	public static String[] strList(String[] lines){
		String temp;
		ArrayList<String> resultStr = new ArrayList<String>();

		for(int i = 0; i < lines.length; i++) {
			temp = lines[i];
			temp = temp.replace(" ", "");
			
			if(temp.contains("<selectid=\"")) {
				temp = temp.replace("<selectid=\"", "");
				int dex = temp.indexOf("\"");
				resultStr.add("select," + temp.substring(0, dex));
			}
			if(temp.contains("<updateid=\"")) {
				temp = temp.replace("<updateid=\"", "");
				int dex = temp.indexOf("\"");
				resultStr.add("update," + temp.substring(0, dex));
			}
			if(temp.contains("<deleteid=\"")) {
				temp = temp.replace("<deleteid=\"", "");
				int dex = temp.indexOf("\"");
				resultStr.add("delete," + temp.substring(0, dex));
			}
			if(temp.contains("<insertid=\"")) {
				temp = temp.replace("<insertid=\"", "");
				int dex = temp.indexOf("\"");
				resultStr.add("insert," + temp.substring(0, dex));
			}
		}
		String[] array = resultStr.toArray(new String[resultStr.size()]);
		
		return array;
	};
	
	//파일 읽기
	public static String[] strLiner(InputSource is) throws SAXException, IOException, ParserConfigurationException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(is);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(writer));
		String output = writer.getBuffer().toString();
				
		String[] lines = output.split(System.getProperty("line.separator"));
		return lines;
	};
	
	//파일 
	public static InputSource isYN(String path) throws FileNotFoundException, UnsupportedEncodingException {
	    File xmlFile = new File(path);
	    InputStream inputStream = new FileInputStream(xmlFile);
	    Reader reader = new InputStreamReader(inputStream, "UTF-8");
	    InputSource is = new InputSource(reader);
	    is.setEncoding("UTF-8");
		return is;
	}
	
	//출력
	public static void resultPrt(String path, String fileName, String low) throws SAXException, IOException, ParserConfigurationException, TransformerException {
		try{
			InputSource is = isYN(path);
			String[] lines = strLiner(is);
			String[] result = strList(lines);

			int sizeNum = result.length;
			
			System.out.println(low);
			System.out.println("파일명: " + fileName);
			System.out.println("경인건수: " + sizeNum + "건");
			System.out.println();
			
			for(int j = 0; j < sizeNum; j++) {
				System.out.println(result[j]);
			}
		} catch(Exception e) {
			System.out.println(low + "데이터 없음");
		}
	}
	
	//메인
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, TransformerException {
	    String midPath1 = "";
		String midPath2 = "";
		String midPath3 = "";
		String fileName = "drawCourseMapper.xml";
	    
	    String path1 = "D:\\dev\\trash\\gyungin\\WEB-INF\\classes\\egovframework\\sqlmap\\sncs\\" + midPath1 + midPath2 + midPath3 + fileName;
	    String path2 = "D:\\dev\\trash\\gaebal\\WEB-INF\\classes\\egovframework\\sqlmap\\sncs\\" + midPath1 + midPath2 + midPath3 + fileName;

	    resultPrt(path1, fileName, "경인"); //경인데이터
	    resultPrt(path2, fileName, "개발"); //개발데이터
	}
}
