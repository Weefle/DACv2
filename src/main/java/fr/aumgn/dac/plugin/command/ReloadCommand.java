package fr.aumgn.dac.plugin.command;

import fr.aumgn.dac.api.DAC;
import fr.aumgn.dac.api.config.DACMessage;
import fr.aumgn.utils.command.BasicCommandExecutor;

public class ReloadCommand extends BasicCommandExecutor {

	@Override
	public boolean checkUsage(String[] args) {
		return args.length == 0;
	}

	@Override
	public void onCommand(Context context, String[] args) {
		if (args.length > 0) {
			usageError();
		}

		DAC.reloadConfig();
		DAC.reloadMessages();
		context.success(DACMessage.CmdReloadSuccess);
	}

}