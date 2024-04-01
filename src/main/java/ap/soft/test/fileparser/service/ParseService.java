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
        String num = "0";

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
                int dot = dotIndex(num, 1);
                if (dot != -1)
                    num = num.substring(0, dot);
                num = Integer.toString(Integer.parseInt(num) + 1);
            }

            // если уровень вложенности > 1
            if (currentHashtagCount > 1) {

                // проверка на то что раздел является первым в блоке разделов с одинаковым уровнем вложенности
                if (dotCount(num) == currentHashtagCount - 2) {
                    num += ".1";

                } else {
                    // определяем индексы в точек между которыми расположено число для увеличения на 1
                    int currentDot = dotIndex(num, currentHashtagCount - 1);
                    int nextDot = dotIndex(num, currentHashtagCount);

                    // проверка на то что после числа нет точки
                    if (nextDot == -1)
                        nextDot = num.length();

                    // вырезаем число и прибавляем к нему 1
                    String add = Integer.toString(
                            Integer.parseInt(num.substring(currentDot + 1, nextDot)) + 1
                    );

                    // обрезаем номер раздела до всё ещё актуальной части
                    // и дописываем изменения относительно номера предыдущего раздела
                    num = num.substring(0, currentDot + 1) + add;
                }
            }

            result.add(num + " " + s.substring(currentHashtagCount));
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

    private int dotIndex(String s, int n) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.')
                n--;

            if (n == 0)
                return i;
        }
        return -1;
    }

    private int dotCount(String s) {
        int counter = 0;
        for (char c: s.toCharArray()){
            if (c == '.')
                counter++;
        }
        return counter;
    }
}