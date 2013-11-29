require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json" ], 
    function(XmlDataHandler,lang,dom,json) {
        var domNode = dom.byId("dates");
        var datesEl = domNode.text || domNode.textContent;

        try {
            var dataHandler1 = new XmlDataHandler({
                data : datesEl,
                xpath : { 
                	date1 : "/dates/date[@type='1']",
                	date2 : "/dates/date[@type='2']",
                	date3 : "/dates/date[@type='3']",
                	date4 : "/dates/date[@type='4']"
                }
            });
            var dataHandler2 = new XmlDataHandler({
                data : datesEl,
                xpath : { 
                	date1 : "/dates/date[@type='1']",
                	date2 : "/dates/date[@type='2']",
                	date3 : "/dates/date[@type='3']",
                	date4 : "/dates/date[@type='4']"
                }
            });
            
            var results = [];
            results.push(dataHandler1.toJson());
            results.push({
            	date1 : dataHandler2.getAsDate("date1"),
            	date2 : dataHandler2.getAsDate("date2"),
            	date3 : dataHandler2.getAsDate("date3"),
            	date4 : dataHandler2.getAsDate("date4")
            });
            
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
