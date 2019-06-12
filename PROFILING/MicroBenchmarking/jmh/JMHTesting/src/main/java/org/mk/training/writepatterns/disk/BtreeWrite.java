/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.writepatterns.disk;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.directory.mavibot.btree.BTree;
import org.apache.directory.mavibot.btree.BTreeFactory;
import org.apache.directory.mavibot.btree.BTreeTypeEnum;
import org.apache.directory.mavibot.btree.PersistedBTreeConfiguration;
import org.apache.directory.mavibot.btree.RecordManager;
import org.apache.directory.mavibot.btree.serializer.IntSerializer;
import org.mk.training.jmhtests.JMHSample_01_HelloWorld;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.directory.mavibot.btree.TupleCursor;
import org.apache.directory.mavibot.btree.exception.KeyNotFoundException;
import org.apache.directory.mavibot.btree.serializer.LongSerializer;

/**
 *
 * @author mohit
 */
public class BtreeWrite {

    private static String BASE_PATH = "/tmp/btree/";
    private static String DATA_DIR_NAME = "db.mine";
    private BTree<Long, Long> btree = null;

    private final Set<PosixFilePermission> fa755;
    private RecordManager recordManager = null;

    private File dataDir = null;

    public BtreeWrite() {
        fa755 = new HashSet<>();
        fa755.addAll(Arrays.asList(PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_WRITE,
                PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE));

        if (!Files.exists(Paths.get(BASE_PATH, DATA_DIR_NAME), LinkOption.NOFOLLOW_LINKS)) {
            try {
                System.out.println("Not exist");
                //Path created=Files.createDirectories(Paths.get(BASE_PATH).getParent(),PosixFilePermissions.asFileAttribute(fa755));
                Path created = Files.createDirectories(Paths.get(BASE_PATH));
                System.out.println("created:" + created);
                dataDir = Files.createFile(Paths.get(BASE_PATH, DATA_DIR_NAME)).toFile();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            dataDir = Paths.get(BASE_PATH, DATA_DIR_NAME).toFile();
        }
        System.out.println("dataDir.getAbsolutePath():" + dataDir.getAbsolutePath());
        // Now, try to reload the file back
        recordManager = new RecordManager(dataDir.getAbsolutePath());
        try {
            if ((btree = recordManager.getManagedTree("sub-btree")) == null) {
            btree = recordManager.addBTree("sub-btree", LongSerializer.INSTANCE, LongSerializer.INSTANCE, false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
    /*BtreeWrite btree = new BtreeWrite();
        try {
            
            ThreadLocalRandom tlr = ThreadLocalRandom.current();
            try {
                for (long i = 0L; i < 10000L; i++) {
                    Long key = (long) tlr.nextLong();
             //String value = Long.toString(key);

                    btree.btree.insert(key, key);
                }
                //btree.btree.insert(Integer.SIZE, Integer.SIZE);
            } catch (IOException ex) {
                Logger.getLogger(BtreeWrite.class.getName()).log(Level.SEVERE, null, ex);

            }

            TupleCursor<Long, Long> cursor = btree.btree.browse();
            System.out.println("" + cursor.hasNext());
            System.out.println("" + cursor.next());
        } catch (IOException ex) {
            Logger.getLogger(BtreeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyNotFoundException ex) {
            Logger.getLogger(BtreeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                Thread.sleep(Integer.MAX_VALUE);
                btree.recordManager.close();
            } catch (Exception ex) {
                Logger.getLogger(BtreeWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
        File f =new File("/tmp/mine","plcr.mine");
        System.out.println(""+f.getAbsolutePath());
        System.out.println(""+f.exists());
    }

}
