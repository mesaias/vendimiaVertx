package io.vertx.vendimia.servicesProvider;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.vendimia.implementation.UserInterfaceImplementation;
import io.vertx.vendimia.serviceInterface.UserInterface;

public final class UserServiceProvider {

	private UserInterface userInterface;

	/* Singleton */
	private static UserServiceProvider instance = new UserServiceProvider();

	public synchronized UserInterface init(Vertx vertx, JsonObject config) {
    setUserInterface(new UserInterfaceImplementation( vertx, config ) );
		return getUserInterface();
	}

	public static UserServiceProvider getInstance() {
		return instance;
	}

	private UserServiceProvider() {
	}

	public UserInterface getUserInterface() {
		return userInterface;
	}

	private void setUserInterface( UserInterface UserInterface ) {
		this.userInterface = UserInterface;
	}

}
