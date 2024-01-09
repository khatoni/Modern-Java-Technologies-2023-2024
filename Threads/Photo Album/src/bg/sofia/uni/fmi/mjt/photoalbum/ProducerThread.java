package bg.sofia.uni.fmi.mjt.photoalbum;

import java.nio.file.Path;

public class ProducerThread extends Thread {

    private ImageProcessor imageProcessor;
    private Path imagePath;

    public ProducerThread(ImageProcessor imageProcessor, Path imagePath) {
        this.imageProcessor = imageProcessor;
        this.imagePath = imagePath;
    }

    @Override
    public void run() {
        imageProcessor.addImage(imageProcessor.loadImage(imagePath));
    }
}
