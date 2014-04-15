<style>
	.ibmsbtCommunityDialog {
		display: none;
  		background:white;
    	z-index:6000; 
    	-moz-box-shadow: 1px 0px 10px rgba(0, 0, 0, 0.7); 
    	-moz-border-radius: 6px; 
    	-webkit-border-radius: 6px; 
    	-webkit-box-shadow: 1px 0px 10px rgba(0, 0, 0, 0.7); 
    	color: black;
    	border-style: none;
    	
    	position:fixed;
  		left:50%;
  		top:50%;
  		padding: 2em 5em 5em 5em;
    }
</style>

<div>
	<button style="font-size: 12px;" class="btn btn-primary" onclick="document.getElementById('ibm-sbt-create-community-***REMOVED*** echo $timestamp; ?>').style.display = 'block';">Create</button>
	<button style="font-size: 12px;" class="btn btn-primary" onclick="window.open('***REMOVED*** echo $settings->getURL($this->config->endpoint);?>/communities', '_blank');">Open Communities</button>
	
	<div class="ibmsbtCommunityDialog" id="ibm-sbt-create-community-***REMOVED*** echo $timestamp; ?>" style="font-size: 12px;">
		<div id="ibm-sbt-success-***REMOVED*** echo $timestamp; ?>" display: none; font-weight: bold; color: green;"></div>
		<div id="ibm-sbt-error-***REMOVED*** echo $timestamp; ?>" style="display:none;" class="alert alert-error"></div>
		<table>
			<tr>
				<td><label class="control-label" for="ibm-sbt-community-title-***REMOVED*** echo $timestamp; ?>">Title:</label></td>
				<td><input id="ibm-sbt-community-title-***REMOVED*** echo $timestamp; ?>" type="text" /></td>
			</tr>
			<tr>
				<td><label class="control-label" for="ibm-sbt-community-content-***REMOVED*** echo $timestamp; ?>">Content:</label></td>
				<td><input id="ibm-sbt-community-content-***REMOVED*** echo $timestamp; ?>" type="text-***REMOVED*** echo $timestamp; ?>" /></td>
			</tr>
			<tr>
				<td><label class="control-label" for="ibm-sbt-community-tags-***REMOVED*** echo $timestamp; ?>">Tags:</label></td>
				<td><input id="ibm-sbt-community-tags-***REMOVED*** echo $timestamp; ?>" type="text" /></td>
			</tr>
		</table>
		<br/><br/>
		<button class="btn btn-primary" id="ibm-sbt-create-community-button-***REMOVED*** echo $timestamp; ?>">Create Community</button>
		<button class="btn btn-primary" onclick="document.getElementById('ibm-sbt-create-community-***REMOVED*** echo $timestamp; ?>').style.display = 'none';">Cancel</button>
	</div>
</div>
<script type="text/template" id="communityRow-***REMOVED*** echo $timestamp; ?>">
<tr class="${rowClass}" style="font-size: 12px;">
	<td style="width:100%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display: inline-block; padding-left: 10px;">
			<span dojoAttachPoint="placeLinkNode">
				<a href="javascript: void(0)" target="_self" title="${title}<br/>Tags: ${tags}" dojoAttachPoint="placeTitleLink" data-dojo-attach-event="onclick: handleClick">${title}</a>
			</span>
			<span dojoAttachPoint="sourceTypePlaceHolder"></span>
			<span style="display: none" class=lotusDivider role=presentation dojoAttachPoint="sourceTypeSectionDevider">|</span>
			<span style="display: none" class="lotusType commType" dojoAttachPoint="moderatedIconNode">
				<img style="display: inline" class="lconnSprite lconnSprite-iconModeratedCommunity16" title="${nls.moderated}" alt="${nls.moderated}" src="${blankGif}">
				${nls.moderated} 
			</span>
			<span style="display: none" class="lotusType commType" dojoAttachPoint="restrictedIconNode">
				<img style="display: inline" class="iconsStates16 iconsStates16-CheckedOut" title="${nls.restricted}" alt="${nls.restricted}" src="${blankGif}">
				${nls.restricted} 
			</span>
	</td>
</tr>
</script>
<script type="text/template" id="pagingHeader-***REMOVED*** echo $timestamp; ?>">
<div dojoAttachPoint="pagingHeader" style="font-size: 12px;">
	<div>
		<hr style="width:90%; margin: 0.7em 0; left: -30px; border: 0; height: 1px;"/>
	</div>
	<span dojoAttachPoint="showingResultsMessage">${pagingResults}</span>
			<span style="padding-left: 40px;">
				<a style="${hidePreviousLink};" title="${nls.previousPage}" href="javascript: void(0)" data-dojo-attach-event="onclick: prevPage">${nls.previous}</a>
				<span style="${hidePreviousLabel}">${nls.previous}</span>
			</span>

			<a style="${hideNextLink} align: right;" title="${nls.nextPage}" href="javascript: void(0)" data-dojo-attach-event="onclick: nextPage">${nls.next}</a>
			<span style="${hideNextLabel}">${nls.next}</span>
		<div>
			<hr style="width:90%; margin: 0.8em 0; left: -30px; border: 0; height: 1px;"/>
		</div>
</div>
</script>
<script type="text/template" id="pagingFooter-***REMOVED*** echo $timestamp; ?>">
<div dojoattachpoint="pagingFooter" class="lotusPaging" style="font-size: 12px;">
	<div>
		<hr style="width:90%; margin: 0.7em 0; left: -30px; border: 0; height: 1px;"/>
	</div>
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
