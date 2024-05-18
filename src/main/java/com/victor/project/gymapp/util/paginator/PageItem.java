package com.victor.project.gymapp.util.paginator;

import lombok.AllArgsConstructor;
import lombok.Getter;

//Clase creada para saber el número de una página y si es la actual

@Getter
@AllArgsConstructor
public class PageItem {

    private int number;
    private boolean isActual;
}
