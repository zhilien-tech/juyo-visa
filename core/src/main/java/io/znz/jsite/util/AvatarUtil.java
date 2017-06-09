package io.znz.jsite.util;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据用户昵称生成用户头像
 */
public class AvatarUtil {
    private static final Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    private DefaultGenerator genartor;

    private AvatarUtil() {
        this.genartor = new DefaultGenerator();
    }

    public static AvatarUtil getInstance() {
        return new AvatarUtil();
    }

    public BufferedImage create(String str, int size) {
        Preconditions.checkArgument(size > 0 && StringUtils.isNotBlank(str));
        String hash = Md5Util.md5(str);
        boolean[][] array = genartor.getBooleanValueArray(hash);

        int ratio = DoubleMath.roundToInt(size / 5.0, RoundingMode.HALF_UP);

        BufferedImage identicon = new BufferedImage(ratio * 5, ratio * 5, BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics = identicon.createGraphics();

        graphics.setColor(genartor.getBackgroundColor()); // 背景色
        graphics.fillRect(0, 0, identicon.getWidth(), identicon.getHeight());

        graphics.setColor(genartor.getForegroundColor()); // 图案前景色
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            str = str.substring(0, 1);
            //设置画笔字体
            Font font = new Font("宋体", Font.BOLD, size / 2);
            graphics.setFont(font);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //判断字体的绘制位置,居中显示
            FontMetrics fm = graphics.getFontMetrics(font);
            int textWidth = fm.stringWidth(str);
            int x = (identicon.getWidth() - textWidth) / 2;
            int ascent = fm.getAscent();
            int descent = fm.getDescent();
            int y = (identicon.getHeight() - ascent - descent) / 2 + ascent;
            //画出字符串
            graphics.drawString(str, x, y);
        } else {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (array[i][j]) {
                        graphics.fillRect(j * ratio, i * ratio, ratio, ratio);
                    }
                }
            }
        }
        return identicon;
    }

    public class DefaultGenerator {
        private String hash;
        private boolean[][] booleanValueArray;

        public boolean[][] getBooleanValueArray(String hash) {
            Preconditions.checkArgument(StringUtils.isNotBlank(hash) && hash.length() >= 16, "illegal argument hash:not null and size >= 16");
            this.hash = hash;
            boolean[][] array = new boolean[6][5];
            //初始化字符串
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    array[i][j] = false;
                }
            }
            for (int i = 0; i < hash.length(); i += 2) {
                int s = i / 2; //只取hash字符串偶数编号（从0开始）的字符
                boolean v =
                    DoubleMath.roundToInt(Integer.parseInt(hash.charAt(i) + "", 16) / 10.0, RoundingMode.HALF_UP) > 0 ? true : false;
                if (s % 3 == 0) {
                    array[s / 3][0] = v;
                    array[s / 3][4] = v;
                } else if (s % 3 == 1) {
                    array[s / 3][1] = v;
                    array[s / 3][3] = v;
                } else {
                    array[s / 3][2] = v;
                }
            }
            this.booleanValueArray = array;
            return this.booleanValueArray;
        }

        public Color getBackgroundColor() {
            int r = Integer.parseInt(String.valueOf(this.hash.charAt(0)), 16) * 16;
            int g = Integer.parseInt(String.valueOf(this.hash.charAt(1)), 16) * 16;
            int b = Integer.parseInt(String.valueOf(this.hash.charAt(2)), 16) * 16;
            return new Color(r, g, b);
        }

        public Color getForegroundColor() {
            int r = Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 1)), 16) * 16;
            int g = Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 2)), 16) * 16;
            int b = Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 3)), 16) * 16;
            return new Color(r, g, b);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedImage image = AvatarUtil.getInstance().create("亮", 200);
        String fileName = dateFormat.format(new Date()) + RandomStringUtils.randomAlphanumeric(8) + ".png";
        ImageIO.write(image, "PNG", new File("/Users/Chaly/Desktop/" + fileName));
    }
}
