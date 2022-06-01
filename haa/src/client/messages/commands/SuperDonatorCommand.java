package client.messages.commands;

import constants.ServerConstants;

public class SuperDonatorCommand {
  public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
    return ServerConstants.PlayerGMRank.SUPERDONATOR;
  }
}
