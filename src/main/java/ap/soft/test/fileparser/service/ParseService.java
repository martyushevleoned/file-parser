package ap.soft.test.fileparser.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParseService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.max-file-size}")
    private int maxFileSize;

    public String saveFile(MultipartFile file) throws IOException {

        if (file.getSize() == 0)
            throw new IOException("file is empty");

        if (file.getSize() > maxFileSize)
            throw new IOException("file is too large");

        if (!Files.exists(Path.of(uploadPath)))
            Files.createDirectory(Path.of(uploadPath));

        String fileId = UUID.randomUUID().toString();
        file.transferTo(new File(uploadPath + "/" + fileId + ".txt"));

        return fileId;
    }

    public List<String> parseByFileId(String fileId) throws IOException {

        Path filePath = Path.of(uploadPath + "/" + fileId + ".txt");

        if (!Files.exists(filePath))
            throw new IOException("File with id: " + fileId + " not found");

        List<String> strings = Files.readAllLines(filePath);

        if (strings.isEmpty())
            throw new IOException("File with id: " + fileId + " is empty");

        return parse(strings);
    }

    private List<String> parse(List<String> strings) {

        List<String> result = new LinkedList<>();
        // счётчик уровня вложенности
        int currentHashtagCount;
        // текущий номер раздела
        List<Integer> num = new LinkedList<>(List.of(0));

        for (String s : strings) {

            // определяем уровень вложенности
            currentHashtagCount = hashtagCount(s);

            // игнорируем не вложенные строки
            if (currentHashtagCount == 0) {
                result.add(s);
                continue;
            }

            // если уровень вложенности 1, то отбрасываем всё после первого числа
            // и увеличиваем число на 1
            if (currentHashtagCount == 1) {
                num = new LinkedList<>(List.of(num.get(0) + 1));
            }

            // если уровень вложенности > 1
            if (currentHashtagCount > 1) {

                // обрезаем номер раздела до актуальной части
                num = num.stream().limit(currentHashtagCount).collect(Collectors.toList());

                // увеличиваем номер подраздела
                if (num.size() == currentHashtagCount)
                    num.set(num.size() - 1, num.get(num.size() - 1) + 1);

                // добавляем номер подраздела
                if (num.size() < currentHashtagCount)
                    num.add(1);
            }

            result.add(listToString(num) + " " + s.substring(currentHashtagCount));
        }

        return result;
    }

    private int hashtagCount(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '#')
                return i;
        }
        return s.length();
    }

    private String listToString(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(i -> stringBuilder.append(".").append(i));
        return stringBuilder.substring(1);
    }
}