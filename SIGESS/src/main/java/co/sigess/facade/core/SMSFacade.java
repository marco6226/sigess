/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.sigess.facade.core;

import co.sigess.entities.com.Mensaje;
import co.sigess.entities.com.TipoMensaje;
import co.sigess.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author fmoreno
 */
@Stateless
public class SMSFacade {

    public final static String PROPKEY_URL = "url";
    public final static String PROPKEY_PUBLIC_API_KEY = "publicApiKey";

    @EJB
    private LoaderFacade loaderFacade;

    public Mensaje enviarSms(String destinatario, String mensaje) throws MalformedURLException, IOException {
        Properties prop = this.loaderFacade.getSmsProperties();
        URL url = new URL(prop.getProperty(PROPKEY_URL));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "App " + prop.getProperty(PROPKEY_PUBLIC_API_KEY));
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setConnectTimeout(10000);
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(this.construirMensaje(destinatario, mensaje).getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        Logger.getLogger(SMSFacade.class.getName()).log(Level.SEVERE, "POST Response Code :  {0}", responseCode);
        Logger.getLogger(SMSFacade.class.getName()).log(Level.SEVERE, "POST Response msg :  {0}", con.getResponseMessage());

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Logger.getLogger(SMSFacade.class.getName()).log(Level.INFO, "POST Response:  {0}", response.toString());
            InfobipSMSResponse resp = Util.fromJson(response.toString(), InfobipSMSResponse.class);
            if ("REJECTED_DESTINATION".equals(resp.messages.get(0).status.name)) {
                return new Mensaje(
                        "NÚMERO MÓVIL NO VÁLIDO",
                        "El número móvil registrado presenta inconsistencias, por favor consulte con el administrador",
                        TipoMensaje.warn,
                        Mensaje.COD_MOVIL_NO_VALIDO
                );
            } else {
                return null;
            }
        } else {
            return new Mensaje(
                    "ERROR AL ENVIAR PIN", "Se ha producido un error al enviar el pin, por favor consulte con el administrador",
                    TipoMensaje.error,
                    Mensaje.COD_ERROR_ENVIO_SMS
            );
        }
    }

    private String construirMensaje(String para, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"to\":\"")
                .append(para)
                .append("\",\"text\":\"")
                .append(msg)
                .append("\"}");
        return sb.toString();
    }

}

class InfobipSMSResponse {

    public List<InfobipSMSMessage> messages = null;
}

class InfobipSMSMessage {

    public String to;
    public InfobipSMSStatus status;
    public String smsCount;
    public String messageId;
}

class InfobipSMSStatus {

    public String groupId;
    public String groupName;
    public String id;
    public String name;
    public String description;
    public String action;
}
