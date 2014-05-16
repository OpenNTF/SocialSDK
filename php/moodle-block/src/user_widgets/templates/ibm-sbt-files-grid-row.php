<style>
	.ibmsbtFileUploadDialog {
  		background: white;
    	z-index: 8000; 
    	-moz-box-shadow: 1px 0px 10px rgba(0, 0, 0, 0.7); 
    	-moz-border-radius: 6px; 
    	-webkit-border-radius: 6px; 
    	-webkit-box-shadow: 1px 0px 10px rgba(0, 0, 0, 0.7); 
    	color: black;
    	border-style: none;
    	display: none;
    	position:fixed;
  		left:50%;
  		top:50%;
  		padding: 2em 2em 2em 2em;
    }
</style>

<div class="ibmsbtFileUploadDialog" id="ibm-sbt-upload-dialog-<?php echo $timestamp; ?>" style="font-size: 12px;">
	<h1><?php echo get_string('upload_file', 'block_ibmsbt');?></h1><br/><br/>
	<input type="file" id="ibm-sbt-file-<?php echo $timestamp; ?>"/>
	<input checked="checked" type="radio" id="ibm-sbt-file-privacy-public-<?php echo $timestamp; ?>" name="ibm-sbt-file-privacy-<?php echo $timestamp; ?>" value="public"/><?php echo get_string('public', 'block_ibmsbt');?>
	<input type="radio" id="ibm-sbt-file-privacy-private-<?php echo $timestamp; ?>" name="ibm-sbt-file-privacy-<?php echo $timestamp; ?>" value="private"/><?php echo get_string('private', 'block_ibmsbt');?>
	<br/><br/>
	<button style="font-size: 12px;" class="btn btn-primary" id="ibm-sbt-upload-button-<?php echo $timestamp; ?>"><?php echo get_string('upload_file', 'block_ibmsbt');?></button>
	<button style="font-size: 12px;" class="btn btn-primary" onclick="document.getElementById('ibm-sbt-upload-dialog-<?php echo $timestamp; ?>').style.display='none';"><?php echo get_string('close', 'block_ibmsbt');?></button>
</div>
<button style="font-size: 12px;" class="btn btn-primary" onclick="document.getElementById('ibm-sbt-upload-dialog-<?php echo $timestamp; ?>').style.display='block';" id="ibm-sbt-upload-button-<?php echo $timestamp; ?>"><?php echo get_string('upload_file', 'block_ibmsbt');?></button>
<button style="font-size: 12px;" class="btn btn-primary" onclick="window.open('<?php echo $settings->getURL($this->config->endpoint);?>/files/app#', '_blank');"><?php echo get_string('open_files', 'block_ibmsbt');?></button><br/><br/>

<div style="font-size: 12px;"><strong id="ibm-sbt-success-<?php echo $timestamp; ?>"></strong></div>
<div style="font-size: 12px;"><strong style="color: red;" id="ibm-sbt-error-<?php echo $timestamp; ?>"></strong></div>
<img id="ibm-sbt-loading-<?php echo $timestamp; ?>" style="display: none;" src="<?php echo $CFG->wwwroot; ?>/blocks/ibmsbt/user_widgets/templates/assets/loading_small.gif" />
<script type="text/template" id="fileRow-<?php echo $timestamp; ?>">
<tr style="padding-bottom: 0.3em; font-size: 12px;">
	<td style="width:14em;  white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display: inline-block; padding-left: 10px;">
			<span dojoAttachPoint="placeLinkNode">
				<a href="javascript: void(0)" target="_self" title="${title}" dojoAttachPoint="placeTitleLink" data-dojo-attach-event="onclick: handleClick">${title}</a>
			</span>
	</td>
</tr>
</script>
<script type="text/template" id="folderRow-<?php echo $timestamp; ?>">
<tr style="padding-bottom: 0.3em; font-size: 12px;">
	<td style="width:14em;  white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display: inline-block; padding-left: 10px;">
			<span role="listitem"> 
                <a class="entry-title" target="_blank" rel="bookmark" title="${title}" href="${folderUrl}"> 
                	${title} 
            	</a>
            </span>
	</td>
</tr>
</script>
<script type="text/template" id="pagingHeader-<?php echo $timestamp; ?>">
<div dojoAttachPoint="pagingHeader" style="font-size: 12px;">
	<div>
		<hr style="width:90%; left: -30px; border: 0; height: 1px;"/>
	</div>
	<span dojoAttachPoint="showingResultsMessage">${pagingResults}</span>
			<span style="padding-left: 20%;">
				<a style="${hidePreviousLink};" title="${nls.previousPage}" href="javascript: void(0)" data-dojo-attach-event="onclick: prevPage">${nls.previous}</a>
				<span style="${hidePreviousLabel}">${nls.previous}</span>
			</span>

			<a style="${hideNextLink} align: right;" title="${nls.nextPage}" href="javascript: void(0)" data-dojo-attach-event="onclick: nextPage">${nls.next}</a>
			<span style="${hideNextLabel}">${nls.next}</span>
</div>
</script>
<script type="text/template" id="pagingFooter-<?php echo $timestamp; ?>">
<div dojoattachpoint="pagingFooter" class="lotusPaging" style="font-size: 12px;">
		Show:
			<a href="javascript: void(0)" title="${nls.show10Items}" aria-pressed="false"
				role="button" data-dojo-attach-event="onclick: show10ItemsPerPage">10</a> |

			<a href="javascript: void(0)"
				title="${nls.show25Items}" data-dojo-attach-event="onclick: show25ItemsPerPage"
				aria-pressed="false" role="button">25</a> |
		
			<a href="javascript: void(0)" title="${nls.show50Items}" data-dojo-attach-event="onclick: show50ItemsPerPage"
			aria-pressed="false" role="button">50</a> |
	
	
			<a href="javascript: void(0)" title="${nls.show100Items}" data-dojo-attach-event="onclick: show100ItemsPerPage"
			aria-pressed="false" role="button">100</a>
		 ${nls.items}
	<div>
		<hr style="width:90%; margin: 0.7em 0; left: -30px; border: 0; height: 1px;"/>
	</div>
</div>
</script>
