package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
			super(elemento);
			color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            // Aquí va su código.
			String s = elemento.toString() + "}"; 
			switch(color) {
				case ROJO:
					return "R{" + s;
				case NEGRO:
					return "N{" + s;
				default:
					return null;
			}
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
			return (color == vertice.color && super.equals(vertice));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() {
        super();
    }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
		return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
		if (!vertice.getClass().getSimpleName().equals("VerticeRojinegro")) 
			throw new ClassCastException();
		VerticeRojinegro v = (VerticeRojinegro) vertice;
		return v.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
		super.agrega(elemento);
		VerticeRojinegro v = (VerticeRojinegro) ultimoAgregado;
		v.color = Color.ROJO;
		rebalanceaAgrega(v);
    }

	// Método auxiliar para rebalancear el árbol despues de agregar.
	private void rebalanceaAgrega(VerticeRojinegro v) {
		if (!v.hayPadre()) {
			v.color = Color.NEGRO;
			return;
		}
		VerticeRojinegro p = getPadre(v);
		if (!esRojo(p))
			return;
		VerticeRojinegro a = getAbuelo(v);
		VerticeRojinegro t = getTio(v);
		if (esRojo(t)) {
			t.color = Color.NEGRO;
			p.color = Color.NEGRO;
			a.color = Color.ROJO;
			rebalanceaAgrega(a);
			return;
		}
		if (estanCruzados(v, p)) {
			if (esIzquierdo(p))
				super.giraIzquierda(p);
			else
				super.giraDerecha(p);
			VerticeRojinegro auxiliar = v;
			v = p;
			p = auxiliar;
		}
		p.color = Color.NEGRO;
		a.color = Color.ROJO;
		if (esIzquierdo(v))
			super.giraDerecha(a);
		else
			super.giraIzquierda(a);
	}

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
		Vertice v = vertice(busca(elemento));
		if (v == null)	
			return;
		elementos--;
		VerticeRojinegro h = (VerticeRojinegro) nuevoVertice(null);
		h.color = Color.NEGRO;	
		if (v.hayDerecho() && v.hayIzquierdo()) 
			v = intercambiaEliminable(v);
		if (!v.hayDerecho() && !v.hayIzquierdo()) {
				h.padre = v;
				v.izquierdo = h;
		} else 
			h = (VerticeRojinegro) (!v.hayDerecho() ? v.izquierdo() : v.derecho());
		eliminaVertice(v);
		if (esRojo(h)) 
			h.color = Color.NEGRO;
		else if (esRojo((VerticeRojinegro) v)) {
		} else 
			rebalanceaElimina(h);
		if (h.elemento == null) 
			eliminaVertice(h);
    }

	// Método auxiliar para rebalancear el árbol despues de eliminar.
	private void rebalanceaElimina(VerticeRojinegro v) {
		if (!v.hayPadre())
			return;
		VerticeRojinegro p = getPadre(v);
		VerticeRojinegro h = getHermano(v);
		if (esRojo(h)) { 
			p.color = Color.ROJO;
			h.color = Color.NEGRO;
			if(esIzquierdo(v))
				super.giraIzquierda(p);
			else
				super.giraDerecha(p);
			h = getHermano(v);
		}
		VerticeRojinegro hi = (VerticeRojinegro) h.izquierdo;
		VerticeRojinegro hd = (VerticeRojinegro) h.derecho;
		if (!esRojo(p) && !esRojo(h) && !esRojo(hi) && !esRojo(hd)) {
			h.color = Color.ROJO;
			rebalanceaElimina(p);
			return;
		} 
		if (esRojo(p) && !esRojo(h) && !esRojo(hi) && !esRojo(hd)) {
			h.color = Color.ROJO;
			p.color = Color.NEGRO;
			return;
		} 
		if ((esIzquierdo(v) && esRojo(hi) && !esRojo(hd)) ||
							!esIzquierdo(v) && !esRojo(hi) && esRojo(hd)) {
			h.color = Color.ROJO;
			if (esRojo(hi)) {
				if (hi != null) 
					hi.color = Color.NEGRO;
			} else {
				if (hd != null) 
					hd.color = Color.NEGRO;
			}
			if (esIzquierdo(v))
				super.giraDerecha(h);
			else
				super.giraIzquierda(h);
			h = getHermano(v);
			hi = (VerticeRojinegro) h.izquierdo;
			hd = (VerticeRojinegro) h.derecho;
		}
		h.color = p.color;
		p.color = Color.NEGRO;
		if (esIzquierdo(v)) {
			if (hd != null)
				hd.color = Color.NEGRO;
		} else {
			if (hi != null)
				hi.color = Color.NEGRO;
		}
		if (esIzquierdo(v))
			super.giraIzquierda(p);
		else
			super.giraDerecha(p);
	}

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }

	// Método auxiliar para verificar si un vertice es izquierdo.
	private boolean esIzquierdo(VerticeRojinegro vertice) {
		VerticeRojinegro p = getPadre(vertice);
		if (!p.hayDerecho())
			return true;
		else if (!p.hayIzquierdo())
			return false;
		else
			return p.izquierdo.equals(vertice);
	}
	
	// Método auxiliar para saber si dos vértices están cruzados.
	private boolean estanCruzados(VerticeRojinegro v1, VerticeRojinegro v2) {
		return ((esIzquierdo(v1) && !esIzquierdo(v2)) ||
					(!esIzquierdo(v1) && esIzquierdo(v2)));
	}

	// Método auxiliar para saber si un vértice es rojo.
	private boolean esRojo(VerticeRojinegro vertice) {
		return (vertice != null && vertice.color == Color.ROJO);
	}

	// Método auxiliar para obtener el padre de un vértice.
	private VerticeRojinegro getPadre(VerticeRojinegro vertice) {
		return (VerticeRojinegro) vertice.padre;
	}

	// Método auxiliar para obtener el abuelo de un vértice.
	private VerticeRojinegro getAbuelo(VerticeRojinegro vertice) {
		return getPadre(getPadre(vertice));
	}

	// Método auxiliar para obtener el tío de un vértice.
	private VerticeRojinegro getTio(VerticeRojinegro vertice) {
		VerticeRojinegro a = getAbuelo(vertice);
		VerticeRojinegro p = getPadre(vertice);
		return (VerticeRojinegro) (p.equals(a.izquierdo) ? 
											a.derecho : a.izquierdo);
	}

	// Método auxiliar para obtener el hermano de un vértice.
	private VerticeRojinegro getHermano(VerticeRojinegro vertice) {
		VerticeRojinegro p = getPadre(vertice);
		return (VerticeRojinegro) (vertice.equals(p.izquierdo) ? 
											p.derecho : p.izquierdo);
	}
}
