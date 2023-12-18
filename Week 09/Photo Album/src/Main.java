import bg.sofia.uni.fmi.mjt.photoalbum.ParallelMonochromeAlbumCreator;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        ParallelMonochromeAlbumCreator parallelMonochromeAlbumCreator = new ParallelMonochromeAlbumCreator(5);

        parallelMonochromeAlbumCreator.processImages("C:\\Users\\Hp\\Desktop\\ColorImages",
            "C:\\Users\\Hp\\Desktop\\FinalImages");
    }
}