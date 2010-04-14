package org.codehaus.mojo.build.git;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.log.ScmLogDispatcher;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.command.info.GitInfoScmResult;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Reads the current branch name.
 * 
 * @author Antony Stubbs
 */
public class GitBranchParseCommand extends AbstractCommand implements
    GitCommand {

  ScmLogDispatcher logger = new ScmLogDispatcher();
  
  String branch;

  protected ScmResult executeCommand(
      ScmProviderRepository scmProviderRepository, ScmFileSet scmFileSet,
      CommandParameters commandParameters) throws ScmException {
    return executeRevParseCommand(scmProviderRepository, scmFileSet,
        commandParameters);
  }

  protected GitInfoScmResult executeRevParseCommand(ScmProviderRepository repo,
      ScmFileSet fileSet, CommandParameters commandParameters)
      throws ScmException {

    Commandline cl = createCommandLine((GitScmProviderRepository) repo, fileSet);

    GitBranchParseConsumer stdin = new GitBranchParseConsumer(getLogger());

    CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

    int exitCode = 0;

    try {
      exitCode = GitCommandLineUtils.execute(cl, stdin, stderr, getLogger());
    } catch (Throwable t) {
      logger.error("Error running SCM command", t);
      t.printStackTrace(); // The logger doesn't seem to show us the stack trace
      throw new RuntimeException(t);
    }
    if (exitCode != 0) {
      // git-status returns non-zero if nothing to do
      String error = stderr.getOutput();
      getLogger().error("Error find git branch name. Output: " + error);     
      return null;
    }

    return new GitInfoScmResult(cl.toString(), null, stdin.getCommandOutput(),
        true);
  }

  public static Commandline createCommandLine(
      GitScmProviderRepository repository, ScmFileSet fileSet) {
    Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet
        .getBasedir(), "branch");

    return cl;
  }

}