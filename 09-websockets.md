# NIS websockets API

## Code
Until now we have actively monitored the Nem blockchain by sending request to a NIS.
Would it be more efficient to be notified by NIS when eg a new block is generated?
Of course it would! And NIS provides the necessary websocket API to achieve this.

As [explained by Wikipedia](https://en.wikipedia.org/wiki/WebSocket),
 "WebSocket is a protocol providing full-duplex communication channels over a single TCP connection".
This means that once the connection is established, the server can notify the client.

In this chapter, we will take a look at how we can use the websocket API from a Java program.
Most example you find online are with javascript code, but we want to continue to use the 
Java classes provided by Nem.core, hence the Java requirement.

The websocket API is provided by a NIS on its port 7777 at URL `/w/messages`.
This is where we will need to connect.

As for the encoding, we need to use [STOMP](http://jmesnil.net/stomp-websocket/doc/),
"a simple text-orientated messaging protocol".

Our code will use the spring framework's websocket client capabilities.
It provides a `WebSocketStompClient` that needs to be initialised with a list of transports
which we construct with 

```
List<Transport> transports = new ArrayList<>(1);
transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
WebSocketClient transport = new SockJsClient(transports);
```

We can then initialise the websocket client:
```
WebSocketStompClient stompClient = new WebSocketStompClient(transport);
```
STOMP being a text-oriented protocol, we need to use a string message converter:

```
stompClient.setMessageConverter(new StringMessageConverter());
```

At this time the stom client can connect and create a session:


```
		StompSessionHandler handler = new Handler() ;
    String WS_URI = "ws://bob.nem.ninja:7777/w/messages";
		ListenableFuture<StompSession> session_f = stompClient.connect(WS_URI, handler);
		StompSession session = session_f.get();
```

The session can be used to subscribe to events, passing 2 arguments:
  * the URI to subscribe to
  * a StompFrameHandler instance, in which we override the methods `getPayloadType` and `handleFrame`.

`getPayloadType` just returns the type of the payload, which is a string in our case.
As for `handleFrame`, it receives as arguments the headers and the payload converted to the type indicated by
`getPayloadType`.
In our case the payload is a JSON-formatted string. Using Java 8, we can use the provided Javascript engine Nashorn
to transform the JSON object to a Java `Map`. 
Here is how to do it. First initialise the script engine manager and get the javascript engine.
Then use the Nashorn-specific Java bridge's method `Java.asJSONCompatible()` which converts its argument to 
a java object. Finally, cast this to a Map:


		ScriptEngineManager sem = new ScriptEngineManager();
    ScriptEngine engine = sem.getEngineByName("javascript");
    String script = "Java.asJSONCompatible(" + payload + ")";
    Object result = engine.eval(script);
    Map contents = (Map) result;

For this example, let's print all key-value pairs from the JSON to 
standard output. Here is the code of our `subscribe` call:

```
session.subscribe("/blocks/new", new StompFrameHandler() {
				@Override
				public Type getPayloadType(StompHeaders headers) {
          return String.class;
				}

				@Override
				public void handleFrame(StompHeaders headers, Object payload) {
					System.out.println("NEW BLOCK \n\n");
          // JSON to Java
					String script = "Java.asJSONCompatible(" + payload + ")";
					try {
						Object result = engine.eval(script);
						Map contents = (Map) result;
            // Iterating over each key-value pair
						contents.forEach((t, u) -> {
								System.out.println( t + " -> " + u);
								});
					}
					catch (Exception e){
					}

				}

				});

```


If that is all you have in you have in your program, it will happily exit.
*FIXME*
What I currently do a make the main thread sleep the time that I want to get the notifications: `Thread.sleep(1000*60*10);`.
This is clearly not the correct way to do it, but I haven't found the correct way yet.

## Running the code

The code is available in the code/09/spring directory of [this repository](https://github.com/rb2nem/nem_programming).
It is buildable and runnable with gradle. Go in the directory containing the file `build.gradle` and type `gradle build` to build it, and `gradle run` to run it.
You need Nem.core available.

## Links


[WebsocketStompClient](http://docs.spring.io/spring/docs/4.2.4.RELEASE/javadoc-api/org/springframework/web/socket/messaging/WebSocketStompClient.html)
[StompSessionHandler](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/simp/stomp/StompSessionHandler.html)
[StompSession](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/simp/stomp/StompSession.html)
[StompFrameHandler](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/simp/stomp/StompFrameHandler.html)
