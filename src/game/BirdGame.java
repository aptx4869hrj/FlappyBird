package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * @author DuanmuXu
 * @date 2019/12/29 0:56
 */
public class BirdGame extends JPanel {
    // 背景图片
    BufferedImage background;

    // 开始图片
    BufferedImage startImage;
    // 结束图片
    BufferedImage endImage;
    // 地面
    Ground ground;
    // 柱子
    Column column1, column2;
    // 小鸟
    Bird bird;
    // 游戏分数
    int score;

    // 游戏状态
    int state;
    // 状态常量
    public static final int START = 0; // 开始
    public static final int RUNNING = 1; // 运行
    public static final int GAME_OVER = 2; // 结束

    /**
     * 初始化游戏
     */
    public BirdGame() throws Exception{
        // 初始化背景图片
        background = ImageIO.read(getClass().getResource("/image/bg.png"));

        // 初始化开始、结束图片
        startImage = ImageIO.read(getClass().getResource("/image/start.png"));
        endImage = ImageIO.read(getClass().getResource("/image/gameover.png"));

        // 初始化地面、柱子、小鸟
        ground = new Ground();
        column1 = new Column(1);
        column2 = new Column(2);
        bird = new Bird();

        // 初始化分数
        score = 0;

        // 初始化状态
        state = START;
    }

    /**
     * 绘制界面
     */
    public void paint(Graphics g){
        // 绘制背景
        g.drawImage(background, 0, 0, null);

        // 绘制地面
        g.drawImage(ground.image, ground.x, ground.y, null);

        // 绘制柱子
        g.drawImage(column1.image, column1.x - column1.width / 2,
                column1.y - column1.height / 2, null);
        g.drawImage(column2.image, column2.x - column2.width / 2,
                column2.y - column2.height / 2, null);

        // 绘制小鸟(旋转坐标系)
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(-bird.alpha, bird.x, bird.y);
        g.drawImage(bird.image, bird.x - bird.width / 2,
                bird.y - bird.height / 2, null);
        g2.rotate(bird.alpha, bird.x, bird.y);

        // 绘制分数
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 40);
        g.setFont(f);
        g.drawString("" + score, 40, 60);
        g.setColor(Color.WHITE);
        g.drawString("" + score, 40 - 3, 60 - 3);

        // 绘制开始与结束界面
        switch (state){
            case START:
                g.drawImage(startImage, 0, 0, null);
                break;
            case GAME_OVER:
                g.drawImage(endImage, 0, 0, null);
                break;
        }
    }

    /**
     * 开始游戏
     */
    public void action() throws Exception{
        // 添加鼠标监听器
        MouseListener ml = new MouseAdapter() {
            // 鼠标按下事件
            @Override
            public void mouseClicked(MouseEvent e) {
                try{
                    switch (state){
                        case START:
                            // 在开始状态，按下鼠标则转为RUNNING状态
                            state = RUNNING;
                            break;
                        case RUNNING:
                            // 在RUNNING状态时，按下鼠标则小鸟向上飞行一下
                            bird.flappy();
                            break;
                        case GAME_OVER:
                            // 在结束状态时，按下鼠标则重置数据，再次转为START状态
                            column1 = new Column(1);
                            column2 = new Column(2);
                            bird = new Bird();
                            score = 0;
                            state = START;
                            break;
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        };
        // 将监听器添加到当前面板上
        addMouseListener(ml);

        // 不断地移动与重绘
        while (true){
            // 根据状态进行绘制
            switch (state){
                case START:
                    // 小鸟做出飞行动作
                    bird.fly();
                    // 地面向左移动一步
                    ground.step();
                    break;
                case RUNNING:
                    // 地面向左移动一步
                    ground.step();
                    // 柱子都向左移动一步
                    column1.step();
                    column2.step();
                    // 小鸟做出飞行动作
                    bird.fly();
                    // 小鸟向上移动一步
                    bird.step();
                    // 计算分数
                    if (bird.x == column1.x || bird.x == column2.x){
                        score++;
                    }
                    // 检测是否发生碰撞
                    if (bird.hit(ground) || bird.hit(column1) || bird.hit(column2)){
                        state = GAME_OVER;
                    }
                    break;
            }
            // 重新绘制界面
            repaint();
            // 休眠 1000/60 毫秒
            Thread.sleep(1000/60);
        }
    }

    /**
     * 启动方法
     */
    public static void main(String[] args) throws Exception{
        JFrame frame = new JFrame();
        BirdGame game = new BirdGame();
        frame.add(game);
        frame.setSize(440, 670);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.action();
    }
}