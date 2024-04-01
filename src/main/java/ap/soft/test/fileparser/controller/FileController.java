package ap.soft.test.fileparser.controller;

import ap.soft.test.fileparser.service.ParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    private ParseService parseService;

    @PostMapping("/parseFile")
    public ResponseEntity<?> parseFile(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(parseService.parseByFileId(parseService.saveFile(file)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/saveFile")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = parseService.saveFile(file);
            return ResponseEntity.ok(fileId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/parseFileById")
    public ResponseEntity<?> parseFileById(@RequestParam("fileId") String fileId) {
        try {
            return ResponseEntity.ok(parseService.parseByFileId(fileId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
