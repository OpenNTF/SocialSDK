require([ "sbt/dom", "sbt/log" ], function(dom, log) {

	log.setLevel("DEBUG");
	log.debug("This is {0} Log statement", "debug");
	log.info("This is {0} Log statement", "info");
	log.warn("This is {0} Log statement", "warn");
	log.error("This is {0} Log statement", "error");

	try {
		throw new Error("Exception text");
	} catch (error) {
		log.exception("Exception happened {0}", error.message);
	}

	dom.setText("content", "Please see the logging statements in console.");
});