package com.xiao.smartband.http;

import java.io.IOException;
import java.io.StringReader;
import org.xml.sax.InputSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class HttpXml {
	public Object parse(String responseBody) throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		MedicalHandler mh = new MedicalHandler();
//		byte [] xmlBytes = responseBody.getBytes("8859_1");
//		String strXml = new String(xmlBytes, "GB2312"); 
		String strXml = responseBody;//"<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<string xmlns=\"http://tempuri.org/\"><NewDataSet> <wxhis.emr_nurscell> <�������>0329</�������> <�������>���ڿ�</�������> </wxhis.emr_nurscell> <wxhis.emr_nurscell> <�������>0332</�������> <�������>�������</�������> </wxhis.emr_nurscell> <wxhis.emr_nurscell> <�������>0335</�������> <�������>����</�������> </wxhis.emr_nurscell> <wxhis.emr_nurscell> <�������>0336</�������> <�������>���</�������> </wxhis.emr_nurscell> <wxhis.emr_nurscell> <�������>0338</�������> <�������>���</�������> </wxhis.emr_nurscell> <wxhis.emr_nurscell> <�������>0339</�������> <�������>������</�������> </wxhis.emr_nurscell> <wxhis.emr_nurscell> <�������>0340</�������> <�������>��ҽ��</�������> </wxhis.emr_nurscell> <wxhis.emr_nurscell> <�������>0610</�������> <�������>10����</�������> </wxhis.emr_nurscell> <wxhis.emr_nurscell> <�������>0609</�������> <�������>09����</�������> </wxhis.emr_nurscell> </NewDataSet></string>";
		parser.parse(new InputSource(new StringReader(strXml)), mh);

		return mh.arryXml;
	}
	class MedicalHandler extends DefaultHandler {
		private JSONArray arryXml;
		private String tagName;

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			super.characters(ch, start, length);
			String data = new String(ch, start, length);//���еĵ�����
			
			if (tagName != null && !tagName.isEmpty() && !data.isEmpty()) {
				System.out.print(tagName);
					//arryXml.put(tagName, data);
				JSONObject obj = new JSONObject();
				try{
					obj.put(tagName, data);
					arryXml.put(obj);
				}catch(JSONException e){
				}
			} 
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);

			tagName = null;//��ȡ����һ����ݣ���Ҫ��������ٰ���һ����ݸ�����
		}

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.startDocument();
			//musics = new ArrayList<Music>();
			arryXml = new JSONArray();//һ��ʼ��4���еķ���
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			super.startElement(uri, localName, qName, attributes);
			tagName = localName;//���еĵڶ����
		}

	}
}
