import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class MainFrame extends JFrame{

    private ImagePanel imagePanel = new ImagePanel();
    private PPMImage ppmImage;

    private Dialog dialog;

    private String lastImageType="ppm";
    private String lastImageFileName="ppm-test-02-p3-comments.ppm";

    public MainFrame(){


        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(imagePanel);

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("Plik");
        menuBar.add(fileMenu);
        MenuItem loadPPMOption = new MenuItem("Wczytaj PPM");
        loadPPMOption.addActionListener(e -> loadPPM());
        MenuItem loadJPGOption = new MenuItem("Wczytaj JPG");
        loadJPGOption.addActionListener(e -> loadJPG());
        MenuItem saveFileOption = new MenuItem("Zapisz");
        saveFileOption.addActionListener(e->saveFile());
        MenuItem resetOption = new MenuItem("Odśwież/Reset");
        resetOption.addActionListener(e->resetImage());


        fileMenu.add(loadPPMOption);
        fileMenu.add(loadJPGOption);
        fileMenu.add(saveFileOption);
        fileMenu.add(resetOption);

        Menu histogram = new Menu("Histogram");
        MenuItem histogramOption = new MenuItem("Rozszerzenie i wyrównanie");
        histogramOption.addActionListener(e->equalization());
        histogram.add(histogramOption);
        menuBar.add(histogram);


        Menu binaryzacja = new Menu("Binaryzacja");
        MenuItem binaryzacja1 = new MenuItem("Binaryzacja Ręczna (próg)");
        binaryzacja1.addActionListener(e->binarizationThreshhold());

        MenuItem binaryzacja2 = new MenuItem("Procentowa selekcja czarnego");
        binaryzacja2.addActionListener(e->binarizePercentBlack());

        MenuItem binaryzacja3 = new MenuItem("Selekcja iteratywna średniej");
        MenuItem binaryzacja4 = new MenuItem("Selekcja entropii");
        MenuItem binaryzacja5 = new MenuItem("Błąd Minimalny");
        MenuItem binaryzacja6 = new MenuItem("Metoda rozmytego błędu minimalnego");



        binaryzacja.add(binaryzacja1);
        binaryzacja.add(binaryzacja2);
        binaryzacja.add(binaryzacja3);
        binaryzacja.add(binaryzacja4);
        binaryzacja.add(binaryzacja5);
        binaryzacja.add(binaryzacja6);
        menuBar.add(binaryzacja);



        setMenuBar(menuBar);

        try {
            System.out.println("Checking for an image");
            ppmImage = readImage("ppm-test-02-p3-comments.ppm");
            System.out.println("Found an Image!");
            loadImage();
        } catch (Exception e) {
            System.out.println("Didn't find the image");
        }


    }

    private void resetImage() {
        if(lastImageType=="ppm"){
            try {
                System.out.println("reset PPM");
                ppmImage = readImage(lastImageFileName);
                loadImage();
            } catch (IOException ex) {
                System.out.println("Błąd wczytywania PPM");
            }
        }else{
            try {
                System.out.println("reset JPG");
                BufferedImage bufferedImage = ImageIO.read(new File(lastImageFileName));
                imagePanel.setImage(bufferedImage);
            } catch (IOException ex) {
                System.out.println("Błąd wczytywania JPG");
            }


        }
        System.out.println("Po resecie");
        imagePanel.repaint();
    }

    private PPMImage readImage(String s) throws FileNotFoundException {

        PPM3Reader ppm3Reader = new PPM3Reader();

        Scanner scanner = new Scanner(new FileInputStream(s));

        return ppm3Reader.fileToPPMImage(scanner);
    }

    private void loadImage() {
        System.out.println("Starting to load image!");

        Integer width = ppmImage.getWidth();
        System.out.println("Loaded Width: "+ppmImage.getWidth());
        Integer height = ppmImage.getHeight();
        System.out.println("Loaded Height: "+ppmImage.getHeight());
        BufferedImage image;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        ppmImage.getPixels()
                .forEach((i, pixel) -> {
                    Color color = new Color(pixel.getR(), pixel.getG(), pixel.getB());
                    int x = i % width;
                    int y = i / width;
                    image.setRGB(x, y, color.getRGB());
                });

        imagePanel.setImage(image);
    }
    private void equalization() {
        BufferedImage bufferedImage = new HisEqualization().equalization(imagePanel.getImage());
        imagePanel.setImage(bufferedImage);
    }


    private void binarizePercentBlack(){
        dialog = new Dialog(this, "Binaryzacja przez procentową selekcję czarnego", true);
        dialog.setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                dialog.dispose();
            }
        });

        TextField percentField = new TextField("50");

        percentField.setColumns(25);
        dialog.add(percentField);
        Button confirm = new Button("Potwierdź");
        confirm.addActionListener(e -> {
            BufferedImage bufferedImage = new Binarization().binarizePercentBlackSelection(imagePanel.getImage(), Integer.parseInt(percentField.getText()));
            imagePanel.setImage(bufferedImage);
            dialog.dispose();
        });



        dialog.add(confirm);
        dialog.setSize(300,100);
        dialog.setVisible(true);
    }
    private void binarizationThreshhold() {
        dialog = new Dialog(this, "Binaryzacja przez ustalenie progu", true);
        dialog.setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                dialog.dispose();
            }
        });

        TextField thresholdField = new TextField("256");

        thresholdField.setColumns(25);
        dialog.add(thresholdField);
        Button confirm = new Button("Potwierdź");
        confirm.addActionListener(e -> {
            BufferedImage bufferedImage = new Binarization().binarizeByThreshold(imagePanel.getImage(), Integer.parseInt(thresholdField.getText()));
            imagePanel.setImage(bufferedImage);
            dialog.dispose();
        });



        dialog.add(confirm);
        dialog.setSize(300,100);
        dialog.setVisible(true);
    }

    private void saveFile() {
        dialog = new Dialog(this, "Zapisz Plik", true);
        dialog.setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                dialog.dispose();
            }
        });

        TextField fileField = new TextField("nazwa.jpg");

        fileField.setColumns(25);
        dialog.add(fileField);
        Button confirm = new Button("Potwierdź");
        confirm.addActionListener(e -> {

            dialog.setVisible(false);
            BufferedImage image =imagePanel.getImage();
            JPEGImageWriteParam jpegImageWriteParam = new JPEGImageWriteParam(null);
            try{
                ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
                imageWriter.setOutput(new FileImageOutputStream(new File(fileField.getText())));
                imageWriter.write(null, new IIOImage(image, null, null),jpegImageWriteParam);
                dialog.dispose();
            }catch(IOException ex){
                System.out.println("Error while saving");
            }

        });

        dialog.add(confirm);


        dialog.setSize(300,100);
        dialog.setVisible(true);

    }

    private void loadJPG() {
        dialog = new Dialog(this, "Wczytaj z pliku", true);
        dialog.setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                dialog.dispose();
            }
        });

        TextField fileField = new TextField("nazwa.jpg");

        fileField.setColumns(25);
        dialog.add(fileField);
        Button confirm = new Button("Potwierdź");
        confirm.addActionListener(e -> {
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(fileField.getText()));
                imagePanel.setImage(bufferedImage);
                lastImageType="jpg";
                lastImageFileName=fileField.getText();
                dialog.dispose();
            } catch (IOException ex) {
                System.out.println("Błąd wczytywania JPG");
            }


        });



        dialog.add(confirm);
        dialog.setSize(300,100);
        dialog.setVisible(true);

    }

    private void loadPPM() {
        dialog = new Dialog(this, "Wczytaj z pliku", true);
        dialog.setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                dialog.dispose();
            }
        });

        TextField fileField = new TextField("nazwa.ppm");

        fileField.setColumns(25);
        dialog.add(fileField);
        Button confirm = new Button("Potwierdź");
        confirm.addActionListener(e -> {
            try {
                ppmImage = readImage(fileField.getText());
                loadImage();
                lastImageType="ppm";
                lastImageFileName=fileField.getText();
                dialog.dispose();
            } catch (IOException ex) {
                System.out.println("Błąd wczytywania PPM");
            }


        });



        dialog.add(confirm);
        dialog.setSize(300,100);
        dialog.setVisible(true);

    }

}
