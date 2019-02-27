
package gametetris;
// Библиотеки для роботи с програмой
import java.awt.*; // Библиотека для внешних форм
import java.awt.event.*;//Библиотека для обработки событий
import javax.swing.*; //Библиотека для графических елементов
import java.util.*; //Библиотека для для генирации случайних чисел

public class GameTetris extends JFrame {
    
    final String TITLE_OF_PROGRAM = "Tetris";
    final int BLOCK_SIZE = 25; // Визначає розмір Блока
    final int ARC_RADIUS = 6; // Закруглення кутів в падаючих блоків
    final int FIELD_WIDTH = 10; // Ширина ігрового поля в блоках
    final int FIELD_HEIGHT = 20; // Висота ігрового поля в блоках
    final int START_LOCATION = 150; // визначає положення лівого угла вікна
    final int FIELD_DX = 7; 
    final int FIELD_DY = 26;
    final int LEFT = 37; // Код клавіши стрілочки ліворуч
    final int UP = 38; // Код клавіши стрілочки вверх
    final int RIGHT = 39; // Код клавіши стрілочки праворуч 
    final int DOWN = 40; // Код клавіши стрілочки вниз
    final int SHOW_DELAY = 400; // Визначає затримку анімації
    final int[][][] SHAPES = {
        {{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}, {4, 0x00f0f0}}, // I  
        {{0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {4, 0xf0f000}}, // O
        {{1,0,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0x0000f0}}, // J
        {{0,0,1,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xf0a000}}, // L
        {{0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0x00f000}}, // S
        {{1,1,1,0}, {0,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xa000f0}}, // T
        {{1,1,0,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xf00000}}  // Z
    }; /*трехмерний масив которий содержит 7 двухмерних масивов каждий 
    масив представляет двоичную заготовку 7 различних фигур и их цветов*/
    final int[] SCORES = {100, 300, 700, 1500}; /*Это масив очковб росписовается
    так если исчезает 1 строка то дается 100 очков, 2 строки 300 очков 3 строки 700 очков а 4 строки 1500 очков*/
    int gameScore = 0; //перемення для подсчёта заработаних очков
    int[][] mine = new int[FIELD_HEIGHT + 1][FIELD_WIDTH]; // mine/glass
    JFrame frame;
    Canvas canvas = new Canvas(); //Создание робочой области    
    Random random = new Random(); //Иницилизируем генератор случайних чисел
    Figure figure = new Figure();
    boolean gameOver = false; // Оприделяет конець игри 
    final int[][] GAME_OVER_MSG = {
        {0,1,1,0,0,0,1,1,0,0,0,1,0,1,0,0,0,1,1,0},
        {1,0,0,0,0,1,0,0,1,0,1,0,1,0,1,0,1,0,0,1},
        {1,0,1,1,0,1,1,1,1,0,1,0,1,0,1,0,1,1,1,1},
        {1,0,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,0,0,0},
        {0,1,1,0,0,1,0,0,1,0,1,0,1,0,1,0,0,1,1,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,1,1,0,0,1,0,0,1,0,0,1,1,0,0,1,1,1,0,0},
        {1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0},
        {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,1,0,0},
        {1,0,0,1,0,1,1,0,0,0,1,0,0,0,0,1,0,0,1,0},
        {0,1,1,0,0,1,0,0,0,0,0,1,1,0,0,1,0,0,1,0}};
    //Вивод фрази Конець игри записаная в двоичном виде

    public static void main(String[] args) {
    new GameTetris().go();
    }

    GameTetris() {
        setTitle(TITLE_OF_PROGRAM);//Определяет заголовка програми
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Определяет кнопку реагирования на закритие окна нажатием на красний крест
        setBounds(START_LOCATION, START_LOCATION, FIELD_WIDTH * BLOCK_SIZE + FIELD_DX, FIELD_HEIGHT * BLOCK_SIZE + FIELD_DY);
        //Определяем Размер окна с использованием више перечислених констант
        setResizable(false); // Определяет неизменяемый размер окна 
        canvas.setBackground(Color.black); // Определяем цвет фона
        // Создаем оброботчик нажатия клавиш
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    if (e.getKeyCode() == DOWN) figure.drop();// Кнопка Перемещения обьекта в самиий низь
                    if (e.getKeyCode() == UP) figure.rotate();// Кнопка переварачивания обьектов
                    if (e.getKeyCode() == LEFT || e.getKeyCode() == RIGHT) figure.move(e.getKeyCode());// Кнопка перемещения влево или вправо
                }
                canvas.repaint();
            }
        });
        add(BorderLayout.CENTER, canvas);
        setVisible(true);
        Arrays.fill(mine[FIELD_HEIGHT], 1); 
    }

    void go() { // основной цикл игры
        while (!gameOver) {
            try {
                Thread.sleep(SHOW_DELAY);
            } catch (Exception e) { e.printStackTrace(); }
            canvas.repaint();
            checkFilling();
            if (figure.isTouchGround()) {
                figure.leaveOnTheGround();
                figure = new Figure();
                gameOver = figure.isCrossGround(); // Проверка если место для новой фигури
            } else
                figure.stepDown();
        }
    }
    //Метод которий проверяет заполненние и подсчитывает очка при запелнении ряда
    void checkFilling() { // проверить ряды заполнения
        int row = FIELD_HEIGHT - 1;
        int countFillRows = 0;
        while (row > 0) {
            int filled = 1;
            for (int col = 0; col < FIELD_WIDTH; col++)
                filled *= Integer.signum(mine[row][col]);
            if (filled > 0) {
                countFillRows++;
                for (int i = row; i > 0; i--) System.arraycopy(mine[i-1], 0, mine[i], 0, FIELD_WIDTH);
            } else
                row--;
        }
        if (countFillRows > 0) {
            gameScore += SCORES[countFillRows - 1];
            setTitle(TITLE_OF_PROGRAM + " : " + gameScore);
        }
    }
    // Класс Фигури для обратотки действий с блоками и функциями, цветом блоков
    class Figure {
        //Определяем свойства класса
        private ArrayList<Block> figure = new ArrayList<Block>();
        private int[][] shape = new int[4][4];
        private int type, size, color;
        private int x = 3, y = 0; // Начало левого угла

        Figure() {
            type = random.nextInt(SHAPES.length);
            size = SHAPES[type][4][0];
            color = SHAPES[type][4][1];
            if (size == 4) y = -1;
            for (int i = 0; i < size; i++)
                System.arraycopy(SHAPES[type][i], 0, shape[i], 0, SHAPES[type][i].length);
            createFromShape();
        }
        //проходя по масиву Shape на основании етого масива он создает фигуру
        void createFromShape() {
            for (int x = 0; x < size; x++)
                for (int y = 0; y < size; y++)
                    if (shape[y][x] == 1) figure.add(new Block(x + this.x, y + this.y));
        }
        //Метод проверки соприкосновения блока с землей
        boolean isTouchGround() {
            for (Block block : figure) if (mine[block.getY() + 1][block.getX()] > 0) return true;
            return false;
        }
        //Метод проверки сопрекосновением с потолком
        boolean isCrossGround() {
            for (Block block : figure) if (mine[block.getY()][block.getX()] > 0) return true;
            return false;
        }
        // метод при котором если блок прикоснулся то он там и остается
        void leaveOnTheGround() {
            for (Block block : figure) mine[block.getY()][block.getX()] = color;
        }
        // Метод проверки соприкосновения блоков со стеной
        boolean isTouchWall(int direction) {
            for (Block block : figure) {
                if (direction == LEFT && (block.getX() == 0 || mine[block.getY()][block.getX() - 1] > 0)) return true;
                if (direction == RIGHT && (block.getX() == FIELD_WIDTH - 1 || mine[block.getY()][block.getX() + 1] > 0)) return true;
            }
            return false;
        }
        // Метод позволяет перемещаться фигурке влево или вправо
        void move(int direction) {
            if (!isTouchWall(direction)) {
                int dx = direction - 38; // лево = -1, право = 1
                for (Block block : figure) block.setX(block.getX() + dx);
                x += dx;
            }
        }
        // 
        void stepDown() {
            for (Block block : figure) block.setY(block.getY() + 1);
            y++;
        }
        //Метод которий заставляет фигуру падать до самого низа или другого блока 
        void drop() { while (!isTouchGround()) stepDown(); }
        // проверочний метод чтоби фигурка не вилизла за придели экрана когда вращяется она возле края стенки
        boolean isWrongPosition() {
            for (int x = 0; x < size; x++)
                for (int y = 0; y < size; y++)
                    if (shape[y][x] == 1) {
                        if (y + this.y < 0) return true;
                        if (x + this.x < 0 || x + this.x > FIELD_WIDTH - 1) return true;
                        if (mine[y + this.y][x + this.x] > 0) return true;
                    }
            return false;
        }
        // Мотод которий позволяет переварачевать блок за часовой стрелкой и против часовой
        void rotateShape(int direction) {
            for (int i = 0; i < size/2; i++)
                for (int j = i; j < size-1-i; j++)
                    if (direction == RIGHT) { //поворот блоков по часовой стрелке
                        int tmp = shape[size-1-j][i];
                        shape[size-1-j][i] = shape[size-1-i][size-1-j];
                        shape[size-1-i][size-1-j] = shape[j][size-1-i];
                        shape[j][size-1-i] = shape[i][j];
                        shape[i][j] = tmp;
                    } else { // поворот блоков против часовой стрелке
                        int tmp = shape[i][j];
                        shape[i][j] = shape[j][size-1-i];
                        shape[j][size-1-i] = shape[size-1-i][size-1-j];
                        shape[size-1-i][size-1-j] = shape[size-1-j][i];
                        shape[size-1-j][i] = tmp;
                }
        }

        void rotate() {
            rotateShape(RIGHT);
            if (!isWrongPosition()) {
                figure.clear();
                createFromShape();
            } else
                rotateShape(LEFT);
        }
        // Прорисовка цвета блоков
        void paint(Graphics g) {
            for (Block block : figure) block.paint(g, color);
        }
    }

    class Block { // строительный элемент для фигуры
        // Визначаем координати блока
        private int x, y;

        public Block(int x, int y) {
            setX(x);
            setY(y);
        }

        void setX(int x) { this.x = x; }
        void setY(int y) { this.y = y; }

        int getX() { return x; }
        int getY() { return y; }
        // Метод которий рисует блоки
        void paint(Graphics g, int color) {
            g.setColor(new Color(color));
            g.drawRoundRect(x*BLOCK_SIZE+1, y*BLOCK_SIZE+1, BLOCK_SIZE-2, BLOCK_SIZE-2, ARC_RADIUS, ARC_RADIUS);
        }
    }

    class Canvas extends JPanel { //Прорисовка робочой области а также лежачих блоков игри 
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int x = 0; x < FIELD_WIDTH; x++)
                for (int y = 0; y < FIELD_HEIGHT; y++) {
                    if (x < FIELD_WIDTH - 1 && y < FIELD_HEIGHT - 1) {
                        g.setColor(Color.lightGray);
                        g.drawLine((x+1)*BLOCK_SIZE-2, (y+1)*BLOCK_SIZE, (x+1)*BLOCK_SIZE+2, (y+1)*BLOCK_SIZE);
                        g.drawLine((x+1)*BLOCK_SIZE, (y+1)*BLOCK_SIZE-2, (x+1)*BLOCK_SIZE, (y+1)*BLOCK_SIZE+2);
                    }
                    if (mine[y][x] > 0) {
                        g.setColor(new Color(mine[y][x]));
                        g.fill3DRect(x*BLOCK_SIZE+1, y*BLOCK_SIZE+1, BLOCK_SIZE-1, BLOCK_SIZE-1, true);
                    }
                } // Вивод слова gameOver на экран после соприкосновением блока с потолком
            if (gameOver) {
                g.setColor(Color.white);
                for (int y = 0; y < GAME_OVER_MSG.length; y++)
                    for (int x = 0; x < GAME_OVER_MSG[y].length; x++)
                        if (GAME_OVER_MSG[y][x] == 1) g.fill3DRect(x*11+18, y*11+160, 10, 10, true);
            } else
                figure.paint(g);
        }
    }
}