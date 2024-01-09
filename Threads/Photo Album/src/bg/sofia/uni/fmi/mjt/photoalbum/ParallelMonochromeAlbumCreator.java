package bg.sofia.uni.fmi.mjt.photoalbum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {

    private int imageProcessorsCount;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
    }

    private void createDirectoryIfNecessary(String outputDirectory) {
        Path dirPath = Path.of(outputDirectory);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectory(dirPath);
            } catch (IOException e) {
                throw new RuntimeException("Unexpected IO Exception");
            }
        }
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {

        createDirectoryIfNecessary(outputDirectory);
        ImageProcessor imageProcessor = new ImageProcessor();
        int currentConsumerNumber = 0;
        try (Stream<Path> paths = Files.walk(Paths.get(sourceDirectory))) {
            List<Path> allFiles = paths
                .filter(Files::isRegularFile)
                .toList();
            for (Path path : allFiles) {
                Thread tmpThread = new ProducerThread(imageProcessor, path);
                tmpThread.start();
                if (currentConsumerNumber < imageProcessorsCount) {
                    Thread tmpConsumer = new ConsumerThread(imageProcessor, outputDirectory);
                    tmpConsumer.start();
                    currentConsumerNumber++;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
