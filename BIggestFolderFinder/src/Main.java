import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static String[] variables = { "b", "Kb", "Mb", "Gb", "Tb"};
    private static String[] ruVariables = { "байт", "Кбайт", "Мбайт", "Гбайт", "Тбайт"};

    static double step = 1024;

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

        String str = null;
        for (int i = 0; i < variables.length; i++){

            if(size > Math.pow(step, i) && size < Math.pow(step, i+1) ){
                str = String.format("%.2f" + variables[i], doubleSize / Math.pow(step, i));
                System.out.println(size);
                break;
                }
            }
        return str;
    }

    public static long getSizeFromHumanReadableSize(String size){
        long byteSize = 0;

        String dimension = size.replaceAll("[^a-zA-Zа-яёА-ЯЁ]|\\s", "");
        System.out.println("size: " + size + "    dimension: " + dimension);
        String number = size.replaceAll("[a-zA-Zа-яёА-ЯЁ]|\\s", "") ;
        number = number.replaceAll(",", ".");
        System.out.println("size: " + size + "    number: " + number);

        for (int i = 0; i < variables.length; i++){
            if(dimension.equals(variables[i]) || dimension.equals(ruVariables[i])){
                byteSize = (long) (Double.parseDouble(number) * Math.pow(step, i));
                break;
            }
        }
        return byteSize;
    }
}
