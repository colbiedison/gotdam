package us.dison.gotdam;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GotDam implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("gotdam");

	@Override
	public void onInitialize() {
		LOGGER.info("Got dam?");
	}
}
