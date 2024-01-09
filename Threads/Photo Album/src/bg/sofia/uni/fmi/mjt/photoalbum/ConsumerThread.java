package bg.sofia.uni.fmi.mjt.photoalbum;

public class ConsumerThread extends Thread {
    private final ImageProcessor imageProcessor;
    private final String directory;

    public ConsumerThread(ImageProcessor imageProcessor, String directory) {
        this.imageProcessor = imageProcessor;
        this.directory = directory;
    }

    @Override
    public void run() {
        Image image = imageProcessor.consumeImage();
        Image.convertToBlackAndWhite(image, directory);
    }
}
