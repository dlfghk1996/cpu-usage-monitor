package io.terra.cpuusagemonitor.common.config.springdoc;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"!prod"})
@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

	@Value("${springdoc.serverInfo.host}")
	private String serverHost;

	@Value("${springdoc.serverInfo.port}")
	private String serverPort;

	@Bean
	public OpenAPI setOpenAPI() throws UnknownHostException {

		List<Server> serverList = new ArrayList<>();
		serverList.add(new Server().url(serverHost + ":" + serverPort));

		OpenAPI openAPI =
			new OpenAPI()
				.info(new Info().title("API 명세서"))
				.servers(serverList);

		return openAPI;
	}
}
