import java.util.Scanner;

public class PPM3Reader {

    private String Comment = "#(.*)";


    public PPMImage fileToPPMImage(Scanner sc){
        if(!sc.hasNext()){
            throw new RuntimeException("Can't open file");
        }

        System.out.println("Opening a file!");



        String version = readVersion(sc);
        // System.out.println("Setting Image Width");
        int width=readNextInt(sc);

        //System.out.println("Setting Image Height");
        int height=readNextInt(sc);

        // System.out.println("Setting Image ColorMax");
        int colorMax=readNextInt(sc);


        PPMImage ppmImage=new PPMImage(version, width,height,colorMax);
        //System.out.println("Made a new PPM Image with Version: "+version+" width: "+width+" height: "+height+ " colorMax: "+colorMax);


        while(sc.hasNext()){
            int rValue = readNextInt(sc);
            int gValue = readNextInt(sc);
            int bValue = readNextInt(sc);
            // System.out.println("Setting Pixel r:"+rValue+" g: "+gValue+ " b: "+bValue);

            Pixel pixel = new Pixel(rValue, gValue,bValue);
            ppmImage.addPixel(pixel);
        }
        return ppmImage;
    }

    private int readNextInt(Scanner sc) {
        int wholeInt=-1;
        boolean finishedChecking=false;
        do {
            if (sc.hasNext(Comment)) {
                sc.nextLine();
                continue;
            }

            if(!sc.hasNext()){
                finishedChecking=true;
            }
            String nextToken = sc.next();
            String[] singleNumber = nextToken.split("/[-0-9]+/");
            if(!singleNumber[0].isEmpty()){
                wholeInt=Integer.parseInt(singleNumber[0]);
                return wholeInt;
            }

        } while (!finishedChecking);

        throw new RuntimeException("Couldn't find next Int");
    }




    String readVersion(Scanner sc){
        if(sc.hasNextLine()){
            String firstLine = sc.nextLine();
            return firstLine;
        }else{
            System.out.println("Doesn't have a first line");
            return "Version Unknown";
        }
    }

}
