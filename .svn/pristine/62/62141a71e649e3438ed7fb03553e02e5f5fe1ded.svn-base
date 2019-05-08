package com.eyunda.part1.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceUtil {

//	// 定义Web Service的命名空间
//	static final String SERVICE_NS = "http://www.hbksw.com/s/services/enroll.jws";
//	// 定义Web Service提供服务的URL
//	static final String SERVICE_URL = "http://119.97.236.11:8080/scoreService/services/enroll.jws";
	
	// 定义Web Service的命名空间
	static final String SERVICE_NS = "http://score.eyunda.com.cn:8090/score/services/enroll.jws";
	// 定义Web Service提供服务的URL
	static final String SERVICE_URL = 
		"http://score.eyunda.com.cn:8090/score/services/enroll.jws";

	public static SoapObject getState(String bmh, String xm,String sfzh,String zkzh) {

		String methodName = "load";
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		soapObject.addProperty("bmh", bmh);
		
		soapObject.addProperty("sfzh", sfzh);
		soapObject.addProperty("zkzh", zkzh);
		soapObject.addProperty("xm", xm);
		
//		<wsdl:part name="bmh" type="xsd:string"/>
//		<wsdl:part name="sfzh" type="xsd:string"/>
//		<wsdl:part name="zkzh" type="xsd:string"/>
//		<wsdl:part name="xm" type="xsd:string"/>
//		<wsdl:part name="ip" type="xsd:string"/>
		soapObject.addProperty("ip", "192.168.1.1");
		envelope.bodyOut = soapObject;
		// 设置与.Net提供的Web Service保持较好的兼容性
		envelope.dotNet = true;
		try {
			ht.call(SERVICE_NS + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			
			if(result.getPropertyCount()>0){
			SoapObject detail = (SoapObject) result.getProperty( 0);
			return detail;}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static String getScore(String sfzh, String zkzh,String bmh,String xm) {
		String methodName = "scoreToJSON";
		HttpTransportSE ht = new HttpTransportSE("http://score.eyunda.com.cn:8090/score/services/score.jws");
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapObject = new SoapObject("http://score.eyunda.com.cn:8090/score/services/score.jws", methodName);
		soapObject.addProperty("bmh", bmh);
		
		soapObject.addProperty("sfzh", sfzh);
		soapObject.addProperty("zkzh", zkzh);
		soapObject.addProperty("xm", xm);

		soapObject.addProperty("ip", "192.168.1.1");
		envelope.bodyOut = soapObject;
		// 设置与.Net提供的Web Service保持较好的兼容性
		envelope.dotNet = true;
		try {
			ht.call("http://score.eyunda.com.cn:8090/score/services/score.jws" + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			
			if(result.getPropertyCount()>0){
				String detail = (String) result.getProperty( 0);
			return detail;}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
