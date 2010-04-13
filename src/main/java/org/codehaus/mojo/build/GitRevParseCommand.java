package org.codehaus.mojo.build;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.command.info.GitInfoScmResult;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class GitRevParseCommand extends AbstractCommand implements GitCommand
{

  String rev;
  
 

  protected ScmResult executeCommand(ScmProviderRepository scmProviderRepository, ScmFileSet scmFileSet,
      CommandParameters commandParameters) throws ScmException
  {
    return executeRevParseCommand(scmProviderRepository, scmFileSet, commandParameters);
  }

  protected GitInfoScmResult executeRevParseCommand(ScmProviderRepository repo, ScmFileSet fileSet,
      CommandParameters commandParameters)
      throws ScmException
  {
    
    Commandline cl = createCommandLine((GitScmProviderRepository) repo, fileSet);
    
    cl.createArg().setValue(getRev());
    
    GitRevParseConsumer stdin = new GitRevParseConsumer(getLogger());

    CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

    int exitCode;

    exitCode = GitCommandLineUtils.execute(cl, stdin, stderr, getLogger());
    if (exitCode != 0)
    {
      // git-status returns non-zero if nothing to do
      getLogger().info("Not a git revision");
      return null;
    }

    return new GitInfoScmResult(cl.toString(), null, stdin.getCommandOutput(), true);
  }

  // ----------------------------------------------------------------------
  //
  // ----------------------------------------------------------------------

  public static Commandline createCommandLine(GitScmProviderRepository repository, ScmFileSet fileSet)
  {
    Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "rev-parse");

    return cl;
  }

  public String getRev()
  {
    return rev;
  }

  public void setRev(String rev)
  {
    this.rev = rev;
  }
}
