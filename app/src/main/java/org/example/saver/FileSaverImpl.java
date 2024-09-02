package org.example.saver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaverImpl implements FileSaver {
    private static final Logger logger = LoggerFactory.getLogger(FileSaverImpl.class);

    @Override
    public void save(String data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data);
            logger.info("Данные успешно сохранены в " + filePath);
        } catch (IOException e) {
            logger.error("Ошибка записи данных в файл: " + e.getMessage());
        }
    }
}