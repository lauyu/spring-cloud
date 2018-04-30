package com.test.perf.common.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class WrapperResponse extends HttpServletResponseWrapper {
	private ByteArrayOutputStream buffer = null;
	private ServletOutputStream out = null;
	private PrintWriter writer = null;
	private boolean isPrintWriter=true;
	
	public WrapperResponse(HttpServletResponse resp) throws IOException {
		super(resp);
		buffer = new ByteArrayOutputStream();// 真正存储数据的流
		out = new WapperedOutputStream(buffer);
		writer = new PrintWriter(new OutputStreamWriter(buffer,
				this.getCharacterEncoding()));
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {

		return out;
	}

	@Override
	public PrintWriter getWriter() throws UnsupportedEncodingException {
		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (out != null) {
			out.flush();
		}
		if (writer != null ) {
			writer.flush();
		}
	}
	public boolean isPrintWriter(){
		return isPrintWriter;
	}
	
	@Override
	public void reset() {
		buffer.reset();
	}

	public byte[] getResponseData() throws IOException {
		flushBuffer();
		return buffer.toByteArray();
	}
	@Override
	public void finalize() throws Throwable {
		super.finalize();
		if(out!=null){
			try {
			out.close();
			} catch(Exception e){
				
			}
		}
		if(writer!=null){
			try {
				writer.close();
			} catch(Exception e){
				
			}
		}
	}
	
	private class WapperedOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream bos = null;

		public WapperedOutputStream(ByteArrayOutputStream stream)
				throws IOException {
			bos = stream;
		}

		@Override
		public void write(int b) throws IOException {
			isPrintWriter=false;
			bos.write(b);
		}

		@Override
		public void write(byte[] b) throws IOException {
			isPrintWriter=false;
			bos.write(b, 0, b.length);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener listener) {
			
		}
	}

}
