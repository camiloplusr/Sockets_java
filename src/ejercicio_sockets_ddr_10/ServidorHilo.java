/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejercicio_sockets_ddr_10;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorHilo extends Thread {
    
    private Socket sc;
    private DataInputStream in;
    private DataOutputStream out;
    private String nombreCliente;
    
    public ServidorHilo(Socket sc, DataInputStream in, DataOutputStream out, String nombreCliente) {
        this.sc = sc;
        this.in = in;
        this.out = out;
        this.nombreCliente = nombreCliente;
    }
    
    @Override
    public void run() {
        
        int opcion;
        File f = new File("datos.txt");
        boolean salir = false;
        while (!salir) {
            
            try {
                opcion = in.readInt();
                switch (opcion) {
                    case 1:
                      // Recibo el valor ingresado a la cuenta
                      String valor = in.readUTF();
                      // escribo el numero
                      escribirNumeroAleatorio(f, valor);
                      System.out.println("Se registro valor en la cuenta: " + nombreCliente);
                      // Mando el mensaje de confirmacion al cliente
                      out.writeUTF("Valor almacenado correctamente");
                      break;   
                      
                    case 2:
                        // Recojo el numero de lineas
                        int numLineas = numeroLineasFichero(f);
                        // Envio el numero de lineas al cliente
                        out.writeInt(numLineas);
                        break;
                    
                    case 3:
                        // Obtenemos el numero de numeros del fichero
                        ArrayList<Integer> numeros = listaNumeros(f);
                        
                        // Envio el numero de elementos que hay al cliente
                        out.writeInt(numeros.size());
                        
                        // Envio al cliente los numeros uno a uno
                        for (int n:numeros) {
                            out.writeInt(n);
                        }
                        break;
                        
                    case 4:
                        salir = true;
                        break;
                    default:
                        out.writeUTF("Solo numero del 1 al 6");
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        try {
            // Cierro el socket
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Conexion cerrada con el cliente " + nombreCliente);
        
    }
    
    
    public void escribirNumeroAleatorio(File f,String valor) throws IOException{
        
        FileWriter fw = new FileWriter(f, true);
        // Por ejemplo, fer:52
        fw.write(nombreCliente + ","+valor + "\r\n");
        fw.close();
        
    }
 
    public int numeroLineasFichero(File f) throws FileNotFoundException, IOException{
        
        int numLineas = 0;
        BufferedReader br = new BufferedReader(new FileReader(f));
        
        String linea = "";
        
        while( (linea = br.readLine()) != null){
            numLineas++;
        }
        
        br.close();
        return numLineas;
        
    }
    
    public ArrayList<Integer> listaNumeros(File f) throws FileNotFoundException, IOException {
        
        ArrayList<Integer> numeros = new ArrayList<>();
        
        BufferedReader br = new BufferedReader(new FileReader(f));
        
        String linea = "";
        
        while( (linea = br.readLine()) != null){
            // Corto la linea en dos partes
            String[] partes = linea.split(",");
            // obtengo el numero
            int numero = Integer.parseInt(partes[1]);
            // añado el numero
            numeros.add(numero);
        }
        
        br.close();
        
        
        return numeros;
    }
    
    public int numeroLineasFicheroCliente(File f) throws FileNotFoundException, IOException{
        
        int numLineas = 0;
        BufferedReader br = new BufferedReader(new FileReader(f));
        
        String linea = "";
        
        while( (linea = br.readLine()) != null){
            // Corto la linea en dos partes
            String[] partes = linea.split(":");
            if(partes[0].equals(nombreCliente)){
                numLineas++;
            }
        }
        
        br.close();
        return numLineas;
        
    }
    
    public byte[] ficheroNumeroCliente(File f) throws FileNotFoundException, IOException{
        
        BufferedReader br = new BufferedReader(new FileReader(f));
        
        String linea = "";
        String contenido="";
        
        while( (linea = br.readLine()) != null){
            // Corto la linea en dos partes
            String[] partes = linea.split(":");
            if(partes[0].equals(nombreCliente)){
                contenido+=linea+"\r\n";
            }
        }
        
        br.close();
        
        return contenido.getBytes();
        
    }
    
    
}
