<div  style="float:left; role="content">
	<div>
		<hr style="width:90%; border: 0; height: 1px; background-color: black;"/>
	</div>
	<button onclick="document.getElementById('ibm-sbt-create-community').show();">Create a community</button>
	
	<dialog id="ibm-sbt-create-community">
		<div id="ibm-sbt-success" display: none; font-weight: bold; color: green;"></div>
		<div id="ibm-sbt-error" style="display:none;" class="alert alert-error"></div>
		<table>
			<tr>
				<td><label class="control-label" for="ibm-sbt-community-title">Title:</label></td>
				<td><input id="ibm-sbt-community-title" type="text" /></td>
			</tr>
			<tr>
				<td><label class="control-label" for="ibm-sbt-community-content">Content:</label></td>
				<td><input id="ibm-sbt-community-content" type="text" /></td>
			</tr>
			<tr>
				<td><label class="control-label" for="ibm-sbt-community-tags">Tags:</label></td>
				<td><input id="ibm-sbt-community-tags" type="text" /></td>
			</tr>
		</table>
		<button class="btn" id="selectedBtn">Create Community</button>
		<button onclick="document.getElementById('ibm-sbt-create-community').close();">Cancel</button>
	</dialog>
</div>
<br/><br/>
<script type="text/template" id="communityRow">
<tr class="${rowClass}" >
	<td style="width:140px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display: inline-block; padding-left: 10px;">
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
<script type="text/template" id="pagingHeader">
<div dojoAttachPoint="pagingHeader">
	<div>
		<hr style="width:90%; margin: 0.7em 0; left: -30px; border: 0; height: 1px;"/>
	</div>
	<span dojoAttachPoint="showingResultsMessage">${pagingResults}</span>
			<span style="padding-left: 70px;">
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
<script type="text/template" id="pagingFooter">
<div dojoattachpoint="pagingFooter" class="lotusPaging" style="">
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
