package mx.unam.ciencias.edd.proyecto1;

/* Clase para implementar el método compareTo adecuado. */
public class Linea implements Comparable<Linea> {

	/* Las cadenas se comparan caracter por caracter */
	private char[] caracteres;

	private int longitud;

	/* Arreglo con letras con acentos que no queremos tener. */
	private static char[] arr1;
	/* Arreglo con las letras que usaremos para remplazar a las anteriores. */
	private static char[] arr2;


	public Linea(String string) {
		caracteres = string.toCharArray();
		longitud = caracteres.length;
	}

	static {
		arr1 = new char[]{'á','é','í','ó','ú','Á','É','Í','Ó','Ú','ñ','Ñ','ü','Ü'};
		arr2 = new char[]{'a','e','i','o','u','A','E','I','O','U','n','N','u','U'};
	}

	public String get() {
		return String.valueOf(caracteres);
	}

	@Override public int compareTo(Linea linea) {
		/* Usamos el primer caso de comparación, 
		sólo números y letras e ignorando mayúsculas. */
		return compareTo(linea, 0, 0, Caso.UNO); 
	}

	/* Método auxiliar para hacer recursion en compareTo. */
	private int compareTo(Linea linea, int it1, int it2, Caso caso) {
		/* Si recorrimos ambas cadenas y no hay diferencias, o ambas son vacías. */
		if (longitud == it1 && linea.longitud == it2) {
			switch (caso) {
				/* Si con el caso uno se compararon iguales pasamos 
				al segundo caso, tomando las mayúsculas en cuenta. */
				case UNO:
					return compareTo(linea, 0, 0, Caso.DOS);
				/* Si con el segundo caso se compararon tambien iguales 
				pasamos al tercer caso, tomando en cuenta cualquier caracter. */ 
				case DOS:
					return compareTo(linea, 0, 0, Caso.TRES);
				/* Si llegamos a este caso, las cadenas son iguales. */
				default:
					return 0;
			}
		}
		/* Si la cadena que mando llamar el método ya fue 
		recorrida por completo o es vacía, y la otra no. */
		else if (longitud == it1) 
			return -1;
		/* Si la cadena pasada como parámetro ya fue 
		recorrida por completo o es vacía, y la otra no. */
		else if (linea.longitud == it2) 
			return 1;
		else {
			/* arreglo con los dos caracteres a comparar. */
			char[] actual = {sinAcentos(caracteres[it1]), 
								sinAcentos(linea.caracteres[it2])};
			/* arreglo con los caracteres a comparar audicionados como números. */
			int[] auxiliares = new int[2];
			for (int k = 0; k < 2; k++) {
				/* Sólo en el caso dos no convertimos los caracteres a minúsculas. */
				if (caso != Caso.DOS)
					auxiliares[k] = convierteMinuscula(actual[k]);
				else 
					auxiliares[k] = (int) actual[k];
			}
			/* Sólo en el caso tres permitimos caracteres que no sean números ni letras. */
			if (auxiliares[0] == auxiliares[1] || (caso != Caso.TRES ? 
									(!caracterComparable(auxiliares[0]) && 
										!caracterComparable(auxiliares[1])) : false))
				return compareTo(linea, it1 + 1, it2 +1, caso);
			else if ((caso != Caso.TRES ? 
								!caracterComparable(auxiliares[0]) : false))
				return compareTo(linea, it1 + 1, it2, caso);
			else if ((caso != Caso.TRES ? 
								!caracterComparable(auxiliares[1]) : false))
				return compareTo(linea, it1, it2 + 1, caso);
			else {
				int s = auxiliares[0] - auxiliares[1];
				/*El programa Sort de Unix ordena primero las minúsculas, 
				aunque en Unicode sea al revés. Lo mismo cuando tomamos en
				cuenta espacios. */
				if (caso == Caso.DOS || esUnEspacio(auxiliares[0]) || 
						esUnEspacio(auxiliares[1]))
					s = -s;
				return s; 
			} 
		}
	}

	/* Método auxiliar no destructivo para ignorar letras
	con acentos que no queremos comparar. */
	private static char sinAcentos(char c) {
		char s = c;
		for (int i = 0; i < arr1.length; i++) {
			if (s == arr1[i])
				s = arr2[i];
		}
		return s;
	}
	
	private static boolean caracterComparable(int c) {
		/* Los caracteres serán comparables sólo si son números,
		letras minúsculas o mayúsculas. */
		return (esUnNumero(c) || esMayuscula(c) || esMinuscula(c));
	}

	private static boolean esUnEspacio (int c) {
		return c == ' ';
	}

	private static boolean esUnNumero(int c) {
		return (48 <= c && c <= 57);
	}

	private static boolean esMayuscula(int c) {
		return (65 <= c && c <= 90);
	}

	private static boolean esMinuscula(int c) {
		return (97 <= c && c <= 122);
	}

	private static int convierteMinuscula(char c) {
		int s = (int) c;
		if (esMayuscula(s))
			s = s + 32;
		return s;
	}
}
