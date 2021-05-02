package com.member.assistance.core.service;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.dto.MemberApplicationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger LOGGER = LogManager.getLogger(FileServiceImpl.class);

    private static final String PROFILE_PHOTO_PREFIX = "_PROFILE_";
    private static final String ID_FRONT_PHOTO_PREFIX = "_ID_FRONT_";
    private static final String ID_BACK_PHOTO_PREFIX = "_ID_BACK_";
    private final static String VOUCHER_FILE_PREFIX = "_VOUCHER_";
    private final Path photo = Paths.get(System.getProperty(MemberAssistanceConstants.FS_HOME)
            .concat(MemberAssistanceConstants.FS_PHOTO));
    private final Path profiles = Paths.get(System.getProperty(MemberAssistanceConstants.FS_HOME)
            .concat(MemberAssistanceConstants.FS_PROFILES));
    private final Path oldProfiles = Paths.get(System.getProperty(MemberAssistanceConstants.FS_HOME)
            .concat(MemberAssistanceConstants.FS_OLD_PROFILES));
    private final Path ids = Paths.get(System.getProperty(MemberAssistanceConstants.FS_HOME)
            .concat(MemberAssistanceConstants.FS_IDS));
    private final Path oldIds = Paths.get(System.getProperty(MemberAssistanceConstants.FS_HOME)
            .concat(MemberAssistanceConstants.FS_OLD_IDS));
    private final Path vouchers = Paths.get(System.getProperty(MemberAssistanceConstants.FS_HOME)
            .concat(MemberAssistanceConstants.FS_VOUCHERS));
    private static final Path reports = Paths.get(System.getProperty(MemberAssistanceConstants.FS_HOME)
            .concat(MemberAssistanceConstants.FS_REPORTS));

    @Override
    public void init() {
        try {
            if (!Files.isDirectory(photo)) {
                LOGGER.info("Create directory, profiles: {}", photo);
                Files.createDirectory(photo);
            }
            if (!Files.isDirectory(profiles)) {
                LOGGER.info("Create directory, profiles: {}", profiles);
                Files.createDirectory(profiles);
            }
            if (!Files.isDirectory(ids)) {
                LOGGER.info("Create directory, ids: {}", profiles);
                Files.createDirectory(ids);
            }
            if (!Files.isDirectory(oldProfiles)) {
                LOGGER.info("Create directory, old_profiles: {}", oldProfiles);
                Files.createDirectory(oldProfiles);
            }
            if (!Files.isDirectory(oldIds)) {
                LOGGER.info("Create directory, old_ids: {}", oldIds);
                Files.createDirectory(oldIds);
            }
            if (!Files.isDirectory(vouchers)) {
                LOGGER.info("Create directory, vouchers: {}", vouchers);
                Files.createDirectory(vouchers);
            }
            if (!Files.isDirectory(reports)) {
                LOGGER.info("Create directory, reports: {}", reports);
                Files.createDirectory(reports);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Resource createUpdateProfilePhoto(String accountNumber,
                                             String profilePhotoUuid,
                                             InputStream is,
                                             Boolean isNew) throws MemberAssistanceException {
        LOGGER.info("Save profile photo w/ account# {}", accountNumber);
        try {
            //TODO: cloud storage integration
            //TODO: encrypt url to save in member account
            if (!isNew) {
                moveCurrProfilePhoto(accountNumber, profilePhotoUuid);
            }
            Path userProfilePath = Paths.get(profiles.toAbsolutePath().normalize().toString()
                    .concat("/").concat(profilePhotoUuid));
            if (!Files.isDirectory(userProfilePath)) {
                LOGGER.info("Create directory in profile photo: {}", userProfilePath.toAbsolutePath().toString());
                Files.createDirectory(userProfilePath);
            }
            File file = new File(createProfilePhotoFilename(accountNumber));
            Path resolve = userProfilePath.resolve(file.getName());
            Files.copy(is, resolve);
            return validateIfFileExists(resolve);
        } catch (RuntimeException re) {
            LOGGER.error("Runtime Exception: " + re.getMessage(), re);
            throw new MemberAssistanceException("Unable to save profile photo");
        } catch (IOException ex) {
            LOGGER.error("IO Exception: " + ex.getMessage(), ex);
            throw new MemberAssistanceException("Unable to save profile photo");
        } catch (Exception e) {
            LOGGER.error("Uncaught Exception" + e.getMessage(), e);
            throw new MemberAssistanceException("Unable to save profile photo.");
        }
    }

    @Override
    public ByteArrayResource getProfilePhoto(String accountNumber,
                                             String uuid) throws MemberAssistanceException {
        LOGGER.info("Get profile photo.");
        try {
            Path filePath = Paths.get(profiles.toAbsolutePath().toString()
                    .concat("/").concat(uuid)
                    .concat("/").concat(createProfilePhotoFilename(accountNumber))
            );
            //TODO: use on cloud or server retrieval
            //Resource resource = new UrlResource(file.toUri());
            //File f = resource.getFile();
            return new ByteArrayResource(Files.readAllBytes(filePath));
        } catch (RuntimeException re) {
            LOGGER.error("Runtime Exception: " + re.getMessage(), re);
            throw new MemberAssistanceException("Unable to get profile photo");
        } catch (IOException ex) {
            LOGGER.error("IO Exception: " + ex.getMessage(), ex);
            throw new MemberAssistanceException("Unable to get profile photo");
        } catch (Exception e) {
            LOGGER.error("Uncaught Exception" + e.getMessage(), e);
            throw new MemberAssistanceException("Unable to get profile photo.");
        }
    }

    @Override
    public ByteArrayResource getIdPhoto(String accountNumber, String idPhotoUuid, String side) throws MemberAssistanceException {
        LOGGER.info("Get id photos");
        try {
            Path filePath = null;
            if(side.equalsIgnoreCase(MemberAssistanceConstants.KEY_ID_FRONT)) {
                filePath = Paths.get(
                        new StringJoiner("/").add(ids.toAbsolutePath().toString())
                                .add(idPhotoUuid)
                                .add(createIdPhotoFilename(ID_FRONT_PHOTO_PREFIX, accountNumber)).toString()
                );
            } else if(side.equalsIgnoreCase(MemberAssistanceConstants.KEY_ID_BACK)) {
                filePath = Paths.get(
                        new StringJoiner("/").add(ids.toAbsolutePath().toString())
                                .add(idPhotoUuid)
                                .add(createIdPhotoFilename(ID_BACK_PHOTO_PREFIX, accountNumber)).toString()
                );
            }

            if(Objects.isNull(filePath)) {
                LOGGER.warn("File is missing for account#:{}, id uuid:{}, side:{}", accountNumber, idPhotoUuid, side);
                throw new MemberAssistanceException("Photo Id " + side + " side is missing.");
            }
            return new ByteArrayResource(Files.readAllBytes(filePath));
            //TODO: use on cloud or server retrieval
            //Resource resource = new UrlResource(file.toUri());
            //File f = resource.getFile();
            //mediaMap.put(MemberAssistanceConstants.KEY_ID_FRONT, new ByteArrayResource(Files.readAllBytes(frontFilePath)));
            //mediaMap.put(MemberAssistanceConstants.KEY_ID_FRONT, new ByteArrayResource(Files.readAllBytes(backFilePath)));
        } catch (RuntimeException re) {
            LOGGER.error("Runtime Exception: " + re.getMessage(), re);
            throw new MemberAssistanceException("Failed to retrieve id photo");
        } catch (IOException ex) {
            LOGGER.error("IO Exception: " + ex.getMessage(), ex);
            throw new MemberAssistanceException("Failed to get id photo");
        } catch (Exception e) {
            LOGGER.error("Uncaught Exception" + e.getMessage(), e);
            throw new MemberAssistanceException("Failed to get id photo.");
        }
    }

    @Override
    public ByteArrayResource getVoucherImage(String voucherNumber, String voucherUuid) throws MemberAssistanceException {
        LOGGER.info("Get voucher image.");
        try {
            Path filePath = Paths.get(
                        new StringJoiner("/").add(vouchers.toAbsolutePath().toString())
                                .add(voucherUuid)
                                .add(createVoucherFilename(voucherNumber)).toString()
                );
            if(Objects.isNull(filePath)) {
                LOGGER.warn("Voucher image is not available.");
                return null;
            }
            return new ByteArrayResource(Files.readAllBytes(filePath));
        } catch (RuntimeException re) {
            LOGGER.error("Runtime Exception: " + re.getMessage(), re);
            throw new MemberAssistanceException("Failed to retrieve id photo");
        } catch (IOException ex) {
            LOGGER.error("IO Exception: " + ex.getMessage(), ex);
            throw new MemberAssistanceException("Failed to get id photo");
        } catch (Exception e) {
            LOGGER.error("Uncaught Exception" + e.getMessage(), e);
            throw new MemberAssistanceException("Failed to get id photo.");
        }
    }

    @Override
    public List<Resource> createUpdateIdPhotos(String accountNumber,
                                               String idPhotoUuid,
                                               InputStream frontIs,
                                               InputStream backIs,
                                               Boolean isNew) throws MemberAssistanceException {
        LOGGER.info("Create/update id photos w/ account #: {}", accountNumber);
        try {
            File frontFile = new File(ID_FRONT_PHOTO_PREFIX.concat(accountNumber));
            File backFile = new File(ID_BACK_PHOTO_PREFIX.concat(accountNumber));
            Path idPhotoDirPath = Paths.get(ids.toAbsolutePath().normalize().toString()
                    .concat("/").concat(idPhotoUuid));
            if (!Files.isDirectory(idPhotoDirPath)) {
                LOGGER.info("Create directory in id photo: {}", idPhotoDirPath.toAbsolutePath().toString());
                Files.createDirectory(idPhotoDirPath);
            }
            //move/backup files
            if (!isNew) {
                moveCurrIdPhotos(accountNumber, idPhotoUuid);
            }
            Path frontFilePath = idPhotoDirPath.resolve(frontFile.getName());
            Files.copy(frontIs, frontFilePath);
            Path backFilePath = idPhotoDirPath.resolve(backFile.getName());
            Files.copy(backIs, backFilePath);

            List<Resource> resources = new ArrayList<>();
            resources.add(validateIfFileExists(frontFilePath));
            resources.add(validateIfFileExists(backFilePath));
            return resources;
        } catch (RuntimeException re) {
            LOGGER.error("Runtime Exception: " + re.getMessage(), re);
            throw new MemberAssistanceException("Unable to save id photos.");
        } catch (IOException ex) {
            LOGGER.error("IO Exception: " + ex.getMessage(), ex);
            throw new MemberAssistanceException("Unable to save id photos.");
        } catch (Exception e) {
            LOGGER.error("Uncaught Exception" + e.getMessage(), e);
            throw new MemberAssistanceException("Unable to save id photos.");
        }
    }

    @Override
    public Resource validateIfFileExists(Path file) {
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private void moveCurrProfilePhoto(String accountNumber, String profilePhotoUuid) {
        LOGGER.info("Move current profile photo of user acct# {}", accountNumber);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(MemberAssistanceConstants.DATE_FORMAT_APPEND_TO_FILE);
            String nowDateStr = sdf.format(new Date());

            File file = new File(createProfilePhotoFilename(accountNumber));
            Path userProfilePath = Paths.get(this.profiles.toAbsolutePath().normalize().toString()
                    .concat("/").concat(profilePhotoUuid));
            Path currFilePath = userProfilePath.resolve(file.getName());
            //move to old-profile
            Path userOldProfilePath = Paths.get(this.oldProfiles.toAbsolutePath().normalize().toString()
                    .concat("/").concat(profilePhotoUuid));
            if (!Files.isDirectory(userOldProfilePath)) {
                Files.createDirectory(userOldProfilePath);
                LOGGER.info("Created backup directory for user profile photo.");
            }
            Path moved = Files.move(currFilePath, userOldProfilePath.resolve(file.getName()));
            //rename
            Path renamed = Files.move(moved,
                    moved.resolveSibling(file.getName().concat("_").concat(nowDateStr)), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Moved profile photo to: {}", renamed.toAbsolutePath().normalize());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Could not move the file. Error: " + e.getMessage());
        }
    }

    private void moveCurrIdPhotos(String accountNumber, String idPhotoUuid) {
        LOGGER.info("Move current id photo of user acct# {}", accountNumber);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(MemberAssistanceConstants.DATE_FORMAT_APPEND_TO_FILE);
            String nowDateStr = sdf.format(new Date());

            File frontFile = new File(createIdPhotoFilename(ID_FRONT_PHOTO_PREFIX, accountNumber));
            File backFile = new File(createIdPhotoFilename(ID_BACK_PHOTO_PREFIX, accountNumber));
            Path idPhotoDirectoryPath = Paths.get(this.ids.toAbsolutePath().normalize().toString()
                    .concat("/").concat(idPhotoUuid));

            //move to old-ids
            Path oldIdDirectoryPath = Paths.get(this.oldIds.toAbsolutePath().normalize().toString()
                    .concat("/").concat(idPhotoUuid));
            if (!Files.isDirectory(oldIdDirectoryPath)) {
                Files.createDirectory(oldIdDirectoryPath);
                LOGGER.info("Created backup directory for user id photo.");
            }

            Path currFrontFile = idPhotoDirectoryPath.resolve(frontFile.getName());
            Path currBackFile = idPhotoDirectoryPath.resolve(backFile.getName());
            Path movedFront = Files.move(currFrontFile, oldIdDirectoryPath.resolve(frontFile.getName()));
            Path movedBack = Files.move(currBackFile, oldIdDirectoryPath.resolve(backFile.getName()));
            //rename
            Path renamedFront = Files.move(movedFront,
                    movedFront.resolveSibling(frontFile.getName()
                            .concat("_").concat(nowDateStr)), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Moved front id photo to: {}", renamedFront.toAbsolutePath().normalize());

            //rename
            Path renamedBack = Files.move(movedBack,
                    movedBack.resolveSibling(backFile.getName()
                            .concat("_").concat(nowDateStr)), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Moved Back id photo to: {}", renamedFront.toAbsolutePath().normalize());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Could not move the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void createVoucherFile(MemberApplicationDto memberApplicationDto, String voucherUuid, BitMatrix bitMatrix) throws MemberAssistanceException {
        String voucherNumber = memberApplicationDto.getVoucherNumber();
        LOGGER.info("Save voucher w/ voucher number: {}", voucherNumber);
        try {
            StringJoiner voucherDirJoiner = new StringJoiner("/").add(vouchers.toAbsolutePath().normalize().toString())
                    .add(voucherUuid);
            Path voucherMemberDir = Paths.get(voucherDirJoiner.toString());
            if (!Files.isDirectory(voucherMemberDir)) {
                Files.createDirectory(voucherMemberDir);
                LOGGER.info("Created voucher member file in : {}", voucherUuid);
            }
            File file = new File(createVoucherFilename(voucherNumber));
            Path voucherMemberPath = voucherMemberDir.resolve(file.getName());
            MatrixToImageWriter.writeToPath(bitMatrix, MemberAssistanceConstants.CODE_PNG, voucherMemberPath);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to save voucher.");
        }
    }

    private static String createProfilePhotoFilename(String accountNumber) {
        return PROFILE_PHOTO_PREFIX.concat(accountNumber);
    }

    private static String createVoucherFilename(String voucherNumber) {
        return VOUCHER_FILE_PREFIX.concat(voucherNumber)
                .concat(MemberAssistanceConstants.FILE_EXTENSION_PNG);
    }

    private static String createIdPhotoFilename(String prefix, String accountNumber) {
        return prefix.concat(accountNumber);
    }

    public static Path getReports() {
        return reports;
    }
}
