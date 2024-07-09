package cn.cloud9.server.tool.file;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import oshi.util.FormatUtil;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode
@Builder
public class FileViewDTO {

    private String name;
    private String path;
    private long size;
    private String sizeRead;
    private long lastModified;
    private LocalDateTime lastModified2;
    private String absolutePath;
    private String canonicalPath;
    private boolean exists;
    private boolean isDirectory;
    private boolean isFile;
    private boolean isHidden;
    private boolean isAbsolute;
    private boolean canExecute;
    private boolean canRead;
    private boolean canWrite;
    private File targetFile;

    @SneakyThrows
    public static FileViewDTO getInstance(File file) {
        if (Objects.isNull(file)) return null;
        return FileViewDTO.builder()
                .targetFile(file)
                .name(file.getName())
                .path(file.getPath())
                .size(file.length())
                .sizeRead(FormatUtil.formatBytes(file.length()))
                .lastModified(file.lastModified())
                .lastModified2(LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()))
                .absolutePath(file.getAbsolutePath())
                .canonicalPath(file.getCanonicalPath())
                .exists(file.exists())
                .isDirectory(file.isDirectory())
                .isFile(file.isFile())
                .isHidden(file.isHidden())
                .isAbsolute(file.isAbsolute())
                .canExecute(file.canExecute())
                .canRead(file.canRead())
                .canWrite(file.canWrite())
                .build();
    }

    public static FileViewDTO getInstance(String path) {
        File file = new File(path);
        return getInstance(file);
    }

    public List<FileViewDTO> list() {
        File[] listFiles = targetFile.listFiles();
        return Arrays.stream(listFiles).map(FileViewDTO::getInstance).collect(Collectors.toList());
    }
}
