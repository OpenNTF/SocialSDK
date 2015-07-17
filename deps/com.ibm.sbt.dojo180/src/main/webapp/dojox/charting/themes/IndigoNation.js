define("dojox/charting/themes/IndigoNation", ["../SimpleTheme", "./common"], function(SimpleTheme, themes){
	// notes: colors generated by moving in 30 degree increments around the hue circle,
	//		at 90% saturation, using a B value of 75 (HSB model).
	themes.IndigoNation = new SimpleTheme({
		colors: [
			"#93a4d0",
			"#3b4152",
			"#687291",
			"#9faed9",
			"#8290b8"
		]
	});
	return themes.IndigoNation;
});
