import java.io.File;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
 *
 *  Game() - функция запуска
 *  initGame() - старт игры
 *  actionPerformed() - дальнейшее взаимодействие функций
 *  createApple() - генерация местоположения яблока
 *  loadImages() - загрузка изображений
 *  paintComponent() - Вывод изображений на экран
 *  move() - движение
 *  checkApple() - проверка на съедение яблока
 *  checkCollisions() - проверка на выход за границу, самосъедение
 *  fieldKeyListener() - считывание действией
 *  repaint() - отрисовка змейки
 *  initStats() - статистка
 *  PlayMainMusic() - фоновая музыка
 *
 */


class Game extends JPanel implements ActionListener {

    private final int SIZE = 592; //Ширина
    private final int DOT_SIZE = 16; //Размер точки
    private final int ALL_DOTS = 592; //Кол-во точек
    private int dots;//Размер змеи
    private int appleX; //Координата Х яблока
    private int appleY; //Координата Y яблока
    private Image dot; //Изображение змеи
    private Image apple;//Изображение яблока
    private Image dothead;
    private int[] x = new int[ALL_DOTS]; //Массив змеи для Х
    private int[] y = new int[ALL_DOTS]; //Массив змеи для Y
    private static int move = 2; //Движение
    private int score = 0; //Кол-во очков
    private Timer timer; //Скорость
    public static boolean inGame = true; //Выход из игры
    public static boolean inMenu = true;
    public static boolean inStats = false;
    private AbstractButton Back;
    ImageIcon icon1 = new ImageIcon("123.jpg");


    public void Menu() {


        setBackground(Color.black);
        addKeyListener(new FieldKeyListener());


        Font font = new Font("Comic Sans MS", Font.BOLD, 20);
        JButton Start = new JButton("Start");
        Start.setFont(font);
        Start.setForeground(Color.BLACK);
        Start.setBounds(SIZE / 2 - 75, SIZE / 2 - 120, 150, 60);
        setLayout(null);

        JButton Exit = new JButton("Exit");
        Exit.setFont(font);
        Exit.setForeground(Color.BLACK);
        Exit.setBounds(SIZE / 2 - 75, SIZE / 2 + 60, 150, 60);
        setLayout(null);

        JButton Stats = new JButton("Stats");
        Stats.setFont(font);
        Stats.setForeground(Color.BLACK);
        Stats.setBounds(SIZE / 2 - 75, SIZE / 2 - 30, 150, 60);
        setLayout(null);

        JButton Back = new JButton("Back");
        Back.setFont(font);
        Back.setForeground(Color.BLACK);
        Back.setBounds(SIZE / 2 - 280, SIZE / 2 - 280, 150, 60);
        setLayout(null);


        Exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseSound();
                System.exit(0);
            }
        });

        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inMenu = false;
                StartGameSound();
                Start.setVisible(false);
                Exit.setVisible(false);
                Stats.setVisible(false);
                loadImages();
                initGame();
                setFocusable(true);
            }
        });

        Stats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inMenu = false;
                ChooseSound();
                Start.setVisible(false);
                Exit.setVisible(false);
                Stats.setVisible(false);
                add(Back);
                initStats();
            }
        });

        Back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseSound();
                inMenu = false;
                Back.setVisible(false);
                inStats = false;
                Menu();
            }
        });

        Start.setContentAreaFilled(false);
        Exit.setContentAreaFilled(false);
        Stats.setContentAreaFilled(false);

        add(Stats);
        add(Exit);
        add(Start);

        setSize(592, 592);
    }

    public void PlayGameMusic() {
        try {
            File soundFile = new File("sound22.wav"); //Звуковой файл
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);//Получаем AudioInputStream
            Clip clip = AudioSystem.getClip();//Получаем реализацию интерфейса Clip
            clip.open(ais);//Загружаем наш звуковой поток в Clip
            FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);//Устанавливаем значение
            vc.setValue(1);//5-громко
            clip.setFramePosition(0); //устанавливаем указатель на старт
            clip.start();//Запуск
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }
    }

    public void AppleSpawn() {
        try {
            File soundFile = new File("yum.wav"); //Звуковой файл
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);//Получаем AudioInputStream
            Clip clip = AudioSystem.getClip();//Получаем реализацию интерфейса Clip
            clip.open(ais);//Загружаем наш звуковой поток в Clip
            FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);//Устанавливаем значение
            vc.setValue(1);//5-громко
            clip.setFramePosition(0); //устанавливаем указатель на старт
            clip.start();//Запуск
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }
    }

    public void LoseSound() {
        try {
            File soundFile = new File("lose.wav"); //Звуковой файл
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);//Получаем AudioInputStream
            Clip clip = AudioSystem.getClip();//Получаем реализацию интерфейса Clip
            clip.open(ais);//Загружаем наш звуковой поток в Clip
            FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);//Устанавливаем значение
            vc.setValue(1);//5-громко
            clip.setFramePosition(0); //устанавливаем указатель на старт
            clip.start();//Запуск
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }
    }

    public void ChooseSound() {
        try {
            File soundFile = new File("ChooseSound.wav"); //Звуковой файл
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);//Получаем AudioInputStream
            Clip clip = AudioSystem.getClip();//Получаем реализацию интерфейса Clip
            clip.open(ais);//Загружаем наш звуковой поток в Clip
            FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);//Устанавливаем значение
            vc.setValue(1);//5-громко
            clip.setFramePosition(0); //устанавливаем указатель на старт
            clip.start();//Запуск
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }
    }

    public void StartGameSound() {
        try {
            File soundFile = new File("StartGame.wav"); //Звуковой файл
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);//Получаем AudioInputStream
            Clip clip = AudioSystem.getClip();//Получаем реализацию интерфейса Clip
            clip.open(ais);//Загружаем наш звуковой поток в Clip
            FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);//Устанавливаем значение
            vc.setValue(1);//5-громко
            clip.setFramePosition(0); //устанавливаем указатель на старт
            clip.start();//Запуск
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }
    }

    public Game() {
        super();
        Menu();
        PlayGameMusic();



    }

    public void loadImages() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
        ImageIcon iih = new ImageIcon("dothead.png");
        dothead = iih.getImage();
    }

    public void initGame() {
        inMenu=false;
        dots = 3;

        for (int i = 0; i < dots; i++) {
            x[i] = 272 - i * DOT_SIZE;
            y[i] = 272;
        }

        timer = new Timer(150, this);
        timer.start();

        createApple();
    }

    public void createApple() {
        appleX = new Random().nextInt(35) * DOT_SIZE;
        appleY = new Random().nextInt(34) * DOT_SIZE;
        AppleSpawn();

    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            createApple();
            dots++;
            score += 1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkCollisions();
            checkApple();
            setFocusable(true);
            requestFocus();

        } else {
            timer.stop();
            return;
        }
        repaint();
    }

    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
                break;
            }
        }
        if (x[0] > SIZE - 20) {
            inGame = false;
        }
        if (y[0] > SIZE - 45) {
            inGame = false;
        }
        if (x[0] < 0) {

            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (move) {
            case 1:
                y[0] -= DOT_SIZE;
                break;
            case 2:
                x[0] += DOT_SIZE;
                break;
            case 3:
                y[0] += DOT_SIZE;
                break;
            case 4:
                x[0] -= DOT_SIZE;
                break;
        }
    }

    public void initStats() {
        inStats = true;
        setBackground(Color.GREEN);

    }

    static class FieldKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_X && !inGame) {
                System.exit(0);
            }
            if (key == KeyEvent.VK_LEFT && move != 2) {
                move = 4;
            }
            if (key == KeyEvent.VK_RIGHT && move != 4) {
                move = 2;
            }

            if (key == KeyEvent.VK_UP && move != 3) {
                move = 1;
            }
            if (key == KeyEvent.VK_DOWN && move != 1) {
                move = 3;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(icon1.getImage(), 0, 0, 592, 592, this);
        if (inGame) { //Если в игре
            g.drawImage(icon1.getImage(), 0, 0, 592, 592, this);
            String str = "Score: " + score + "";//конец игры
            g.setColor(Color.black);
            Font font = new Font("Comic Sans MS", Font.BOLD, 25);
            g.setFont(font);

            g.drawString(str, SIZE - 150, 25);
            g.drawImage(apple, appleX, appleY, this); //выводим на экран яблоко

            AffineTransform original = ((Graphics2D) g).getTransform();
            AffineTransform newForm = (AffineTransform) original.clone();

            if (move == 1) {
                newForm.rotate(1.6d, x[0] + 8, y[0] + 8);
                ((Graphics2D) g).setTransform(newForm);
            } else if (move == 2) {
                newForm.rotate(3.1d, x[0] + 8, y[0] + 8);
                ((Graphics2D) g).setTransform(newForm);
            } else if (move == 3) {
                newForm.rotate(4.7d, x[0] + 8, y[0] + 8);
                ((Graphics2D) g).setTransform(newForm);
            }

            g.drawImage(dothead, x[0], y[0], this);

            ((Graphics2D) g).setTransform(original);
            for (int i = 1; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this); //Отрисовываем змейку
            }
        } else { //Иначе
            String str = "Game Over :(";//конец игры
            g.setColor(Color.black);
            Font font = new Font("Comic Sans MS", Font.BOLD, 25);
            g.setFont(font);
            g.drawString(str, 230, SIZE / 3);
            str = "Score: " + score + "";//конец игры
            g.setColor(Color.black);
            font = new Font("Comic Sans MS", Font.BOLD, 25);
            g.setFont(font);
            g.drawString(str, 240, SIZE - 360);
            str = "Press X to exit!";
            g.setColor(Color.black);
            font = new Font("Comic Sans MS", Font.BOLD, 25);
            g.setFont(font);
            g.drawString(str, 200, SIZE - 320);

            LoseSound();

            try (FileWriter writer = new FileWriter("notes3.txt", true)) {
                writer.append(String.valueOf(score));
                writer.append('\n');
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (inStats) {
            String str = "Statistic";//конец игры
            g.setColor(Color.black);
            Font font = new Font("Comic Sans MS", Font.BOLD, 28);
            g.setFont(font);
            g.drawString(str, 210, SIZE / 3);

            java.util.List<String> score = new ArrayList<>();

            try {
                BufferedReader reader = new BufferedReader(new FileReader("notes3.txt"));

                while (reader.ready()) {
                    score.add(reader.readLine());
                }
                Collections.reverse(score);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            int y = 240;
            for (int i = 0; i < (Math.min(score.size(), 5)); i++) {
                str = (i + 1) + ". Score: " + score.get(i);
                g.setColor(Color.black);
                font = new Font("Comic Sans MS", Font.BOLD, 25);
                g.setFont(font);
                g.drawString(str, 200, y);
                y += 40;
            }
        }
    }
}