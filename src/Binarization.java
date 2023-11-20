import java.awt.*;
import java.awt.image.BufferedImage;

public class Binarization {

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
        percent=750*percent/100;
        int width = image.getWidth();
        int height = image.getHeight();
        int sum = 0;
        int nrOfPixels = width * height;
        int nrOfBlackPixels = 0;
        int threshold = 0;
        int actualPercent = 0;

        BufferedImage bufferedImage = null;
        while (threshold <= percent) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            nrOfBlackPixels = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int color = image.getRGB(j, i);
                    int blue = color & 0xff;
                    int green = (color & 0xff00) >> 8;
                    int red = (color & 0xff0000) >> 16;
                    sum = (red + green + blue);
                    if (sum >= threshold) {
                        bufferedImage.setRGB(j, i, Color.WHITE.getRGB());
                    } else {
                        bufferedImage.setRGB(j, i, 0);
                        nrOfBlackPixels++;
                    }
                    if ((double)nrOfBlackPixels >= (double) (nrOfPixels * percent / 100)) break;
                }
            }
            threshold++;
        }
        return bufferedImage;
    }

    public BufferedImage binarizeMeanIterativeSelection(BufferedImage image){
        return image;
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

}
