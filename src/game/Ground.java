package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * @author DuanmuXu
 * @date 2020/1/2 15:58
 */
public class Ground {
    // 图片
    BufferedImage image;
    // 位置
    int x, y;
    // 宽高
    int width, height;

    // 初始化地面
    public Ground() throws Exception{
        image = ImageIO.read(getClass().getResource("/image/ground.png"));
        width = image.getWidth();
        height = image.getHeight();
        x = 0;
        y = 500;
    }

    // 向左移动一步
    public void step(){
        x--;
        if (x == -109){
            x = 0;
        }
    }
}
