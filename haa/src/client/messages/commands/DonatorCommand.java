package client.messages.commands;

import constants.ServerConstants;

public class DonatorCommand {
  public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
    return ServerConstants.PlayerGMRank.DONATOR;
  }
}
