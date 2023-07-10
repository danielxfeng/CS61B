package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Utils.*;
import static gitlet.Commit.Cmt;



/** Represents a gitlet repository.
 *  This file will handle all the actual git commands.
 *
 *  @author Daniel Feng
 */
public class Repository {

    /** The current working directory. */
    public static final File CWD =  new File(System.getProperty("user.dir")); // join("/Users/hf/JavaProjects/CS61B/CS61B/proj2/myTest");

    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** The directory that saves the Serialised Fields of this class. */
    public static final File OBJ_DIR = join(GITLET_DIR, "obj");

    /** The Instance of Class Blobs which manages the blobs. */
    private Blobs blobs;

    /** The Instance of Class Branches which manages the branches. */
    private Branches branches;

    /** The instance of Class Commit which manages the commits. */
    private Commit commits;

    /** The instance of Class Stage which manages the stage. */
    private Stage stage;

    /**
     * Return a Repository instance if the objects were saved in the disk.
     */
    public static Repository fromFile() {
        if (!GITLET_DIR.exists()) {
            Main.exitWithMessage("Not in an initialized Gitlet directory.");
        }
        return initField();
    }

    /**
     * Creates a new Gitlet version-control system in the current directory.
     * This method will automatically generate an init commit and a branch called master.
     * This method will save the commit and branch to the disk.
     * Call Method fromFile() to get the Gitlet Instance.
     */
    public static void init() {
        if (GITLET_DIR.exists()) { // if Gitlet dir exist, do NOT overwrite it.
            Main.exitWithMessage("A Gitlet version-control system already exists in the current directory.");
        }

        // Create the folders.
        if (!GITLET_DIR.mkdir() || !OBJ_DIR.mkdir()) {
            throw error("Error when create folders");
        }

        initField();
    }

    /** Read the Fields from disk or generate new ones. */
    private static Repository initField() {
        Repository repo = new Repository();
        try {
            repo.blobs = new Blobs();
            repo.commits = new Commit();
            repo.branches = new Branches();
            repo.stage = new Stage();
            return repo;
        } catch (IllegalArgumentException  e) {
            Main.exitWithMessage(e.getMessage());
            return null;
        }
    }

    /** Add a file which exists in the CWD to the stage. */
    public void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists() || !file.isFile()) {
            throw error("File does not exist.");
        }

        if (stage.removedStageHas(fileName)) {
            stage.removeFromRemovedStage(fileName);
        }

        byte[] fileContent = readContents(file);
        String hashCode = sha1(fileContent);

        if (blobs.addBlob(hashCode, fileContent)) {
            stage.setStage(fileName, hashCode);
            blobs.saveBlobs();
            stage.saveStage();
        }
    }

    /** Commit to the repository.
     *  1. Call Add to the files which were tracked in last commit and stage,
     *     to follow the version change, also prevent the files in the removedStage.
     *  2. Use the treeMap of stage to commit.
     *  3. Then init the stage.
     *
     *  @param message the message of commit, cannot be null.
     */
    public void commit(String message) {
        if (message.length() == 0) { // error when has NOT a commit message
            throw error("Please enter a commit message.");
        }

        TreeMap<String, String> tree = new TreeMap<>();
        boolean isChanged = setCommitTree(tree);

        if (!isChanged) { // quit if NOT changed
            Main.exitWithMessage("No changes added to the commit.");
        }

        String newHashCode = commits.newCommit(message, tree, branches.getHead());
        stage.initStage();
        branches.setCurrentHead(newHashCode);
    }

    /** Iterate the file name in last commit and the stage to build the commit tree this time.
     *  @param tree the tree should be built.
     *  @return true if the stage updated.
     */
    private boolean setCommitTree(TreeMap<String, String> tree) {
        String[] stageFiles = stage.getFilesFromStage();
        if (stageFiles == null || stageFiles.length == 0) {
            return false;
        }

        Cmt currCommit = commits.getCommit(branches.getHead());
        String[] commitFiles = Commit.getFileNames(currCommit);

        if (commitFiles != null) {
            for (String fileName: commitFiles) { // deal with the currCommit
                if (stage.removedStageHas(fileName)) { // Un-track the removed files.
                    continue;
                }

                tree.put(fileName, Commit.getHashOfFile(currCommit, fileName));
            }
        }


        for (String fileName: stageFiles) { // deal with the stage
            tree.put(fileName, stage.getHashForFileInStage(fileName));
        }

        return true;
    }

    /** Un-track a file.
     *  1. If exist in last commit, add to removedStage and delete the file.
     *  2. If exist in the stage, un-stage it.
     */
    public void rm(String fileName) {

        Cmt currCommit = commits.getCommit(branches.getHead());
        boolean inLastCommit = Commit.commitHasFile(currCommit, fileName);
        boolean inStage = stage.stageHas(fileName);

        if (!inLastCommit && !inStage) {
            throw error("No reason to remove the file.");
        }

        if (inLastCommit) {
            stage.setRemovedStage(fileName);
            restrictedDelete(join(CWD, fileName));
        }

        if (inStage) {
            String hashCode = stage.removeFromStage(fileName);
            blobs.removeBlob(hashCode);
        }
    }

    /** Print all logs of the current commit and all parents. */
    public void log() {
        log(branches.getHead());
    }

    /** Help method for recursive call. */
    private void log(String hashCode) {
        Cmt commit = commits.getCommit(hashCode);
        printHelper(commit);

        String parent = Commit.getParent(commit);
        if (parent == null) {
            return;
        }
        log(parent);
    }

    /** Print all logs of all commits. */
    public void globalLog() {
        for (Cmt commit : commits.getAllCommits()) {
            printHelper(commit);
        }
    }

    /** Print the Fields of a commit. */
    private void printHelper(Cmt commit) {
        System.out.println("===");
        System.out.println("commit " + Commit.getHash(commit));
        System.out.println("Date: " + Commit.getDateTime(commit));
        System.out.println(Commit.getMessage(commit));
        System.out.println();
    }

    /** Print the Hash Value of a commit by the message. */
    public void find(String message) {
        Cmt[] cmts = commits.getCommit(true, message);

        if (cmts.length == 0) {
            System.out.println("Found no commit with that message.");
            return;
        }

        for (Cmt commit : cmts) {
            System.out.println(Commit.getMessage(commit));
        }

    }

    /** Print Fields: Branches, Stage, Removed Stage.
     * As well as modified but not staged and untracked files.
     */
    public void status() {

        String[] branchNames = branches.getBranches();
        String currBranch = branches.getCurrBranch();
        for (int i = 0; i < branchNames.length; i++) {
            if (branchNames[i].equals(currBranch)) {
                branchNames[i] = "*" + branchNames[i];
            }
        }
        statusPrintHelper("Branches", branchNames);

        String[] stageFiles = stage.getFilesFromStage();
        statusPrintHelper("Staged Files", stageFiles);

        String[] removedStageFiles = stage.getFilesFromRemovedStage();
        statusPrintHelper("Removed Files", removedStageFiles);

        String[] modifiedFiles = getModifiedFiles();
        statusPrintHelper("Modifications Not Staged For Commit", modifiedFiles);

        String[] unTrackedFiles = getUnTrackedFiles();
        statusPrintHelper("Untracked Files", modifiedFiles);

    }

    /** A helper method for Method Status to find out the un-tracked files. */
    private String[] getUnTrackedFiles() {
        ArrayList<String> res = new ArrayList<>();

        List<String> cwdFiles = plainFilenamesIn(CWD);

        if (cwdFiles == null) {
            return null;
        }

        Cmt currCommit = commits.getCommit(branches.getHead());
        List<String> currFiles = Arrays.asList(Commit.getFileNames(currCommit));
        List<String> stageFiles = Arrays.asList(stage.getFilesFromStage());
        List<String> removedFiles = Arrays.asList(stage.getFilesFromRemovedStage());

        for (String fileName : cwdFiles) {
            if ((!currFiles.contains(fileName)
                    && !stageFiles.contains(fileName))
                    || removedFiles.contains(fileName)) {
                res.add(fileName);
            }
        }

        return res.toArray(new String[0]);
    }

    /** A helper method for Method Status to find out the modified files. */
    private String[] getModifiedFiles() {
        ArrayList<String> res = new ArrayList<>();

        Cmt currCommit = commits.getCommit(branches.getHead());
        String[] currFiles = Commit.getFileNames(currCommit);
        for (String fileName : currFiles) {
            File cwdFile = join(CWD, fileName);
            // Not staged for removal, but tracked in the current commit
            // and deleted from the working directory.
            if (!cwdFile.exists()) {
                if (!stage.removedStageHas(fileName)) {
                    res.add(fileName + " (deleted)");
                }
                // Tracked in the current commit,
                // changed in the working directory, but not staged;
            } else if (!stage.stageHas(fileName)
                    && !Commit.getHashOfFile(currCommit, fileName).equals(
                            sha1(readContents(join(CWD, fileName))))) {
                res.add(fileName + " (modified)");
            }
        }

        String[] stageFiles = stage.getFilesFromStage();
        for (String fileName : stageFiles) {
            File cwdFile = join(CWD, fileName);
            // Staged for addition, but deleted in the working directory;
            if (!cwdFile.exists()) {
                res.add(fileName + " (deleted)");
                // Staged for addition, but with different contents than in the working directory;
            } else if (!stage.getHashForFileInStage(fileName).equals(sha1(readContents(cwdFile)))) {
                res.add(fileName + " (modified)");
            }
        }
        return res.toArray(new String[0]);
    }

    /** A helper method for Method Status to deal with the format problem. */
    private void statusPrintHelper(String title, String[] arr) {
        System.out.println("=== " + title + " ===");
        for (String str : arr) {
            System.out.println(str);
        }
        System.out.println();
    }

    /** A helper method for map to the actual Checkout Method */
    public void checkout(String[] args) {
        switch (args.length) {
            case 2 -> checkout(true, args[1]);
            case 3 -> checkout(args[2]);
            case 4 -> checkout(true, args[1], args[3]);
            default -> throw new IllegalStateException("Unexpected value: " + args.length);
        }
    }


    /** An Actual Checkout Method by a commit and a file.
     *  Take a FILE from THE commit to overwrite the version of the work dir.
     */
    public void checkout(boolean isCommit, String commitId, String fileName) {
        if (!commits.hasCommit(commitId)) {
            throw error("No commit with that id exists.");
        }
        Cmt commit = commits.getCommit(commitId);
        if (!Commit.commitHasFile(commit, fileName)) {
            throw error("File does not exist in that commit.");
        }

        File cwdFile = join(CWD, fileName);
        String commitHashCode = Commit.getHashOfFile(commit, fileName);

        if (!cwdFile.exists() || !commitHashCode.equals(sha1(readContents(cwdFile)))) {
            byte[] content = blobs.getBlob(commitHashCode);
            writeContents(cwdFile, (Object) content);
        }
     }

    /** An Actual Checkout Method by a file.
     *  Take the file from LAST commit to overwrite the version of the work dir.
     */
    public void checkout(String fileName) {
        checkout(true, branches.getHead(), fileName);
    }

    /** An Actual Checkout Method by a branch. */
    public void checkout(boolean isBranch, String branchName) {
        if (!branches.hasBranch(branchName)) {
            throw error("No such branch exists.");
        }
        if (branches.getCurrBranch().equals(branchName)) {
            throw error("No need to checkout the current branch.");
        }
        String[] unTrackedFiles = getUnTrackedFiles();
        if (unTrackedFiles != null && unTrackedFiles.length > 0) {
            throw error("There is an untracked file in the way; delete it, or add and commit it first.");
        }

        // checkout the files in the last commit;
        String branchPoint = branches.getBranchPoint(branchName);
        List<String> files = Arrays.asList(Commit.getFileNames(commits.getCommit(branchPoint)));

        for (String file : files) {
            checkout(true, branchPoint, file);
        }

        // delete the files in working dir but not be tracked.
        List<String> cwdFiles = plainFilenamesIn(CWD);
        for (String cwdFile : cwdFiles) {
            if (!files.contains(cwdFile)) {
                restrictedDelete(join(CWD, cwdFile));
            }
        }

        // set the point
        branches.setCurrBranch(branchName);

    }

    /** Add a new branch. */
    public void branch(String branchName) {
        if (branches.hasBranch(branchName)) {
            throw error("A branch with that name already exists.");
        }
        branches.setBranches(branchName, branches.getHead());
    }

    /** Rm a branch. */
    public void rmBranch(String branchName) {
        if (!branches.hasBranch(branchName)) {
            throw error("A branch with that name does not exist.");
        }
        if (branches.getCurrBranch().equals(branchName)) {
            throw error("Cannot remove the current branch.");
        }
        branches.removeBranch(branchName);
    }

    public void reset(String arg) {
        //TODO
    }

    public void merge(String arg) {
        //TODO
    }

    // for debug
    public Commit getCommits() {
        return commits;
    }

    public Stage getStage() {
        return stage;
    };
}
