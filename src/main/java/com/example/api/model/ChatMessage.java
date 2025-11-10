package com.example.api.model;

import jakarta.validation.constraints.NotBlank;

public class ChatMessage {
	public enum Sender { USER, BOT }
	  public Sender sender;
	  @NotBlank public String text;
	  public String contextId;
}
