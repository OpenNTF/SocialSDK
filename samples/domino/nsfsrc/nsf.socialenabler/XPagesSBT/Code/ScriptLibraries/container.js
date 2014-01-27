define(['dojo/_base/declare', 'dojo/_base/lang', 'dojo/query'],
  function(declare, lang, query) {
	return declare([],{
		constructor : function(response) {
			this.token = response.token;
			this.ttl = response.ttl;
			var config = {};
			config[osapi.container.ContainerConfig.RENDER_DEBUG] = '1';
			config[osapi.container.ContainerConfig.GET_CONTAINER_TOKEN] = lang.hitch(this, 'getContainerToken');
			this.container = new osapi.container.Container(config);
	        this.site = this.container.newGadgetSite(query('#gadgetSite')[0]);
		},
		
		getContainerToken : function(result) {
			result(this.token, this.ttl);
		},
		
		renderGadget : function(url) {
			this.container.closeGadget(this.site);
			var renderParams = {};   
	        renderParams[osapi.container.RenderParam.HEIGHT] = '100%';
	        renderParams[osapi.container.RenderParam.WIDTH] = '100%';
			this.container.navigateGadget(this.site, url, {}, 
	                renderParams);
		}
	});
});