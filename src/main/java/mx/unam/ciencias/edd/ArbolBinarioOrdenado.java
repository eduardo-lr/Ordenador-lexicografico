package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
			pila = new Pila<>();
			Vertice v = raiz;
			while (v != null) {
				pila.mete(v);
				v = v.izquierdo;
			}
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
			return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
			Vertice v = pila.saca();
			if (v.hayDerecho()) {
				pila.mete(v.derecho);
				Vertice vertice = v.derecho.izquierdo;
					while (vertice != null) {
						pila.mete(vertice);
						vertice = vertice.izquierdo;
				}
			}
			return v.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
		if (elemento == null)
			throw new IllegalArgumentException();
		Vertice v = nuevoVertice(elemento);
		ultimoAgregado = v;
		elementos++;
		if (esVacia()) {
			raiz = v;
			return;
		}
		agrega(raiz, v);
    }

	// Metodo auxiliar para agrega
	private void agrega(Vertice actual, Vertice nuevo) {
		if (nuevo.elemento.compareTo(actual.elemento) <= 0) { 
			if (actual.izquierdo == null) { 
				actual.izquierdo = nuevo;
				nuevo.padre = actual;
				return;
			} else 
				agrega(actual.izquierdo, nuevo);			
		} else {
			if (actual.derecho == null) {
				actual.derecho = nuevo;
				nuevo.padre = actual;
				return;
			} else 
				agrega(actual.derecho, nuevo);		
		}	
	}

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
		Vertice v = vertice(busca(elemento));
		if (v == null)	
			return;
		elementos--;
		eliminaVertice(v.hayDerecho() && v.hayIzquierdo() ?
							 	intercambiaEliminable(v) : v);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
		Vertice u = MaximoEnSubarbol(vertice.izquierdo);
		T auxiliar = vertice.elemento;
		vertice.elemento = u.elemento;
		u.elemento = auxiliar;
		return u;
    }

	// Método auxiliar para intercambiaEliminable
	private Vertice MaximoEnSubarbol(Vertice v) {
		return v.derecho == null ? v : MaximoEnSubarbol(v.derecho);
	}	

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
		Vertice p = vertice.padre;
		Vertice u;
		if (!vertice.hayDerecho() && !vertice.hayIzquierdo())
			u = null;
		else if (!vertice.hayDerecho())
			u = vertice.izquierdo;
		else
			u = vertice.derecho;
		if (u != null) 
			u.padre = p;
		if (p != null) {
			if (vertice.equals(p.izquierdo)) 
				p.izquierdo = u;
			else
				p.derecho = u;
		} else 
			raiz = u;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
		return busca(elemento, raiz);
    }

	// Método auxiliar para busca
	private VerticeArbolBinario<T> busca(T elemento, Vertice v) {
		if (v == null || v.elemento.equals(elemento))
			return v;
		else if (elemento.compareTo(v.elemento) < 0)
			return busca(elemento, v.izquierdo);
		else 
			return busca(elemento, v.derecho);	
	}

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        // Aquí va su código.
		return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
		if (vertice == null || !vertice.hayIzquierdo())
			return;
		Vertice q = vertice(vertice);
		Vertice p = q.izquierdo;
		Vertice v = q.padre;
		p.padre = v;
		if (v != null) {
			if (q.equals(v.izquierdo)) 
				v.izquierdo = p;
			else 
				v.derecho = p;
		} else
			raiz = p;
		q.padre = p;
		Vertice s = p.derecho;
		p.derecho = q;
		q.izquierdo = s;
		if (s != null) {
			s.padre = q;
		}
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
		if (vertice == null || !vertice.hayDerecho())
			return;
		Vertice q = vertice(vertice);
		Vertice p = q.derecho;
		Vertice v = q.padre;
		p.padre = v;
		if (v != null) {
			if (q.equals(v.izquierdo)) 
				v.izquierdo = p;
			else 
				v.derecho = p;
		} else
			raiz = p;
		q.padre = p;
		Vertice s = p.izquierdo;
		p.izquierdo = q;
		q.derecho = s;
		if (s != null) {
			s.padre = q;
		}
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
		dfsPreOrder(accion, raiz);
    }
	
	// Método auxiliar para DFS pre-order
	private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
		if (v == null)
			return;
		accion.actua(v);
		dfsPreOrder(accion, v.izquierdo);
		dfsPreOrder(accion, v.derecho);
	}

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
		dfsInOrder(accion, raiz);
    }

	// Método auxiliar para DFS in-order
	private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
		if (v == null)
			return;
		dfsInOrder(accion, v.izquierdo);
		accion.actua(v);
		dfsInOrder(accion, v.derecho);
	}

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
		dfsPostOrder(accion, raiz);
    }

	// Método auxiliar para DFS post-order
	private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
		if (v == null)
			return;
		dfsPostOrder(accion, v.izquierdo);
		dfsPostOrder(accion, v.derecho);
		accion.actua(v);
	}

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
