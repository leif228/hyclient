package com.eyunda.third.chat.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class TextLineDecoder implements ProtocolDecoder {

	Charset charset = Charset.forName("UTF-8");
	IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
	char end = '`';

	@Override
	public void decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput output) throws Exception {
		String event = (String) session.getAttribute("event");
		if (event == null)
			event = "";

		while (in.hasRemaining()) {
			byte b = in.get();
			if (b == end) {
				buf.flip();
				byte[] bytes = new byte[buf.limit()];
				buf.get(bytes);
				String message = new String(bytes, charset);
				buf.clear();

				output.write(event + message);
				System.out.println("@@"+event+message);
				session.setAttribute("event", "");
				return;
			} else {
				buf.put(b);
			}
		}
		if (buf.position() > 1) {
			buf.flip();
			byte[] bytes = new byte[buf.limit()];
			buf.get(bytes);
			String message = new String(bytes, charset);
			buf.clear();
			System.out.println("**"+event+message);

			session.setAttribute("event", event + message);
		}
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {

	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {

	}

}
