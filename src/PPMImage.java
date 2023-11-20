import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PPMImage {

    public String version;
    private int width;
    private int height;

    private int colorMax;
    private Map<Integer, Pixel> pixels;

    public PPMImage(String version, int width, int height, int colorMax) {
        this.width = width;
        this.height = height;
        this.colorMax = colorMax;
        this.pixels = new HashMap<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<Integer, Pixel> getPixels() {
        return pixels;
    }


    public void addPixel(Pixel pixel){
        this.pixels.put(this.pixels.size(), pixel);
        //System.out.println("Added New Pixel");
    }

}
