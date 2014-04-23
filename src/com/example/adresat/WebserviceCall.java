package com.example.adresat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
 

@SuppressWarnings("deprecation")
public class WebserviceCall {
     
	/**
	 * Deklarimi i variablave namespace dhe url per websherbimin qe do ti qasemi.
	 */
    String namespace = "http://adresat.org/";
    private String url = "http://192.168.1.130/Adresat/Sherbimi.asmx";
     
    String SOAP_ACTION;
    SoapObject request = null, objMessages = null;
    SoapSerializationEnvelope envelope;
    AndroidHttpTransport androidHttpTransport;
     
    WebserviceCall() {
    }
 
     
    /**
     * Krijojme zarfin e SOAP
     */
    protected void SetEnvelope() {
         
        try {
             
            /**
             * / Krijojme zarfin e SOAP          
             */
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
             
            //You can comment that line if your web service is not .NET one.
            envelope.dotNet = true;
             
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new AndroidHttpTransport(url);
            androidHttpTransport.debug = true;
             
        } catch (Exception e) {
              
        }
    }
 
    /**
     * Kjo metode therret nje metode te web sherbimit me argumentet perkatese.
     */
    public String thirrMetoden(String Metoda, String[] argumentet) 
      {
    	
         
        try {
            SOAP_ACTION = namespace + Metoda;
             
            //Shtojme argumentet ne kerkese
            request = new SoapObject(namespace, Metoda);
            
            if(argumentet.length>0)
            {
            	for(int i=0;i<argumentet.length;i+=2)
            	{
            		
            		request.addProperty(argumentet[i],""+argumentet[i+1]);
            	}
            
            }            
            SetEnvelope();
             
            try {
                 
                //Dergojme SOAP kerkesen
                androidHttpTransport.call(SOAP_ACTION, envelope);
                 
                //Marrim pergjigjjen nga Web Sherbimi
                String result = envelope.getResponse().toString();
 
                return result;
                 
            } catch (Exception e) {
                // TODO: handle exception
                return e.toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }
 
    }

    
    
}