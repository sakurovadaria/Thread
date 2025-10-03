package com.Thread.service;

import com.Thread.exception.AvatarNotFoundException;
import com.Thread.exception.StudentNotFoundException;
import com.Thread.model.Avatar;
import com.Thread.model.Student;
import com.Thread.repository.AvatarRepository;
import com.Thread.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AvatarServiceImpl implements AvatarService {


    @Autowired
    private final AvatarRepository avatarRepository;

    private final StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarServiceImpl(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    @Transactional
    @Override
    public void uploadAvatar(Long id, MultipartFile file) throws IOException {

        logger.debug("Was invoked method uploadAvatar for studentId = {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cannot upload avatar. Student with id = {} not found", id);

                    return new StudentNotFoundException("Student not found");
                });

        try {
            String extension = getExtension(file.getOriginalFilename());
            String fileName = id + "_avatar." + extension;
            Path filePath = Path.of(avatarsDir, fileName);

            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            file.transferTo(filePath);

            Avatar avatar = avatarRepository.findByStudentId(id)
                    .orElseGet(Avatar::new);

            avatar.setStudent(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(file.getSize());
            avatar.setMediaType(file.getContentType());

            if (file.getContentType().startsWith("image/")) {
                avatar.setData(generatePreview(filePath));
            } else {
                avatar.setData(Files.readAllBytes(filePath));
            }

            avatarRepository.save(avatar);

            logger.info("Avatar successfully uploaded for studentId = {}", id);

        } catch (IOException e) {
            logger.error("Failed to upload avatar for studentId = {}: {}", id, e.getMessage());

            throw e;
        }
    }



    private byte[] generatePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath)) {
            BufferedImage image = ImageIO.read(is);

            // Проверяем, что изображение успешно загружено
            if (image == null) {
                throw new IOException("Failed to read image from file: " + filePath);
            }

            // Генерация превью (уменьшение размера)
            int previewWidth = 100;
            int previewHeight = (int) ((double) image.getHeight() / image.getWidth() * previewWidth);
            BufferedImage preview = new BufferedImage(previewWidth, previewHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = preview.createGraphics();
            g2d.drawImage(image, 0, 0, previewWidth, previewHeight, null);
            g2d.dispose();

            // Сохранение превью в байтовый массив
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }


    private String getExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex > 0) {
            return fileName.substring(extensionIndex + 1);
        }
        return "";
    }

    @Override
    public Avatar findAvatar(Long studentId) throws AvatarNotFoundException {logger.debug("Was invoked method findAvatar for studentId = {}", studentId);
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Avatar not found for studentId = {}", studentId);

                    return new AvatarNotFoundException("Avatar not found for student ID: " + studentId);
                });
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @Override
    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.debug("Was invoked method getAllAvatars with page = {} and size = {}", page, size);

        return avatarRepository.findAll(PageRequest.of(page, size));
    }
}
