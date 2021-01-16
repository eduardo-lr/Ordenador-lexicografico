package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Pila;
import mx.unam.ciencias.edd.Cola;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter; 

public class Proyecto1 {

	public static void main (String[] args) {

		/* Cola para guardar temporalmente los archivos pasados como parámetros*/
		Cola<String> archivos = new Cola<>();
		/* Árbol rojinegro con las lineas a ordenar. El árbol permanece ordenado
		cada vez que una línea nueva se agrega. */
		ArbolRojinegro<Linea> lineas = new ArbolRojinegro<>();
		/* Pila para imprimir las lineas en reversa si es el caso. */
		Pila<Linea> pila = new Pila<>();
		/* Ésta cadena tendrá el nombre del archivo de salida, si existe. */
		String salida = null; 
		/* Para la bandera 'r'. */
		boolean reversa = false; 
		/* Para la bandera 'o'. */
		boolean guarda = false;	
		/* Auxiliar para que cuando se reciba la bandera 'o' se guarde lo que sigue 
		como el archivo de salida, salvo en el caso en el cual el archivo de salida
		se pasa en la misma cadena que la bandera.*/
		boolean auxiliar = false; 

		for (String s: args) {
			if (!auxiliar) { 
				if (s.contains("-o")) {
					guarda = true;
					if (s.equals("-o"))
						auxiliar = true;
					else 
						salida = s.substring(2);
				} else if (s.contains("-ro")) {
					reversa = true;
					guarda = true;	
					if (s.equals("-ro")) 
						auxiliar = true;
					else 
						salida = s.substring(3);	
				} else if (s.equals("-r")) 
					reversa = true;
				else 
					archivos.mete(s);
			} else {
				salida = s;
				auxiliar = false;	
			}
		}

		/* Leemos los archivos de entrada que guardamos en la cola de archivos. */
		while (!archivos.esVacia()) {
			String s = archivos.saca();		
			try {
        	    FileInputStream fileIn = new FileInputStream(s);
        	    InputStreamReader isIn = new InputStreamReader(fileIn);
        	  	BufferedReader in = new BufferedReader(isIn);
				String linea = in.readLine();
				while (linea != null) {
					lineas.agrega(new Linea(linea));
					linea = in.readLine();
				}
        	    in.close();
        	} catch (IOException e) {
            	System.out.printf("No se pudo cargar el archivo \"%s\" \n", s);
            	System.exit(1);
        	}
		}

		/* Si el árbol es vacío, leemos de la entrada estándar. */
		if (lineas.esVacia()) {
			try {
				InputStreamReader isIn = new InputStreamReader(System.in);
				BufferedReader in = new BufferedReader(isIn);
				String linea = in.readLine();
				while (linea != null) {
					lineas.agrega(new Linea(linea));
					linea = in.readLine();
				}
        	    in.close();
			} catch (IOException e) {
				System.out.println("Error de entrada estandar.");
				System.exit(1);		
			}
		}

		/* Si queremos mostrar las lineas en reversa usamos la pila. */
		if (reversa)
			lineas.dfsInOrder(v -> pila.mete(v.get()));

		/* Si es el caso, guardamos en el archivo de salida. */
		if (guarda) {
			try {
            	FileOutputStream fileOut = new FileOutputStream(salida);
            	OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
            	BufferedWriter out = new BufferedWriter(osOut);
				if (reversa) {
					while (!pila.esVacia())
						out.write(pila.saca().get()+"\n");
				} else {
					for (Linea l: lineas) 
						out.write(l.get()+"\n");
				}
            	out.close();
        	} catch (IOException e) {
            	System.out.printf("No pude guardar en el archivo \"%s\".\n", salida);
            	System.exit(1);
        	} catch (NullPointerException e) {
				System.out.println("La opción 'o' requiere un argumento.");
            	System.exit(1);
			}
		} else {
			/* Si no, imprimimos el texto ordenado en pantalla. */
			if (reversa) {
				while (!pila.esVacia())
					System.out.println(pila.saca().get());
			} else {
				for (Linea l: lineas) 
					System.out.println(l.get());
			} 
		}
	}
}
