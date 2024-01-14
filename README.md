# Z-buffer de renderização
Este programa demonstra o uso do algoritmo de z-buffer para renderizar uma pirâmide em 3D. O z-buffer é uma técnica que permite determinar a visibilidade dos pixels de uma cena tridimensional, levando em conta a profundidade de cada pixel. O programa usa dois sliders para controlar a rotação da pirâmide em torno dos eixos x e y.

### Como funciona o código
O código está dividido em duas partes: a parte 1 e a parte 2. A parte 1 é responsável por criar a janela principal, os sliders e o painel de renderização. A parte 2 é responsável por implementar o algoritmo de z-buffer e desenhar a imagem renderizada no painel.

#### Parte 1
A parte 1 começa com a criação de um objeto JFrame, que é a janela principal do programa. Em seguida, é criado um objeto Container, que é o painel que contém os componentes da interface gráfica. O painel usa um layout de BorderLayout, que divide o espaço em cinco regiões: norte, sul, leste, oeste e centro.

Em seguida, são criados dois objetos JSlider, que são os sliders que permitem controlar a rotação da pirâmide. O sliderRotacaoHorizontal é adicionado à região sul do painel, e o sliderRotacaoVertical é adicionado à região leste. Os sliders têm valores mínimos e máximos de -180 e 180 graus para o horizontal, e -90 e 90 graus para o vertical. Os valores iniciais são zero, o que significa que a pirâmide está na posição original.

Depois, é criado um objeto JPanel, que é o painel de renderização. Esse painel é adicionado à região central do painel principal. O painel de renderização tem um método paintComponent sobrescrito, que é chamado sempre que o painel precisa ser redesenhado. Esse método é responsável por realizar a renderização da pirâmide usando o z-buffer.

Finalmente, são adicionados ouvintes aos sliders, que são objetos que reagem a eventos de mudança de valor dos sliders. Os ouvintes chamam o método repaint do painel de renderização, que faz com que o método paintComponent seja executado novamente, atualizando a imagem da pirâmide de acordo com os valores dos sliders.

A parte 1 termina com a configuração da janela principal, definindo o seu tamanho e tornando-a visível.

#### Parte 2
A parte 2 começa com a criação de uma lista de triângulos, que representa a pirâmide em 3D. Cada triângulo é formado por três vértices, que são objetos que armazenam as coordenadas x, y e z de um ponto no espaço. A pirâmide tem seis triângulos: dois para a base e quatro para as faces laterais. Cada triângulo tem uma cor branca.

Em seguida, são feitos os cálculos de transformação e projeção dos vértices da pirâmide. A transformação consiste em rotacionar os vértices de acordo com os valores dos sliders, usando matrizes de rotação. A projeção consiste em converter as coordenadas tridimensionais dos vértices em coordenadas bidimensionais na tela, usando uma projeção ortográfica simples. Além disso, os vértices são transladados para o centro da tela, somando metade da largura e da altura do painel de renderização às coordenadas x e y dos vértices.

Depois, é criado um objeto BufferedImage, que é uma imagem que pode ser manipulada pixel a pixel. A imagem tem o mesmo tamanho do painel de renderização, e é inicializada com pixels pretos. Também é criado um array de double chamado zBuffer, que armazena a profundidade de cada pixel da imagem. O zBuffer tem o mesmo tamanho da imagem, e é inicializado com valores infinitamente negativos, indicando que nenhum pixel foi renderizado ainda.

Em seguida, é feito um loop para renderizar cada triângulo da lista. Para cada triângulo, são feitos os seguintes passos:

* São calculados os limites mínimos e máximos do triângulo na tela, usando as coordenadas x e y dos vértices. Esses limites são usados para restringir a área de rasterização do triângulo, evitando processar pixels desnecessários.
* É calculada a área do triângulo, usando a fórmula de determinante de uma matriz 3x3 formada pelas coordenadas dos vértices. Essa área é usada para calcular as coordenadas baricêntricas de cada pixel dentro do triângulo, que são valores que indicam a proporção de cada vértice na composição do pixel.
* É feito um loop para rasterizar cada pixel dentro dos limites do triângulo. Para cada pixel, são feitos os seguintes passos:
    * São calculadas as coordenadas baricêntricas do pixel, usando a fórmula que envolve a área do triângulo e as coordenadas dos vértices. Se as coordenadas baricêntricas forem todas maiores ou iguais a zero e menores ou iguais a um, significa que o pixel está dentro do triângulo. Caso contrário, o pixel está fora do triângulo e é ignorado.
    * É calculada a profundidade do pixel, usando uma média ponderada das profundidades dos vértices, usando as coordenadas baricêntricas como pesos. Essa profundidade indica a distância do pixel em relação à câmera, e é usada para determinar a visibilidade do pixel.
    * É calculado o índice do pixel no zBuffer, usando a fórmula que envolve a coordenada y do pixel e a largura da imagem. Esse índice é usado para acessar o valor de profundidade armazenado no zBuffer para aquele pixel.
    * É feita a verificação do zBuffer para renderização. Se o valor de profundidade armazenado no zBuffer for menor que a profundidade calculada do pixel, significa que o pixel é mais próximo da câmera do que o pixel anteriormente renderizado naquele ponto, e portanto deve ser renderizado. Caso contrário, o pixel é mais distante da câmera do que o pixel anteriormente renderizado naquele ponto, e portanto deve ser descartado.
    * É calculado o tom de cinza do pixel, usando uma fórmula que envolve a profundidade do pixel e um fator de escala. Essa fórmula cria um efeito de sombreamento, onde os pixels mais próximos da câmera são mais claros e os pixels mais distantes da câmera são mais escuros. O tom de cinza é limitado entre 0 e 255, que são os valores mínimos e máximos de intensidade de cor.
    * É criada a cor do pixel, usando o tom de cinza para os componentes vermelho, verde e azul. Essa cor é usada para pintar o pixel na imagem, usando o método setRGB da classe BufferedImage.
    * É atualizado o valor de profundidade no zBuffer, usando a profundidade calculada do pixel. Isso garante que o zBuffer sempre armazene a profundidade do pixel mais próximo da câmera em cada ponto da imagem.
O loop de renderização termina com o desenho da imagem renderizada no painel, usando o método drawImage da classe Graphics2D. Esse método copia os pixels da imagem para o painel, mostrando o resultado da renderização na tela.