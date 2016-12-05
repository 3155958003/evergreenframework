package org.evergreen.web.upload;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.Part;

import org.evergreen.web.MultipartFile;

public class ServletPartFile extends MultipartFile{

	private static final long serialVersionUID = 9202366354209980173L;
	
	private Part part;
	
	public ServletPartFile(Part part){
		this.part = part;
	}
	
	public long getSize(){
		return part.getSize();
	}
	
	public String getContentType(){
		return part.getContentType();
	}

	// 上传
	public void upload(File file) throws IOException {
		part.write(file.getPath());
	}

}
