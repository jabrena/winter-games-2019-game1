package org.jab.microservices.wintergames1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WinterGames1Tests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void applicationStarts() {
		WinterGames1.main(new String[] {});
	}

}

