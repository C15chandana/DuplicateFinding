import java.io.*;
import java.security.*;
import java.util.*;

public class DuplicateFinder {

    private static String getFileHash(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        fis.close();

        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static List<File> getAllFiles(File dir) {
        List<File> fileList = new ArrayList<>();
        File[] files = dir.listFiles();

        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    fileList.addAll(getAllFiles(f));
                } else {
                    fileList.add(f);
                }
            }
        }
        return fileList;
    }

    public static void findDuplicates(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Invalid directory!");
            return;
        }

        HashMap<String, List<File>> hashMap = new HashMap<>();

        try {
            List<File> files = getAllFiles(dir);
            for (File file : files) {
                String hash = getFileHash(file);
                hashMap.computeIfAbsent(hash, k -> new ArrayList<>()).add(file);
            }

            boolean found = false;
            for (Map.Entry<String, List<File>> entry : hashMap.entrySet()) {
                List<File> duplicates = entry.getValue();
                if (duplicates.size() > 1) {
                    found = true;
                    System.out.println("\nDuplicate files:");
                    for (File f : duplicates)
                        System.out.println(" - " + f.getAbsolutePath());
                }
            }

            if (!found) System.out.println("No duplicate files found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter directory path to scan: ");
        String path = sc.nextLine();
        findDuplicates(path);
    }
}
