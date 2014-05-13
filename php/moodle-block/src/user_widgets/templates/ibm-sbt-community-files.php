<style>
	.ibmsbtCommunityFileUploadDialog {
  		background:white;
    	z-index:5000; 
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
  		padding: 2em 5em 5em 5em;
    }
</style>
<button style="font-size: 12px;" class="btn btn-primary" onclick="document.getElementById('ibm-sbt-communities-upload-dialog-<?php echo $timestamp; ?>').style.display='block';">Upload</button>
<br/>
<span style="font-size: 12px;"><?php echo get_string('select_community', 'block_ibmsbt');?></span>
<select style="font-size: 12px;" id="ibm-sbt-communities-<?php echo $timestamp; ?>" onchange="onCommunityChange<?php echo $timestamp; ?>();"></select>
<br />
<div id="ibm-sbt-community-files-list-<?php echo $timestamp; ?>"></div>

<div class="ibmsbtCommunityFileUploadDialog" id="ibm-sbt-communities-upload-dialog-<?php echo $timestamp; ?>">
	<input style="font-size: 12px;" type="file" id="ibm-sbt-community-files-<?php echo $timestamp; ?>" accept="image/*"></input>
	<br/>
	<button style="font-size: 12px;" class="btn btn-primary" id="ibm-sbt-communities-file-upload-dialog-button-<?php echo $timestamp; ?>"><?php echo get_string('upload', 'block_ibmsbt');?></button>
	<button style="font-size: 12px;" class="btn btn-primary" onclick="document.getElementById('ibm-sbt-communities-upload-dialog-<?php echo $timestamp; ?>').style.display='none';"><?php echo get_string('close', 'block_ibmsbt');?></button>
	<br/>
</div>
<div id="ibm-sbt-community-files-error-<?php echo $timestamp; ?>" style="display: none;" class="alert alert-error"></div>
<img id="ibm-sbt-loading-<?php echo $timestamp; ?>" style="display: none;" src="<?php echo $CFG->wwwroot; ?>/blocks/ibmsbt/user_widgets/templates/assets/loading_small.gif" />
<span style="display: none; font-size: 12px;" id="ibm-sbt-community-files-success-<?php echo $timestamp; ?>"></span><br/><br/>
<script type="text/template" id="fileRow-<?php echo $timestamp; ?>">
<tr style="padding-bottom: 0.3em; font-size: 12px;">
	<td style="width:100%;  white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display: inline-block; padding-left: 10px;">
			<span dojoAttachPoint="placeLinkNode">
				<a href="javascript: void(0)" target="_self" title="${title}" dojoAttachPoint="placeTitleLink" data-dojo-attach-event="onclick: handleClick">${title}</a>
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
			<span style="padding-left: 10px;">
				<a style="${hidePreviousLink};" title="${nls.previousPage}" href="javascript: void(0)" data-dojo-attach-event="onclick: prevPage">${nls.previous}</a>
				<span style="${hidePreviousLabel}">${nls.previous}</span>
			</span>

			<a style="${hideNextLink} align: right;" title="${nls.nextPage}" href="javascript: void(0)" data-dojo-attach-event="onclick: nextPage">${nls.next}</a>
			<span style="${hideNextLabel}">${nls.next}</span>
</div>
</script>
<script type="text/template" id="pagingFooter-<?php echo $timestamp; ?>" style="font-size: 12px;">
<div dojoattachpoint="pagingFooter" class="lotusPaging">
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