package com.eyunda.third.chat.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.chat.mina.codec.TextLineCodecFactory;
import com.eyunda.third.chat.utils.LogUtil;

public class MinaClientConn {

	protected static final String LOGTAG = LogUtil.makeLogTag(MinaClientConn.class);

	private static MinaClientConn instance = null;

	private SocketConnector connector = null;
	private IoSession session = null;
	private boolean isConned = false;

	private MinaClientConn() {
	}

	public boolean isConned() {
		return isConned;
	}

	public void setConned(boolean isConned) {
		this.isConned = isConned;
	}

	public static MinaClientConn getInstance() {
		if (instance == null) {
			instance = new MinaClientConn();
		}
		return instance;
	}

	public IoSession getSession() {
		return session;
	}

	public void connect() {
		try {
			if(connector==null){
				connector = new NioSocketConnector();
				connector.setConnectTimeoutMillis(3000);
	
				connector.getSessionConfig().setReceiveBufferSize(10240); // 设置接收缓冲区的大小
				connector.getSessionConfig().setSendBufferSize(10240);// 设置输出缓冲区的大小
	
				// 加入解码器
				ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(new TextLineCodecFactory());
				connector.getFilterChain().addLast("codec", codecFilter);// 配置CodecFactory
	
				// 设置默认访问地址
				connector.setDefaultRemoteAddress(new InetSocketAddress(ApplicationConstants.MINA_SERVERNAME,
						ApplicationConstants.MINA_SERVERPORT));
	
				// 添加处理器
				connector.setHandler(new MinaClientHandle());
	
				// 添加重连监听
				connector.addListener(new IoListener() {
					@Override
					public void sessionDestroyed(IoSession s) throws Exception {
						session = null;
						isConned = false;
					}
	
					@Override
					public void sessionCreated(IoSession s) throws Exception {
						session = s;
						isConned = true;
						// 重新建立连接成功，发送登录事件
						MessageSender.getInstance().sendLoginEvent();
					}
	
				});
			}	
			
			ConnectFuture future = connector.connect();
			future.awaitUninterruptibly(3, TimeUnit.SECONDS);

		} catch (Exception e) {
			session = null;
			isConned = false;
		}

	}

	public boolean close() {
		if (session != null) {
			CloseFuture future = session.getCloseFuture();
			future.awaitUninterruptibly(1000);
			connector.dispose();

			isConned = false;

			return true;
		} else {
			return false;
		}
	}

}