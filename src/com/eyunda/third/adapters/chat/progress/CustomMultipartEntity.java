package com.eyunda.third.adapters.chat.progress;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CustomMultipartEntity extends SimpleMultipartEntity{
	private final ProgressListener listener;
	 
    public CustomMultipartEntity(final ProgressListener listener) {
 
        super();
 
        this.listener = listener;
 
    }
 
    @Override
 
    public void writeTo(final OutputStream outstream) throws IOException {
 
        super.writeTo(new CountingOutputStream(outstream, this.listener));
 
    }
 
    public static interface ProgressListener {
 
        void transferred(long num);
 
    }
 
  
 
    public static class CountingOutputStream extends FilterOutputStream {
 
        private final ProgressListener listener;
 
        private long transferred;
 
        public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
 
            super(out);
 
            this.listener = listener;
 
            this.transferred = 0;
 
        }
 
        public void write(byte[] b, int off, int len) throws IOException {
 
            out.write(b, off, len);
 
            this.transferred += len;
 
            this.listener.transferred(this.transferred);
 
        }
 
        public void write(int b) throws IOException {
 
            out.write(b);
 
            this.transferred++;
 
            this.listener.transferred(this.transferred);
 
        }
 
    }
}
