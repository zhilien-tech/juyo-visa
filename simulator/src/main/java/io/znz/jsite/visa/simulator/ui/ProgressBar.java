package io.znz.jsite.visa.simulator.ui;

/**
 * Created by Chaly on 2017/3/3.
 */
public class ProgressBar {
    private double max;
    private double length;

    public ProgressBar() {
        this.max = 100;
        this.length = 20;
    }

    public ProgressBar(double max, int length) {
        this.max = max;
        this.length = length;
    }

    /**
     * 显示进度条
     *
     * @param point 当前点
     * @return 进度条结果
     */
    public void progress(double point) {
        // 根据进度参数计算进度比率
        double rate = point / this.max;
        // 根据进度条长度计算当前记号
        int barSign = (int) (rate * this.length);
        // 生成进度条
        System.out.print("\r");
        System.out.print(makeBarBySignAndLength(barSign) + String.format(" %.2f%%", rate * 100));
    }

    /**
     * 构造进度条
     *
     * @param barSign 进度条标记(当前点)
     * @return 字符型进度条
     */
    private String makeBarBySignAndLength(int barSign) {
        StringBuilder bar = new StringBuilder();
        bar.append("[");
        for (int i = 1; i <= this.length; i++) {
            if (i < barSign) {
                bar.append("-");
            } else if (i == barSign) {
                bar.append(">");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]");
        return bar.toString();
    }
}
