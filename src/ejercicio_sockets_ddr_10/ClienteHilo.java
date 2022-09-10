package ejercicio_sockets_ddr_10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteHilo extends Thread {

    private DataInputStream in;
    private DataOutputStream out;

    public ClienteHilo(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {

    	Socket sc;
        Scanner sn = new Scanner(System.in);

        String mensaje;
        int opcion = 0;
        boolean salir = false;

        while (!salir) {

            try {
                System.out.println("1. Almacenar valor en el numero de cuenta");
                System.out.println("2. Valores almacenados hasta el momento");
                System.out.println("3. Lista de valores almacenados");
                System.out.println("4. Salir");
                
                opcion = sn.nextInt();
                out.writeInt(opcion);
                
                switch (opcion) {
                    case 1:
                    	// Capturo el valor
                    	System.out.println("Ingrese Valor: ");
                    	String valor = sn.next();
                    	// Notifico el valor ingresado
                        System.out.println("Valor ingresado: " + valor);
                        // Le mando al servidor el valor ingresado
                        out.writeUTF(valor);
                        // Recibo y muestro el mensaje
                        mensaje = in.readUTF();
                        System.out.println(mensaje);                  
                        break;
                    case 2:
                        // recibo el numero de lineas
                        int numLineas = in.readInt();
                        System.out.println("Hay " + numLineas + " valores ingresados a su número de cuenta");
                        break;
                    case 3:
                        // recibo el numero de numeros que hay
                        int limite = in.readInt();
                        // Recibo los numeros uno a uno
                        for (int i = 0; i < limite; i++) {
                            System.out.println(in.readInt());
                        }
                        break;                   
                    case 4:
                        salir = true;
                        break;
                    default:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        
                }
            } catch (IOException ex) {
                Logger.getLogger(ClienteHilo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
    
}
