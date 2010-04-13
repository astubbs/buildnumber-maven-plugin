package org.codehaus.mojo.build;

import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitRevParseConsumer
implements StreamConsumer
{
  private ScmLogger logger;

    // private Map properties = new HashMap();

    private StringBuffer commandOutput = new StringBuffer();

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    public GitRevParseConsumer(ScmLogger logger) {
    this.logger = logger;
    }

    // ----------------------------------------------------------------------
    // StreamConsumer Implementation
    // ----------------------------------------------------------------------

    public void consumeLine(String line) {
	commandOutput.append(line);
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
