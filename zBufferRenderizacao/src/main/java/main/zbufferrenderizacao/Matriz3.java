package main.zbufferrenderizacao;

/**
 *
 * @author mathe
 */
// Classe para representar uma matriz 3x3 e realizar operações de multiplicação e transformação
public class Matriz3 {

    double[] valores;

    Matriz3(double[] valores) {
        this.valores = valores;
    }

    // Método para multiplicar duas matrizes 3x3
    Matriz3 multiplicar(Matriz3 outra) {
        double[] resultado = new double[9];
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                for (int i = 0; i < 3; i++) {
                    resultado[linha * 3 + coluna]
                            += this.valores[linha * 3 + i] * outra.valores[i * 3 + coluna];
                }
            }
        }
        return new Matriz3(resultado);
    }

    // Método para transformar um vértice usando a matriz
    Vertice transformar(Vertice in) {
        return new Vertice(
                in.x * valores[0] + in.y * valores[3] + in.z * valores[6],
                in.x * valores[1] + in.y * valores[4] + in.z * valores[7],
                in.x * valores[2] + in.y * valores[5] + in.z * valores[8]
        );
    }

}
