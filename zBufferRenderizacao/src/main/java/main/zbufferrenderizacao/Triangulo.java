package main.zbufferrenderizacao;

import java.awt.Color;

/**
 *
 * @author mathe
 */
// Classe para representar um triângulo no espaço tridimensional
public class Triangulo {

    Vertice v1;
    Vertice v2;
    Vertice v3;
    Color cor;

    Triangulo(Vertice v1, Vertice v2, Vertice v3, Color cor) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.cor = cor;
    }
}
