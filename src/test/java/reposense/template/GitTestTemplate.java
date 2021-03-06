package reposense.template;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import reposense.authorship.FileInfoAnalyzer;
import reposense.authorship.FileInfoExtractor;
import reposense.authorship.model.FileInfo;
import reposense.authorship.model.FileResult;
import reposense.authorship.model.LineInfo;
import reposense.git.GitCheckout;
import reposense.git.GitClone;
import reposense.model.Author;
import reposense.model.CommitHash;
import reposense.model.FileTypeTest;
import reposense.model.RepoConfiguration;
import reposense.model.RepoLocation;
import reposense.util.FileUtil;

/**
 * Contains templates for git testing.
 */
public class GitTestTemplate {
    protected static final String TEST_REPO_GIT_LOCATION = "https://github.com/reposense/testrepo-Alpha.git";
    protected static final String DISK_REPO_DISPLAY_NAME = "testrepo-Alpha_master";
    protected static final String FIRST_COMMIT_HASH = "7d7584f";
    protected static final String TEST_COMMIT_HASH = "2fb6b9b";
    protected static final String MAIN_AUTHOR_NAME = "harryggg";
    protected static final String FAKE_AUTHOR_NAME = "fakeAuthor";
    protected static final String EUGENE_AUTHOR_NAME = "eugenepeh";
    protected static final String YONG_AUTHOR_NAME = "Yong Hao TENG";
    protected static final String MINGYI_AUTHOR_NAME = "myteo";
    protected static final String JAMES_AUTHOR_NAME = "jamessspanggg";
    protected static final String JAMES_ALTERNATIVE_AUTHOR_NAME = "James Pang";
    protected static final String JINYAO_AUTHOR_NAME = "jylee-git";
    protected static final String LATEST_COMMIT_HASH = "136c6713fc00cfe79a1598e8ce83c6ef3b878660";
    protected static final String EMPTY_TREE_HASH = "4b825dc642cb6eb9a060e54bf8d69288fbee4904";
    protected static final String EUGENE_AUTHOR_README_FILE_COMMIT_07052018_STRING =
            "2d87a431fcbb8f73a731b6df0fcbee962c85c250";
    protected static final CommitHash EUGENE_AUTHOR_README_FILE_COMMIT_07052018 =
            new CommitHash(EUGENE_AUTHOR_README_FILE_COMMIT_07052018_STRING);
    protected static final String FAKE_AUTHOR_BLAME_TEST_FILE_COMMIT_08022018_STRING =
            "768015345e70f06add2a8b7d1f901dc07bf70582";
    protected static final CommitHash FAKE_AUTHOR_BLAME_TEST_FILE_COMMIT_08022018 =
            new CommitHash(FAKE_AUTHOR_BLAME_TEST_FILE_COMMIT_08022018_STRING);
    protected static final String MAIN_AUTHOR_BLAME_TEST_FILE_COMMIT_06022018_STRING =
            "8d0ac2ee20f04dce8df0591caed460bffacb65a4";
    protected static final CommitHash MAIN_AUTHOR_BLAME_TEST_FILE_COMMIT_06022018 =
            new CommitHash(MAIN_AUTHOR_BLAME_TEST_FILE_COMMIT_06022018_STRING);
    protected static final String FAKE_AUTHOR_BLAME_RANGED_COMMIT_ONE_06022018_STRING =
            "7d7584fc204922cc5ff3bd5ca073cad6bed2c46a";
    protected static final String FAKE_AUTHOR_BLAME_RANGED_COMMIT_TWO_06022018_STRING =
            "8d0ac2ee20f04dce8df0591caed460bffacb65a4";
    protected static final String FAKE_AUTHOR_BLAME_RANGED_COMMIT_THREE_07022018_STRING =
            "8e4ca1da5d413e9ab84a1e8d1474918afa97f7a1";
    protected static final String FAKE_AUTHOR_BLAME_RANGED_COMMIT_FOUR_08022018_STRING =
            "768015345e70f06add2a8b7d1f901dc07bf70582";
    protected static final List<CommitHash> FAKE_AUTHOR_BLAME_RANGED_COMMIT_LIST_09022018 = Arrays.asList(
            new CommitHash(FAKE_AUTHOR_BLAME_RANGED_COMMIT_ONE_06022018_STRING),
            new CommitHash(FAKE_AUTHOR_BLAME_RANGED_COMMIT_TWO_06022018_STRING),
            new CommitHash(FAKE_AUTHOR_BLAME_RANGED_COMMIT_THREE_07022018_STRING),
            new CommitHash(FAKE_AUTHOR_BLAME_RANGED_COMMIT_FOUR_08022018_STRING));
    protected static final String NONEXISTENT_COMMIT_HASH = "nonExistentCommitHash";
    protected static final String TIME_ZONE_ID_STRING = "Asia/Singapore";


    protected static RepoConfiguration config;

    @Before
    public void before() throws Exception {
        config = new RepoConfiguration(new RepoLocation(TEST_REPO_GIT_LOCATION), "master");
        config.setAuthorList(Collections.singletonList(getAlphaAllAliasAuthor()));
        config.setFormats(FileTypeTest.DEFAULT_TEST_FORMATS);
        config.setIsLastModifiedDateIncluded(false);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        deleteRepos();
        config = new RepoConfiguration(new RepoLocation(TEST_REPO_GIT_LOCATION), "master");
        config.setZoneId(TIME_ZONE_ID_STRING);
        GitClone.clone(config);
    }

    @AfterClass
    public static void afterClass() throws IOException {
        deleteRepos();
    }

    @After
    public void after() {
        GitCheckout.checkout(config.getRepoRoot(), "master");
    }

    private static void deleteRepos() throws IOException {
        FileUtil.deleteDirectory(FileUtil.REPOS_ADDRESS);
    }

    /**
     * Generates the information for test file.
     */
    public FileInfo generateTestFileInfo(String relativePath) {
        FileInfo fileInfo = FileInfoExtractor.generateFileInfo(config.getRepoRoot(), relativePath);

        config.getAuthorDetailsToAuthorMap().put(MAIN_AUTHOR_NAME, new Author(MAIN_AUTHOR_NAME));
        config.getAuthorDetailsToAuthorMap().put(FAKE_AUTHOR_NAME, new Author(FAKE_AUTHOR_NAME));

        return fileInfo;
    }

    public FileResult getFileResult(String relativePath) {
        FileInfo fileinfo = generateTestFileInfo(relativePath);
        return FileInfoAnalyzer.analyzeFile(config, fileinfo);
    }

    /**
     * Asserts the correctness of file analysis with regards to the contribution
     * made by author named in {@code FAKE_AUTHOR_NAME}.
     */
    public void assertFileAnalysisCorrectness(FileResult fileResult) {
        for (LineInfo line : fileResult.getLines()) {
            if (line.getContent().startsWith("fake")) {
                Assert.assertEquals(line.getAuthor(), new Author(FAKE_AUTHOR_NAME));
            } else {
                Assert.assertNotEquals(line.getAuthor(), new Author(FAKE_AUTHOR_NAME));
            }
        }
    }

    /**
     * Returns a {@code Author} that has git id and aliases of all authors in testrepo-Alpha, so that no commits
     * will be filtered out in the `git log` command.
     */
    protected Author getAlphaAllAliasAuthor() {
        Author author = new Author(MAIN_AUTHOR_NAME);
        author.setAuthorAliases(Arrays.asList(FAKE_AUTHOR_NAME, EUGENE_AUTHOR_NAME, YONG_AUTHOR_NAME));
        return author;
    }
}
