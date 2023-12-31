import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Console;

public class Binarization {

    int[] avgValueHis;


    public BufferedImage binarizeByThreshold(BufferedImage image, int threshold){
        int width = image.getWidth();
        int height = image.getHeight();
        threshold=threshold*3;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                int color = image.getRGB(j,i);
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;
                int sum = (red + green + blue);
                if(sum>=threshold){
                    bufferedImage.setRGB(j,i, Color.WHITE.getRGB());
                } else{
                    bufferedImage.setRGB(j,i,0);
                }

            }

        }
        return bufferedImage;
    }

    public BufferedImage binarizePercentBlackSelection(BufferedImage image, int percent) {

        System.out.println("Percent: "+percent+"%");

        setAvgValueHistogram(image);
        int i=0;
        int threshold= 255;
        int blackPixels=0;
        while(blackPixels<(float)image.getHeight()*(float)image.getWidth()*(float)percent/100f&&threshold!=0){
            i++;
            System.out.println("Przejście nr:"+i+" HistogramValue: "+avgValueHis[threshold]);
            blackPixels+=avgValueHis[threshold--];
        }
        System.out.println("Ustalony Threshold: "+threshold);
        return binarizeByThreshold(image,255-threshold);
    }

    public BufferedImage binarizeMeanIterativeSelection(BufferedImage image){
        int threshold=77;
        int iterationNumber=1000;
        setAvgValueHistogram(image);
        for(int i=0;i<iterationNumber;i++)
        {
            int sum=0;
            int count=0;
            for (int j = 0; j <= threshold; j++)
            {
                sum += j * avgValueHis[j];
                count += avgValueHis[j];
            }
            double firstValue=(count == 0) ? 0 : (double) sum / count;
            sum=0;
            count=0;
            for (int j = threshold+1; j <= avgValueHis.length-1; j++)
            {
                sum += j * avgValueHis[j];
                count += avgValueHis[j];
            }
            double secondValue=(count == 0) ? 0 : (double) sum / count;


            threshold = (int) ((firstValue + secondValue) / 2);
        }
        return binarizeByThreshold(image,threshold);
    }




    public BufferedImage binarizeEntropySelection(BufferedImage image){
        return image;
    }
    public BufferedImage binarizeMinimumError(BufferedImage image){
        return image;
    }
    public BufferedImage binarizeFuzzyMinimumError(BufferedImage image){
        return image;
    }
    public void setAvgValueHistogram(BufferedImage image)
    {
        avgValueHis = new int[256];
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                int pixel=image.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                int avg=(int) (0.21*r + 0.715*g + 0.072*b);
                avgValueHis[avg]++;
            }
        }

    }

}
