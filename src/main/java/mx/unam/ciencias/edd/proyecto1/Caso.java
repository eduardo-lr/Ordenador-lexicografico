package mx.unam.ciencias.edd.proyecto1;

/* Enumeración para tratar los distintos 
casos posibles de comparación */
public enum Caso {
    
	/* Comparamos ignorando mayúsculas y caracteres que no sean
	números o letras. Este es el caso que usamos por default */
	UNO,

	/* Si despues de haber comparado con el criterio anterior, 
	las cadenas se compararon iguales, comparamos tomando en 
	cuenta mayúsculas y minúsculas, pero ignorando caracteres 
	que no sean números o letras. */
	DOS,

	/* Si despues de haber comparado con el criterio anterior, 
	las cadenas aún se comparan como iguales, comparamos tomando 
	en cuenta mayúsculas, minúsculas y todo tipo de caracteres. */
	TRES
}
