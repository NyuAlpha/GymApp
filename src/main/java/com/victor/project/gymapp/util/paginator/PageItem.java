package com.victor.project.gymapp.util.paginator;

import lombok.AllArgsConstructor;
import lombok.Getter;


/*
 * Clase creada para saber el número de una página y si es la actual en el contexto de la paginación
 */
@Getter
@AllArgsConstructor
public class PageItem {

    private int number; //Número de la página
    private boolean isActual; //True si el número de página coincide con la página actual
}
