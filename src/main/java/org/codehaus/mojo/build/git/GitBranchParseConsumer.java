package org.codehaus.mojo.build.git;

import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * @see GitBranchParseCommand
 * @author Antony Stubbs
 */
public class GitBranchParseConsumer implements StreamConsumer {
  private ScmLogger logger;

  private StringBuffer commandOutput = new StringBuffer();

  public GitBranchParseConsumer(ScmLogger logger) {
    this.logger = logger;
  }

  public void consumeLine(String line) {
    commandOutput.append(line + "\n");
  }

  /**
   * The raw output from the command.
   * 
   * @return the raw output from the command
   */
  public String getCommandOutput() {
    return commandOutput.toString();
  }

}
