package main.zbufferrenderizacao;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class DemoViewerV2ZBuffer {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container painel = frame.getContentPane();
        painel.setLayout(new BorderLayout());

        // Slider para controlar a rotação horizontal
        JSlider sliderRotacaoHorizontal = new JSlider(-180, 180, 0);
        painel.add(sliderRotacaoHorizontal, BorderLayout.SOUTH);

        // Slider para controlar a rotação vertical
        JSlider sliderRotacaoVertical = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        painel.add(sliderRotacaoVertical, BorderLayout.EAST);

        // Painel para exibir os resultados da renderização
        JPanel painelRenderizacao = new JPanel() {
            // Sobrescrevendo o método paintComponent para realizar a renderização
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Lista de triângulos a serem renderizados
                List<Triangulo> tris = new ArrayList<>();
                // Base da pirâmide
                tris.add(new Triangulo(new Vertice(-100, -100, -100),
                        new Vertice(100, -100, -100),
                        new Vertice(100, -100, 100),
                        Color.WHITE));
                tris.add(new Triangulo(new Vertice(-100, -100, -100),
                        new Vertice(-100, -100, 100),
                        new Vertice(100, -100, 100),
                        Color.WHITE));
                // Faces laterais da pirâmide
                tris.add(new Triangulo(new Vertice(-100, -100, -100),
                        new Vertice(100, -100, -100),
                        new Vertice(0, 100, 0),
                        Color.WHITE));
                tris.add(new Triangulo(new Vertice(100, -100, -100),
                        new Vertice(100, -100, 100),
                        new Vertice(0, 100, 0),
                        Color.WHITE));
                tris.add(new Triangulo(new Vertice(100, -100, 100),
                        new Vertice(-100, -100, 100),
                        new Vertice(0, 100, 0),
                        Color.WHITE));
                tris.add(new Triangulo(new Vertice(-100, -100, 100),
                        new Vertice(-100, -100, -100),
                        new Vertice(0, 100, 0),
                        Color.WHITE));

                // Cálculos de transformação e projeção
                double rotacaoHorizontal = Math.toRadians(sliderRotacaoHorizontal.getValue());
                Matriz3 transformacaoRotacaoHorizontal = new Matriz3(new double[]{
                    Math.cos(rotacaoHorizontal), 0, -Math.sin(rotacaoHorizontal),
                    0, 1, 0,
                    Math.sin(rotacaoHorizontal), 0, Math.cos(rotacaoHorizontal)
                });
                double rotacaoVertical = Math.toRadians(sliderRotacaoVertical.getValue());
                Matriz3 transformacaoRotacaoVertical = new Matriz3(new double[]{
                    1, 0, 0,
                    0, Math.cos(rotacaoVertical), Math.sin(rotacaoVertical),
                    0, -Math.sin(rotacaoVertical), Math.cos(rotacaoVertical)
                });
                Matriz3 transformacao = transformacaoRotacaoHorizontal.multiplicar(transformacaoRotacaoVertical);

                // BufferedImage e Z-buffer
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                double[] zBuffer = new double[img.getWidth() * img.getHeight()];

                // Inicialização do Z-buffer com profundidades extremamente distantes
                for (int q = 0; q < zBuffer.length; q++) {
                    zBuffer[q] = Double.NEGATIVE_INFINITY;
                }

                // Loop para renderização de cada triângulo
                for (Triangulo t : tris) {
                    // Transformação dos vértices do triângulo
                    Vertice v1 = transformacao.transformar(t.v1);
                    v1.x += getWidth() / 2;
                    v1.y += getHeight() / 2;
                    Vertice v2 = transformacao.transformar(t.v2);
                    v2.x += getWidth() / 2;
                    v2.y += getHeight() / 2;
                    Vertice v3 = transformacao.transformar(t.v3);
                    v3.x += getWidth() / 2;
                    v3.y += getHeight() / 2;

                    // Cálculos de projeção e rasterização
                    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
                    int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
                    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
                    int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

                    double areaTriangulo = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);

                    // Loop para rasterização
                    for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {
                            double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / areaTriangulo;
                            double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / areaTriangulo;
                            double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / areaTriangulo;
                            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                                double profundidade = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                                int indiceZBuffer = y * img.getWidth() + x;
                                // Verificação do Z-buffer para renderização
                                if (zBuffer[indiceZBuffer] < profundidade) {
                                    // Cálculo do tom de cinza com base na profundidade
                                    int gray = (int) (255 - (profundidade + 200) / 400 * 255);
                                    // Limitação do tom de cinza entre 0 e 255
                                    gray = Math.max(0, Math.min(255, gray));
                                    // Criação da cor em tons de cinza
                                    Color cor = new Color(gray, gray, gray);
                                    img.setRGB(x, y, cor.getRGB());
                                    zBuffer[indiceZBuffer] = profundidade;
                                }
                            }
                        }
                    }
                }

                // Desenho da imagem renderizada no painel
                g2.drawImage(img, 0, 0, null);
            }
        };
        painel.add(painelRenderizacao, BorderLayout.CENTER);

        // Adição de ouvintes para os sliders
        sliderRotacaoHorizontal.addChangeListener(e -> painelRenderizacao.repaint());
        sliderRotacaoVertical.addChangeListener(e -> painelRenderizacao.repaint());

        // Configuração da janela principal
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
}


