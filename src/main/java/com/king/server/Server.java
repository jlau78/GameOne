package com.king.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @deprecated Abandoned creating a server to handle incoming requests
 */
public class Server {

	public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	public static final String PROP_CLASSPATH = "com.king.config.Props.properties";

	private static ServerSocket singleServer;

	private int port = 8879;

	public Server() {
		start();
	}

	public static synchronized ServerSocket getInstance(final int port) throws IOException {

		if (singleServer == null) {
			singleServer = new ServerSocket(port);
		} else {
			LOGGER.log(Level.INFO, "Server already started.");
		}

		return singleServer;
	}

	/**
	 * Start the server
	 */
	public void start() {

		try {
			singleServer = Server.getInstance(getPort());

			while (true) {

				listen();
			}

		} catch (IOException e) {

			LOGGER.log(Level.SEVERE, "Failed to start server on port " + String.valueOf(getPort()));
		}
	}

	/**
	 * Stop the server
	 */
	public void stop() {

		if (singleServer != null) {
			try {
				singleServer.close();
				singleServer = null;

				LOGGER.log(Level.INFO, "Stopping the server....");
			} catch (IOException e) {

				LOGGER.log(Level.INFO,
						"Closing server. Expect any accept blocked during this close will throw a SocketException.", e);
			}
		}

	}

	protected void listen() throws IOException {
		if (singleServer != null) {
			final Socket socket = singleServer.accept();

			handleInput(socket);
			// listenNoThread(socket);

		}
	}

	private void handleInput(final Socket socket) {

		List<Callable> callables = new ArrayList<>();

		ExecutorService executor = Executors.newCachedThreadPool();

		Callable callable = new Callable<String>() {
			@Override
			public String call() throws Exception {

				String response = "hello client";
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String data = null;
				while ((data = in.readLine()) != null) {
					System.out.println("\r\nMessage: " + data);
				}

				return response;
			}

		};

		Future<String> future = executor.submit(callable);

		try {
			String s = future.get();

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			// write object to Socket
			oos.writeObject("Hi Client " + s);
			// close resources
			oos.close();

		} catch (InterruptedException | ExecutionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void listenNoThread(final InputStream input) throws IOException {
		String response = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String data = null;
		while ((data = in.readLine()) != null) {
			System.out.println(data);
		}
	}

	public ServerSocket getServer() {
		return singleServer;
	}

	public int getPort() {
		return this.port;
	}

	public static void main(String[] args) {

		Server server = new Server();

	}
}
