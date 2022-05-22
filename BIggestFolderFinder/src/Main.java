import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static double oneKB = Math.pow(2, 10);
    static double oneMB = Math.pow(2, 20);
    static double oneGB = Math.pow(2, 30);
    static double oneTB = Math.pow(2, 40);

    public static void main(String[] args) {

        String folderPath = "D:\\work\\vid";
        File file = new File(folderPath);
        long start = System.currentTimeMillis();

        //System.out.println(getFolderSize(file));

        FolderSizeCalculator calculator =
                new FolderSizeCalculator(file);
        ForkJoinPool pool = new ForkJoinPool();
        long size = pool.invoke(calculator);
        System.out.println(getHumanReadableSize(size));
        System.out.println(getSizeFromHumanReadableSize(getHumanReadableSize(size)) + " в байтах");

        long duration = (System.currentTimeMillis() - start);
        System.out.println(duration + " ms");

    }

    public static long getFolderSize(File folder){

        if(folder.isFile()){
            return folder.length();
        }

        long sum = 0;
        File[] files = folder.listFiles();
        for(File file : files){
            sum += getFolderSize(file);
        }
        return sum;
    }

    public static String getHumanReadableSize(long size){
        double doubleSize = (double) size;


        if (doubleSize < oneKB) {
            return doubleSize + " байт";
        }
        if (doubleSize < oneMB) {
            String str = String.format("%.2f Кбайт", doubleSize / oneKB);
            return str;
        }
        if (doubleSize < oneGB) {
            String str = String.format("%.2f Мбайт", doubleSize / oneMB);
            return str;
        }
        if (doubleSize < oneTB) {
            String str = String.format("%.2f Гбайт", doubleSize / oneGB);
            System.out.println(doubleSize / oneGB);
            return str;
        }
        String str = String.format("%.2f Тбайт", doubleSize / oneTB);
        return str;
    }

    public static long getSizeFromHumanReadableSize(String size){


        String dimension = size.replaceAll("[^a-zA-Zа-яёА-ЯЁ]|\\s", "");
        System.out.println("size: " + size + "    dimension: " + dimension);
        String number = size.replaceAll("[a-zA-Zа-яёА-ЯЁ]|\\s", "") ;
        number = number.replaceAll(",", ".");
        System.out.println("size: " + size + "    number: " + number);


       //Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
       //Matcher matcher = pat.matcher(size);

       //while (matcher.find()){
       //    number += matcher.group();
       //}
        if (dimension.equals("байт") || dimension.equals("b")){

            return Long.valueOf(number).longValue();
        }
        if (dimension.equals("Кбайт") || dimension.equals("Kb")){
            return  (long) (Double.parseDouble(number) * oneKB);
        }
        if (dimension.equals("Мбайт") || dimension.equals("Mb")){
            return  (long) (Double.parseDouble(number) * oneMB);
        }
        if (dimension.equals("Гбайт") || dimension.equals("Gb")){
            return  (long) (Double.parseDouble(number) * oneGB);
        }
        if (dimension.equals("Тбайт") || dimension.equals("Tb")){
            return  (long) (Double.parseDouble(number) * oneTB);
        }




        return 0;
    }
}
