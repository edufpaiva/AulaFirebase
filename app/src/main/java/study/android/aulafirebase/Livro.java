package study.android.aulafirebase;

import java.io.Serializable;

/**
 * Created by aluno on 07/10/2016.
 */

public class Livro implements Serializable {
    private String capa;
    private String titulo;
    private String autor;
    private int paginas;
    private int ano;
    private String categoria;

    public Livro(String titulo, String autor, int paginas, int ano, String categoria) {
        //this.capa = capa;
        this.titulo = titulo;
        this.autor = autor;
        this.paginas = paginas;
        this.ano = ano;
        this.categoria = categoria;
    }

    public Livro(String titulo, String autor, int paginas, int ano, String categoria, String capa) {
        this.capa = capa;
        this.titulo = titulo;
        this.autor = autor;
        this.paginas = paginas;
        this.ano = ano;
        this.categoria = categoria;
    }

    public Livro(){


    }


    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
