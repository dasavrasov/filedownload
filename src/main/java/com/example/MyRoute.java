package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

	@Value("${crlfile}")
	private String crlFile; //файл содержащий список ссылок для скачивания
	
	@Value("${destination}")
	private String destination; //директория куда складывать скачанные файлы
	
	@Value("${duration}")
	private String duration; //периодичность
	
	@Override
	public void configure() throws Exception {

		String fileName = crlFile;
		List<String> list = new ArrayList<>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			list = stream
					.filter(line -> line.startsWith("http"))
					.filter(line -> line.endsWith(".crl"))
		  			.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		for (String href : list) {
			String filename = href.substring(href.lastIndexOf("/")+1);
			  			
			from("timer:crl?fixedRate=true&period="+duration)
			.to(href)
			.log("download from "+href)
			.to("file:"+ destination +"?fileName="+filename);
		}		
		
	}

}
