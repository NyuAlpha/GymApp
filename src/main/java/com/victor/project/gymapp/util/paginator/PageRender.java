package com.victor.project.gymapp.util.paginator;

import org.springframework.data.domain.Page;
import java.util.List;
import java.util.ArrayList;

public class PageRender<T> {

    private String url;
    private Page<T> page;
    private List<PageItem> pages;

    private int totalPages;
    private int numElementsPerPage;
    private int actualPage;

    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.pages = new ArrayList<>();

        numElementsPerPage = page.getSize();
        totalPages = page.getTotalPages();
        actualPage = page.getNumber()+1;

        int since, until;
        //Si hay un número de paginas no superior al número de elementos, se muestran todas.
        if(totalPages <= numElementsPerPage){
            since = 1;
            until = totalPages;
        }//En caso contrario se muestran rangos, dependiendo de la posición actual
        else{
            //Mientras la página actual no pase de la mitad de los elementos por página
            if(actualPage <= numElementsPerPage/2){
                since = 1;
                until = numElementsPerPage;
            }//En las últimas páginas
            else if(actualPage >= totalPages - numElementsPerPage/2 ){
                since = totalPages - numElementsPerPage + 1; // De la 11 a la 
                until = numElementsPerPage;
            }//Entre las primeras y últimas
            else{
                since = actualPage  - numElementsPerPage/2;
                until = numElementsPerPage;
            }
        }


        for (int i = 0; i < until; i++) {
            pages.add(new PageItem(since+i, actualPage== since+i));
        }
    }

    public String getUrl() {
        return url;
    }

    public List<PageItem> getPages() {
        return pages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getActualPage() {
        return actualPage;
    }

    public boolean isFirst(){
        return page.isFirst();
    }

    public boolean isLast(){
        return page.isLast();
    }

    public boolean hasNext(){
        return page.hasNext();
    }

    public boolean hasPrevious(){
        return page.hasPrevious();
    }

    
    
}
