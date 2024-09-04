package org.example.saver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaverImpl implements FileSaver {
    private static final Logger logger = LoggerFactory.getLogger(FileSaverImpl.class);

    @Override
    public void save(String data, String filePath) {
        logger.info("Начинается сохранение данных в файл: {}", filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data);
            logger.debug("Данные успешно сохранены в файл: {}", filePath);
        } catch (IOException e) {
            logger.error("Ошибка записи данных в файл: {}", filePath, e);
        }
    }
}