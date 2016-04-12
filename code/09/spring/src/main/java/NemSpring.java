package rb2nem.nemws;


import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

import org.springframework.web.socket.client.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import rb2nem.nemws.Handler;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;
import javax.script.ScriptException;




/**
 *
 * @author rb2
 */
@SpringBootApplication
public class NemSpring {

	private static final String WS_URI = "ws://bob.nem.ninja:7777/w/messages";
    
	public static void main(String[] args) throws Throwable {

		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("javascript");

		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
		WebSocketClient transport = new SockJsClient(transports);


		WebSocketStompClient stompClient = new WebSocketStompClient(transport);
		stompClient.setMessageConverter(new StringMessageConverter());
		StompSessionHandler handler = new Handler() ;
		ListenableFuture<StompSession> session_f = stompClient.connect(WS_URI, handler);
		StompSession session = session_f.get();
		session.send("/w/api/node/info","");
		session.subscribe("/blocks/new", new StompFrameHandler() {
				@Override
				public Type getPayloadType(StompHeaders headers) {
				return String.class;
				}

				@Override
				public void handleFrame(StompHeaders headers, Object payload) {
					System.out.println("NEW BLOCK \n\n");
					System.out.println(payload);
					String script = "Java.asJSONCompatible(" + payload + ")";
					try {
						Object result = engine.eval(script);
						Map contents = (Map) result;
						contents.forEach((t, u) -> {
								System.out.println( t + " -> " + u);
								});
					}
					catch (Exception e){
					}

				}

				});
		//Wait 10 minutes
		Thread.sleep(1000*60*10);
		System.out.println("end");
	}
}
