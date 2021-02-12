/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author carlo
 */
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Ejecuta una conexion SSH con el servidor
 */
public class Main {

    
    private static final Integer puerto = 22;
    
    static Session session;
    static ChannelExec channelExec;
    String log="";
    String user;
    public Main(String usuario,String servidor,String clave) throws JSchException {
        JSch jSSH = new JSch();
        user=usuario;
        session = jSSH.getSession(usuario, servidor, puerto);
        UserInfo ui = new SesionUsuario(clave, null);
        session.setUserInfo(ui);
        session.setPassword(clave);
        session.connect();
        log=user+"=:>"+'\n';
    }

    public String execute(String cmd) throws JSchException, IOException {
        channelExec = (ChannelExec) session.openChannel("exec");
        
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(cmd);
        channelExec.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String linea = "";
        while ((linea = reader.readLine()) != null) {
            log += linea+'\n';
        }
        log='\n'+log+user+"=:>"+'\n';
        return log;
    }
    
    public void disconnect(){
        channelExec.disconnect();
        session.disconnect();
    }

    public boolean connect(String usuario, String servidor, String clave) throws JSchException {
        JSch jSSH = new JSch();
        Session session = jSSH.getSession(usuario, servidor, puerto);
        UserInfo ui = new SesionUsuario(clave, null);
        session.setUserInfo(ui);
        session.setPassword(clave);
        session.connect();
        if (session.isConnected()) {
            session.disconnect();
            return true;
        } else {
            return false;
        }
    }
}
