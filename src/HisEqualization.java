import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

public class HisEqualization {


    public BufferedImage equalization(BufferedImage original) {

        int r,g,b;
        int alpha;
        int pixel;

        //tworzenie LookUpTables zawierających tabele z danymi o konkretnych kolorach po operacji rozszerzenia
        ArrayList<int[]> histLUT = lookUpTable(original);
        BufferedImage equalizedImage = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        //ustawianie pojedynczych pixeli obrazka wyjściowego
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                alpha = new Color(original.getRGB(i, j)).getAlpha();
                r = new Color(original.getRGB(i, j)).getRed();
                g = new Color(original.getRGB(i, j)).getGreen();
                b = new Color(original.getRGB(i, j)).getBlue();

                r = histLUT.get(0)[r];
                g = histLUT.get(1)[g];
                b = histLUT.get(2)[b];

                pixel = colorToRGB(alpha, r, g, b);
                equalizedImage.setRGB(i, j, pixel);

            }
        }

        return equalizedImage;

    }

    // Rozszerzenie poszczególnych kanałów
    private ArrayList<int[]> lookUpTable(BufferedImage image) {

        ArrayList<int[]> imageHis = imageHistogram(image);
        ArrayList<int[]> imageLUT = new ArrayList<int[]>();


        int[] rHis = new int[256];
        int[] gHis = new int[256];
        int[] bHis = new int[256];

        long rSum = 0;
        long gSum = 0;
        long bSum = 0;

        //rozszerzenie
        float scale_factor = (float) (255.0 / (image.getWidth() * image.getHeight()));
        for (int i = 0; i < rHis.length; i++) {
            // red
            rSum += imageHis.get(0)[i];
            int valr = (int) (rSum * scale_factor);
            rHis[i] = Math.min(valr, 255);

            // green
            gSum += imageHis.get(1)[i];
            int valg = (int) (gSum * scale_factor);
            gHis[i] = Math.min(valg, 255);

            // blue
            bSum += imageHis.get(2)[i];
            int valb = (int) (bSum * scale_factor);
            bHis[i] = Math.min(valb, 255);
        }

        imageLUT.add(rHis);
        imageLUT.add(gHis);
        imageLUT.add(bHis);

        return imageLUT;
    }

    // Pełny histogram zawierający 3 histogramy kanałów r,g,b
    public ArrayList<int[]> imageHistogram(BufferedImage image) {

        int[] rHis = new int[256];
        int[] gHis = new int[256];
        int[] bHis = new int[256];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                int red = new Color(image.getRGB(i, j)).getRed();
                int green = new Color(image.getRGB(i, j)).getGreen();
                int blue = new Color(image.getRGB(i, j)).getBlue();

                rHis[red]++;
                gHis[green]++;
                bHis[blue]++;

            }
        }
        ArrayList<int[]> fullHist = new ArrayList<int[]>();
        fullHist.add(rHis);
        fullHist.add(gHis);
        fullHist.add(bHis);

        return fullHist;
    }

    // konwersja koloru z intów do 8bit
    private int colorToRGB(int alpha, int red, int green, int blue) {

        int pixel = 0;
        pixel += alpha;
        pixel = pixel << 8;
        pixel += red;
        pixel = pixel << 8;
        pixel += green;
        pixel = pixel << 8;
        pixel += blue;

        return pixel;
    }
}
