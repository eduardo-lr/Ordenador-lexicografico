package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
            // Aquí va su código.
			this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <code>true</code> si el vértice tiene padre,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayPadre() {
            // Aquí va su código.
			return padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <code>true</code> si el vértice tiene izquierdo,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            // Aquí va su código.
			return izquierdo != null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <code>true</code> si el vértice tiene derecho,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayDerecho() {
            // Aquí va su código.
			return derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            // Aquí va su código.
			if (!hayPadre())
				throw new NoSuchElementException();
			return padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            // Aquí va su código.
			if (!hayIzquierdo())
				throw new NoSuchElementException();
			return izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            // Aquí va su código.
			if (!hayDerecho())
				throw new NoSuchElementException();
			return derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
			if (!hayIzquierdo() && !hayDerecho())
				return 0;
			else if (!hayIzquierdo())
				return 1 + derecho.altura();
			else if (!hayDerecho())
				return 1 + izquierdo.altura();
			else
				return 1 + (izquierdo.altura() > derecho.altura() ?
								izquierdo.altura() : derecho.altura());
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            // Aquí va su código.
			return hayPadre() ? 1 + padre.profundidad() : 0;
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            // Aquí va su código.
			return elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            // Aquí va su código.
			if (elemento != null && vertice.elemento != null) {
				if (!elemento.equals(vertice.elemento)) 
					return false;
			} else if (elemento == null & vertice.elemento == null) {
			} else 
				return false;
			if (!hayDerecho() && !hayIzquierdo() && 
						!vertice.hayDerecho() && !vertice.hayIzquierdo())
				return true;
			else if (hayDerecho() && hayIzquierdo() && 
							vertice.hayDerecho() && vertice.hayIzquierdo())
				return izquierdo.equals(vertice.izquierdo) 
								&& derecho.equals(vertice.derecho);
			else if (hayDerecho() && vertice.hayDerecho() &&
							!hayIzquierdo() && !vertice.hayIzquierdo())
				return derecho.equals(vertice.derecho);
			else if (hayIzquierdo() && vertice.hayIzquierdo() &&
							!hayDerecho() && !vertice.hayDerecho())
				return izquierdo.equals(vertice.izquierdo);
			else 
				return false;
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            // Aquí va su código.
			return elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        // Aquí va su código.
		for (T elemento : coleccion) {
			agrega(elemento);
		}
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        // Aquí va su código.
		return esVacia() ? -1 : raiz.altura();
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        // Aquí va su código.
		return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
		return busca(elemento) != null;
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <code>null</code>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <code>null</code> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
		return esVacia() ? null : busca(elemento, raiz);
    }

	// Metodo auxiliar para hacer recursion en busca()
	private VerticeArbolBinario<T> busca(T elemento, Vertice v) {
		if (v.elemento.equals(elemento))
			return v;
		VerticeArbolBinario v1 = v.hayIzquierdo() ?
									busca(elemento, v.izquierdo) : null;
		return v1 != null ? v1 : 
					v.hayDerecho() ? busca(elemento, v.derecho) : null;		
	}

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        // Aquí va su código.
		if (esVacia()) 
			throw new NoSuchElementException();
		return raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
		return raiz == null;
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
		raiz = null;
		elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        // Aquí va su código.
		if (esVacia() && arbol.esVacia()) 
			return true;
		else if (esVacia())
			return false;
		else
			return raiz.equals(arbol.raiz);
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        // Aquí va su código.
		if (esVacia())
			return "";
		int[] arreglo = new int[altura()+1];
		for (int i = 0; i < altura() + 1; i++)  
			arreglo[i] = 0;
		return toString(raiz, 0, arreglo);
    }

	// Método auxiliar para crear la cadena antes de un vértice
	private String dibujaEspacios(int nivel, int[] arreglo) {
		String s = "";
		for (int i = 0; i < nivel; i++) 
			s += arreglo[i] == 1 ? "│  " : "   ";
		return s;
	}

	// Método auxiliar para hacer toString recursivo
	private String toString(Vertice v, int nivel, int[] arreglo) {
		String s = v.toString() + "\n";
		arreglo[nivel] = 1;
		if (v.hayDerecho() && v.hayIzquierdo()) {
			s += dibujaEspacios(nivel, arreglo);
			s += "├─›";
			s += toString(v.izquierdo, nivel + 1, arreglo);
			s += dibujaEspacios(nivel, arreglo);
			s += "└─»";
			arreglo[nivel] = 0;
			s += toString(v.derecho, nivel + 1, arreglo);
		} else if (v.hayIzquierdo()) {
			s += dibujaEspacios(nivel, arreglo);
			s += "└─›";
			arreglo[nivel] = 0;
			s += toString(v.izquierdo, nivel + 1, arreglo);
		} else if (v.hayDerecho()) {
			s += dibujaEspacios(nivel, arreglo);
			s += "└─»";
			arreglo[nivel] = 0;
			s += toString(v.derecho, nivel + 1, arreglo);
		}
		return s;
	}

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
